package br.com.consorcio.service;

import br.com.consorcio.dto.ParametroRequestDTO;
import br.com.consorcio.dto.SimulacaoDTO;
import br.com.consorcio.enums.Estrategia;
import br.com.consorcio.enums.Modalidade;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.*;

import static br.com.consorcio.utils.Util.getRandomNumber;
import static br.com.consorcio.utils.Util.validaCampos;

@Service
public class SimulacaoService {

    private static final int COTA = 10;
    public static final int ESCALA2 = 2;
    public static final int ESCALA10 = 10;

    public List<SimulacaoDTO> simular(ParametroRequestDTO parametroRequestDTO) {
        validaCampos(parametroRequestDTO);
        // monta a tabela para retornar
        List<SimulacaoDTO> simulacaoDTOList = new ArrayList<>();
        LocalDate currentdate = LocalDate.now();
        double lance = parametroRequestDTO.getLance() * 0.01;
        double taxaAdm = parametroRequestDTO.getTaxaAdm() * 0.01;
        double incc = parametroRequestDTO.getIncc() * 0.01;
        int prazo = parametroRequestDTO.getPrazo();
        BigDecimal valorCredito = parametroRequestDTO.getValorCredito();
        Modalidade modalidade = parametroRequestDTO.getModalidade();
        String formaContemplacao = lance == 0.0 || ObjectUtils.isEmpty(lance) ? "SORTEIO" : "Lance Fixo";
        for (int i = 1; i <= COTA; i++) {
            List<BigDecimal> valorCreditoList = new ArrayList<>();
            Set<BigDecimal> investimentoMensalSet = new HashSet<>();

            int valorMesContemplacao = valorMesContemplacao(prazo);
            BigDecimal creditoComIncc = gerarCreditoComIncc(valorMesContemplacao, incc, valorCredito, parametroRequestDTO.getMesAtual(), valorCreditoList);
            BigDecimal valorCreditoMaisTaxaAdm = gerarValorCreditoMaisTaxaAdm(creditoComIncc, taxaAdm);
            BigDecimal valorCreditoAtualizadoEscala2 = gerarCreditoAtualizado(creditoComIncc, valorCreditoMaisTaxaAdm, lance, ESCALA2);
            BigDecimal investimentoMensalCorrigidoEscala2 = gerarInvestimentoMensalCorrigido(valorCreditoMaisTaxaAdm, prazo, ESCALA2,modalidade);
            BigDecimal investimentoMensalCorrigidoEscala10Cheia = gerarInvestimentoMensalCorrigido(valorCreditoMaisTaxaAdm, prazo, ESCALA10,Modalidade.CHEIA);
            BigDecimal valorInvestidoCorrigidoEscala10 = gerarValorInvestidoCorrigido(valorCreditoList,investimentoMensalSet, taxaAdm, prazo, ESCALA10,modalidade);
            BigDecimal valorInvestidoCorrigidoEscala2 = valorInvestidoCorrigidoEscala10.setScale(ESCALA2,RoundingMode.HALF_EVEN);
            BigDecimal valorVendaEscala10 = gerarValorVenda(valorCreditoAtualizadoEscala2, valorMesContemplacao, ESCALA10);
            BigDecimal valorVendaEscala2 = valorVendaEscala10.setScale(ESCALA2,RoundingMode.HALF_EVEN);
            BigDecimal valorIREscala10 = gerarIR(valorVendaEscala2, valorInvestidoCorrigidoEscala2, valorMesContemplacao, ESCALA10);
            BigDecimal valorIREscala2 = valorIREscala10.setScale(ESCALA2,RoundingMode.HALF_EVEN);
            BigDecimal valorLucroLiquidoEscala2 = gerarLucroLiquido(valorVendaEscala10, valorIREscala10, valorInvestidoCorrigidoEscala10, ESCALA2);
            BigDecimal retornSobCapitalInvest = gerarRetornoSobreCapitalInvestido(valorLucroLiquidoEscala2, valorInvestidoCorrigidoEscala2);

            simulacaoDTOList.add(SimulacaoDTO.builder()
                    .cota(i)
                    .mesContemplacao(valorMesContemplacao)
                    .formaContemplacao(formaContemplacao)
                    .creditoAtualizado(valorCreditoAtualizadoEscala2)
                    .investimentoMensalCorrigido(investimentoMensalCorrigidoEscala2)
                    .valorInvestidoCorrigido(valorInvestidoCorrigidoEscala2)
                    .parcelaPosContemplacao(gerarParcelaPosContemplacao(investimentoMensalCorrigidoEscala10Cheia,investimentoMensalSet,valorInvestidoCorrigidoEscala10, modalidade, valorMesContemplacao, prazo, lance,taxaAdm))
                    .valorVenda(valorVendaEscala2)
                    .ir(valorIREscala2)
                    .lucroLiquido(valorLucroLiquidoEscala2)
                    .retornSobCapitalInvest(retornSobCapitalInvest.toString().concat("%"))
                    .estrategia(setarEstrategia(retornSobCapitalInvest,valorMesContemplacao,prazo))
                    .build());
        }
        return simulacaoDTOList;
    }

