package br.com.consorcio.resource;

import br.com.consorcio.dto.ParametroRequestDTO;
import br.com.consorcio.dto.SimulacaoDTO;
import br.com.consorcio.dto.SomaSimulacaoDTO;
import br.com.consorcio.dto.TabelaAnualDTO;
import br.com.consorcio.dto.TabelaMensalDTO;
import br.com.consorcio.service.SimulacaoAnualService;
import br.com.consorcio.service.SimulacaoMensalService;
import br.com.consorcio.service.SimulacaoService;
import br.com.consorcio.service.SomaSimulacaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/api/simulacoes")
@RequiredArgsConstructor
public class SimulacaoResource {

    private final SimulacaoService simulacaoService;
    private final SimulacaoAnualService simulacaoAnualService;
    private final SomaSimulacaoService somaSimulacaoService;
    private final SimulacaoMensalService simulacaoMensalService;

    private List<SimulacaoDTO> simulacaoDTOList = new ArrayList<>();

    @PostMapping
    public ResponseEntity<List<SimulacaoDTO>> simular(@RequestBody ParametroRequestDTO parametroRequestDTO) {
        simulacaoDTOList = simulacaoService.simular(parametroRequestDTO);
        return ok(simulacaoDTOList);
    }

    @GetMapping("/soma")
    public ResponseEntity<SomaSimulacaoDTO> simular() {
        return ok(somaSimulacaoService.somaSimulacao(simulacaoDTOList));
    }


    @PostMapping("/anual")
    public ResponseEntity<List<TabelaAnualDTO>> simulacaoAnual(@RequestBody ParametroRequestDTO parametroRequestDTO) {
        return ok(simulacaoAnualService.simular(parametroRequestDTO));
    }

    @PostMapping("/mensal")
    public ResponseEntity<List<TabelaMensalDTO>> simulacaoMensal(@RequestBody ParametroRequestDTO parametroRequestDTO) {
        return ok(simulacaoMensalService.simular(parametroRequestDTO));
    }

}
