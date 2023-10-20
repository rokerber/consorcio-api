package br.com.consorcio.dto;

import br.com.consorcio.enums.Modalidade;
import lombok.*;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ParametroRequestDTO {
    private Modalidade modalidade; // CHEIA ou MEIA
    private BigDecimal valorCredito; // input
    private Integer prazo; // input em meses
    private Double incc; // inicialmente input( devara buscar online)
    private Double taxaAdm; // input (devera haver uma tabela com a composicao das taxas)
    private BigDecimal recompra30; // input
    private BigDecimal acima30; // input
    private Double lance; // input
    private BigDecimal selic; // input (deverar buscar online)
}
