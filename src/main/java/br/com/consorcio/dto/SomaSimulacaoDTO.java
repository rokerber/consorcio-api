package br.com.consorcio.dto;

import lombok.*;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class SomaSimulacaoDTO {
    private BigDecimal somaCreditoAtualizado;
    private BigDecimal somaInvestimentoMensalCorrigido;
    private BigDecimal somaInvestimentoPosContemplacao;
    private BigDecimal somaValorInvestidoCorrigido;
}
