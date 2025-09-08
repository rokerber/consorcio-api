package br.com.consorcio.service;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.List;

@Service
public class RendaFixaService {

    private static final MathContext MC = new MathContext(15, RoundingMode.HALF_UP);
    private static final BigDecimal MESES_NO_ANO = new BigDecimal("12");

    public BigDecimal calcularRendimento(List<BigDecimal> depositosMensais, BigDecimal taxaAnual) {
        if (taxaAnual == null || taxaAnual.compareTo(BigDecimal.ZERO) == 0 || depositosMensais.isEmpty()) {
            return BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        }

        // Converte a taxa anual para mensal usando a fórmula: (1 + i_anual)^(1/12) - 1
        BigDecimal taxaMensal = BigDecimal.valueOf(
            Math.pow(BigDecimal.ONE.add(taxaAnual).doubleValue(), 1.0 / 12.0)
        ).subtract(BigDecimal.ONE);

        BigDecimal montanteAcumulado = BigDecimal.ZERO;
        BigDecimal totalDepositado = BigDecimal.ZERO;

        for (BigDecimal deposito : depositosMensais) {
            // Soma o depósito atual ao montante
            montanteAcumulado = montanteAcumulado.add(deposito);
            // Aplica os juros do mês sobre o novo montante
            montanteAcumulado = montanteAcumulado.multiply(BigDecimal.ONE.add(taxaMensal), MC);
            
            totalDepositado = totalDepositado.add(deposito);
        }

        BigDecimal rendimentoBruto = montanteAcumulado.subtract(totalDepositado);
        return rendimentoBruto.setScale(2, RoundingMode.HALF_UP);
    }
}
