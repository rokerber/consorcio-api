package br.com.consorcio.dto;

import lombok.*;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class TabelaAnualDTO {
    private Integer ano;
    private BigDecimal creditoAtualizadoAnual;
    private BigDecimal investimentoAnualCorrigido;
    private BigDecimal saldoDevedor;
    private BigDecimal valorDaVenda;
}
