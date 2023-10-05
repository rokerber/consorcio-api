package br.com.consorcio.service;

import br.com.consorcio.dto.ParametroRequestDTO;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SimulacaoServiceTest {

    @Test
    void testCreditoAtualizado() {
        SimulacaoService simulacaoService = new SimulacaoService();
        ParametroRequestDTO parametroRequestDTO = ParametroRequestDTO.builder()
                .modalidade("CHEIA")
                .valorCredito(new BigDecimal(100000))
                .prazo(240)
                .incc(9.0)
                .taxaAdm(25.0)
                .lance(30.0)
                .build();

        double creditoAtualizado = simulacaoService.gerarCreditoAtualizado(1,parametroRequestDTO,10).doubleValue();
        double creditoAtualizado2 = simulacaoService.gerarCreditoAtualizado(4,parametroRequestDTO,10).doubleValue();
        double creditoAtualizado3 = simulacaoService.gerarCreditoAtualizado(25,parametroRequestDTO,10).doubleValue();
        double creditoAtualizado4 = simulacaoService.gerarCreditoAtualizado(12,parametroRequestDTO,1).doubleValue();
        double creditoAtualizado5 = simulacaoService.gerarCreditoAtualizado(13,parametroRequestDTO,1).doubleValue();
        double creditoAtualizado6 = simulacaoService.gerarCreditoAtualizado(1,parametroRequestDTO,12).doubleValue();
        double creditoAtualizado7 = simulacaoService.gerarCreditoAtualizado(2,parametroRequestDTO,12).doubleValue();

        assertEquals(62500.0, creditoAtualizado);
        assertEquals(68125.0, creditoAtualizado2);
        assertEquals(74256.25, creditoAtualizado3);
        assertEquals(62500.0, creditoAtualizado4);
        assertEquals(68125.0, creditoAtualizado5);
        assertEquals(62500.0, creditoAtualizado6);
        assertEquals(68125.0, creditoAtualizado7);

    }

}
