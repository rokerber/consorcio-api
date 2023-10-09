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
        double lance = parametroRequestDTO.getLance() * 0.01;
        double taxaAdm = parametroRequestDTO.getTaxaAdm() * 0.01;
        double incc = parametroRequestDTO.getIncc() * 0.01;
        Integer prazo = parametroRequestDTO.getPrazo();
        BigDecimal valorCredito = parametroRequestDTO.getValorCredito();
        for (int i = 1; i <= COTA; i++) {
            List<BigDecimal> valorCreditoList = new ArrayList<>();
            Integer valorMesContemplacao = valorMesContemplacao(prazo);
            BigDecimal creditoComIncc = gerarCreditoComIncc(valorMesContemplacao, incc, valorCredito, currentdate.getMonthValue(),valorCreditoList);
            BigDecimal valorCreditoMaisTaxaAdm = gerarValorCreditoMaisTaxaAdm(creditoComIncc, taxaAdm);
            BigDecimal investimentoMensalCorrigido = gerarInvestimentoMensalCorrigido(valorCreditoMaisTaxaAdm, prazo);

            simulacaoDTOList.add(SimulacaoDTO.builder()
                    .cota(i)
                    .mesContemplacao(valorMesContemplacao)
                    .formaContemplacao("SORTEIO")
                    .creditoAtualizado(gerarCreditoAtualizado(creditoComIncc, valorCreditoMaisTaxaAdm,lance))
                    .investimentoMensalCorrigido(investimentoMensalCorrigido)
                    .valorInvestidoCorrigido(gerarValorInvestidoCorrigido(valorCreditoList,taxaAdm,prazo))
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

    public BigDecimal gerarCreditoComIncc(Integer valorMesContemplacao, double incc, BigDecimal valorCredito, Integer monthValue, List<BigDecimal> valorCreditoList) {
        int counter = 0;
        int mesesRestantes = 13 - monthValue;

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

    public BigDecimal gerarValorCreditoMaisTaxaAdm(BigDecimal creditoComIncc, double taxaAdm) {
        BigDecimal valorCreditoVezesTaxaAdm = creditoComIncc.multiply(new BigDecimal(taxaAdm));
        return creditoComIncc.add(valorCreditoVezesTaxaAdm);
    }

    public BigDecimal gerarCreditoAtualizado(BigDecimal creditoComIncc, BigDecimal valorCreditoMaisTaxaAdm, double lance) {
        if (lance > 0) {
            BigDecimal valorCreditoVezesLance = valorCreditoMaisTaxaAdm.multiply(new BigDecimal(lance));
            creditoComIncc = creditoComIncc.subtract(valorCreditoVezesLance);
        }

        return creditoComIncc.setScale(2, RoundingMode.HALF_EVEN);
    }

    public BigDecimal gerarInvestimentoMensalCorrigido(BigDecimal valorCreditoMaisTaxaAdm, Integer prazo) {
        return valorCreditoMaisTaxaAdm.divide(new BigDecimal(prazo),2,RoundingMode.HALF_EVEN);
    }

    public BigDecimal gerarValorInvestidoCorrigido(List<BigDecimal> valorCreditoList, double taxaAdm,Integer prazo) {
        BigDecimal valorInvestidoCorrigido = new BigDecimal(0);
        for (BigDecimal credito: valorCreditoList) {
            BigDecimal valorCreditoVezesTaxaAdm = credito.multiply(new BigDecimal(taxaAdm));
            BigDecimal creditoComInccMaisValorCredito = credito.add(valorCreditoVezesTaxaAdm);
            BigDecimal valorCreditoMaisTaxaAdmDivididoPrazo = creditoComInccMaisValorCredito.divide(new BigDecimal(prazo),10,RoundingMode.HALF_EVEN);
            valorInvestidoCorrigido = valorInvestidoCorrigido.add(valorCreditoMaisTaxaAdmDivididoPrazo);
        }
        return valorInvestidoCorrigido.setScale(2,RoundingMode.HALF_EVEN);
    }


}
