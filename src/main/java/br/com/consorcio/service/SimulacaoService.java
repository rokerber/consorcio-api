package br.com.consorcio.service;

import br.com.consorcio.dto.ParametroRequestDTO;
import br.com.consorcio.dto.SimulacaoDTO;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static br.com.consorcio.utils.Util.getRandomNumber;

@Service
public class SimulacaoService {

    private static final int cota = 10;
    public List<SimulacaoDTO> simular(ParametroRequestDTO parametroRequestDTO){
        // monta a tabela para retornar
        List<SimulacaoDTO> simulacaoDTOList = new ArrayList<>();
        LocalDate currentdate = LocalDate.now();
        for (int i = 1; i <= cota; i++) {
            Integer valorMesContemplacao = valorMesContemplacao(parametroRequestDTO.getPrazo());
            simulacaoDTOList.add(SimulacaoDTO.builder()
                    .cota(i)
                    .mesContemplacao(valorMesContemplacao)
                    .formaContemplacao("SORTEIO")
                    .creditoAtualizado(gerarCreditoAtualizado(valorMesContemplacao, parametroRequestDTO,currentdate.getMonthValue()))
                    .investimentoMensalCorrigido(new BigDecimal(123))
                    .valorInvestidoCorrigido(new BigDecimal(123))
                    .parcelaPosContemplacao(new BigDecimal(123))
                    .valorVenda(new BigDecimal(123))
                    .ir(new BigDecimal(123))
                    .build());
        }
        return simulacaoDTOList;
    }

    private Integer valorMesContemplacao(Integer prazo) {
        return getRandomNumber(1,prazo);
    }

    public BigDecimal gerarCreditoAtualizado(Integer valorMesContemplacao, ParametroRequestDTO parametroRequestDTO, Integer monthValue) {
        double incc = parametroRequestDTO.getIncc() * 0.01;
        double taxaAdm = parametroRequestDTO.getTaxaAdm() * 0.01;
        double valorLance = parametroRequestDTO.getLance() * 0.01;
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

        if (valorLance > 0) {
            BigDecimal valorCreditoVezesTaxaAdm = valorCredito.multiply(new BigDecimal(taxaAdm));
            BigDecimal valorCreditoMaisTaxaAdm = valorCredito.add(valorCreditoVezesTaxaAdm);
            BigDecimal valorCreditoVezesLance = valorCreditoMaisTaxaAdm.multiply(new BigDecimal(valorLance));
            valorCredito = valorCredito.subtract(valorCreditoVezesLance);
        }

        return valorCredito.setScale(2, RoundingMode.HALF_EVEN);
    }
}
