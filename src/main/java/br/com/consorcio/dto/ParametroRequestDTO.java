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
    private Modalidade modalidade = Modalidade.CHEIA;
    @NotNull(message = "O campo valorCredito é requerido")
    private BigDecimal valorCredito = BigDecimal.ZERO;
    @NotNull(message = "O campo prazo é requerido")
    private Integer prazo = 0;
    @NotNull(message = "O campo incc é requerido (apenas para testes)")
    private Double incc = 0.0;
    @NotNull(message = "O campo taxaAdm é requerido (apenas para testes)")
    private Double taxaAdm = 0.0;
    private BigDecimal recompra30 = BigDecimal.ZERO;
    private BigDecimal acima30 =  BigDecimal.ZERO;
    private Double lance = 0.0;
    private BigDecimal selic = BigDecimal.ZERO;
    @NotNull(message = "O campo mesAtual é requerido (apenas para testes)")
    private Integer mesAtual = 1;
}
