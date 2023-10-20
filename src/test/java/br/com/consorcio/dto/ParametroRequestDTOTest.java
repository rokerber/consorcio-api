package br.com.consorcio.dto;

import br.com.consorcio.enums.Modalidade;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

public class ParametroRequestDTOTest {

    @Test
    public void testParametroRequestDTO() {
        ParametroRequestDTO parametro = ParametroRequestDTO.builder()
                .modalidade(Modalidade.CHEIA)
                .valorCredito(new BigDecimal("10000"))
                .prazo(12)
                .incc(1.5)
                .taxaAdm(2.5)
                .recompra30(new BigDecimal("5000"))
                .acima30(new BigDecimal("7000"))
                .lance(1000.0)
                .selic(new BigDecimal("4.5"))
                .build();

        // Verify the values of the ParametroRequestDTO instance
        Assertions.assertEquals(Modalidade.CHEIA, parametro.getModalidade());
        Assertions.assertEquals(new BigDecimal("10000"), parametro.getValorCredito());
        Assertions.assertEquals(12, parametro.getPrazo());
        Assertions.assertEquals(1.5, parametro.getIncc());
        Assertions.assertEquals(2.5, parametro.getTaxaAdm());
        Assertions.assertEquals(new BigDecimal("5000"), parametro.getRecompra30());
        Assertions.assertEquals(new BigDecimal("7000"), parametro.getAcima30());
        Assertions.assertEquals(1000.0, parametro.getLance());
        Assertions.assertEquals(new BigDecimal("4.5"), parametro.getSelic());
    }
}