package br.com.consorcio.utils;

import br.com.consorcio.dto.ParametroRequestDTO;
import br.com.consorcio.common.DataIntegrityViolationException;
import org.apache.commons.lang3.ObjectUtils;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.stream.Collectors;

public class Util {
    public static int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    public static void validaCampos(ParametroRequestDTO parametroRequestDTO) {
        if (parametroRequestDTO.getCota() == 0 || ObjectUtils.isEmpty(parametroRequestDTO.getCota())) {
            throw new DataIntegrityViolationException("campo cota não pode ser zero ou nulo!");
        }
        if (parametroRequestDTO.getCota() < parametroRequestDTO.getMesContemplacaoList().size()) {
            throw new DataIntegrityViolationException("campo cota = " + parametroRequestDTO.getCota() + ", tem mais meses de mesContemplacao do que cotas!");
        }
        if (parametroRequestDTO.getCota() != parametroRequestDTO.getMesContemplacaoList().size()) {
            throw new DataIntegrityViolationException("campo cota = " + parametroRequestDTO.getCota() + ", faltando dados em mesContemplacao!");
        }
        if (parametroRequestDTO.getMesContemplacaoList().stream().anyMatch((mes -> mes == 0))) {
            throw new DataIntegrityViolationException("campo MesContemplacaoList não pode ter valores zero!");
        }
        if (parametroRequestDTO.getValorCredito().compareTo(BigDecimal.ZERO) == 0) {
            throw new DataIntegrityViolationException("campo credito não pode ser zero!");
        }
        if (parametroRequestDTO.getPrazo() == 0 || ObjectUtils.isEmpty(parametroRequestDTO.getPrazo())) {
            throw new DataIntegrityViolationException("campo prazo não pode ser zero ou nulo!");
        }
        if (parametroRequestDTO.getIncc() == 0 || ObjectUtils.isEmpty(parametroRequestDTO.getPrazo())) {
            throw new DataIntegrityViolationException("campo incc não pode ser zero ou nulo!");
        }
        if (parametroRequestDTO.getTaxaAdm() == 0 || ObjectUtils.isEmpty(parametroRequestDTO.getPrazo())) {
            throw new DataIntegrityViolationException("campo taxaAdm não pode ser zero ou nulo!");
        }
        if (parametroRequestDTO.getMesAtual() == 0 || ObjectUtils.isEmpty(parametroRequestDTO.getPrazo())) {
            throw new DataIntegrityViolationException("campo mesAtual não pode ser zero (apenas para testes) ou nulo!");
        }
    }
}
