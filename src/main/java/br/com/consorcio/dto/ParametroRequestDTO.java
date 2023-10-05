package br.com.consorcio.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ParametroRequestDTO {
    private String modalidade; // falor fixo CHEIA
    private BigDecimal valorCredito; // input
    private Integer prazo; // input em meses
    private Double incc; // inicialmente input( devara buscar online)
    private Double taxaAdm; // input (devera haver uma tabela com a composicao das taxas)
    private BigDecimal recompra30; // input
    private BigDecimal acima30; // input
    private Double lance; // input
    private BigDecimal selic; // input (deverar buscar online)
}
