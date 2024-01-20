package br.com.consorcio.service;

import br.com.consorcio.dto.ParametroRequestDTO;
import br.com.consorcio.dto.TabelaAnualDTO;
import br.com.consorcio.enums.Modalidade;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class SimulacaoAnualServiceTest {

    @Autowired
    private SimulacaoAnualService simulacaoAnualService;

    @Test
    void validaTodosOsCamposDoAnoZeroEAno19() {
        ParametroRequestDTO parametroRequestDTO = ParametroRequestDTO.builder()
                .modalidade(Modalidade.MEIA)
                .valorCredito(BigDecimal.valueOf(100000))
                .prazo(240)
                .incc(7.0)
                .taxaAdm(25.0)
                .lance(30.0)
                .mesAtual(1)
                .build();
        List<TabelaAnualDTO> tabelaAnualDTOList = simulacaoAnualService.simular(parametroRequestDTO);
        TabelaAnualDTO tabelaAnualDTOAnoZero = tabelaAnualDTOList.get(0);

        assertEquals(0, tabelaAnualDTOAnoZero.getAno());
        assertEquals(new BigDecimal("62500.00"), tabelaAnualDTOAnoZero.getCreditoAtualizadoAnual());
        assertEquals(new BigDecimal("125000.00"), tabelaAnualDTOAnoZero.getSaldoDevedor());
        assertEquals(new BigDecimal("3125.00"), tabelaAnualDTOAnoZero.getInvestimentoAnualCorrigido());
        assertEquals(new BigDecimal("9375.00"), tabelaAnualDTOAnoZero.getValorDaVenda());

        TabelaAnualDTO tabelaAnualDTOAno19 = tabelaAnualDTOList.get(tabelaAnualDTOList.size() - 1);

        assertEquals(new BigDecimal("226032.97"), tabelaAnualDTOAno19.getCreditoAtualizadoAnual());
        assertEquals(new BigDecimal("22603.30"), tabelaAnualDTOAno19.getSaldoDevedor());
        assertEquals(new BigDecimal("128110.91"), tabelaAnualDTOAno19.getInvestimentoAnualCorrigido());
        assertEquals(new BigDecimal("45206.59"), tabelaAnualDTOAno19.getValorDaVenda());

    }


}
