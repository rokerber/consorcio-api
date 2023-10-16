package br.com.consorcio.service;

import br.com.consorcio.dto.ParametroRequestDTO;
import br.com.consorcio.dto.SimulacaoDTO;
import br.com.consorcio.dto.enums.Modalidade;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static br.com.consorcio.utils.Util.getRandomNumber;

@Service
public class SimulacaoService {

    private static final int COTA = 10;
    private static final int ESCALA2 = 2;
    private static final int ESCALA10 = 10;

    public List<SimulacaoDTO> simular(ParametroRequestDTO parametroRequestDTO){
        // monta a tabela para retornar
        List<SimulacaoDTO> simulacaoDTOList = new ArrayList<>();
        LocalDate currentdate = LocalDate.now();
        double lance = parametroRequestDTO.getLance() * 0.01;
        double taxaAdm = parametroRequestDTO.getTaxaAdm() * 0.01;
        double incc = parametroRequestDTO.getIncc() * 0.01;
        int prazo = parametroRequestDTO.getPrazo();
        BigDecimal valorCredito = parametroRequestDTO.getValorCredito();
        Modalidade modalidade = parametroRequestDTO.getModalidade();
        for (int i = 1; i <= COTA; i++) {
            List<BigDecimal> valorCreditoList = new ArrayList<>();
            int valorMesContemplacao = valorMesContemplacao(prazo);
            BigDecimal creditoComIncc = gerarCreditoComIncc(valorMesContemplacao, incc, valorCredito, 1,valorCreditoList);
            BigDecimal valorCreditoMaisTaxaAdm = gerarValorCreditoMaisTaxaAdm(creditoComIncc, taxaAdm);
            BigDecimal valorCreditoAtualizado = gerarCreditoAtualizado(creditoComIncc, valorCreditoMaisTaxaAdm,lance);
            BigDecimal investimentoMensalCorrigidoEscala2 = gerarInvestimentoMensalCorrigido(valorCreditoMaisTaxaAdm, prazo,ESCALA2);
            BigDecimal investimentoMensalCorrigidoEscala10 = gerarInvestimentoMensalCorrigido(valorCreditoMaisTaxaAdm, prazo,ESCALA10);
            BigDecimal valorInvestidoCorrigidoEscala2 = gerarValorInvestidoCorrigido(valorCreditoList,taxaAdm,prazo,ESCALA2);
            BigDecimal valorInvestidoCorrigidoEscala10 = gerarValorInvestidoCorrigido(valorCreditoList,taxaAdm,prazo,ESCALA10);
            BigDecimal valorVendaEscala2 = gerarValorVenda(valorCreditoAtualizado,valorMesContemplacao,ESCALA2);
            BigDecimal valorVendaEscala10 = gerarValorVenda(valorCreditoAtualizado,valorMesContemplacao,ESCALA10);
            BigDecimal valorIREscala2 = gerarIR(valorVendaEscala2,valorInvestidoCorrigidoEscala2,valorMesContemplacao,ESCALA2);
            BigDecimal valorIREscala10 = gerarIR(valorVendaEscala2,valorInvestidoCorrigidoEscala2,valorMesContemplacao,ESCALA10);


            simulacaoDTOList.add(SimulacaoDTO.builder()
                    .cota(i)
                    .mesContemplacao(valorMesContemplacao)
                    .formaContemplacao("SORTEIO")
                    .creditoAtualizado(valorCreditoAtualizado)
                    .investimentoMensalCorrigido(investimentoMensalCorrigidoEscala2)
                    .valorInvestidoCorrigido(valorInvestidoCorrigidoEscala2)
                    .parcelaPosContemplacao(gerarParcelaPosContemplacao(investimentoMensalCorrigidoEscala10,modalidade,valorMesContemplacao,prazo,lance))
                    .valorVenda(valorVendaEscala2)
                    .ir(valorIREscala2)
                    .lucroLiquido(gerarLucroLiquido(valorVendaEscala10,valorIREscala10,valorInvestidoCorrigidoEscala10))
                    .build());
        }
        return simulacaoDTOList;
    }

    public int valorMesContemplacao(int prazo) {
        return getRandomNumber(1,prazo);
    }

    public BigDecimal gerarCreditoComIncc(int valorMesContemplacao, double incc, BigDecimal valorCredito, int monthValue, List<BigDecimal> valorCreditoList) {
        int counter = 0;
        int mesesRestantes = 13 - monthValue;

        // para definir valorInvestidoCorrigido apenas
        for (int i = 0; i < valorMesContemplacao && i < mesesRestantes; i++) {
            valorCreditoList.add(valorCredito);
        }

        if (valorMesContemplacao > mesesRestantes) {
            counter++;
        }

        for (int i = 25; i <= valorMesContemplacao; i = i + 12) {
            counter++;
        }

        int meses = valorMesContemplacao - mesesRestantes;

        for (int i = 0; i < counter; i++) {
            BigDecimal creditoMaisIncc = valorCredito.multiply(new BigDecimal(incc));
            valorCredito = valorCredito.add(creditoMaisIncc);
            // para definir valorInvestidoCorrigido apenas
            if (meses > 12 ) {
                for (int y = 0; y < 12; y++) {
                    valorCreditoList.add(valorCredito);
                }
                meses = meses - 12;
            } else {
                for (int y = 0; y < meses; y++) {
                    valorCreditoList.add(valorCredito);
                }
            }
        }

        return valorCredito;
    }

