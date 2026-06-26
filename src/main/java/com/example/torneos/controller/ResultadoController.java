package com.example.torneos.controller;
import com.example.torneos.dto.GeneralResponse;
import com.example.torneos.dto.request.RegistrarResultadoRequest;
import com.example.torneos.service.ResultadoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/api/resultados")
@RequiredArgsConstructor
public class ResultadoController {
    private final ResultadoService resultadoService;
    @PostMapping("/registrar/{matchId}")
    public ResponseEntity<GeneralResponse> registrarResultado(@PathVariable Long matchId, @Valid @RequestBody RegistrarResultadoRequest request) {
        return ResponseEntity.ok(GeneralResponse.builder()
                .data(resultadoService.registrarResultado(matchId, request))
                .message("Resultado registrado exitosamente")
                .build());
    }
}
