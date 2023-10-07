package br.com.consorcio.service;

import br.com.consorcio.dto.ParametroRequestDTO;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SimulacaoServiceTest {

    static SimulacaoService simulacaoService;
    static ParametroRequestDTO parametroRequestDTO;
    static BigDecimal creditoComInccOutContempla110;
    static BigDecimal creditoComInccJanContempla12;
    static BigDecimal creditoComInccJanContempla230;
    static BigDecimal creditoComInccDezContempla1;
    static BigDecimal valorCreditoMaisTaxaAdmOutContempla110;
    static BigDecimal valorCreditoMaisTaxaAdmJanContempla12;
    static BigDecimal valorCreditoMaisTaxaAdmJanContempla230;
    static BigDecimal valorCreditoMaisTaxaAdmDezContempla1;
    @BeforeAll
    static void setup() {
        simulacaoService = new SimulacaoService();
        parametroRequestDTO = ParametroRequestDTO.builder()
                .modalidade("CHEIA")
                .valorCredito(new BigDecimal(100000))
                .prazo(240)
                .incc(9.0)
                .taxaAdm(25.0)
                .lance(30.0)
                .build();

        creditoComInccOutContempla110 = simulacaoService.gerarCreditoComIncc(110, parametroRequestDTO, 10);
        creditoComInccJanContempla12 = simulacaoService.gerarCreditoComIncc(12, parametroRequestDTO, 1);
        creditoComInccJanContempla230 = simulacaoService.gerarCreditoComIncc(230, parametroRequestDTO, 1);
        creditoComInccDezContempla1 = simulacaoService.gerarCreditoComIncc(1, parametroRequestDTO, 12);

        valorCreditoMaisTaxaAdmOutContempla110 = simulacaoService.gerarValorCreditoMaisTaxaAdm(creditoComInccOutContempla110, parametroRequestDTO);
        valorCreditoMaisTaxaAdmJanContempla12 = simulacaoService.gerarValorCreditoMaisTaxaAdm(creditoComInccJanContempla12, parametroRequestDTO);
        valorCreditoMaisTaxaAdmJanContempla230 = simulacaoService.gerarValorCreditoMaisTaxaAdm(creditoComInccJanContempla230, parametroRequestDTO);
        valorCreditoMaisTaxaAdmDezContempla1 = simulacaoService.gerarValorCreditoMaisTaxaAdm(creditoComInccDezContempla1, parametroRequestDTO);
    }

    @Test
    void testCreditoAtualizado() {
        ParametroRequestDTO parametroRequestDTO = ParametroRequestDTO.builder()
                .modalidade("CHEIA")
                .valorCredito(new BigDecimal(100000))
                .prazo(240)
                .incc(9.0)
                .taxaAdm(25.0)
                .lance(30.0)
                .build();

        double creditoAtualizadoOutContempla110 = simulacaoService.gerarCreditoAtualizado(creditoComInccOutContempla110,valorCreditoMaisTaxaAdmOutContempla110,parametroRequestDTO).doubleValue();
        double creditoAtualizadoJanContempla12 = simulacaoService.gerarCreditoAtualizado(creditoComInccJanContempla12,valorCreditoMaisTaxaAdmJanContempla12,parametroRequestDTO).doubleValue();
        double creditoAtualizadoJanContempla230 = simulacaoService.gerarCreditoAtualizado(creditoComInccJanContempla230,valorCreditoMaisTaxaAdmJanContempla230,parametroRequestDTO).doubleValue();
        double creditoAtualizadoDezContempla1 = simulacaoService.gerarCreditoAtualizado(creditoComInccDezContempla1,valorCreditoMaisTaxaAdmDezContempla1,parametroRequestDTO).doubleValue();

        assertEquals(135743.33, creditoAtualizadoOutContempla110);
        assertEquals(62500.0, creditoAtualizadoJanContempla12);
        assertEquals(321353.83, creditoAtualizadoJanContempla230);
        assertEquals(62500.0, creditoAtualizadoDezContempla1);
    }

    @Test
    void testInvestimentoMensalCorrigido() {
        double investMenCorrOutContempla110 = simulacaoService.gerarInvestimentoMensalCorrigido(valorCreditoMaisTaxaAdmOutContempla110,parametroRequestDTO).doubleValue();
        double investMenCorrJanContempla12 = simulacaoService.gerarInvestimentoMensalCorrigido(valorCreditoMaisTaxaAdmJanContempla12,parametroRequestDTO).doubleValue();
        double investMenCorrJanContempla230 = simulacaoService.gerarInvestimentoMensalCorrigido(valorCreditoMaisTaxaAdmJanContempla230,parametroRequestDTO).doubleValue();
        double investMenCorrDezContempla1 = simulacaoService.gerarInvestimentoMensalCorrigido(valorCreditoMaisTaxaAdmDezContempla1,parametroRequestDTO).doubleValue();

        assertEquals(1131.19, investMenCorrOutContempla110);
        assertEquals(520.83, investMenCorrJanContempla12);
        assertEquals(2677.95, investMenCorrJanContempla230);
        assertEquals(520.83, investMenCorrDezContempla1);
    }

}
