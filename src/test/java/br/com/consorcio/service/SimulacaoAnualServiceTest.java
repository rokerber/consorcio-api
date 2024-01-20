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

        assertEquals(240, tabelaAnualDTOAnoZero.getMes());
        assertEquals(0, tabelaAnualDTOAnoZero.getAno());
        assertEquals(new BigDecimal("62500.00"), tabelaAnualDTOAnoZero.getCredito());
        assertEquals(new BigDecimal("125000.00"), tabelaAnualDTOAnoZero.getSaldoDevedor());
        assertEquals(new BigDecimal("3125.00"), tabelaAnualDTOAnoZero.getAcumuladoMeiaParcela());
        assertEquals(new BigDecimal("260.42"), tabelaAnualDTOAnoZero.getMeiaParcela());
        assertEquals(new BigDecimal("3125.00"), tabelaAnualDTOAnoZero.getAnual());
        assertEquals(new BigDecimal("520.83"), tabelaAnualDTOAnoZero.getParcelaCheia());
        assertEquals(new BigDecimal("6250.00"), tabelaAnualDTOAnoZero.getAnualCheia());
        assertEquals(new BigDecimal("6250.00"), tabelaAnualDTOAnoZero.getAcumuladoParcelaCheia());
        assertEquals(new BigDecimal("125000.00"), tabelaAnualDTOAnoZero.getTotalAserPago());

        TabelaAnualDTO tabelaAnualDTOAno19 = tabelaAnualDTOList.get(tabelaAnualDTOList.size() - 1);

        assertEquals(new BigDecimal("226032.97"), tabelaAnualDTOAno19.getCredito());
        assertEquals(new BigDecimal("22603.30"), tabelaAnualDTOAno19.getSaldoDevedor());
        assertEquals(new BigDecimal("128110.91"), tabelaAnualDTOAno19.getAcumuladoMeiaParcela());
        assertEquals(new BigDecimal("941.80"), tabelaAnualDTOAno19.getMeiaParcela());
        assertEquals(new BigDecimal("11301.65"), tabelaAnualDTOAno19.getAnual());
        assertEquals(new BigDecimal("1883.61"), tabelaAnualDTOAno19.getParcelaCheia());
        assertEquals(new BigDecimal("22603.30"), tabelaAnualDTOAno19.getAnualCheia());
        assertEquals(new BigDecimal("256221.83"), tabelaAnualDTOAno19.getAcumuladoParcelaCheia());
        assertEquals(new BigDecimal("256221.83"), tabelaAnualDTOAno19.getTotalAserPago());

    }


}
