package br.com.consorcio.service;

import br.com.consorcio.dto.ParametroRequestDTO;
import br.com.consorcio.dto.SimulacaoDTO;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.MathContext;
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
        for (int i = 1; i <= cota; i++) {
            simulacaoDTOList.add(SimulacaoDTO.builder()
                    .cota(i)
                    .mesContemplacao(valorMesContemplacao(parametroRequestDTO.getPrazo()))
                    .formaContemplacao("SORTEIO")
                    .creditoAtualizado(gerarCreditoAtualizado(parametroRequestDTO))
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

    private BigDecimal gerarCreditoAtualizado(ParametroRequestDTO parametroRequestDTO) {
        LocalDate currentdate = LocalDate.now();
        int valorMesContemplacao = valorMesContemplacao(parametroRequestDTO.getPrazo());
        int monthValue = currentdate.getMonthValue();
        int counter = 0;
        int mesesRestantes = 13 - monthValue;

        if (valorMesContemplacao > mesesRestantes) {
            counter++;
        }

        for (int i = 25; i <= valorMesContemplacao; i = i + 12) {
                counter++;
        }
        double incc = parametroRequestDTO.getIncc() * 0.01;
        BigDecimal valorCredito = parametroRequestDTO.getValorCredito();
        // 4 precision
        MathContext mathContext = new MathContext(8);
        BigDecimal creditoMaisIncc = valorCredito.multiply(new BigDecimal(incc),mathContext);
        BigDecimal creditoMaisInccFinal = creditoMaisIncc.multiply(new BigDecimal(counter),mathContext);
        valorCredito = valorCredito.add(creditoMaisInccFinal,mathContext);
        return valorCredito;
    }
}
