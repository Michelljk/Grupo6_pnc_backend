package com.example.torneos.service.strategy;

import com.example.torneos.entity.*;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class LigaStrategy implements FormatoStrategy {

    @Override
    public void validarConfiguracion(Torneo torneo) {
        if (torneo.getMaxParticipantes() < 2) {
            throw new IllegalArgumentException("El formato Liga requiere al menos 2 participantes");
        }
    }

    @Override
    public Bracket generarBracketInicial(Torneo torneo, List<Usuario> participantes) {
        // En este contexto, Liga y Round Robin comparten la misma lógica de todos contra todos
        return new RoundRobinStrategy().generarBracketInicial(torneo, participantes);
    }

    @Override
    public void avanzarRonda(Bracket bracket) {
        new RoundRobinStrategy().avanzarRonda(bracket);
    }
}
