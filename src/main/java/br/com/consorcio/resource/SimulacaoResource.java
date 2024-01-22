package br.com.consorcio.resource;

import br.com.consorcio.dto.*;
import br.com.consorcio.service.SimulacaoAnualService;
import br.com.consorcio.service.SimulacaoMensalService;
import br.com.consorcio.service.SimulacaoService;
import br.com.consorcio.service.SomaSimulacaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/simulacoes")
@RequiredArgsConstructor
public class SimulacaoResource {

    private final SimulacaoService simulacaoService;
    private final SimulacaoAnualService simulacaoAnualService;
    private final SomaSimulacaoService somaSimulacaoService;
    private final SimulacaoMensalService simulacaoMensalService;

    private List<SimulacaoDTO> simulacaoDTOList = new ArrayList<>();

    @PostMapping
    @Operation(summary = "Simula um investimento no consorcio")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "422", description = "Erro na validação dos campos",
                    content = @Content) })
    public ResponseEntity<List<SimulacaoDTO>> simular(@Valid @RequestBody @Parameter(description = "Parametros de entrada para simular o consorcio") ParametroRequestDTO parametroRequestDTO){
        simulacaoDTOList = simulacaoService.simular(parametroRequestDTO);
        return ok(simulacaoDTOList);
    }

    @GetMapping("/soma")
    @Operation(summary = "Soma os campos Crédito, Investimento Inicial, Investimento Pós Contemplação e Valor Investido")
    public ResponseEntity<SomaSimulacaoDTO> simular(){
        return ok(somaSimulacaoService.somaSimulacao(simulacaoDTOList));
    }


    @PostMapping("/anual")
    @Operation(summary = "Demostra os dados da simulação anualmente")
    @ApiResponses(value = {@ApiResponse(responseCode = "422", description = "Erro na validação dos campos", content = @Content)})
    public ResponseEntity<List<TabelaAnualDTO>> simulacaoAnual(@Valid @RequestBody @Parameter(description = "Parametros de entrada para simular o consorcio anualmente") ParametroRequestDTO parametroRequestDTO) {
        return ok(simulacaoAnualService.simular(parametroRequestDTO));
    }

    @PostMapping("/mensal")
    @Operation(summary = "Demostra os dados da simulação mensalmente")
    @ApiResponses(value = {@ApiResponse(responseCode = "422", description = "Erro na validação dos campos", content = @Content)})
    public ResponseEntity<List<TabelaMensalDTO>> simulacaoMensal(@Valid @RequestBody @Parameter(description = "Parametros de entrada para simular o consorcio mensalmente") ParametroRequestDTO parametroRequestDTO) {
        return ok(simulacaoMensalService.simular(parametroRequestDTO));
    }

}
