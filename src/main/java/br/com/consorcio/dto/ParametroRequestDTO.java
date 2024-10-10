package br.com.consorcio.dto;

import br.com.consorcio.enums.Modalidade;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ParametroRequestDTO {
    @NotNull(message = "O campo cota é requerido")
    private Integer cota = 1;
    @NotNull(message = "O campo mesContemplacaoList é requerido")
    private List<Integer> mesContemplacaoList;
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
