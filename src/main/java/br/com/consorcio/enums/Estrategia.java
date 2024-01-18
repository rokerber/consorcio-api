package br.com.consorcio.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Estrategia {

    ESTRATEGIAA(0, "Estratégia A"),
    ESTRATEGIAB(1, "Estratégia B");

    private final int codigo;
    private final String descricao;

    public static Estrategia toEnum(Integer cod) {
        if (cod == null) {
            return null;
        }
        for (Estrategia estrategia : Estrategia.values()) {
            if (cod.equals(estrategia.getCodigo()))
                return estrategia;
        }
        throw new IllegalArgumentException("Id inválido: " + cod);
    }

}
