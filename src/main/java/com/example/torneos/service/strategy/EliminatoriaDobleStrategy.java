package com.example.torneos.service.strategy;

import org.springframework.stereotype.Component;
import com.example.torneos.entity.Torneo;

@Component
public class EliminatoriaDobleStrategy implements FormatoStrategy {

    @Override
    public void validarConfiguracion(Torneo torneo) {
        int n = torneo.getMaxParticipantes();
        if (n < 4 || (n & (n - 1)) != 0) {
            throw new IllegalArgumentException("Para eliminatoria doble, el número de participantes debe ser mínimo 4 y potencia de 2 (4, 8, 16, 32...)");
        }
    }
}