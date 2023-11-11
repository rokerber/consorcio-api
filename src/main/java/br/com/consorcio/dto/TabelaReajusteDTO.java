package br.com.consorcio.dto;

import lombok.*;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class TabelaReajusteDTO {
    private Integer mes;
    private Integer ano;
    private BigDecimal credito;
    private BigDecimal saldoDevedor;
    private BigDecimal acumuladoMeiaParcela;
    private BigDecimal meiaParcela;
    private BigDecimal anual;
    private BigDecimal parcelaCheia;
    private BigDecimal anualCheia;
    private BigDecimal acumuladoParcelaCheia;
    private BigDecimal totalAserPago;
}
