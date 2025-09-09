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
    // --- Parâmetros Principais ---
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

    @NotNull(value = "O campo percentualVendaAte30 é requerido")
    private Double percentualVendaAte30;

    @NotNull(value = "O campo percentualVendaApos30 é requerido")
    private Double percentualVendaApos30;
    private Double lance;
    private BigDecimal selic;
    private Integer mesAtual = 1; // Default
    private Integer cota = 1; // Default
}
