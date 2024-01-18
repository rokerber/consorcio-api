package br.com.consorcio.service;

import br.com.consorcio.dto.SimulacaoDTO;
import br.com.consorcio.dto.SomaSimulacaoDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class SomaSimulacaoServiceTest {

    @Autowired
    SomaSimulacaoService somaSimulacaoService;

    @Test
    public void testSomaSimulacao() {
        List<SimulacaoDTO> simulacaoDTOList = new ArrayList<>();
        for(int i=1; i<=5; i++) {
            simulacaoDTOList.add(SimulacaoDTO.builder()
                    .cota(i)
                    .mesContemplacao(1)
                    .formaContemplacao("SORTEIO")
                    .creditoAtualizado(BigDecimal.valueOf(100000 + i))
                    .investimentoMensalCorrigido(BigDecimal.valueOf(100000 + i))
                    .valorInvestidoCorrigido(BigDecimal.valueOf(100000 + i))
                    .parcelaPosContemplacao(BigDecimal.valueOf(100000 + i))
                    .build());
        }

        SomaSimulacaoDTO somaSimulacaoDTO = somaSimulacaoService.somaSimulacao(simulacaoDTOList);
        assertEquals(BigDecimal.valueOf(500015),somaSimulacaoDTO.getSomaCreditoAtualizado());
        assertEquals(BigDecimal.valueOf(500015),somaSimulacaoDTO.getSomaInvestimentoMensalCorrigido());
        assertEquals(BigDecimal.valueOf(500015),somaSimulacaoDTO.getSomaInvestimentoPosContemplacao());
        assertEquals(BigDecimal.valueOf(500015),somaSimulacaoDTO.getSomaValorInvestidoCorrigido());
    }
}
