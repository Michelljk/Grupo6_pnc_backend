package com.example.torneos.service.strategy;

import com.example.torneos.entity.*;
import com.example.torneos.enums.EstadoBracket;
import com.example.torneos.enums.EstadoEnfrentamiento;
import com.example.torneos.enums.EstadoRonda;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class EliminatoriaSimpleStrategy implements FormatoStrategy {

    @Override
    public void validarConfiguracion(Torneo torneo) {
        int n = torneo.getMaxParticipantes();
        if (n < 2 || (n & (n - 1)) != 0) {
            throw new IllegalArgumentException("Para eliminatoria simple, el número de participantes debe ser potencia de 2");
        }
    }

    @Override
    public Bracket generarBracketInicial(Torneo torneo, List<Usuario> participantes) {
        if (participantes == null || participantes.size() < 2) {
            throw new IllegalArgumentException("Se necesitan al menos 2 participantes para generar el bracket");
        }

        // Crear el bracket
        Bracket bracket = Bracket.builder()
                .torneo(torneo)
                .estado(EstadoBracket.EN_PROCESO)
                .rondas(new ArrayList<>())
                .build();

        // Mezclar participantes para seeding aleatorio
        List<Usuario> shuffled = new ArrayList<>(participantes);
        Collections.shuffle(shuffled);

        // Generar la primera ronda
        Ronda primeraRonda = Ronda.builder()
                .bracket(bracket)
                .numeroRonda(1)
                .nombre("Ronda 1")
                .estado(EstadoRonda.EN_PROCESO)
                .enfrentamientos(new ArrayList<>())
                .build();

        // Crear los enfrentamientos de la primera ronda
        for (int i = 0; i < shuffled.size(); i += 2) {
            Enfrentamiento enfrentamiento = Enfrentamiento.builder()
                    .ronda(primeraRonda)
                    .participante1(shuffled.get(i))
                    .participante2((i + 1 < shuffled.size()) ? shuffled.get(i + 1) : null)
                    .estado(EstadoEnfrentamiento.PROGRAMADO)
                    .build();
            primeraRonda.getEnfrentamientos().add(enfrentamiento);
        }

        bracket.getRondas().add(primeraRonda);

        // Calcular cuántas rondas más se necesitan (log2 n)
        int numParticipantes = shuffled.size();
        int numRondas = (int) (Math.log(numParticipantes) / Math.log(2));

        // Crear las rondas subsiguientes (vacías por ahora)
        for (int r = 2; r <= numRondas; r++) {
            Ronda siguienteRonda = Ronda.builder()
                    .bracket(bracket)
                    .numeroRonda(r)
                    .nombre("Ronda " + r)
                    .estado(EstadoRonda.PENDIENTE)
                    .enfrentamientos(new ArrayList<>())
                    .build();

            // Generar enfrentamientos vacíos (TBD)
            int enfrentamientosEnRonda = numParticipantes / (int) Math.pow(2, r);
            for (int e = 0; e < enfrentamientosEnRonda; e++) {
                Enfrentamiento tbd = Enfrentamiento.builder()
                        .ronda(siguienteRonda)
                        .estado(EstadoEnfrentamiento.PROGRAMADO)
                        .build();
                siguienteRonda.getEnfrentamientos().add(tbd);
            }

            bracket.getRondas().add(siguienteRonda);
        }

        return bracket;
    }

    @Override
    public void avanzarRonda(Bracket bracket) {
        List<Ronda> rondas = bracket.getRondas();
        for (int i = 0; i < rondas.size() - 1; i++) {
            Ronda rondaActual = rondas.get(i);
            Ronda rondaSiguiente = rondas.get(i + 1);

            if (rondaActual.getEstado() == EstadoRonda.EN_PROCESO) {
                boolean todosFinalizados = rondaActual.getEnfrentamientos().stream()
                        .allMatch(e -> e.getEstado() == EstadoEnfrentamiento.FINALIZADO);

                if (todosFinalizados) {
                    // Mover ganadores
                    for (int j = 0; j < rondaActual.getEnfrentamientos().size(); j += 2) {
                        Enfrentamiento e1 = rondaActual.getEnfrentamientos().get(j);
                        Enfrentamiento e2 = (j + 1 < rondaActual.getEnfrentamientos().size()) ? rondaActual.getEnfrentamientos().get(j + 1) : null;

                        int matchIndexSiguiente = j / 2;
                        if (matchIndexSiguiente < rondaSiguiente.getEnfrentamientos().size()) {
                            Enfrentamiento siguienteMatch = rondaSiguiente.getEnfrentamientos().get(matchIndexSiguiente);
                            siguienteMatch.setParticipante1(e1.getGanador());
                            if (e2 != null) {
                                siguienteMatch.setParticipante2(e2.getGanador());
                            }
                        }
                    }
                    rondaActual.setEstado(EstadoRonda.FINALIZADO);
                    rondaSiguiente.setEstado(EstadoRonda.EN_PROCESO);
                    break;
                }
            }
        }

        // Verificar si el torneo terminó
        Ronda ultimaRonda = rondas.get(rondas.size() - 1);
        if (ultimaRonda.getEstado() == EstadoRonda.EN_PROCESO) {
            boolean finalizado = ultimaRonda.getEnfrentamientos().stream()
                    .allMatch(e -> e.getEstado() == EstadoEnfrentamiento.FINALIZADO);
            if (finalizado) {
                ultimaRonda.setEstado(EstadoRonda.FINALIZADO);
                bracket.setEstado(EstadoBracket.FINALIZADO);
                bracket.getTorneo().setEstado(com.example.torneos.enums.EstadoTorneo.FINALIZADO);
            }
        }
    }
}