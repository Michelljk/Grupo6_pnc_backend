package com.example.torneos.service.strategy;

import org.springframework.stereotype.Component;
import com.example.torneos.entity.Torneo;

@Component
public class LigaStrategy implements FormatoStrategy {

    @Override
    public void validarConfiguracion(Torneo torneo) {
        if (torneo.getMaxParticipantes() < 3) {
            throw new IllegalArgumentException("El formato de liga requiere un mínimo de 3 participantes");
        }
    }
}