    public BigDecimal gerarValorCreditoMaisTaxaAdm(BigDecimal creditoComIncc, double taxaAdm) {
        BigDecimal valorCreditoVezesTaxaAdm = creditoComIncc.multiply(new BigDecimal(taxaAdm));
        return creditoComIncc.add(valorCreditoVezesTaxaAdm);
    }

    public BigDecimal gerarCreditoAtualizado(BigDecimal creditoComIncc, BigDecimal valorCreditoMaisTaxaAdm, double lance) {
        if (lance > 0) {
            BigDecimal valorCreditoVezesLance = valorCreditoMaisTaxaAdm.multiply(new BigDecimal(lance));
            creditoComIncc = creditoComIncc.subtract(valorCreditoVezesLance);
        }

        return creditoComIncc.setScale(2, RoundingMode.HALF_EVEN);
    }

    public BigDecimal gerarInvestimentoMensalCorrigido(BigDecimal valorCreditoMaisTaxaAdm, int prazo, int scale) {
        return valorCreditoMaisTaxaAdm.divide(new BigDecimal(prazo),scale,RoundingMode.HALF_EVEN);
    }

    public BigDecimal gerarValorInvestidoCorrigido(List<BigDecimal> valorCreditoList, double taxaAdm,int prazo, int scale) {
        BigDecimal valorInvestidoCorrigido = new BigDecimal(0);
        for (BigDecimal credito: valorCreditoList) {
            BigDecimal valorCreditoVezesTaxaAdm = credito.multiply(new BigDecimal(taxaAdm));
            BigDecimal creditoComInccMaisValorCredito = credito.add(valorCreditoVezesTaxaAdm);
            BigDecimal valorCreditoMaisTaxaAdmDivididoPrazo = creditoComInccMaisValorCredito.divide(new BigDecimal(prazo),ESCALA10,RoundingMode.HALF_EVEN);
            valorInvestidoCorrigido = valorInvestidoCorrigido.add(valorCreditoMaisTaxaAdmDivididoPrazo);
        }
        return valorInvestidoCorrigido.setScale(scale,RoundingMode.HALF_EVEN);
    }

    public BigDecimal gerarParcelaPosContemplacao(BigDecimal investimentoMensalCorrigido, Modalidade modalidade, int valorMesContemplacao, int prazo, double lance) {
        if (modalidade == Modalidade.CHEIA) {
            if (valorMesContemplacao == prazo) {
                return new BigDecimal(0);
            } else {
                BigDecimal investMenCorrVezesLance = investimentoMensalCorrigido.multiply(new BigDecimal(lance));
                return investimentoMensalCorrigido.subtract(investMenCorrVezesLance).setScale(2,RoundingMode.HALF_EVEN);
            }
        } else {
            return BigDecimal.ZERO; //TODO
        }
    }

    public BigDecimal gerarValorVenda(BigDecimal valorCreditoAtulizado, int valorMesContemplacao, int scale) {
        double taxaValorVenda = valorMesContemplacao <= 30 ? 0.15 : 0.2;
        return valorCreditoAtulizado.multiply(new BigDecimal(taxaValorVenda)).setScale(scale,RoundingMode.HALF_EVEN);
    }

    public BigDecimal gerarIR(BigDecimal valorDaVenda, BigDecimal valorInvestidoCorrigido, int mesContemplacao, int scale) {
        double taxaIR;
        double retorno = 30000.0;
        if (mesContemplacao <= 6) {
            taxaIR = 0.225;
        } else if (mesContemplacao <= 12) {
            taxaIR = 0.2;
        } else if (mesContemplacao <= 24) {
            taxaIR = 0.175;
        } else {
            taxaIR = 0.15;
        }
        BigDecimal valorDaVendaMenosValorInvestido = valorDaVenda.subtract(valorInvestidoCorrigido);
        if (valorDaVendaMenosValorInvestido.compareTo(new BigDecimal(retorno)) > 0) {
            return valorDaVendaMenosValorInvestido.multiply(new BigDecimal(taxaIR)).setScale(scale,RoundingMode.HALF_EVEN);
        } else {
            return BigDecimal.ZERO;
        }
    }

    public BigDecimal gerarLucroLiquido(BigDecimal valorDaVenda, BigDecimal valorIR, BigDecimal investimentoMesalCorrigido) {
        return valorDaVenda.subtract(valorIR).subtract(investimentoMesalCorrigido).setScale(2,RoundingMode.HALF_EVEN);
    }


}
