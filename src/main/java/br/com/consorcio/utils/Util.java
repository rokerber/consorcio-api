package br.com.consorcio.utils;

import br.com.consorcio.dto.ParametroRequestDTO;
import br.com.consorcio.common.DataIntegrityViolationException;
import org.apache.commons.lang3.ObjectUtils;

import java.math.BigDecimal;

public class Util {
    public static int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    public static void validaCampos(ParametroRequestDTO parametroRequestDTO) {
        if (parametroRequestDTO.getValorCredito().compareTo(BigDecimal.ZERO) == 0) {
            throw new DataIntegrityViolationException("credito nao pode ser zero");
        }
        if (parametroRequestDTO.getPrazo() == 0 || ObjectUtils.isEmpty(parametroRequestDTO.getPrazo())) {
            throw new DataIntegrityViolationException("prazo nao pode ser zero ou nulo");
        }
        if (parametroRequestDTO.getIncc() == 0 || ObjectUtils.isEmpty(parametroRequestDTO.getPrazo())) {
            throw new DataIntegrityViolationException("incc nao pode ser zero ou nulo");
        }
        if (parametroRequestDTO.getTaxaAdm() == 0 || ObjectUtils.isEmpty(parametroRequestDTO.getPrazo())) {
            throw new DataIntegrityViolationException("taxaAdm nao pode ser zero ou nulo");
        }
        if (parametroRequestDTO.getMesAtual() == 0 || ObjectUtils.isEmpty(parametroRequestDTO.getPrazo())) {
            throw new DataIntegrityViolationException("mesAtual nao pode ser zero (apenas para testes) ou nulo");
        }
        if (ObjectUtils.isEmpty(parametroRequestDTO.getLance())) {
            throw new DataIntegrityViolationException("lance nao pode ser nulo");
        }
    }
}
