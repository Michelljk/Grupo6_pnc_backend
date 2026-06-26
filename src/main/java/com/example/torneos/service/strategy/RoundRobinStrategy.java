package com.example.torneos.service.strategy;

import com.example.torneos.entity.*;
import com.example.torneos.enums.EstadoBracket;
import com.example.torneos.enums.EstadoEnfrentamiento;
import com.example.torneos.enums.EstadoRonda;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class RoundRobinStrategy implements FormatoStrategy {

    @Override
    public void validarConfiguracion(Torneo torneo) {
        if (torneo.getMaxParticipantes() < 2) {
            throw new IllegalArgumentException("Round Robin requiere al menos 2 participantes");
        }
    }

    @Override
    public Bracket generarBracketInicial(Torneo torneo, List<Usuario> participantes) {
        if (participantes == null || participantes.size() < 2) {
            throw new IllegalArgumentException("Se necesitan al menos 2 participantes");
        }

        Bracket bracket = Bracket.builder()
                .torneo(torneo)
                .estado(EstadoBracket.EN_PROCESO)
                .rondas(new ArrayList<>())
                .build();

        int n = participantes.size();
        int numRondas = (n % 2 == 0) ? n - 1 : n;
        int partidosPorRonda = n / 2;

        for (int r = 1; r <= numRondas; r++) {
            Ronda ronda = Ronda.builder()
                    .bracket(bracket)
                    .numeroRonda(r)
                    .nombre("Jornada " + r)
                    .estado(r == 1 ? EstadoRonda.EN_PROCESO : EstadoRonda.PENDIENTE)
                    .enfrentamientos(new ArrayList<>())
                    .build();


            for (int i = 0; i < partidosPorRonda; i++) {
                Enfrentamiento enfrentamiento = Enfrentamiento.builder()
                        .ronda(ronda)
                        .participante1(participantes.get(i))
                        .participante2(participantes.get(n - 1 - i))
                        .estado(EstadoEnfrentamiento.PROGRAMADO)
                        .build();
                ronda.getEnfrentamientos().add(enfrentamiento);
            }

            bracket.getRondas().add(ronda);
        }

        return bracket;
    }

    @Override
    public void avanzarRonda(Bracket bracket) {
        
    }
}