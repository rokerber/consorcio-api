package br.com.consorcio.service;

import br.com.consorcio.dto.ParametroRequestDTO;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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
    static List<BigDecimal> valCredOutContempla110List = new ArrayList<>();
    static List<BigDecimal> valCredJanContempla12List = new ArrayList<>();
    static List<BigDecimal> valCredJanContempla230List = new ArrayList<>();
    static List<BigDecimal> valCredDezContempla1List = new ArrayList<>();

    static List<BigDecimal> valCredTaxaAdmOutContempla110List = new ArrayList<>();
    static List<BigDecimal> valCredTaxaAdmJanContempla12List = new ArrayList<>();
    static List<BigDecimal> valCredTaxaAdmJanContempla230List = new ArrayList<>();
    static List<BigDecimal> valCredTaxaAdmDezContempla1List = new ArrayList<>();

    static List<BigDecimal> investMenCorrOutContempla110List = new ArrayList<>();
    static List<BigDecimal> investMenCorrJanContempla12List = new ArrayList<>();
    static List<BigDecimal> investMenCorranContempla230List = new ArrayList<>();
    static List<BigDecimal> investMenCorrDezContempla1List = new ArrayList<>();

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

        creditoComInccOutContempla110 = simulacaoService.gerarCreditoComIncc(110, parametroRequestDTO, 10,valCredOutContempla110List);
        valorCreditoMaisTaxaAdmOutContempla110 = simulacaoService.gerarValorCreditoMaisTaxaAdm(creditoComInccOutContempla110, parametroRequestDTO,valCredOutContempla110List,valCredTaxaAdmOutContempla110List);

        creditoComInccJanContempla12 = simulacaoService.gerarCreditoComIncc(12, parametroRequestDTO, 1,valCredJanContempla12List);
        valorCreditoMaisTaxaAdmJanContempla12 = simulacaoService.gerarValorCreditoMaisTaxaAdm(creditoComInccJanContempla12, parametroRequestDTO,valCredJanContempla12List,valCredTaxaAdmJanContempla12List);

        creditoComInccJanContempla230 = simulacaoService.gerarCreditoComIncc(230, parametroRequestDTO, 1,valCredJanContempla230List);
        valorCreditoMaisTaxaAdmJanContempla230 = simulacaoService.gerarValorCreditoMaisTaxaAdm(creditoComInccJanContempla230, parametroRequestDTO,valCredJanContempla230List,valCredTaxaAdmJanContempla230List);

        creditoComInccDezContempla1 = simulacaoService.gerarCreditoComIncc(1, parametroRequestDTO, 12,valCredDezContempla1List);
        valorCreditoMaisTaxaAdmDezContempla1 = simulacaoService.gerarValorCreditoMaisTaxaAdm(creditoComInccDezContempla1, parametroRequestDTO,valCredDezContempla1List,valCredTaxaAdmDezContempla1List);
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
    void testInvestimentoMensalCorrigidoEValorCorrigido() {
        double investMenCorrOutContempla110 = simulacaoService.gerarInvestimentoMensalCorrigido(valorCreditoMaisTaxaAdmOutContempla110,parametroRequestDTO,valCredTaxaAdmOutContempla110List,investMenCorrOutContempla110List).doubleValue();
        double investMenCorrJanContempla12 = simulacaoService.gerarInvestimentoMensalCorrigido(valorCreditoMaisTaxaAdmJanContempla12,parametroRequestDTO,valCredTaxaAdmJanContempla12List,investMenCorrJanContempla12List).doubleValue();
        double investMenCorrJanContempla230 = simulacaoService.gerarInvestimentoMensalCorrigido(valorCreditoMaisTaxaAdmJanContempla230,parametroRequestDTO,valCredTaxaAdmJanContempla230List,investMenCorranContempla230List).doubleValue();
        double investMenCorrDezContempla1 = simulacaoService.gerarInvestimentoMensalCorrigido(valorCreditoMaisTaxaAdmDezContempla1,parametroRequestDTO,valCredTaxaAdmDezContempla1List,investMenCorrDezContempla1List).doubleValue();

        assertEquals(1131.19, investMenCorrOutContempla110);
        assertEquals(520.83, investMenCorrJanContempla12);
        assertEquals(2677.95, investMenCorrJanContempla230);
        assertEquals(520.83, investMenCorrDezContempla1);

        double valorCorridigoOutContempla110 = simulacaoService.gerarValorInvestidoCorrigido(investMenCorrOutContempla110List).doubleValue();
        double valorCorridigoJanContempla12 = simulacaoService.gerarValorInvestidoCorrigido(investMenCorrJanContempla12List).doubleValue();
        double valorCorridigoJanContempla230 = simulacaoService.gerarValorInvestidoCorrigido(investMenCorranContempla230List).doubleValue();
        double valorCorridigoDezContempla1 = simulacaoService.gerarValorInvestidoCorrigido(investMenCorrDezContempla1List).doubleValue();

        assertEquals(89136.98, valorCorridigoOutContempla110);
        assertEquals(6249.96, valorCorridigoJanContempla12);
        assertEquals(292971.06, valorCorridigoJanContempla230);
        assertEquals(520.83, valorCorridigoDezContempla1);

    }


}
