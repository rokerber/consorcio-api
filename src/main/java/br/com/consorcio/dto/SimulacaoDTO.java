package br.com.consorcio.dto;

import lombok.*;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class SimulacaoDTO {
    private Integer cota; // sugestao 10
    private Integer mesContemplacao; // math randon entre o 1 e o prazo maximo.
    private String formaContemplacao; // a principio sempre Sorteio
    private BigDecimal creditoAtualizado;
    private BigDecimal investimentoMensalCorrigido;
    private BigDecimal valorInvestidoCorrigido;
    private BigDecimal parcelaPosContemplacao;
    private BigDecimal valorVenda;
    private BigDecimal ir;
    private BigDecimal lucroLiquido;
    private String retornSobCapitalInvest;
    private String estrategia;
}
