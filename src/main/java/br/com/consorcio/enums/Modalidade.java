package br.com.consorcio.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Modalidade {

    MEIA(0, "MEIA"),
    CHEIA(1, "CHEIA");

    private final int codigo;
    private final String descricao;

    public static Modalidade toEnum(Integer cod) {
        if (cod == null) {
            return null;
        }
        for (Modalidade modalidade : Modalidade.values()) {
            if (cod.equals(modalidade.getCodigo()))
                return modalidade;
        }
        throw new IllegalArgumentException("Id inválido: " + cod);
    }

}
