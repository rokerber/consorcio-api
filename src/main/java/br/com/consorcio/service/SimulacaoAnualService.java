package br.com.consorcio.service;

import br.com.consorcio.dto.ParametroRequestDTO;
import br.com.consorcio.dto.TabelaAnualDTO;
import br.com.consorcio.enums.Modalidade;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

import static br.com.consorcio.service.SimulacaoService.ESCALA10;
import static br.com.consorcio.service.SimulacaoService.ESCALA2;
import static br.com.consorcio.utils.Util.validaCampos;

@Service
public class SimulacaoAnualService {

    private final static int TOTALMESESANO = 12;

    @Autowired
    private SimulacaoService simulacaoService;

    public List<TabelaAnualDTO> simular(ParametroRequestDTO parametroRequestDTO) {
        validaCampos(parametroRequestDTO);
        double lance = ObjectUtils.isEmpty(parametroRequestDTO.getLance()) ? 0.0 : parametroRequestDTO.getLance() * 0.01;
        List<TabelaAnualDTO> tabelaAnualDTOList = new ArrayList<>();
        List<Integer> mesesList = new ArrayList<>();
        Set<BigDecimal> investimentoMensalSet = new LinkedHashSet<>();
        Modalidade modalidade = parametroRequestDTO.getModalidade();
        int prazo = parametroRequestDTO.getPrazo();
        double incc = parametroRequestDTO.getIncc() * 0.01;
        double taxaAdm = parametroRequestDTO.getTaxaAdm() * 0.01;
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
            BigDecimal valorCreditoAtualizadoEscala10 = simulacaoService.gerarCreditoAtualizado(creditoComIncc, valorCreditoMaisTaxaAdm, lance, ESCALA10);
            BigDecimal investimentoMensalCorrigidoEscala10Cheia = simulacaoService.gerarInvestimentoMensalCorrigido(valorCreditoMaisTaxaAdm, prazo, ESCALA10,Modalidade.CHEIA);
            BigDecimal saldoDevedorEscala10 = getSaldoDevedorEscala10(mesesList, i, investimentoMensalCorrigidoEscala10Cheia);
            BigDecimal saldoDevedorEscala2 = saldoDevedorEscala10.setScale(ESCALA2,RoundingMode.HALF_EVEN);
            BigDecimal valorInvestidoCorrigidoEscala2 = simulacaoService.gerarValorInvestidoCorrigido(valorCreditoList,investimentoMensalSet, taxaAdm, prazo, ESCALA10,modalidade).setScale(ESCALA2,RoundingMode.HALF_EVEN);

            BigDecimal valorDaVendaEscala2 = simulacaoService.gerarValorVenda(
                    valorCreditoAtualizadoEscala10,
                    (mesesList.get(getValueDeBaixoParaCima)),
                    parametroRequestDTO.getPercentualVendaAte30(),
                    parametroRequestDTO.getPercentualVendaApos30(),
                    ESCALA2
            );

            tabelaAnualDTOList.add(TabelaAnualDTO.builder()
                    .ano(i)
                    .creditoAtualizadoAnual(valorCreditoAtualizadoEscala2)
                    .saldoDevedor(saldoDevedorEscala2)
                    .investimentoAnualCorrigido(valorInvestidoCorrigidoEscala2)
                    .valorDaVenda(valorDaVendaEscala2)
                    .build());
        }
        return tabelaAnualDTOList;
    }

    private BigDecimal getSaldoDevedorEscala10(List<Integer> mesesList, int index, BigDecimal investimentoMensalCorrigidoEscala10Cheia) {
        return investimentoMensalCorrigidoEscala10Cheia.multiply(BigDecimal.valueOf(mesesList.get(index))).setScale(ESCALA10, RoundingMode.HALF_EVEN);
    }

}
