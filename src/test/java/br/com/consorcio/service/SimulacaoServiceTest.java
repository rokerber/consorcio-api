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
    private static final int ESCALA2 = 2;
    private static final int ESCALA10 = 10;

    @BeforeAll
    static void setup() {
        simulacaoService = new SimulacaoService();
        parametroRequestDTO = ParametroRequestDTO.builder()
                .modalidade(Modalidade.CHEIA)
                .valorCredito(new BigDecimal(500000))
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
    void testCreditoAtualizadoEValorVenda() {
        double lance = parametroRequestDTO.getLance() * 0.01;

        double creditoAtualizadoOutContempla110 = simulacaoService.gerarCreditoAtualizado(creditoComInccOutContempla110,valorCreditoMaisTaxaAdmOutContempla110, lance).doubleValue();
        double creditoAtualizadoJanContempla12 = simulacaoService.gerarCreditoAtualizado(creditoComInccJanContempla12,valorCreditoMaisTaxaAdmJanContempla12,lance).doubleValue();
        double creditoAtualizadoJanContempla230 = simulacaoService.gerarCreditoAtualizado(creditoComInccJanContempla230,valorCreditoMaisTaxaAdmJanContempla230,lance).doubleValue();
        double creditoAtualizadoDezContempla1 = simulacaoService.gerarCreditoAtualizado(creditoComInccDezContempla1,valorCreditoMaisTaxaAdmDezContempla1,lance).doubleValue();

        assertEquals(678716.65, creditoAtualizadoOutContempla110);
        assertEquals(312500.0, creditoAtualizadoJanContempla12);
        assertEquals(1606769.14, creditoAtualizadoJanContempla230);
        assertEquals(312500.0, creditoAtualizadoDezContempla1);

        double valorVendaOutContempla110 = simulacaoService.gerarValorVenda(BigDecimal.valueOf(creditoAtualizadoOutContempla110),110,ESCALA2).doubleValue();
        double valorVendaJanContempla12 = simulacaoService.gerarValorVenda(BigDecimal.valueOf(creditoAtualizadoJanContempla12),12,ESCALA2).doubleValue();
        double valorVendaJanContempla230 = simulacaoService.gerarValorVenda(BigDecimal.valueOf(creditoAtualizadoJanContempla230),230,ESCALA2).doubleValue();
        double valorVendaDezContempla1 = simulacaoService.gerarValorVenda(BigDecimal.valueOf(creditoAtualizadoDezContempla1),1,ESCALA2).doubleValue();

        assertEquals(135743.33, valorVendaOutContempla110);
        assertEquals(46875.0, valorVendaJanContempla12);
        assertEquals(321353.83, valorVendaJanContempla230);
        assertEquals(46875.0, valorVendaDezContempla1);

    }

    @Test
    void testInvestimentoMensalCorrigidoEValorCorrigido() {
        Integer prazo = parametroRequestDTO.getPrazo();
        double taxaAdm = parametroRequestDTO.getTaxaAdm() * 0.01;


        double investMenCorrOutContempla110 = simulacaoService.gerarInvestimentoMensalCorrigido(valorCreditoMaisTaxaAdmOutContempla110,prazo,ESCALA2).doubleValue();
        double investMenCorrJanContempla12 = simulacaoService.gerarInvestimentoMensalCorrigido(valorCreditoMaisTaxaAdmJanContempla12,prazo,ESCALA2).doubleValue();
        double investMenCorrJanContempla230 = simulacaoService.gerarInvestimentoMensalCorrigido(valorCreditoMaisTaxaAdmJanContempla230,prazo,ESCALA2).doubleValue();
        double investMenCorrDezContempla1 = simulacaoService.gerarInvestimentoMensalCorrigido(valorCreditoMaisTaxaAdmDezContempla1,prazo,ESCALA2).doubleValue();

        assertEquals(5655.97, investMenCorrOutContempla110);
        assertEquals(2604.17, investMenCorrJanContempla12);
        assertEquals(13389.74, investMenCorrJanContempla230);
        assertEquals(2604.17, investMenCorrDezContempla1);

        double valorCorridigoOutContempla110 = simulacaoService.gerarValorInvestidoCorrigido(valCredOutContempla110List,taxaAdm,prazo,ESCALA2).doubleValue();
        double valorCorridigoJanContempla12 = simulacaoService.gerarValorInvestidoCorrigido(valCredJanContempla12List,taxaAdm,prazo,ESCALA2).doubleValue();
        double valorCorridigoJanContempla230 = simulacaoService.gerarValorInvestidoCorrigido(valCredJanContempla230List,taxaAdm,prazo,ESCALA2).doubleValue();
        double valorCorridigoDezContempla1 = simulacaoService.gerarValorInvestidoCorrigido(valCredDezContempla1List,taxaAdm,prazo,ESCALA2).doubleValue();

        assertEquals(445685.58, valorCorridigoOutContempla110);
        assertEquals(31250.0, valorCorridigoJanContempla12);
        assertEquals(1464856.31, valorCorridigoJanContempla230);
        assertEquals(2604.17, valorCorridigoDezContempla1);
    }

    @Test
    void testParcelaPosContemplacao() {
        Integer prazo = parametroRequestDTO.getPrazo();
        double lance = parametroRequestDTO.getLance() * 0.01;

        BigDecimal investMenCorrOutContempla110 = simulacaoService.gerarInvestimentoMensalCorrigido(valorCreditoMaisTaxaAdmOutContempla110,prazo,ESCALA10);
        BigDecimal investMenCorrJanContempla12 = simulacaoService.gerarInvestimentoMensalCorrigido(valorCreditoMaisTaxaAdmJanContempla12,prazo,ESCALA10);
        BigDecimal investMenCorrJanContempla230 = simulacaoService.gerarInvestimentoMensalCorrigido(valorCreditoMaisTaxaAdmJanContempla230,prazo,ESCALA10);
        BigDecimal investMenCorrDezContempla1 = simulacaoService.gerarInvestimentoMensalCorrigido(valorCreditoMaisTaxaAdmDezContempla1,prazo,ESCALA10);

        double parPosContemplaOutContempla110 = simulacaoService.gerarParcelaPosContemplacao(investMenCorrOutContempla110,Modalidade.CHEIA,110,prazo,lance).doubleValue();
        double parPosContemplaJanContempla12 = simulacaoService.gerarParcelaPosContemplacao(investMenCorrJanContempla12,Modalidade.CHEIA,12,prazo,lance).doubleValue();
        double parPosContemplaJanContempla230 = simulacaoService.gerarParcelaPosContemplacao(investMenCorrJanContempla230,Modalidade.CHEIA,230,prazo,lance).doubleValue();
        double parPosContemplaDezContempla1 = simulacaoService.gerarParcelaPosContemplacao(investMenCorrDezContempla1,Modalidade.CHEIA,1,prazo,lance).doubleValue();

        assertEquals(3959.18, parPosContemplaOutContempla110);
        assertEquals(1822.92, parPosContemplaJanContempla12);
        assertEquals(9372.82, parPosContemplaJanContempla230);
        assertEquals(1822.92, parPosContemplaDezContempla1);
    }

    @Test
    void testGerarIR() {
        double lance = parametroRequestDTO.getLance() * 0.01;
        Integer prazo = parametroRequestDTO.getPrazo();
        double taxaAdm = parametroRequestDTO.getTaxaAdm() * 0.01;

        BigDecimal creditoAtualizadoOutContempla110 = simulacaoService.gerarCreditoAtualizado(creditoComInccOutContempla110,valorCreditoMaisTaxaAdmOutContempla110, lance);
        BigDecimal creditoAtualizadoJanContempla12 = simulacaoService.gerarCreditoAtualizado(creditoComInccJanContempla12,valorCreditoMaisTaxaAdmJanContempla12,lance);
        BigDecimal creditoAtualizadoJanContempla230 = simulacaoService.gerarCreditoAtualizado(creditoComInccJanContempla230,valorCreditoMaisTaxaAdmJanContempla230,lance);
        BigDecimal creditoAtualizadoDezContempla1 = simulacaoService.gerarCreditoAtualizado(creditoComInccDezContempla1,valorCreditoMaisTaxaAdmDezContempla1,lance);

        BigDecimal valorVendaOutContempla110 = simulacaoService.gerarValorVenda(creditoAtualizadoOutContempla110,110,ESCALA2);
        BigDecimal valorVendaJanContempla12 = simulacaoService.gerarValorVenda(creditoAtualizadoJanContempla12,12,ESCALA2);
        BigDecimal valorVendaJanContempla230 = simulacaoService.gerarValorVenda(creditoAtualizadoJanContempla230,230,ESCALA2);
        BigDecimal valorVendaDezContempla1 = simulacaoService.gerarValorVenda(creditoAtualizadoDezContempla1,1,ESCALA2);

        BigDecimal valorCorridigoOutContempla110 = simulacaoService.gerarValorInvestidoCorrigido(valCredOutContempla110List,taxaAdm,prazo,ESCALA2);
        BigDecimal valorCorridigoJanContempla12 = simulacaoService.gerarValorInvestidoCorrigido(valCredJanContempla12List,taxaAdm,prazo,ESCALA2);
        BigDecimal valorCorridigoJanContempla230 = simulacaoService.gerarValorInvestidoCorrigido(valCredJanContempla230List,taxaAdm,prazo,ESCALA2);
        BigDecimal valorCorridigoDezContempla1 = simulacaoService.gerarValorInvestidoCorrigido(valCredDezContempla1List,taxaAdm,prazo,ESCALA2);

        double valorIROutContempla110 = simulacaoService.gerarIR(valorVendaOutContempla110,valorCorridigoOutContempla110,110,ESCALA2).doubleValue();
        double valorIRJanContempla12 = simulacaoService.gerarIR(valorVendaJanContempla12,valorCorridigoJanContempla12,12,ESCALA2).doubleValue();
        double valorIRJanContempla230 = simulacaoService.gerarIR(valorVendaJanContempla230,valorCorridigoJanContempla230,230,ESCALA2).doubleValue();
        double valorIRDezContempla1 = simulacaoService.gerarIR(valorVendaDezContempla1,valorCorridigoDezContempla1,1,ESCALA2).doubleValue();

        assertEquals(0.0, valorIROutContempla110);
        assertEquals(0.0, valorIRJanContempla12);
        assertEquals(0.0, valorIRJanContempla230);
        assertEquals(9960.94, valorIRDezContempla1);

    }

}
