package br.com.consorcio.service;

import br.com.consorcio.dto.ParametroRequestDTO;
import br.com.consorcio.dto.TabelaMensalDTO;
import br.com.consorcio.enums.Modalidade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static br.com.consorcio.service.SimulacaoService.ESCALA10;
import static br.com.consorcio.service.SimulacaoService.ESCALA2;
import static br.com.consorcio.utils.Util.validaCampos;

@Service
public class SimulacaoMensalService {

    private final static int TOTALMESESANO = 12;

    @Autowired
    private SimulacaoService simulacaoService;

    public List<TabelaMensalDTO> simular(ParametroRequestDTO parametroRequestDTO) {
        validaCampos(parametroRequestDTO);
        double lance = ObjectUtils.isEmpty(parametroRequestDTO.getLance()) ? 0.0 : parametroRequestDTO.getLance() * 0.01;
        List<TabelaMensalDTO> tabelaMensalDTOList = new ArrayList<>();
        List<Integer> mesesList = new ArrayList<>();
        Set<BigDecimal> investimentoMensalSet = new LinkedHashSet<>();
        Modalidade modalidade = parametroRequestDTO.getModalidade();
        int prazo = parametroRequestDTO.getPrazo();
        double incc = parametroRequestDTO.getIncc() * 0.01;
        double taxaAdm = parametroRequestDTO.getTaxaAdm() * 0.01;
        BigDecimal valorCredito = parametroRequestDTO.getValorCredito();
        int mesAtual = parametroRequestDTO.getMesAtual();

        for (int i = prazo; i > 0; i--) {
            mesesList.add(i);
        }

        for (int i = 1; i <= prazo; i++) {
            List<BigDecimal> valorCreditoList = new ArrayList<>();
            BigDecimal creditoComIncc = simulacaoService.gerarCreditoComIncc(i,incc,valorCredito,mesAtual,valorCreditoList);
            BigDecimal valorCreditoMaisTaxaAdm = simulacaoService.gerarValorCreditoMaisTaxaAdm(creditoComIncc, taxaAdm);
            BigDecimal valorCreditoAtualizadoEscala2 = simulacaoService.gerarCreditoAtualizado(creditoComIncc, valorCreditoMaisTaxaAdm, lance, ESCALA2);
            BigDecimal investimentoMensalCorrigidoEscala10Cheia = simulacaoService.gerarInvestimentoMensalCorrigido(valorCreditoMaisTaxaAdm, prazo, ESCALA10,Modalidade.CHEIA);
            BigDecimal investimentoMensalCorrigidoEscala2 = investimentoMensalCorrigidoEscala10Cheia.setScale(ESCALA2,RoundingMode.HALF_EVEN);
            BigDecimal saldoDevedorEscala10 = getSaldoDevedorEscala10(mesesList, i - 1, investimentoMensalCorrigidoEscala10Cheia);
            BigDecimal saldoDevedorEscala2 = saldoDevedorEscala10.setScale(ESCALA2,RoundingMode.HALF_EVEN);
            BigDecimal valorInvestidoCorrigidoEscala2 = simulacaoService.gerarValorInvestidoCorrigido(valorCreditoList,investimentoMensalSet, taxaAdm, prazo, ESCALA10,modalidade).setScale(ESCALA2,RoundingMode.HALF_EVEN);

            tabelaMensalDTOList.add(TabelaMensalDTO.builder()
                            .mes(i)
                            .creditoAtualizadoMensal(valorCreditoAtualizadoEscala2)
                            .investimentoMensalCorrigido(investimentoMensalCorrigidoEscala2)
                            .valorInvestidoCorrigido(valorInvestidoCorrigidoEscala2)
                            .saldoDevedor(saldoDevedorEscala2)
                    .build());
        }
        return tabelaMensalDTOList;
    }

    private BigDecimal getSaldoDevedorEscala10(List<Integer> mesesList, int index, BigDecimal investimentoMensalCorrigidoEscala10Cheia) {
        return investimentoMensalCorrigidoEscala10Cheia.multiply(BigDecimal.valueOf(mesesList.get(index))).setScale(ESCALA10, RoundingMode.HALF_EVEN);
    }

}
