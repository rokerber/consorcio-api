package br.com.consorcio.utils;

import br.com.consorcio.dto.ParametroRequestDTO;
import br.com.consorcio.service.exceptions.DataIntegrityViolationException;

import java.math.BigDecimal;

public class Util {
    public static int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    public static void validaCampos(ParametroRequestDTO parametroRequestDTO) {
        if (parametroRequestDTO.getValorCredito().compareTo(BigDecimal.ZERO) == 0) {
            throw new DataIntegrityViolationException("credito nao pode ser zero");
        }
        if (parametroRequestDTO.getPrazo() == 0) {
            throw new DataIntegrityViolationException("prazo nao pode ser zero");
        }
        if (parametroRequestDTO.getIncc() == 0) {
            throw new DataIntegrityViolationException("incc nao pode ser zero");
        }
        if (parametroRequestDTO.getTaxaAdm() == 0) {
            throw new DataIntegrityViolationException("taxaAdm nao pode ser zero");
        }
        if (parametroRequestDTO.getMesAtual() == 0) {
            throw new DataIntegrityViolationException("mesAtual nao pode ser zero (apenas para testes)");
        }
    }
}
