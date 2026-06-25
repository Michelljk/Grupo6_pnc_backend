package com.example.torneos.service.strategy;

import org.springframework.stereotype.Component;
import com.example.torneos.entity.Torneo;

@Component
public class SuizoStrategy implements FormatoStrategy {

    @Override
    public void validarConfiguracion(Torneo torneo) {
        int n = torneo.getMaxParticipantes();
        if (n < 6 || n % 2 != 0) {
            throw new IllegalArgumentException("El formato Suizo requiere un número par de participantes y un mínimo de 6");
        }
    }
}