    public int valorMesContemplacao(int prazo) {
        return getRandomNumber(1, prazo);
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

        for (int i = 25 - monthValue; i <= valorMesContemplacao; i += 12) {
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
        BigDecimal valorCreditoVezesTaxaAdm = creditoComIncc.multiply(BigDecimal.valueOf(taxaAdm));
        return creditoComIncc.add(valorCreditoVezesTaxaAdm);
    }

    public BigDecimal gerarCreditoAtualizado(BigDecimal creditoComIncc, BigDecimal valorCreditoMaisTaxaAdm, double lance, int scale) {
        if (lance > 0) {
            BigDecimal valorCreditoVezesLance = valorCreditoMaisTaxaAdm.multiply(BigDecimal.valueOf(lance));
            creditoComIncc = creditoComIncc.subtract(valorCreditoVezesLance);
        }

        return creditoComIncc.setScale(scale, RoundingMode.HALF_EVEN);
    }

    public BigDecimal gerarInvestimentoMensalCorrigido(BigDecimal valorCreditoMaisTaxaAdm, int prazo, int scale, Modalidade modalidade) {
        BigDecimal investMenCorr = valorCreditoMaisTaxaAdm.divide(BigDecimal.valueOf(prazo), ESCALA10, RoundingMode.HALF_EVEN);

        if (modalidade == Modalidade.CHEIA) {
            return investMenCorr.setScale(scale,RoundingMode.HALF_EVEN);
        } else {
            return investMenCorr.divide(BigDecimal.valueOf(2),scale,RoundingMode.HALF_EVEN);
        }
    }

    public BigDecimal gerarValorInvestidoCorrigido(List<BigDecimal> valorCreditoList,Set<BigDecimal> investimentoMensalSet, double taxaAdm, int prazo, int scale, Modalidade modalidade) {
        BigDecimal valorInvestidoCorrigido = BigDecimal.ZERO;
        for (BigDecimal credito : valorCreditoList) {
            BigDecimal valorCreditoVezesTaxaAdm = credito.multiply(BigDecimal.valueOf(taxaAdm));
            BigDecimal creditoComInccMaisValorCredito = credito.add(valorCreditoVezesTaxaAdm);
            BigDecimal valorCreditoMaisTaxaAdmDivididoPrazo = creditoComInccMaisValorCredito.divide(BigDecimal.valueOf(prazo), ESCALA10, RoundingMode.HALF_EVEN);
            if(scale == ESCALA10) {
                investimentoMensalSet.add(valorCreditoMaisTaxaAdmDivididoPrazo);
            }
            valorInvestidoCorrigido = valorInvestidoCorrigido.add(valorCreditoMaisTaxaAdmDivididoPrazo);
        }
        if (modalidade == Modalidade.CHEIA) {
            return valorInvestidoCorrigido.setScale(scale, RoundingMode.HALF_EVEN);
        } else {
            return valorInvestidoCorrigido.divide(BigDecimal.valueOf(2),scale,RoundingMode.HALF_EVEN);
        }
    }

    public BigDecimal gerarParcelaPosContemplacao(BigDecimal investimentoMensalCorrigido,Set<BigDecimal> investimentoMensalSet, BigDecimal valorInvestidoCorrigido, Modalidade modalidade, int valorMesContemplacao, int prazo, double lance, double taxaAdm) {
        if (valorMesContemplacao == prazo) {
            return BigDecimal.valueOf(0);
        }
        BigDecimal investMenCorrVezesLance = investimentoMensalCorrigido.multiply(BigDecimal.valueOf(lance));
        if (modalidade == Modalidade.CHEIA) {
           return investimentoMensalCorrigido.subtract(investMenCorrVezesLance).setScale(ESCALA2, RoundingMode.HALF_EVEN);
        } else {
            int ano = prazo - 12 * (investimentoMensalSet.size() - 1);
            int mesesRestantes = prazo - valorMesContemplacao;
            BigDecimal saldoDevedor = investimentoMensalCorrigido.multiply(BigDecimal.valueOf(ano));

            BigDecimal saldoDevedorInicial = getSaldoDevedorInicial(investimentoMensalSet, saldoDevedor);

            BigDecimal saldoDevedorInicialMenosLance = saldoDevedorInicial.subtract(saldoDevedorInicial.multiply(BigDecimal.valueOf(lance)));

            BigDecimal saldoDevedorFinal = saldoDevedorInicialMenosLance.subtract(valorInvestidoCorrigido);

            return saldoDevedorFinal.divide(BigDecimal.valueOf(mesesRestantes),ESCALA2,RoundingMode.HALF_EVEN);
        }
    }

    public BigDecimal getSaldoDevedorInicial(Set<BigDecimal> investimentoMensalSet, BigDecimal saldoDevedor) {
        BigDecimal anual = BigDecimal.ZERO;

        List<BigDecimal> investimentoMensalList = new ArrayList<>(investimentoMensalSet);

        Collections.sort(investimentoMensalList);

        investimentoMensalList.remove(investimentoMensalList.size() - 1);

        for (BigDecimal invest: investimentoMensalList) {
            anual = anual.add(invest.multiply(BigDecimal.valueOf(12)));
        }

        return saldoDevedor.add(anual);
    }

    public BigDecimal gerarValorVenda(BigDecimal valorCreditoAtulizado, int valorMesContemplacao, int scale) {
        double taxaValorVenda = valorMesContemplacao <= 30 ? 0.15 : 0.2;
        return valorCreditoAtulizado.multiply(BigDecimal.valueOf(taxaValorVenda)).setScale(scale, RoundingMode.HALF_EVEN);
    }

    public BigDecimal gerarIR(BigDecimal valorDaVenda, BigDecimal valorInvestidoCorrigido, int mesContemplacao, int scale) {
        double taxaIR;
        double retorno;
        taxaIR = switch (mesContemplacao) {
            case 1, 2, 3, 4, 5, 6 -> 0.225;
            case 7, 8, 9, 10, 11, 12 -> 0.2;
            case 13, 14, 15, 16, 17, 18 -> 0.175;
            default -> 0.15;
        };
        retorno = 30000.0;
        BigDecimal valorDaVendaMenosValorInvestido = valorDaVenda.subtract(valorInvestidoCorrigido);
        return valorDaVendaMenosValorInvestido.compareTo(BigDecimal.valueOf(retorno)) > 0
                ? valorDaVendaMenosValorInvestido.multiply(BigDecimal.valueOf(taxaIR)).setScale(scale, RoundingMode.HALF_EVEN)
                : BigDecimal.ZERO;
    }

    public BigDecimal gerarLucroLiquido(BigDecimal valorDaVenda, BigDecimal valorIR, BigDecimal valorInvestidoCorrigido, int scale) {
        return valorDaVenda.subtract(valorIR).subtract(valorInvestidoCorrigido).setScale(scale, RoundingMode.HALF_EVEN);
    }

    public BigDecimal gerarRetornoSobreCapitalInvestido(BigDecimal lucroLiquido, BigDecimal valorInvestidoCorrigido) {
        return lucroLiquido.divide(valorInvestidoCorrigido, ESCALA10, RoundingMode.HALF_EVEN).multiply(BigDecimal.valueOf(100)).setScale(ESCALA2, RoundingMode.HALF_EVEN);
    }

    public String setarEstrategia(BigDecimal retornSobCapitalInvest, int mesContemplacao, int prazo) {
        if (mesContemplacao == prazo ) {
            return Estrategia.PREVTURBINADA.getDescricao();
        } else if (retornSobCapitalInvest.compareTo(BigDecimal.ZERO) > 0) {
            return Estrategia.CISRG.getDescricao();
        } else {
            return Estrategia.CIPRP.getDescricao();
        }
    }

}
