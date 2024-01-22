package br.com.consorcio.service;

import br.com.consorcio.dto.ParametroRequestDTO;
import br.com.consorcio.dto.TabelaMensalDTO;
import br.com.consorcio.enums.Modalidade;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class SimulacaoMensalServiceTest {

    @Autowired
    private SimulacaoMensalService simulacaoMensalService;

    @Test
    void validaTodosOsCamposDoMes1AoMes240() {
        ParametroRequestDTO parametroRequestDTO = ParametroRequestDTO.builder()
                .cota(2)
                .mesContemplacaoList(List.of(1,2))
                .modalidade(Modalidade.CHEIA)
                .valorCredito(BigDecimal.valueOf(100000))
                .prazo(240)
                .incc(10.0)
                .taxaAdm(25.0)
                .lance(30.0)
                .mesAtual(1)
                .build();
        List<TabelaMensalDTO> tabelaMensalDTOList = simulacaoMensalService.simular(parametroRequestDTO);
        TabelaMensalDTO tabelaMensalDTOMesUm = tabelaMensalDTOList.get(0);

        assertEquals(1, tabelaMensalDTOMesUm.getMes());
        assertEquals(new BigDecimal("62500.00"), tabelaMensalDTOMesUm.getCreditoAtualizadoMensal());
        assertEquals(new BigDecimal("520.83"), tabelaMensalDTOMesUm.getInvestimentoMensalCorrigido());
        assertEquals(new BigDecimal("520.83"), tabelaMensalDTOMesUm.getValorInvestidoCorrigido());
        assertEquals(new BigDecimal("125000.00"), tabelaMensalDTOMesUm.getSaldoDevedor());

        TabelaMensalDTO tabelaMensalDTOMes240 = tabelaMensalDTOList.get(tabelaMensalDTOList.size() - 1);

        assertEquals(240, tabelaMensalDTOMes240.getMes());
        assertEquals(new BigDecimal("382244.32"), tabelaMensalDTOMes240.getCreditoAtualizadoMensal());
        assertEquals(new BigDecimal("3185.37"), tabelaMensalDTOMes240.getInvestimentoMensalCorrigido());
        assertEquals(new BigDecimal("357968.75"), tabelaMensalDTOMes240.getValorInvestidoCorrigido());
        assertEquals(new BigDecimal("3185.37"), tabelaMensalDTOMes240.getSaldoDevedor());

    }


}
