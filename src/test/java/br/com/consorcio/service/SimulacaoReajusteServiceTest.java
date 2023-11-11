package br.com.consorcio.service;

import br.com.consorcio.dto.ParametroRequestDTO;
import br.com.consorcio.dto.TabelaReajusteDTO;
import br.com.consorcio.enums.Modalidade;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class SimulacaoReajusteServiceTest {

    @Autowired
    private SimulacaoReajusteService simulacaoReajusteService;

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
        List<TabelaReajusteDTO> tabelaReajusteDTOList = simulacaoReajusteService.simular(parametroRequestDTO);
        TabelaReajusteDTO tabelaReajusteDTOAnoZero = tabelaReajusteDTOList.get(0);

        assertEquals(240, tabelaReajusteDTOAnoZero.getMes());
        assertEquals(0, tabelaReajusteDTOAnoZero.getAno());
        assertEquals(new BigDecimal("62500.00"), tabelaReajusteDTOAnoZero.getCredito());
        assertEquals(new BigDecimal("125000.00"), tabelaReajusteDTOAnoZero.getSaldoDevedor());
        assertEquals(new BigDecimal("3125.00"), tabelaReajusteDTOAnoZero.getAcumuladoMeiaParcela());
        assertEquals(new BigDecimal("260.42"), tabelaReajusteDTOAnoZero.getMeiaParcela());
        assertEquals(new BigDecimal("3125.00"), tabelaReajusteDTOAnoZero.getAnual());
        assertEquals(new BigDecimal("520.83"), tabelaReajusteDTOAnoZero.getParcelaCheia());
        assertEquals(new BigDecimal("6250.00"), tabelaReajusteDTOAnoZero.getAnualCheia());
        assertEquals(new BigDecimal("6250.00"), tabelaReajusteDTOAnoZero.getAcumuladoParcelaCheia());
        assertEquals(new BigDecimal("125000.00"), tabelaReajusteDTOAnoZero.getTotalAserPago());

        TabelaReajusteDTO tabelaReajusteDTOAno19 = tabelaReajusteDTOList.get(tabelaReajusteDTOList.size() - 1);

        assertEquals(new BigDecimal("226032.97"), tabelaReajusteDTOAno19.getCredito());
        assertEquals(new BigDecimal("22603.30"), tabelaReajusteDTOAno19.getSaldoDevedor());
        assertEquals(new BigDecimal("128110.91"), tabelaReajusteDTOAno19.getAcumuladoMeiaParcela());
        assertEquals(new BigDecimal("941.80"), tabelaReajusteDTOAno19.getMeiaParcela());
        assertEquals(new BigDecimal("11301.65"), tabelaReajusteDTOAno19.getAnual());
        assertEquals(new BigDecimal("1883.61"), tabelaReajusteDTOAno19.getParcelaCheia());
        assertEquals(new BigDecimal("22603.30"), tabelaReajusteDTOAno19.getAnualCheia());
        assertEquals(new BigDecimal("256221.83"), tabelaReajusteDTOAno19.getAcumuladoParcelaCheia());
        assertEquals(new BigDecimal("256221.83"), tabelaReajusteDTOAno19.getTotalAserPago());

    }


}
