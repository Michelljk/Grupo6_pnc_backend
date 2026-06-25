package com.example.torneos.service.strategy;

import org.springframework.stereotype.Component;
import com.example.torneos.entity.Torneo;

@Component
public class EliminatoriaSimpleStrategy implements FormatoStrategy {

    @Override
    public void validarConfiguracion(Torneo torneo) {
        int n = torneo.getMaxParticipantes();
        if (n < 2 || (n & (n - 1)) != 0) {
            throw new IllegalArgumentException("Para eliminatoria simple, el número de participantes debe ser potencia de 2");
        }
    }
}