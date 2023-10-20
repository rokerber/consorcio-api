package br.com.consorcio.service;

import br.com.consorcio.dto.ParametroRequestDTO;
import br.com.consorcio.enums.Modalidade;
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
                .valorCredito(BigDecimal.valueOf(500000))
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

        BigDecimal creditoAtualizadoOutContempla110 = simulacaoService.gerarCreditoAtualizado(creditoComInccOutContempla110,valorCreditoMaisTaxaAdmOutContempla110, lance);
        BigDecimal creditoAtualizadoJanContempla12 = simulacaoService.gerarCreditoAtualizado(creditoComInccJanContempla12,valorCreditoMaisTaxaAdmJanContempla12,lance);
        BigDecimal creditoAtualizadoJanContempla230 = simulacaoService.gerarCreditoAtualizado(creditoComInccJanContempla230,valorCreditoMaisTaxaAdmJanContempla230,lance);
        BigDecimal creditoAtualizadoDezContempla1 = simulacaoService.gerarCreditoAtualizado(creditoComInccDezContempla1,valorCreditoMaisTaxaAdmDezContempla1,lance);

        assertEquals(0, BigDecimal.valueOf(678716.65).compareTo(creditoAtualizadoOutContempla110));
        assertEquals(0, BigDecimal.valueOf(312500.0).compareTo(creditoAtualizadoJanContempla12));
        assertEquals(0, BigDecimal.valueOf(1606769.14).compareTo(creditoAtualizadoJanContempla230));
        assertEquals(0, BigDecimal.valueOf(312500.0).compareTo(creditoAtualizadoDezContempla1));

        BigDecimal valorVendaOutContempla110 = simulacaoService.gerarValorVenda(creditoAtualizadoOutContempla110,110,ESCALA2);
        BigDecimal valorVendaJanContempla12 = simulacaoService.gerarValorVenda(creditoAtualizadoJanContempla12,12,ESCALA2);
        BigDecimal valorVendaJanContempla230 = simulacaoService.gerarValorVenda(creditoAtualizadoJanContempla230,230,ESCALA2);
        BigDecimal valorVendaDezContempla1 = simulacaoService.gerarValorVenda(creditoAtualizadoDezContempla1,1,ESCALA2);

        assertEquals(0, BigDecimal.valueOf(135743.33).compareTo(valorVendaOutContempla110));
        assertEquals(0, BigDecimal.valueOf(46875.0).compareTo(valorVendaJanContempla12));
        assertEquals(0, BigDecimal.valueOf(321353.83).compareTo(valorVendaJanContempla230));
        assertEquals(0, BigDecimal.valueOf(46875.0).compareTo(valorVendaDezContempla1));

    }

    @Test
    void testInvestimentoMensalCorrigidoEValorCorrigido() {
        Integer prazo = parametroRequestDTO.getPrazo();
        double taxaAdm = parametroRequestDTO.getTaxaAdm() * 0.01;


        BigDecimal investMenCorrOutContempla110 = simulacaoService.gerarInvestimentoMensalCorrigido(valorCreditoMaisTaxaAdmOutContempla110,prazo,ESCALA2);
        BigDecimal investMenCorrJanContempla12 = simulacaoService.gerarInvestimentoMensalCorrigido(valorCreditoMaisTaxaAdmJanContempla12,prazo,ESCALA2);
        BigDecimal investMenCorrJanContempla230 = simulacaoService.gerarInvestimentoMensalCorrigido(valorCreditoMaisTaxaAdmJanContempla230,prazo,ESCALA2);
        BigDecimal investMenCorrDezContempla1 = simulacaoService.gerarInvestimentoMensalCorrigido(valorCreditoMaisTaxaAdmDezContempla1,prazo,ESCALA2);

        assertEquals(0, BigDecimal.valueOf(5655.97).compareTo(investMenCorrOutContempla110));
        assertEquals(0, BigDecimal.valueOf(2604.17).compareTo(investMenCorrJanContempla12));
        assertEquals(0, BigDecimal.valueOf(13389.74).compareTo(investMenCorrJanContempla230));
        assertEquals(0, BigDecimal.valueOf(2604.17).compareTo(investMenCorrDezContempla1));

        BigDecimal valorCorridigoOutContempla110 = simulacaoService.gerarValorInvestidoCorrigido(valCredOutContempla110List,taxaAdm,prazo,ESCALA2);
        BigDecimal valorCorridigoJanContempla12 = simulacaoService.gerarValorInvestidoCorrigido(valCredJanContempla12List,taxaAdm,prazo,ESCALA2);
        BigDecimal valorCorridigoJanContempla230 = simulacaoService.gerarValorInvestidoCorrigido(valCredJanContempla230List,taxaAdm,prazo,ESCALA2);
        BigDecimal valorCorridigoDezContempla1 = simulacaoService.gerarValorInvestidoCorrigido(valCredDezContempla1List,taxaAdm,prazo,ESCALA2);

        assertEquals(0, BigDecimal.valueOf(445685.58).compareTo(valorCorridigoOutContempla110));
        assertEquals(0, BigDecimal.valueOf(31250.0).compareTo(valorCorridigoJanContempla12));
        assertEquals(0, BigDecimal.valueOf(1464856.31).compareTo(valorCorridigoJanContempla230));
        assertEquals(0, BigDecimal.valueOf(2604.17).compareTo(valorCorridigoDezContempla1));
    }

    @Test
    void testParcelaPosContemplacao() {
        Integer prazo = parametroRequestDTO.getPrazo();
        double lance = parametroRequestDTO.getLance() * 0.01;

        BigDecimal investMenCorrOutContempla110 = simulacaoService.gerarInvestimentoMensalCorrigido(valorCreditoMaisTaxaAdmOutContempla110,prazo,ESCALA10);
        BigDecimal investMenCorrJanContempla12 = simulacaoService.gerarInvestimentoMensalCorrigido(valorCreditoMaisTaxaAdmJanContempla12,prazo,ESCALA10);
        BigDecimal investMenCorrJanContempla230 = simulacaoService.gerarInvestimentoMensalCorrigido(valorCreditoMaisTaxaAdmJanContempla230,prazo,ESCALA10);
        BigDecimal investMenCorrDezContempla1 = simulacaoService.gerarInvestimentoMensalCorrigido(valorCreditoMaisTaxaAdmDezContempla1,prazo,ESCALA10);

        BigDecimal parPosContemplaOutContempla110 = simulacaoService.gerarParcelaPosContemplacao(investMenCorrOutContempla110,Modalidade.CHEIA,110,prazo,lance);
        BigDecimal parPosContemplaJanContempla12 = simulacaoService.gerarParcelaPosContemplacao(investMenCorrJanContempla12,Modalidade.CHEIA,12,prazo,lance);
        BigDecimal parPosContemplaJanContempla230 = simulacaoService.gerarParcelaPosContemplacao(investMenCorrJanContempla230,Modalidade.CHEIA,230,prazo,lance);
        BigDecimal parPosContemplaDezContempla1 = simulacaoService.gerarParcelaPosContemplacao(investMenCorrDezContempla1,Modalidade.CHEIA,1,prazo,lance);

        assertEquals(0, BigDecimal.valueOf(3959.18).compareTo(parPosContemplaOutContempla110));
        assertEquals(0, BigDecimal.valueOf(1822.92).compareTo(parPosContemplaJanContempla12));
        assertEquals(0, BigDecimal.valueOf(9372.82).compareTo(parPosContemplaJanContempla230));
        assertEquals(0, BigDecimal.valueOf(1822.92).compareTo(parPosContemplaDezContempla1));
    }

    @Test
    void testGerarIRELucroLiquidoERetornoSobreCapitalInvestido() {
        double lance = parametroRequestDTO.getLance() * 0.01;
        Integer prazo = parametroRequestDTO.getPrazo();
        double taxaAdm = parametroRequestDTO.getTaxaAdm() * 0.01;

        BigDecimal creditoAtualizadoOutContempla110 = simulacaoService.gerarCreditoAtualizado(creditoComInccOutContempla110,valorCreditoMaisTaxaAdmOutContempla110, lance);
        BigDecimal creditoAtualizadoJanContempla12 = simulacaoService.gerarCreditoAtualizado(creditoComInccJanContempla12,valorCreditoMaisTaxaAdmJanContempla12,lance);
        BigDecimal creditoAtualizadoJanContempla230 = simulacaoService.gerarCreditoAtualizado(creditoComInccJanContempla230,valorCreditoMaisTaxaAdmJanContempla230,lance);
        BigDecimal creditoAtualizadoDezContempla1 = simulacaoService.gerarCreditoAtualizado(creditoComInccDezContempla1,valorCreditoMaisTaxaAdmDezContempla1,lance);

        BigDecimal valorVendaOutContempla110 = simulacaoService.gerarValorVenda(creditoAtualizadoOutContempla110,110,ESCALA10);
        BigDecimal valorVendaJanContempla12 = simulacaoService.gerarValorVenda(creditoAtualizadoJanContempla12,12,ESCALA10);
        BigDecimal valorVendaJanContempla230 = simulacaoService.gerarValorVenda(creditoAtualizadoJanContempla230,230,ESCALA10);
        BigDecimal valorVendaDezContempla1 = simulacaoService.gerarValorVenda(creditoAtualizadoDezContempla1,1,ESCALA10);

        BigDecimal valorCorridigoOutContempla110 = simulacaoService.gerarValorInvestidoCorrigido(valCredOutContempla110List,taxaAdm,prazo,ESCALA10);
        BigDecimal valorCorridigoJanContempla12 = simulacaoService.gerarValorInvestidoCorrigido(valCredJanContempla12List,taxaAdm,prazo,ESCALA10);
        BigDecimal valorCorridigoJanContempla230 = simulacaoService.gerarValorInvestidoCorrigido(valCredJanContempla230List,taxaAdm,prazo,ESCALA10);
        BigDecimal valorCorridigoDezContempla1 = simulacaoService.gerarValorInvestidoCorrigido(valCredDezContempla1List,taxaAdm,prazo,ESCALA10);

        BigDecimal valorIROutContempla110 = simulacaoService.gerarIR(valorVendaOutContempla110,valorCorridigoOutContempla110,110,ESCALA10);
        BigDecimal valorIRJanContempla12 = simulacaoService.gerarIR(valorVendaJanContempla12,valorCorridigoJanContempla12,12,ESCALA10);
        BigDecimal valorIRJanContempla230 = simulacaoService.gerarIR(valorVendaJanContempla230,valorCorridigoJanContempla230,230,ESCALA10);
        BigDecimal valorIRDezContempla1Escala2 = simulacaoService.gerarIR(valorVendaDezContempla1,valorCorridigoDezContempla1,1,ESCALA2);

        assertEquals(0, BigDecimal.ZERO.compareTo(valorIROutContempla110));
        assertEquals(0, BigDecimal.ZERO.compareTo(valorIRJanContempla12));
        assertEquals(0, BigDecimal.ZERO.compareTo(valorIRJanContempla230));
        assertEquals(0, BigDecimal.valueOf(9960.94).compareTo(valorIRDezContempla1Escala2));

        BigDecimal valorIRDezContempla1Escala10 = simulacaoService.gerarIR(valorVendaDezContempla1,valorCorridigoDezContempla1,1,ESCALA10);

        BigDecimal lucroLiquidoOutContempla110 = simulacaoService.gerarLucroLiquido(valorVendaOutContempla110,valorIROutContempla110,valorCorridigoOutContempla110,ESCALA2);
        BigDecimal lucroLiquidoJanContempla12 = simulacaoService.gerarLucroLiquido(valorVendaJanContempla12,valorIRJanContempla12,valorCorridigoJanContempla12,ESCALA2);
        BigDecimal lucroLiquidoJanContempla230 = simulacaoService.gerarLucroLiquido(valorVendaJanContempla230,valorIRJanContempla230,valorCorridigoJanContempla230,ESCALA2);
        BigDecimal lucroLiquidoDezContempla1 = simulacaoService.gerarLucroLiquido(valorVendaDezContempla1,valorIRDezContempla1Escala10,valorCorridigoDezContempla1,ESCALA2);

        assertEquals(0, BigDecimal.valueOf(-309942.25).compareTo(lucroLiquidoOutContempla110));
        assertEquals(0, BigDecimal.valueOf(15625.00).compareTo(lucroLiquidoJanContempla12));
        assertEquals(0, BigDecimal.valueOf(-1143502.48).compareTo(lucroLiquidoJanContempla230));
        assertEquals(0, BigDecimal.valueOf(34309.90).compareTo(lucroLiquidoDezContempla1));

        BigDecimal retornSobCapitalInvestOutContempla110 = simulacaoService.gerarRetornoSobreCapitalInvestido(lucroLiquidoOutContempla110,valorCorridigoOutContempla110);
        BigDecimal retornSobCapitalInvestJanContempla12 = simulacaoService.gerarRetornoSobreCapitalInvestido(lucroLiquidoJanContempla12,valorCorridigoJanContempla12);
        BigDecimal retornSobCapitalInvestJanContempla230 = simulacaoService.gerarRetornoSobreCapitalInvestido(lucroLiquidoJanContempla230,valorCorridigoJanContempla230);
        BigDecimal retornSobCapitalInvestDezContempla1 = simulacaoService.gerarRetornoSobreCapitalInvestido(lucroLiquidoDezContempla1,valorCorridigoDezContempla1);

        assertEquals(0, BigDecimal.valueOf(-69.54).compareTo(retornSobCapitalInvestOutContempla110));
        assertEquals(0, BigDecimal.valueOf(50.00).compareTo(retornSobCapitalInvestJanContempla12));
        assertEquals(0, BigDecimal.valueOf(-78.06).compareTo(retornSobCapitalInvestJanContempla230));
        assertEquals(0, BigDecimal.valueOf(1317.50).compareTo(retornSobCapitalInvestDezContempla1));
    }

    @Test
    void testSetarEstrategia() {
        String estragediaCisRg = simulacaoService.setarEstrategia(BigDecimal.valueOf(1),10,240);
        String estragediaCipRp = simulacaoService.setarEstrategia(BigDecimal.valueOf(-1),10,240);
        String estragediaPrevTurb = simulacaoService.setarEstrategia(BigDecimal.valueOf(0),240,240);

        assertEquals("CIS-RG", estragediaCisRg);
        assertEquals("CIP-RP", estragediaCipRp);
        assertEquals("PREV TURBINADA", estragediaPrevTurb);
    }


}
