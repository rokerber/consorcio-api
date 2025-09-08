package br.com.consorcio.service;

import br.com.consorcio.dto.ParametroRequestDTO;
import br.com.consorcio.enums.Modalidade;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SimulacaoServiceTest {

    static SimulacaoService simulacaoService;
    static ParametroRequestDTO parametroRequestDTO;
    static BigDecimal creditoComInccOutContempla110;
    static BigDecimal creditoComInccJanContempla12;
    static BigDecimal creditoComInccJanContempla230;
    static BigDecimal creditoComInccDezContempla1;
    static BigDecimal creditoComInccJunContempla44;
    static BigDecimal valorCreditoMaisTaxaAdmOutContempla110;
    static BigDecimal valorCreditoMaisTaxaAdmJanContempla12;
    static BigDecimal valorCreditoMaisTaxaAdmJanContempla230;
    static BigDecimal valorCreditoMaisTaxaAdmDezContempla1;
    static BigDecimal valorCreditoMaisTaxaAdmJunContempla44;
    static List<BigDecimal> valCredOutContempla110List = new ArrayList<>();
    static List<BigDecimal> valCredJanContempla12List = new ArrayList<>();
    static List<BigDecimal> valCredJanContempla230List = new ArrayList<>();
    static List<BigDecimal> valCredDezContempla1List = new ArrayList<>();
    static Set<BigDecimal> invMenOutContempla110Set = new LinkedHashSet<>();
    static Set<BigDecimal> invMenJanContempla12Set = new LinkedHashSet<>();
    static Set<BigDecimal> invMenJanContempla230Set = new LinkedHashSet<>();
    static Set<BigDecimal> invMenDezContempla1Set = new LinkedHashSet<>();
    private static final int ESCALA2 = 2;
    private static final int ESCALA10 = 10;

    @BeforeAll
    static void setup() {
        simulacaoService = new SimulacaoService();
        parametroRequestDTO = ParametroRequestDTO.builder()
                .cota(2)
                .mesContemplacaoList(List.of(1))
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

        creditoComInccJunContempla44 = simulacaoService.gerarCreditoComIncc(44, incc, valorCredito, 6,valCredDezContempla1List);
        valorCreditoMaisTaxaAdmJunContempla44 = simulacaoService.gerarValorCreditoMaisTaxaAdm(creditoComInccDezContempla1,taxaAdm);
    }

    @Test
    void testCreditoAtualizadoEValorVenda() {
        double lance = parametroRequestDTO.getLance() * 0.01;

        BigDecimal creditoAtualizadoOutContempla110 = simulacaoService.gerarCreditoAtualizado(creditoComInccOutContempla110,valorCreditoMaisTaxaAdmOutContempla110,lance,ESCALA2);
        BigDecimal creditoAtualizadoJanContempla12 = simulacaoService.gerarCreditoAtualizado(creditoComInccJanContempla12,valorCreditoMaisTaxaAdmJanContempla12,lance,ESCALA2);
        BigDecimal creditoAtualizadoJanContempla230 = simulacaoService.gerarCreditoAtualizado(creditoComInccJanContempla230,valorCreditoMaisTaxaAdmJanContempla230,lance,ESCALA2);
        BigDecimal creditoAtualizadoDezContempla1 = simulacaoService.gerarCreditoAtualizado(creditoComInccDezContempla1,valorCreditoMaisTaxaAdmDezContempla1,lance,ESCALA2);
        BigDecimal creditoAtualizadoJunContempla44 = simulacaoService.gerarCreditoAtualizado(creditoComInccJunContempla44,valorCreditoMaisTaxaAdmJunContempla44,lance,ESCALA2);


        assertEquals(new BigDecimal("678716.65"), creditoAtualizadoOutContempla110);
        assertEquals(new BigDecimal("312500.00"), creditoAtualizadoJanContempla12);
        assertEquals(new BigDecimal("1606769.14"), creditoAtualizadoJanContempla230);
        assertEquals(new BigDecimal("312500.00"), creditoAtualizadoDezContempla1);
        assertEquals(new BigDecimal("518290.80"), creditoAtualizadoJunContempla44);


        BigDecimal valorVendaOutContempla110 = simulacaoService.gerarValorVenda(creditoAtualizadoOutContempla110,110,ESCALA2);
        BigDecimal valorVendaJanContempla12 = simulacaoService.gerarValorVenda(creditoAtualizadoJanContempla12,12,ESCALA2);
        BigDecimal valorVendaJanContempla230 = simulacaoService.gerarValorVenda(creditoAtualizadoJanContempla230,230,ESCALA2);
        BigDecimal valorVendaDezContempla1 = simulacaoService.gerarValorVenda(creditoAtualizadoDezContempla1,1,ESCALA2);

        assertEquals(new BigDecimal("135743.33"), valorVendaOutContempla110);
        assertEquals(new BigDecimal("46875.00"), valorVendaJanContempla12);
        assertEquals(new BigDecimal("321353.83"), valorVendaJanContempla230);
        assertEquals(new BigDecimal("46875.00"), valorVendaDezContempla1);

    }

    @Test
    void testInvestimentoMensalCorrigidoEValorCorrigido() {
        Integer prazo = parametroRequestDTO.getPrazo();
        double taxaAdm = parametroRequestDTO.getTaxaAdm() * 0.01;

        BigDecimal investMenCorrOutContempla110Cheia = simulacaoService.gerarInvestimentoMensalCorrigido(valorCreditoMaisTaxaAdmOutContempla110,prazo,ESCALA2,Modalidade.CHEIA);
        BigDecimal investMenCorrJanContempla12Cheia = simulacaoService.gerarInvestimentoMensalCorrigido(valorCreditoMaisTaxaAdmJanContempla12,prazo,ESCALA2,Modalidade.CHEIA);
        BigDecimal investMenCorrJanContempla230Cheia = simulacaoService.gerarInvestimentoMensalCorrigido(valorCreditoMaisTaxaAdmJanContempla230,prazo,ESCALA2,Modalidade.CHEIA);
        BigDecimal investMenCorrDezContempla1Cheia = simulacaoService.gerarInvestimentoMensalCorrigido(valorCreditoMaisTaxaAdmDezContempla1,prazo,ESCALA2,Modalidade.CHEIA);

        assertEquals(new BigDecimal("5655.97"), investMenCorrOutContempla110Cheia);
        assertEquals(new BigDecimal("2604.17"), investMenCorrJanContempla12Cheia);
        assertEquals(new BigDecimal("13389.74"), investMenCorrJanContempla230Cheia);
        assertEquals(new BigDecimal("2604.17"), investMenCorrDezContempla1Cheia);

        BigDecimal investMenCorrOutContempla110Meia = simulacaoService.gerarInvestimentoMensalCorrigido(valorCreditoMaisTaxaAdmOutContempla110,prazo,ESCALA2,Modalidade.MEIA);
        BigDecimal investMenCorrJanContempla12Meia = simulacaoService.gerarInvestimentoMensalCorrigido(valorCreditoMaisTaxaAdmJanContempla12,prazo,ESCALA2,Modalidade.MEIA);
        BigDecimal investMenCorrJanContempla230Meia = simulacaoService.gerarInvestimentoMensalCorrigido(valorCreditoMaisTaxaAdmJanContempla230,prazo,ESCALA2,Modalidade.MEIA);
        BigDecimal investMenCorrDezContempla1Meia = simulacaoService.gerarInvestimentoMensalCorrigido(valorCreditoMaisTaxaAdmDezContempla1,prazo,ESCALA2,Modalidade.MEIA);

        assertEquals(new BigDecimal("2827.99"), investMenCorrOutContempla110Meia);
        assertEquals(new BigDecimal("1302.08"), investMenCorrJanContempla12Meia);
        assertEquals(new BigDecimal("6694.87"), investMenCorrJanContempla230Meia);
        assertEquals(new BigDecimal("1302.08"), investMenCorrDezContempla1Meia);

        BigDecimal valorCorridigoOutContempla110Cheia = simulacaoService.gerarValorInvestidoCorrigido(valCredOutContempla110List,invMenOutContempla110Set,taxaAdm,prazo,ESCALA2,Modalidade.CHEIA);
        BigDecimal valorCorridigoJanContempla12Cheia = simulacaoService.gerarValorInvestidoCorrigido(valCredJanContempla12List,invMenJanContempla12Set,taxaAdm,prazo,ESCALA2,Modalidade.CHEIA);
        BigDecimal valorCorridigoJanContempla230Cheia = simulacaoService.gerarValorInvestidoCorrigido(valCredJanContempla230List,invMenJanContempla230Set,taxaAdm,prazo,ESCALA2,Modalidade.CHEIA);
        BigDecimal valorCorridigoDezContempla1Cheia = simulacaoService.gerarValorInvestidoCorrigido(valCredDezContempla1List,invMenDezContempla1Set,taxaAdm,prazo,ESCALA2,Modalidade.CHEIA);

        assertEquals(new BigDecimal("445685.58"), valorCorridigoOutContempla110Cheia);
        assertEquals(new BigDecimal("31250.00"), valorCorridigoJanContempla12Cheia);
        assertEquals(new BigDecimal("1464856.31"), valorCorridigoJanContempla230Cheia);
        assertEquals(new BigDecimal("136169.61"), valorCorridigoDezContempla1Cheia);

        BigDecimal valorCorridigoOutContempla110Meia = simulacaoService.gerarValorInvestidoCorrigido(valCredOutContempla110List,invMenOutContempla110Set,taxaAdm,prazo,ESCALA2,Modalidade.MEIA);
        BigDecimal valorCorridigoJanContempla12Meia = simulacaoService.gerarValorInvestidoCorrigido(valCredJanContempla12List,invMenJanContempla12Set,taxaAdm,prazo,ESCALA2,Modalidade.MEIA);
        BigDecimal valorCorridigoJanContempla230Meia = simulacaoService.gerarValorInvestidoCorrigido(valCredJanContempla230List,invMenJanContempla230Set,taxaAdm,prazo,ESCALA2,Modalidade.MEIA);
        BigDecimal valorCorridigoDezContempla1Meia = simulacaoService.gerarValorInvestidoCorrigido(valCredDezContempla1List,invMenDezContempla1Set,taxaAdm,prazo,ESCALA2,Modalidade.MEIA);

        assertEquals(BigDecimal.valueOf(222842.79),valorCorridigoOutContempla110Meia);
        assertEquals(new BigDecimal("15625.00"), valorCorridigoJanContempla12Meia);
        assertEquals(new BigDecimal("732428.16"), valorCorridigoJanContempla230Meia);
        assertEquals(new BigDecimal("68084.80"), valorCorridigoDezContempla1Meia);
    }

    @Test
    void testParcelaPosContemplacao() {
        Integer prazo = parametroRequestDTO.getPrazo();
        double lance = parametroRequestDTO.getLance() * 0.01;
        double taxaAdm = parametroRequestDTO.getTaxaAdm() * 0.01;


        BigDecimal investMenCorrOutContempla110 = simulacaoService.gerarInvestimentoMensalCorrigido(valorCreditoMaisTaxaAdmOutContempla110,prazo,ESCALA10,Modalidade.CHEIA);
        BigDecimal investMenCorrJanContempla12 = simulacaoService.gerarInvestimentoMensalCorrigido(valorCreditoMaisTaxaAdmJanContempla12,prazo,ESCALA10,Modalidade.CHEIA);
        BigDecimal investMenCorrJanContempla230 = simulacaoService.gerarInvestimentoMensalCorrigido(valorCreditoMaisTaxaAdmJanContempla230,prazo,ESCALA10,Modalidade.CHEIA);
        BigDecimal investMenCorrDezContempla1 = simulacaoService.gerarInvestimentoMensalCorrigido(valorCreditoMaisTaxaAdmDezContempla1,prazo,ESCALA10,Modalidade.CHEIA);

        BigDecimal valorCorridigoOutContempla110Meia = simulacaoService.gerarValorInvestidoCorrigido(valCredOutContempla110List,invMenOutContempla110Set,taxaAdm,prazo,ESCALA10,Modalidade.MEIA);
        BigDecimal valorCorridigoJanContempla12Meia = simulacaoService.gerarValorInvestidoCorrigido(valCredJanContempla12List,invMenJanContempla12Set,taxaAdm,prazo,ESCALA10,Modalidade.MEIA);
        BigDecimal valorCorridigoJanContempla230Meia = simulacaoService.gerarValorInvestidoCorrigido(valCredJanContempla230List,invMenJanContempla230Set,taxaAdm,prazo,ESCALA10,Modalidade.MEIA);
        BigDecimal valorCorridigoDezContempla1Meia = simulacaoService.gerarValorInvestidoCorrigido(valCredDezContempla1List,invMenDezContempla1Set,taxaAdm,prazo,ESCALA10,Modalidade.MEIA);

        BigDecimal parPosContemplaOutContempla110Cheia = simulacaoService.gerarParcelaPosContemplacao(investMenCorrOutContempla110,invMenOutContempla110Set,valorCorridigoOutContempla110Meia,Modalidade.CHEIA,110,prazo,lance,taxaAdm);
        BigDecimal parPosContemplaJanContempla12Cheia = simulacaoService.gerarParcelaPosContemplacao(investMenCorrJanContempla12,invMenJanContempla12Set,valorCorridigoJanContempla12Meia,Modalidade.CHEIA,12,prazo,lance,taxaAdm);
        BigDecimal parPosContemplaJanContempla230Cheia = simulacaoService.gerarParcelaPosContemplacao(investMenCorrJanContempla230,invMenJanContempla230Set,valorCorridigoJanContempla230Meia,Modalidade.CHEIA,230,prazo,lance,taxaAdm);
        BigDecimal parPosContemplaDezContempla1Cheia = simulacaoService.gerarParcelaPosContemplacao(investMenCorrDezContempla1,invMenDezContempla1Set,valorCorridigoDezContempla1Meia,Modalidade.CHEIA,1,prazo,lance,taxaAdm);

        assertEquals(new BigDecimal("3959.18"), parPosContemplaOutContempla110Cheia);
        assertEquals(new BigDecimal("1822.92"), parPosContemplaJanContempla12Cheia);
        assertEquals(new BigDecimal("9372.82"), parPosContemplaJanContempla230Cheia);
        assertEquals(new BigDecimal("1822.92"), parPosContemplaDezContempla1Cheia);

        BigDecimal parPosContemplaOutContempla110Meia = simulacaoService.gerarParcelaPosContemplacao(investMenCorrOutContempla110,invMenOutContempla110Set,valorCorridigoOutContempla110Meia,Modalidade.MEIA,110,prazo,lance,taxaAdm);
        BigDecimal parPosContemplaJanContempla12Meia = simulacaoService.gerarParcelaPosContemplacao(investMenCorrJanContempla12,invMenJanContempla12Set,valorCorridigoJanContempla12Meia,Modalidade.MEIA,12,prazo,lance,taxaAdm);
        BigDecimal parPosContemplaJanContempla230Meia = simulacaoService.gerarParcelaPosContemplacao(investMenCorrJanContempla230,invMenJanContempla230Set,valorCorridigoJanContempla230Meia,Modalidade.MEIA,230,prazo,lance,taxaAdm);
        BigDecimal parPosContemplaDezContempla1Meia = simulacaoService.gerarParcelaPosContemplacao(investMenCorrDezContempla1,invMenDezContempla1Set,valorCorridigoDezContempla1Meia,Modalidade.MEIA,1,prazo,lance,taxaAdm);

        assertEquals(new BigDecimal("4496.96"), parPosContemplaOutContempla110Meia);
        assertEquals(new BigDecimal("1850.33"), parPosContemplaJanContempla12Meia);
        assertEquals(new BigDecimal("38669.95"), parPosContemplaJanContempla230Meia);
        assertEquals(new BigDecimal("1598.13"), parPosContemplaDezContempla1Meia);
    }

    @Test
    void testGerarIRELucroLiquidoERetornoSobreCapitalInvestido() {
        double lance = parametroRequestDTO.getLance() * 0.01;
        Integer prazo = parametroRequestDTO.getPrazo();
        double taxaAdm = parametroRequestDTO.getTaxaAdm() * 0.01;

        BigDecimal creditoAtualizadoOutContempla110 = simulacaoService.gerarCreditoAtualizado(creditoComInccOutContempla110,valorCreditoMaisTaxaAdmOutContempla110, lance,ESCALA10);
        BigDecimal creditoAtualizadoJanContempla12 = simulacaoService.gerarCreditoAtualizado(creditoComInccJanContempla12,valorCreditoMaisTaxaAdmJanContempla12,lance,ESCALA10);
        BigDecimal creditoAtualizadoJanContempla230 = simulacaoService.gerarCreditoAtualizado(creditoComInccJanContempla230,valorCreditoMaisTaxaAdmJanContempla230,lance,ESCALA10);
        BigDecimal creditoAtualizadoDezContempla1 = simulacaoService.gerarCreditoAtualizado(creditoComInccDezContempla1,valorCreditoMaisTaxaAdmDezContempla1,lance,ESCALA10);

        BigDecimal valorVendaOutContempla110 = simulacaoService.gerarValorVenda(creditoAtualizadoOutContempla110,110,ESCALA10);
        BigDecimal valorVendaJanContempla12 = simulacaoService.gerarValorVenda(creditoAtualizadoJanContempla12,12,ESCALA10);
        BigDecimal valorVendaJanContempla230 = simulacaoService.gerarValorVenda(creditoAtualizadoJanContempla230,230,ESCALA10);
        BigDecimal valorVendaDezContempla1 = simulacaoService.gerarValorVenda(creditoAtualizadoDezContempla1,1,ESCALA10);

        BigDecimal valorCorridigoOutContempla110 = simulacaoService.gerarValorInvestidoCorrigido(valCredOutContempla110List,invMenOutContempla110Set,taxaAdm,prazo,ESCALA10,Modalidade.CHEIA);
        BigDecimal valorCorridigoJanContempla12 = simulacaoService.gerarValorInvestidoCorrigido(valCredJanContempla12List,invMenJanContempla12Set,taxaAdm,prazo,ESCALA10,Modalidade.CHEIA);
        BigDecimal valorCorridigoJanContempla230 = simulacaoService.gerarValorInvestidoCorrigido(valCredJanContempla230List,invMenJanContempla230Set,taxaAdm,prazo,ESCALA10,Modalidade.CHEIA);
        BigDecimal valorCorridigoDezContempla1 = simulacaoService.gerarValorInvestidoCorrigido(valCredDezContempla1List,invMenDezContempla1Set,taxaAdm,prazo,ESCALA10,Modalidade.CHEIA);

        BigDecimal valorIROutContempla110 = simulacaoService.gerarIR(valorVendaOutContempla110,valorCorridigoOutContempla110,110,ESCALA10);
        BigDecimal valorIRJanContempla12 = simulacaoService.gerarIR(valorVendaJanContempla12,valorCorridigoJanContempla12,12,ESCALA10);
        BigDecimal valorIRJanContempla230 = simulacaoService.gerarIR(valorVendaJanContempla230,valorCorridigoJanContempla230,230,ESCALA10);
        BigDecimal valorIRDezContempla1Escala2 = simulacaoService.gerarIR(valorVendaDezContempla1,valorCorridigoDezContempla1,1,ESCALA2);

        assertEquals(BigDecimal.ZERO, valorIROutContempla110);
        assertEquals(BigDecimal.ZERO, valorIRJanContempla12);
        assertEquals(BigDecimal.ZERO, valorIRJanContempla230);
        assertEquals(new BigDecimal("0"), valorIRDezContempla1Escala2);

        BigDecimal valorIRDezContempla1Escala10 = simulacaoService.gerarIR(valorVendaDezContempla1,valorCorridigoDezContempla1,1,ESCALA10);

        BigDecimal lucroLiquidoOutContempla110 = simulacaoService.gerarLucroLiquido(valorVendaOutContempla110,valorIROutContempla110,valorCorridigoOutContempla110,ESCALA2);
        BigDecimal lucroLiquidoJanContempla12 = simulacaoService.gerarLucroLiquido(valorVendaJanContempla12,valorIRJanContempla12,valorCorridigoJanContempla12,ESCALA2);
        BigDecimal lucroLiquidoJanContempla230 = simulacaoService.gerarLucroLiquido(valorVendaJanContempla230,valorIRJanContempla230,valorCorridigoJanContempla230,ESCALA2);
        BigDecimal lucroLiquidoDezContempla1 = simulacaoService.gerarLucroLiquido(valorVendaDezContempla1,valorIRDezContempla1Escala10,valorCorridigoDezContempla1,ESCALA2);

        assertEquals(new BigDecimal("-309942.25"), lucroLiquidoOutContempla110);
        assertEquals(new BigDecimal("15625.00"), lucroLiquidoJanContempla12);
        assertEquals(new BigDecimal("-1143502.48"), lucroLiquidoJanContempla230);
        assertEquals(new BigDecimal("-89294.61"), lucroLiquidoDezContempla1);

        BigDecimal retornSobCapitalInvestOutContempla110 = simulacaoService.gerarRetornoSobreCapitalInvestido(lucroLiquidoOutContempla110,valorCorridigoOutContempla110);
        BigDecimal retornSobCapitalInvestJanContempla12 = simulacaoService.gerarRetornoSobreCapitalInvestido(lucroLiquidoJanContempla12,valorCorridigoJanContempla12);
        BigDecimal retornSobCapitalInvestJanContempla230 = simulacaoService.gerarRetornoSobreCapitalInvestido(lucroLiquidoJanContempla230,valorCorridigoJanContempla230);
        BigDecimal retornSobCapitalInvestDezContempla1 = simulacaoService.gerarRetornoSobreCapitalInvestido(lucroLiquidoDezContempla1,valorCorridigoDezContempla1);

        assertEquals(new BigDecimal("-69.54"), retornSobCapitalInvestOutContempla110);
        assertEquals(new BigDecimal("50.00"), retornSobCapitalInvestJanContempla12);
        assertEquals(new BigDecimal("-78.06"), retornSobCapitalInvestJanContempla230);
        assertEquals(new BigDecimal("-65.58"), retornSobCapitalInvestDezContempla1);
    }

    @Test
    void testSetarEstrategia() {
        String estragediaCisRg = simulacaoService.setarEstrategia(BigDecimal.valueOf(1),10,240);
        String estragediaCipRp = simulacaoService.setarEstrategia(BigDecimal.valueOf(-1),10,240);

        assertEquals("Estratégia A", estragediaCisRg);
        assertEquals("Estratégia B", estragediaCipRp);
    }


}
