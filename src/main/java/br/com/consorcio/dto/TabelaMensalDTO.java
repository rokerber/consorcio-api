package br.com.consorcio.dto;

import lombok.*;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class TabelaMensalDTO {
    private Integer mes;
    private BigDecimal creditoAtualizadoMensal;
    private BigDecimal investimentoMensalCorrigido;
    private BigDecimal valorInvestidoCorrigido;
    private BigDecimal saldoDevedor;
}
