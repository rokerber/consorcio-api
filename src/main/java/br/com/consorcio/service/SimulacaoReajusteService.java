package br.com.consorcio.service;

import br.com.consorcio.dto.ParametroRequestDTO;
import br.com.consorcio.dto.TabelaReajusteDTO;
import br.com.consorcio.enums.Modalidade;
import org.bouncycastle.math.raw.Mod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

import static br.com.consorcio.service.SimulacaoService.ESCALA10;
import static br.com.consorcio.service.SimulacaoService.ESCALA2;

@Service
public class SimulacaoReajusteService {

    private final static int TOTALMESESANO = 12;

    @Autowired
    private SimulacaoService simulacaoService;

    public List<TabelaReajusteDTO> simular(ParametroRequestDTO parametroRequestDTO) {
        List<TabelaReajusteDTO> tabelaReajusteDTOList = new ArrayList<>();
        List<Integer> mesesList = new ArrayList<>();
        Set<BigDecimal> investimentoMensalSet = new LinkedHashSet<>();
        Modalidade modalidade = parametroRequestDTO.getModalidade();
        int prazo = parametroRequestDTO.getPrazo();
        double incc = parametroRequestDTO.getIncc() * 0.01;
        double taxaAdm = parametroRequestDTO.getTaxaAdm() * 0.01;
        double lance = parametroRequestDTO.getLance() * 0.01;
        BigDecimal valorCredito = parametroRequestDTO.getValorCredito();
        int mesAtual = parametroRequestDTO.getMesAtual();
        int anos = prazo / TOTALMESESANO;
        int meses = prazo;

        for (int i = 0; i < anos; i++) {
            if (meses != 0) {
                mesesList.add(meses);
            }
            meses -= TOTALMESESANO;
        }

        int getValueDeBaixoParaCima = anos;
        for (int i = 0; i < anos; i++) {
            --getValueDeBaixoParaCima;
            List<BigDecimal> valorCreditoList = new ArrayList<>();
            BigDecimal creditoComIncc = simulacaoService.gerarCreditoComIncc(mesesList.get(getValueDeBaixoParaCima),incc,valorCredito,mesAtual,valorCreditoList);
            BigDecimal valorCreditoMaisTaxaAdm = simulacaoService.gerarValorCreditoMaisTaxaAdm(creditoComIncc, taxaAdm);
            BigDecimal valorCreditoAtualizadoEscala2 = simulacaoService.gerarCreditoAtualizado(creditoComIncc, valorCreditoMaisTaxaAdm, lance, ESCALA2);
            BigDecimal investimentoMensalCorrigidoEscala10 = simulacaoService.gerarInvestimentoMensalCorrigido(valorCreditoMaisTaxaAdm, prazo, ESCALA10,modalidade);
            BigDecimal anualEscala2 = getAnualEscala2(investimentoMensalCorrigidoEscala10);
            BigDecimal investimentoMensalCorrigidoEscala10Cheia = simulacaoService.gerarInvestimentoMensalCorrigido(valorCreditoMaisTaxaAdm, prazo, ESCALA10,Modalidade.CHEIA);
            BigDecimal investimentoMensalCorrigidoEscala2Cheia = investimentoMensalCorrigidoEscala10Cheia.setScale(ESCALA2,RoundingMode.HALF_EVEN);
            BigDecimal anualEscala2Cheia = getAnualEscala2Cheia(investimentoMensalCorrigidoEscala10Cheia);
            BigDecimal investimentoMensalCorrigidoEscala2 = investimentoMensalCorrigidoEscala10.setScale(ESCALA2,RoundingMode.HALF_EVEN);
            BigDecimal saldoDevedorEscala10 = getSaldoDevedorEscala10(mesesList, i, investimentoMensalCorrigidoEscala10Cheia);
            BigDecimal saldoDevedorEscala2 = saldoDevedorEscala10.setScale(ESCALA2,RoundingMode.HALF_EVEN);
            BigDecimal valorInvestidoCorrigidoEscala2 = simulacaoService.gerarValorInvestidoCorrigido(valorCreditoList,investimentoMensalSet, taxaAdm, prazo, ESCALA10,modalidade).setScale(ESCALA2,RoundingMode.HALF_EVEN);
            BigDecimal saldoDevedorInicialEscala2 = simulacaoService.getSaldoDevedorInicial(investimentoMensalSet,saldoDevedorEscala10).setScale(ESCALA2,RoundingMode.HALF_EVEN);
            BigDecimal valorInvestidoCorrigidoEscala2Cheia = simulacaoService.gerarValorInvestidoCorrigido(valorCreditoList,investimentoMensalSet, taxaAdm, prazo, ESCALA10,Modalidade.CHEIA).setScale(ESCALA2,RoundingMode.HALF_EVEN);
            tabelaReajusteDTOList.add(TabelaReajusteDTO.builder()
                            .mes(mesesList.get(i))
                            .ano(i)
                            .credito(valorCreditoAtualizadoEscala2)
                            .saldoDevedor(saldoDevedorEscala2)
                            .acumuladoMeiaParcela(valorInvestidoCorrigidoEscala2)
                            .meiaParcela(investimentoMensalCorrigidoEscala2)
                            .anual(anualEscala2)
                            .parcelaCheia(investimentoMensalCorrigidoEscala2Cheia)
                            .anualCheia(anualEscala2Cheia)
                            .acumuladoParcelaCheia(valorInvestidoCorrigidoEscala2Cheia)
                            .totalAserPago(saldoDevedorInicialEscala2)
                    .build());
        }
        return tabelaReajusteDTOList;
    }

    private BigDecimal getSaldoDevedorEscala10(List<Integer> mesesList, int index, BigDecimal investimentoMensalCorrigidoEscala10Cheia) {
        return investimentoMensalCorrigidoEscala10Cheia.multiply(BigDecimal.valueOf(mesesList.get(index))).setScale(ESCALA10, RoundingMode.HALF_EVEN);
    }

    private BigDecimal getAnualEscala2Cheia(BigDecimal investimentoMensalCorrigidoEscala10Cheia) {
        return investimentoMensalCorrigidoEscala10Cheia.multiply(BigDecimal.valueOf(TOTALMESESANO)).setScale(ESCALA2, RoundingMode.HALF_EVEN);
    }

    private BigDecimal getAnualEscala2(BigDecimal investimentoMensalCorrigidoEscala10) {
        return investimentoMensalCorrigidoEscala10.multiply(BigDecimal.valueOf(TOTALMESESANO)).setScale(ESCALA2, RoundingMode.HALF_EVEN);
    }
}
