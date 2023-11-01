package br.com.consorcio.service;

import br.com.consorcio.dto.ParametroRequestDTO;
import br.com.consorcio.dto.TabelaReajusteDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static br.com.consorcio.service.SimulacaoService.ESCALA2;

@Service
public class SimulacaoReajusteService {

    private final static int TOTALMESESANO = 12;

    @Autowired
    private SimulacaoService simulacaoService;

    public List<TabelaReajusteDTO> simular(ParametroRequestDTO parametroRequestDTO) {
        List<TabelaReajusteDTO> tabelaReajusteDTOList = new ArrayList<>();
        List<BigDecimal> valorCreditoAtualizadoList = new ArrayList<>();
        int prazo = parametroRequestDTO.getPrazo();
        double incc = parametroRequestDTO.getIncc() * 0.01;
        double taxaAdm = parametroRequestDTO.getTaxaAdm() * 0.01;
        double lance = parametroRequestDTO.getLance() * 0.01;
        BigDecimal valorCredito = parametroRequestDTO.getValorCredito();
        int mesAtual = parametroRequestDTO.getMesAtual();
        int anos = prazo / TOTALMESESANO;
        int meses = prazo;

        for (int i = 0; i < anos; i++) {
            List<BigDecimal> valorCreditoList = new ArrayList<>();
            BigDecimal creditoComIncc = simulacaoService.gerarCreditoComIncc(meses,incc,valorCredito,mesAtual,valorCreditoList);
            BigDecimal valorCreditoMaisTaxaAdm = simulacaoService.gerarValorCreditoMaisTaxaAdm(creditoComIncc, taxaAdm);
            BigDecimal valorCreditoAtualizadoEscala2 = simulacaoService.gerarCreditoAtualizado(creditoComIncc, valorCreditoMaisTaxaAdm, lance, ESCALA2);
            valorCreditoAtualizadoList.add(valorCreditoAtualizadoEscala2);
            meses -= TOTALMESESANO;
        }

        meses = prazo;

        int getCreditoDeBaixoPraCima = anos;
        for (int i = 0; i < anos; i++) {
            --getCreditoDeBaixoPraCima;
            tabelaReajusteDTOList.add(TabelaReajusteDTO.builder()
                            .mes(meses)
                            .ano(i)
                            .credito(valorCreditoAtualizadoList.get(getCreditoDeBaixoPraCima))
                            .saldoDevedor(BigDecimal.ZERO)
                            .acumuladoMeiaParcela(BigDecimal.ZERO)
                            .meiaParcela(BigDecimal.ZERO)
                            .anual(BigDecimal.ZERO)
                            .parcelaCheia(BigDecimal.ZERO)
                            .anualCheia(BigDecimal.ZERO)
                            .acumuladoParcelaCheia(BigDecimal.ZERO)
                            .totalAserPago(BigDecimal.ZERO)
                    .build());
            meses -= TOTALMESESANO;
        }
        return tabelaReajusteDTOList;
    }
}
