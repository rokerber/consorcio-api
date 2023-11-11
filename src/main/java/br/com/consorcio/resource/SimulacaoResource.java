package br.com.consorcio.resource;

import br.com.consorcio.dto.ParametroRequestDTO;
import br.com.consorcio.dto.SimulacaoDTO;
import br.com.consorcio.dto.TabelaReajusteDTO;
import br.com.consorcio.service.SimulacaoReajusteService;
import br.com.consorcio.service.SimulacaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/simulacoes")
@RequiredArgsConstructor
public class SimulacaoResource {

    private final SimulacaoService simulacaoService;
    private final SimulacaoReajusteService simulacaoReajusteService;

    @PostMapping
    @Operation(summary = "Simula um investimento no consorcio com até 10 cotas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "422", description = "Erro na validação dos campos",
                    content = @Content) })
    public ResponseEntity<List<SimulacaoDTO>> simular(@Valid @RequestBody @Parameter(description = "Parametros de entrada para simular o consorcio") ParametroRequestDTO parametroRequestDTO){
        return ok(simulacaoService.simular(parametroRequestDTO));
    }

    @PostMapping("/reajuste")
    @Operation(summary = "Simula todos os anos de reajuste do consorcio")
    @ApiResponses(value = {@ApiResponse(responseCode = "422", description = "Erro na validação dos campos", content = @Content)})
    public ResponseEntity<List<TabelaReajusteDTO>> reajuste(@Valid @RequestBody @Parameter(description = "Parametros de entrada para simular o reajuste") ParametroRequestDTO parametroRequestDTO) {
        return ok(simulacaoReajusteService.simular(parametroRequestDTO));
    }

}
