package br.com.consorcio.resource;

import br.com.consorcio.dto.ParametroRequestDTO;
import br.com.consorcio.service.SimulacaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/simulacoes")
@RequiredArgsConstructor
public class SimulacaoResource {

    private final SimulacaoService simulacaoService;

    @PostMapping
    public ResponseEntity simular(@RequestBody ParametroRequestDTO parametroRequestDTO){
        return ok(simulacaoService.simular(parametroRequestDTO));
    }

}
