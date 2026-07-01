package com.example.torneos.dto.response;
import com.example.torneos.enums.EstadoRonda;
import lombok.Builder;
import lombok.Data;
import java.util.List;
@Data
@Builder
public class RondaDTOResponse {
    private Long id;
    private Integer numeroRonda;
    private String nombre;
    private EstadoRonda estado;
    private List<EnfrentamientoDTOResponse> enfrentamientos;
}
