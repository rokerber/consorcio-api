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
    private Modalidade modalidade = Modalidade.CHEIA; // CHEIA ou MEIA
    @NotNull(message = "O campo valorCredito é requerido")
    private BigDecimal valorCredito = BigDecimal.ZERO; // input
    @NotNull(message = "O campo prazo é requerido")
    private Integer prazo = 0; // input em meses
    @NotNull(message = "O campo incc é requerido (apenas para testes)")
    private Double incc = 0.0; // inicialmente input( devara buscar online)
    @NotNull(message = "O campo taxaAdm é requerido (apenas para testes)")
    private Double taxaAdm = 0.0; // input (devera haver uma tabela com a composicao das taxas)
    private BigDecimal recompra30 = BigDecimal.ZERO; // input
    private BigDecimal acima30 =  BigDecimal.ZERO; // input
    private Double lance = 0.0; // input
    private BigDecimal selic = BigDecimal.ZERO; // input (deverar buscar online)
    @NotNull(message = "O campo mesAtual é requerido (apenas para testes)")
    private Integer mesAtual = 1; // apenas para testes, sera removido no futuro
}
