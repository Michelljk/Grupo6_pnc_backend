package com.example.torneos.controller;

import com.example.torneos.dto.GeneralResponse;
import com.example.torneos.service.ExportacionBracketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/exportacion")
@RequiredArgsConstructor
public class ExportacionController {

    private final ExportacionBracketService exportacionService;

    @GetMapping("/bracket/{bracketId}/pdf")
    public ResponseEntity<byte[]> exportarBracketAPDF(@PathVariable Long bracketId) throws IOException {
        byte[] pdfContent = exportacionService.exportarBracketAPDF(bracketId);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=bracket_" + bracketId + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfContent);
    }

    @GetMapping("/bracket/{bracketId}/json")
    public ResponseEntity<GeneralResponse> exportarBracketAJSON(@PathVariable Long bracketId) {
        String jsonContent = exportacionService.generarResumenBracketJSON(bracketId);

        return ResponseEntity.ok(GeneralResponse.builder()
                .data(jsonContent)
                .message("Bracket exportado a JSON exitosamente")
                .build());
    }
}
