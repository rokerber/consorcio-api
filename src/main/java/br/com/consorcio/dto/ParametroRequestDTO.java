package br.com.consorcio.dto;

import br.com.consorcio.enums.Modalidade;
import lombok.Builder;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class ParametroRequestDTO {
    @NotNull(value = "O campo valorCredito é requerido")
    private BigDecimal valorCredito;

    @NotNull(value = "O campo prazo é requerido")
    private Integer prazo;

    @NotNull(value = "O campo taxaAdm é requerido")
    private Double taxaAdm;

    @NotNull(value = "O campo incc é requerido")
    private Double incc;

    @NotNull(value = "O campo modalidade é requerido")
    private Modalidade modalidade;

    private List<Integer> mesContemplacaoList;

    private BigDecimal recompra30;
    private BigDecimal acima30;
    private Double lance;

    private BigDecimal selic;
    private Integer mesAtual = 1;
    private Integer cota = 1;
}
