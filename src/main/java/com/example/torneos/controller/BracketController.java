package com.example.torneos.controller;
import com.example.torneos.dto.GeneralResponse;
import com.example.torneos.mapper.BracketMapper;
import com.example.torneos.service.BracketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/brackets")
@RequiredArgsConstructor

public class BracketController {
    private final BracketService bracketService;

    @PostMapping("/iniciar/{torneoId}")
    public ResponseEntity<GeneralResponse> iniciarTorneo(@PathVariable Long torneoId) {
        return ResponseEntity.ok(GeneralResponse.builder()
                .data(BracketMapper.toResponse(bracketService.iniciarTorneo(torneoId)))
                .message("Torneo iniciado y bracket generado")
                .build());
    }

    @PostMapping("/{bracketId}/avanzar")
    public ResponseEntity<GeneralResponse> avanzarRonda(@PathVariable Long bracketId) {
        bracketService.avanzarRonda(bracketId);
        return ResponseEntity.ok(GeneralResponse.builder()
                .message("Bracket avanzado a la siguiente ronda")
                .build());
    }
}
