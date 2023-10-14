package br.com.consorcio.service;

import br.com.consorcio.dto.ParametroRequestDTO;
import br.com.consorcio.dto.enums.Modalidade;
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

    @BeforeAll
    static void setup() {
        simulacaoService = new SimulacaoService();
        parametroRequestDTO = ParametroRequestDTO.builder()
                .modalidade(Modalidade.CHEIA)
                .valorCredito(new BigDecimal(100000))
                .prazo(240)
                .incc(9.0)
                .taxaAdm(25.0)
                .lance(30.0)
                .build();

        double taxaAdm = parametroRequestDTO.getTaxaAdm() * 0.01;
        double incc = parametroRequestDTO.getIncc() * 0.01;
        BigDecimal valorCredito = parametroRequestDTO.getValorCredito();

        creditoComInccOutContempla110 = simulacaoService.gerarCreditoComIncc(110, incc, valorCredito,10,valCredOutContempla110List);
        valorCreditoMaisTaxaAdmOutContempla110 = simulacaoService.gerarValorCreditoMaisTaxaAdm(creditoComInccOutContempla110, taxaAdm);

        creditoComInccJanContempla12 = simulacaoService.gerarCreditoComIncc(12, incc, valorCredito,1,valCredJanContempla12List);
        valorCreditoMaisTaxaAdmJanContempla12 = simulacaoService.gerarValorCreditoMaisTaxaAdm(creditoComInccJanContempla12, taxaAdm);

        creditoComInccJanContempla230 = simulacaoService.gerarCreditoComIncc(230, incc, valorCredito, 1,valCredJanContempla230List);
        valorCreditoMaisTaxaAdmJanContempla230 = simulacaoService.gerarValorCreditoMaisTaxaAdm(creditoComInccJanContempla230, taxaAdm);

        creditoComInccDezContempla1 = simulacaoService.gerarCreditoComIncc(1, incc, valorCredito, 12,valCredDezContempla1List);
        valorCreditoMaisTaxaAdmDezContempla1 = simulacaoService.gerarValorCreditoMaisTaxaAdm(creditoComInccDezContempla1,taxaAdm);
    }

    @Test
    void testCreditoAtualizado() {
        double lance = parametroRequestDTO.getLance() * 0.01;

        double creditoAtualizadoOutContempla110 = simulacaoService.gerarCreditoAtualizado(creditoComInccOutContempla110,valorCreditoMaisTaxaAdmOutContempla110, lance).doubleValue();
        double creditoAtualizadoJanContempla12 = simulacaoService.gerarCreditoAtualizado(creditoComInccJanContempla12,valorCreditoMaisTaxaAdmJanContempla12,lance).doubleValue();
        double creditoAtualizadoJanContempla230 = simulacaoService.gerarCreditoAtualizado(creditoComInccJanContempla230,valorCreditoMaisTaxaAdmJanContempla230,lance).doubleValue();
        double creditoAtualizadoDezContempla1 = simulacaoService.gerarCreditoAtualizado(creditoComInccDezContempla1,valorCreditoMaisTaxaAdmDezContempla1,lance).doubleValue();

        assertEquals(135743.33, creditoAtualizadoOutContempla110);
        assertEquals(62500.0, creditoAtualizadoJanContempla12);
        assertEquals(321353.83, creditoAtualizadoJanContempla230);
        assertEquals(62500.0, creditoAtualizadoDezContempla1);
    }

    @Test
    void testInvestimentoMensalCorrigidoEValorCorrigido() {
        Integer prazo = parametroRequestDTO.getPrazo();
        double taxaAdm = parametroRequestDTO.getTaxaAdm() * 0.01;


        double investMenCorrOutContempla110 = simulacaoService.gerarInvestimentoMensalCorrigido(valorCreditoMaisTaxaAdmOutContempla110,prazo,2).doubleValue();
        double investMenCorrJanContempla12 = simulacaoService.gerarInvestimentoMensalCorrigido(valorCreditoMaisTaxaAdmJanContempla12,prazo,2).doubleValue();
        double investMenCorrJanContempla230 = simulacaoService.gerarInvestimentoMensalCorrigido(valorCreditoMaisTaxaAdmJanContempla230,prazo,2).doubleValue();
        double investMenCorrDezContempla1 = simulacaoService.gerarInvestimentoMensalCorrigido(valorCreditoMaisTaxaAdmDezContempla1,prazo,2).doubleValue();

        assertEquals(1131.19, investMenCorrOutContempla110);
        assertEquals(520.83, investMenCorrJanContempla12);
        assertEquals(2677.95, investMenCorrJanContempla230);
        assertEquals(520.83, investMenCorrDezContempla1);

        double valorCorridigoOutContempla110 = simulacaoService.gerarValorInvestidoCorrigido(valCredOutContempla110List,taxaAdm,prazo).doubleValue();
        double valorCorridigoJanContempla12 = simulacaoService.gerarValorInvestidoCorrigido(valCredJanContempla12List,taxaAdm,prazo).doubleValue();
        double valorCorridigoJanContempla230 = simulacaoService.gerarValorInvestidoCorrigido(valCredJanContempla230List,taxaAdm,prazo).doubleValue();
        double valorCorridigoDezContempla1 = simulacaoService.gerarValorInvestidoCorrigido(valCredDezContempla1List,taxaAdm,prazo).doubleValue();

        assertEquals(89137.12, valorCorridigoOutContempla110);
        assertEquals(6250.0, valorCorridigoJanContempla12);
        assertEquals(292971.26, valorCorridigoJanContempla230);
        assertEquals(520.83, valorCorridigoDezContempla1);

    }

    @Test
    void testParcelaPosContemplacao() {
        Integer prazo = parametroRequestDTO.getPrazo();
        double lance = parametroRequestDTO.getLance() * 0.01;

        BigDecimal investMenCorrOutContempla110 = simulacaoService.gerarInvestimentoMensalCorrigido(valorCreditoMaisTaxaAdmOutContempla110,prazo,10);
        BigDecimal investMenCorrJanContempla12 = simulacaoService.gerarInvestimentoMensalCorrigido(valorCreditoMaisTaxaAdmJanContempla12,prazo,10);
        BigDecimal investMenCorrJanContempla230 = simulacaoService.gerarInvestimentoMensalCorrigido(valorCreditoMaisTaxaAdmJanContempla230,prazo,10);
        BigDecimal investMenCorrDezContempla1 = simulacaoService.gerarInvestimentoMensalCorrigido(valorCreditoMaisTaxaAdmDezContempla1,prazo,10);

        double parPosContemplaOutContempla110 = simulacaoService.gerarParcelaPosContemplacao(investMenCorrOutContempla110,Modalidade.CHEIA,110,prazo,lance).doubleValue();
        double parPosContemplaJanContempla12 = simulacaoService.gerarParcelaPosContemplacao(investMenCorrJanContempla12,Modalidade.CHEIA,12,prazo,lance).doubleValue();
        double parPosContemplaJanContempla230 = simulacaoService.gerarParcelaPosContemplacao(investMenCorrJanContempla230,Modalidade.CHEIA,230,prazo,lance).doubleValue();
        double parPosContemplaDezContempla1 = simulacaoService.gerarParcelaPosContemplacao(investMenCorrDezContempla1,Modalidade.CHEIA,1,prazo,lance).doubleValue();

        assertEquals(791.84, parPosContemplaOutContempla110);
        assertEquals(364.58, parPosContemplaJanContempla12);
        assertEquals(1874.56, parPosContemplaJanContempla230);
        assertEquals(364.58, parPosContemplaDezContempla1);

    }


}
