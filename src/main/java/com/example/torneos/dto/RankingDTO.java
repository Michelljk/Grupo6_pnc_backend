package com.example.torneos.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RankingDTO {
    private Long usuarioId;
    private String nombreUsuario;
    private Integer totalPuntos;
    private Integer posicion;
}
