package br.com.consorcio.service;

import br.com.consorcio.dto.ParametroRequestDTO;
import br.com.consorcio.dto.SimulacaoDTO;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static br.com.consorcio.utils.Util.getRandomNumber;

@Service
public class SimulacaoService {

    private static final int COTA = 10;



    public List<SimulacaoDTO> simular(ParametroRequestDTO parametroRequestDTO){
        // monta a tabela para retornar
        List<SimulacaoDTO> simulacaoDTOList = new ArrayList<>();
        LocalDate currentdate = LocalDate.now();
        for (int i = 1; i <= COTA; i++) {
            List<BigDecimal> valorCreditoList = new ArrayList<>();
            List<BigDecimal> valorCreditoMaisTaxaAdmList = new ArrayList<>();
            List<BigDecimal> investimentoMensalCorrigidoList = new ArrayList<>();
            Integer valorMesContemplacao = valorMesContemplacao(parametroRequestDTO.getPrazo());
            BigDecimal creditoComIncc = gerarCreditoComIncc(valorMesContemplacao, parametroRequestDTO, currentdate.getMonthValue(),valorCreditoList);
            BigDecimal valorCreditoMaisTaxaAdm = gerarValorCreditoMaisTaxaAdm(creditoComIncc, parametroRequestDTO,valorCreditoList, valorCreditoMaisTaxaAdmList);
            BigDecimal investimentoMensalCorrigido = gerarInvestimentoMensalCorrigido(valorCreditoMaisTaxaAdm, parametroRequestDTO,valorCreditoMaisTaxaAdmList, investimentoMensalCorrigidoList);

            simulacaoDTOList.add(SimulacaoDTO.builder()
                    .cota(i)
                    .mesContemplacao(valorMesContemplacao)
                    .formaContemplacao("SORTEIO")
                    .creditoAtualizado(gerarCreditoAtualizado(creditoComIncc, valorCreditoMaisTaxaAdm, parametroRequestDTO))
                    .investimentoMensalCorrigido(investimentoMensalCorrigido)
                    .valorInvestidoCorrigido(gerarValorInvestidoCorrigido(investimentoMensalCorrigidoList))
                    .parcelaPosContemplacao(new BigDecimal(123))
                    .valorVenda(new BigDecimal(123))
                    .ir(new BigDecimal(123))
                    .build());
        }
        return simulacaoDTOList;
    }

    public Integer valorMesContemplacao(Integer prazo) {
        return getRandomNumber(1,prazo);
    }

    public BigDecimal gerarCreditoComIncc(Integer valorMesContemplacao, ParametroRequestDTO parametroRequestDTO, Integer monthValue, List<BigDecimal> valorCreditoList) {
        double incc = parametroRequestDTO.getIncc() * 0.01;
        int counter = 0;
        int mesesRestantes = 13 - monthValue;
        BigDecimal valorCredito = parametroRequestDTO.getValorCredito();

        // para definir valorInvestidoCorrigido apenas
        for (int i = 0; i < valorMesContemplacao && i < mesesRestantes; i++) {
            valorCreditoList.add(valorCredito);
        }

        if (valorMesContemplacao > mesesRestantes) {
            counter++;
        }

        for (int i = 25; i <= valorMesContemplacao; i = i + 12) {
            counter++;
        }

        int meses = valorMesContemplacao - mesesRestantes;

        for (int i = 0; i < counter; i++) {
            BigDecimal creditoMaisIncc = valorCredito.multiply(new BigDecimal(incc));
            valorCredito = valorCredito.add(creditoMaisIncc);
            // para definir valorInvestidoCorrigido apenas
            if (meses > 12 ) {
                for (int y = 0; y < 12; y++) {
                    valorCreditoList.add(valorCredito);
                }
                meses = meses - 12;
            } else {
                for (int y = 0; y < meses; y++) {
                    valorCreditoList.add(valorCredito);
                }
            }
        }

        return valorCredito;
    }

    public BigDecimal gerarValorCreditoMaisTaxaAdm(BigDecimal creditoComIncc, ParametroRequestDTO parametroRequestDTO, List<BigDecimal> valorCreditoList, List<BigDecimal> valorCreditoMaisTaxaAdmList) {
        double taxaAdm = parametroRequestDTO.getTaxaAdm() * 0.01;
        BigDecimal valorCreditoVezesTaxaAdm = creditoComIncc.multiply(new BigDecimal(taxaAdm));
        BigDecimal creditoComInccMaisValorCredito = creditoComIncc.add(valorCreditoVezesTaxaAdm);
        // para definir valorInvestidoCorrigido apenas
        for (BigDecimal credito: valorCreditoList) {
            BigDecimal valorCreditoVezesTaxaAdm2 = credito.multiply(new BigDecimal(taxaAdm));
            BigDecimal creditoComInccMaisValorCredito2 = credito.add(valorCreditoVezesTaxaAdm2);
            valorCreditoMaisTaxaAdmList.add(creditoComInccMaisValorCredito2);
        }
        return creditoComInccMaisValorCredito;
    }

    public BigDecimal gerarCreditoAtualizado(BigDecimal creditoComIncc, BigDecimal valorCreditoMaisTaxaAdm, ParametroRequestDTO parametroRequestDTO) {
        double valorLance = parametroRequestDTO.getLance() * 0.01;

        if (valorLance > 0) {
            BigDecimal valorCreditoVezesLance = valorCreditoMaisTaxaAdm.multiply(new BigDecimal(valorLance));
            creditoComIncc = creditoComIncc.subtract(valorCreditoVezesLance);
        }

        return creditoComIncc.setScale(2, RoundingMode.HALF_EVEN);
    }

    public BigDecimal gerarInvestimentoMensalCorrigido(BigDecimal valorCreditoMaisTaxaAdm, ParametroRequestDTO parametroRequestDTO, List<BigDecimal> valorCreditoMaisTaxaAdmList, List<BigDecimal> investimentoMensalCorrigidoList) {
        BigDecimal valorCreditoMaisTaxaAdmDivididoPrazo = valorCreditoMaisTaxaAdm.divide(new BigDecimal(parametroRequestDTO.getPrazo()),2,RoundingMode.HALF_EVEN);
        // para definir valorInvestidoCorrigido apenas
        for (BigDecimal  valorCredito: valorCreditoMaisTaxaAdmList) {
            BigDecimal valorCreditoMaisTaxaAdmDivididoPrazo2 = valorCredito.divide(new BigDecimal(parametroRequestDTO.getPrazo()),2,RoundingMode.HALF_EVEN);
            investimentoMensalCorrigidoList.add(valorCreditoMaisTaxaAdmDivididoPrazo2);
        }
        return valorCreditoMaisTaxaAdmDivididoPrazo;
    }

    public BigDecimal gerarValorInvestidoCorrigido(List<BigDecimal> investimentoMensalCorrigidoList) {
        return investimentoMensalCorrigidoList.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
    }


}
