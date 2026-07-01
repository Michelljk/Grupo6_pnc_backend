package com.example.torneos.dto.usuario;

import com.example.torneos.enums.RolUsuario;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class UsuarioResponse {
    private Long id;
    private String nombre;
    private String email;
    private RolUsuario rol;
    private Integer creditosVirtuales;
    private Integer eloRating;
    private LocalDateTime creadoEn;
}
