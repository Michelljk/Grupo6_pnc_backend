package com.example.torneos.service.strategy;

import org.springframework.stereotype.Component;
import com.example.torneos.entity.Torneo;

@Component
public class RoundRobinStrategy implements FormatoStrategy {

    @Override
    public void validarConfiguracion(Torneo torneo) {
        if (torneo.getMaxParticipantes() < 2) {
            throw new IllegalArgumentException("Round Robin requiere al menos 2 participantes");
        }
    }
}