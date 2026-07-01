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

        // Crear lista de participantes para rotacion (agregar bye si es numero impar)
        List<Usuario> rotacion = new ArrayList<>(participantes);
        if (n % 2 == 1) {
            rotacion.add(null); // null representa un bye
            n = rotacion.size();
        }

        for (int r = 1; r <= numRondas; r++) {
            Ronda ronda = Ronda.builder()
                    .bracket(bracket)
                    .numeroRonda(r)
                    .nombre("Jornada " + r)
                    .estado(r == 1 ? EstadoRonda.EN_PROCESO : EstadoRonda.PENDIENTE)
                    .enfrentamientos(new ArrayList<>())
                    .build();

            // Generar emparejamientos para esta ronda
            for (int i = 0; i < n / 2; i++) {
                Usuario p1 = rotacion.get(i);
                Usuario p2 = rotacion.get(n - 1 - i);

                // Solo crear enfrentamiento si ambos participantes existen (no hay bye)
                if (p1 != null && p2 != null) {
                    Enfrentamiento enfrentamiento = Enfrentamiento.builder()
                            .ronda(ronda)
                            .participante1(p1)
                            .participante2(p2)
                            .estado(EstadoEnfrentamiento.PROGRAMADO)
                            .build();
                    ronda.getEnfrentamientos().add(enfrentamiento);
                }
            }

            bracket.getRondas().add(ronda);

            // Rotar participantes para la siguiente ronda (mantener el primero fijo)
            if (r < numRondas) {
                Usuario ultimo = rotacion.remove(n - 1);
                rotacion.add(1, ultimo);
            }
        }

        return bracket;
    }

    @Override
    public void avanzarRonda(Bracket bracket) {
        List<Ronda> rondas = bracket.getRondas();
        boolean todasRondasFinalizadas = true;

        for (int i = 0; i < rondas.size(); i++) {
            Ronda rondaActual = rondas.get(i);
            if (rondaActual.getEstado() == EstadoRonda.EN_PROCESO) {
                boolean todosFinalizados = rondaActual.getEnfrentamientos().stream()
                        .allMatch(e -> e.getEstado() == EstadoEnfrentamiento.FINALIZADO);
                if (todosFinalizados) {
                    rondaActual.setEstado(EstadoRonda.FINALIZADO);
                    if (i + 1 < rondas.size()) {
                        rondas.get(i + 1).setEstado(EstadoRonda.EN_PROCESO);
                    }
                } else {
                    todasRondasFinalizadas = false;
                }
            } else if (rondaActual.getEstado() == EstadoRonda.PENDIENTE) {
                todasRondasFinalizadas = false;
            }
        }

        if (todasRondasFinalizadas) {
            bracket.setEstado(EstadoBracket.FINALIZADO);
            bracket.getTorneo().setEstado(com.example.torneos.enums.EstadoTorneo.FINALIZADO);
        }
    }
}