package br.com.consorcio.resource;

import br.com.consorcio.dto.ParametroRequestDTO;
import br.com.consorcio.dto.TabelaReajusteDTO;
import br.com.consorcio.service.SimulacaoReajusteService;
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
@RequestMapping("/simulacoes/reajuste")
@RequiredArgsConstructor
public class SimulacaoReajusteResource {

    private final SimulacaoReajusteService simulacaoReajusteService;

    @PostMapping
    @CrossOrigin
    @Operation(summary = "Simula todos os anos de reajuste do consorcio")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "422", description = "Erro na validação dos campos",
                    content = @Content) })
    public ResponseEntity<List<TabelaReajusteDTO>> simular(@Valid @RequestBody @Parameter(description = "Parametros de entrada para simular o reajuste") ParametroRequestDTO parametroRequestDTO){
        return ok(simulacaoReajusteService.simular(parametroRequestDTO));
    }

}
