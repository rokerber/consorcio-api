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
            Integer valorMesContemplacao = valorMesContemplacao(parametroRequestDTO.getPrazo());
            BigDecimal creditoComIncc = gerarCreditoComIncc(valorMesContemplacao, parametroRequestDTO, currentdate.getMonthValue());
            BigDecimal valorCreditoMaisTaxaAdm = gerarValorCreditoMaisTaxaAdm(creditoComIncc, parametroRequestDTO);

            simulacaoDTOList.add(SimulacaoDTO.builder()
                    .cota(i)
                    .mesContemplacao(valorMesContemplacao)
                    .formaContemplacao("SORTEIO")
                    .creditoAtualizado(gerarCreditoAtualizado(creditoComIncc, valorCreditoMaisTaxaAdm, parametroRequestDTO))
                    .investimentoMensalCorrigido(gerarInvestimentoMensalCorrigido(valorCreditoMaisTaxaAdm, parametroRequestDTO))
                    .valorInvestidoCorrigido(new BigDecimal(123))
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

    public BigDecimal gerarCreditoComIncc(Integer valorMesContemplacao, ParametroRequestDTO parametroRequestDTO, Integer monthValue) {
        double incc = parametroRequestDTO.getIncc() * 0.01;
        int counter = 0;
        int mesesRestantes = 13 - monthValue;
        BigDecimal valorCredito = parametroRequestDTO.getValorCredito();

        if (valorMesContemplacao > mesesRestantes) {
            counter++;
        }

        for (int i = 25; i <= valorMesContemplacao; i = i + 12) {
            counter++;
        }

        for (int i = 0; i < counter; i++) {
            BigDecimal creditoMaisIncc = valorCredito.multiply(new BigDecimal(incc));
            valorCredito = valorCredito.add(creditoMaisIncc);
        }
        return valorCredito;
    }

    public BigDecimal gerarValorCreditoMaisTaxaAdm(BigDecimal creditoComIncc, ParametroRequestDTO parametroRequestDTO) {
        double taxaAdm = parametroRequestDTO.getTaxaAdm() * 0.01;
        BigDecimal valorCreditoVezesTaxaAdm = creditoComIncc.multiply(new BigDecimal(taxaAdm));
        return creditoComIncc.add(valorCreditoVezesTaxaAdm);
    }

    public BigDecimal gerarCreditoAtualizado(BigDecimal creditoComIncc, BigDecimal valorCreditoMaisTaxaAdm, ParametroRequestDTO parametroRequestDTO) {
        double valorLance = parametroRequestDTO.getLance() * 0.01;

        if (valorLance > 0) {
            BigDecimal valorCreditoVezesLance = valorCreditoMaisTaxaAdm.multiply(new BigDecimal(valorLance));
            creditoComIncc = creditoComIncc.subtract(valorCreditoVezesLance);
        }

        return creditoComIncc.setScale(2, RoundingMode.HALF_EVEN);
    }

    public BigDecimal gerarInvestimentoMensalCorrigido(BigDecimal valorCreditoMaisTaxaAdm, ParametroRequestDTO parametroRequestDTO ) {
        return valorCreditoMaisTaxaAdm.divide(new BigDecimal(parametroRequestDTO.getPrazo()),2,RoundingMode.HALF_EVEN);
    }
}
