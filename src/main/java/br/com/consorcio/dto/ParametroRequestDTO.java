package br.com.consorcio.dto;

import br.com.consorcio.enums.Modalidade;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ParametroRequestDTO {
    @NotNull(message = "O campo modalidade é requerido")
    private Modalidade modalidade; // CHEIA ou MEIA
    @NotNull(message = "O campo valorCredito é requerido")
    private BigDecimal valorCredito; // input
    @NotNull(message = "O campo prazo é requerido")
    private Integer prazo; // input em meses
    @NotNull(message = "O campo incc é requerido (apenas para testes)")
    private Double incc; // inicialmente input( devara buscar online)
    @NotNull(message = "O campo taxaAdm é requerido (apenas para testes)")
    private Double taxaAdm; // input (devera haver uma tabela com a composicao das taxas)
    private BigDecimal recompra30; // input
    private BigDecimal acima30; // input
    private Double lance; // input
    private BigDecimal selic; // input (deverar buscar online)
    @NotNull(message = "O campo mesAtual é requerido (apenas para testes)")
    private Integer mesAtual; // apenas para testes, sera removido no futuro
}
