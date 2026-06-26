package com.example.torneos.dto.response;
import com.example.torneos.enums.EstadoBracket;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;
@Data
@Builder
public class BracketDTOResponse {
    private Long id;
    private Long torneoId;
    private EstadoBracket estado;
    private LocalDateTime fechaCreacion;
    private List<RondaDTOResponse> rondas;
}

