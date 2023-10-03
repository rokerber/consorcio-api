package br.com.consorcio.service;

import br.com.consorcio.dto.ParametroRequestDTO;
import br.com.consorcio.dto.SimulacaoDTO;
import org.springframework.stereotype.Service;

@Service
public class SimulacaoService {
    public SimulacaoDTO simular(ParametroRequestDTO parametroRequestDTO){
        // monta a tabela para retornar
        return new SimulacaoDTO();
    }
}
