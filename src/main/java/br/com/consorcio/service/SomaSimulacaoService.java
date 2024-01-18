package br.com.consorcio.service;

import br.com.consorcio.dto.SimulacaoDTO;
import br.com.consorcio.dto.SomaSimulacaoDTO;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class SomaSimulacaoService {

    public SomaSimulacaoDTO somaSimulacao(List<SimulacaoDTO> simulacaoDTOList) {
        List<BigDecimal> creditoAtualizadoList = simulacaoDTOList.stream().map(SimulacaoDTO::getCreditoAtualizado).toList();
        List<BigDecimal> investimentoMensalList = simulacaoDTOList.stream().map(SimulacaoDTO::getInvestimentoMensalCorrigido).toList();
        List<BigDecimal> investimentoPosContemplacaoList = simulacaoDTOList.stream().map(SimulacaoDTO::getParcelaPosContemplacao).toList();
        List<BigDecimal> valorInvestidoCorrigidoList = simulacaoDTOList.stream().map(SimulacaoDTO::getValorInvestidoCorrigido).toList();

        return SomaSimulacaoDTO.builder()
                .somaCreditoAtualizado(creditoAtualizadoList.stream().reduce(BigDecimal.ZERO, BigDecimal::add))
                .somaInvestimentoMensalCorrigido(investimentoMensalList.stream().reduce(BigDecimal.ZERO, BigDecimal::add))
                .somaInvestimentoPosContemplacao(investimentoPosContemplacaoList.stream().reduce(BigDecimal.ZERO, BigDecimal::add))
                .somaValorInvestidoCorrigido(valorInvestidoCorrigidoList.stream().reduce(BigDecimal.ZERO, BigDecimal::add)).build();

    }
}
