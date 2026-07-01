package com.example.torneos.service.strategy;

import com.example.torneos.entity.*;
import com.example.torneos.enums.EstadoBracket;
import com.example.torneos.enums.EstadoEnfrentamiento;
import com.example.torneos.enums.EstadoRonda;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class EliminatoriaDobleStrategy implements FormatoStrategy {

    @Override
    public void validarConfiguracion(Torneo torneo) {
        int n = torneo.getMaxParticipantes();
        if (n < 4 || (n & (n - 1)) != 0) {
            throw new IllegalArgumentException("Para eliminatoria doble, el número de participantes debe ser potencia de 2 y al menos 4");
        }
    }

    @Override
    public Bracket generarBracketInicial(Torneo torneo, List<Usuario> participantes) {
        if (participantes == null || participantes.size() < 4) {
            throw new IllegalArgumentException("Se necesitan al menos 4 participantes para generar un bracket de eliminatoria doble");
        }

        Bracket bracket = Bracket.builder()
                .torneo(torneo)
                .estado(EstadoBracket.EN_PROCESO)
                .rondas(new ArrayList<>())
                .build();

        List<Usuario> shuffled = new ArrayList<>(participantes);
        Collections.shuffle(shuffled);

        // Generar bracket de ganadores (Winners Bracket)
        generarWinnersBracket(bracket, shuffled);

        // Generar bracket de perdedores (Losers Bracket)
        generarLosersBracket(bracket, shuffled);

        return bracket;
    }

    private void generarWinnersBracket(Bracket bracket, List<Usuario> participantes) {
        int numParticipantes = participantes.size();
        int numRondas = (int) (Math.log(numParticipantes) / Math.log(2));

        for (int r = 1; r <= numRondas; r++) {
            Ronda ronda = Ronda.builder()
                    .bracket(bracket)
                    .numeroRonda(r)
                    .nombre("Winners Ronda " + r)
                    .estado(r == 1 ? EstadoRonda.EN_PROCESO : EstadoRonda.PENDIENTE)
                    .enfrentamientos(new ArrayList<>())
                    .build();

            if (r == 1) {
                // Primera ronda de ganadores: emparejar todos los participantes
                for (int i = 0; i < participantes.size(); i += 2) {
                    Enfrentamiento enfrentamiento = Enfrentamiento.builder()
                            .ronda(ronda)
                            .participante1(participantes.get(i))
                            .participante2(participantes.get(i + 1))
                            .estado(EstadoEnfrentamiento.PROGRAMADO)
                            .build();
                    ronda.getEnfrentamientos().add(enfrentamiento);
                }
            } else {
                // Rondas subsiguientes: crear enfrentamientos vacíos (TBD)
                int enfrentamientosEnRonda = numParticipantes / (int) Math.pow(2, r);
                for (int e = 0; e < enfrentamientosEnRonda; e++) {
                    Enfrentamiento tbd = Enfrentamiento.builder()
                            .ronda(ronda)
                            .estado(EstadoEnfrentamiento.PROGRAMADO)
                            .build();
                    ronda.getEnfrentamientos().add(tbd);
                }
            }

            bracket.getRondas().add(ronda);
        }
    }

    private void generarLosersBracket(Bracket bracket, List<Usuario> participantes) {
        int numParticipantes = participantes.size();
        int numRondasWinners = (int) (Math.log(numParticipantes) / Math.log(2));

        // El losers bracket tiene el doble de rondas que el winners bracket
        int numRondasLosers = 2 * numRondasWinners - 1;

        for (int r = 1; r <= numRondasLosers; r++) {
            Ronda ronda = Ronda.builder()
                    .bracket(bracket)
                    .numeroRonda(numRondasWinners + r)
                    .nombre("Losers Ronda " + r)
                    .estado(EstadoRonda.PENDIENTE)
                    .enfrentamientos(new ArrayList<>())
                    .build();

            // Calcular el número de enfrentamientos en esta ronda del losers bracket
            int enfrentamientosEnRonda;
            if (r == 1) {
                // Primera ronda de perdedores: todos los perdedores de la primera ronda de ganadores
                enfrentamientosEnRonda = numParticipantes / 4;
            } else {
                // Rondas subsiguientes
                enfrentamientosEnRonda = (int) Math.max(1, numParticipantes / Math.pow(2, r + 1));
            }

            for (int e = 0; e < enfrentamientosEnRonda; e++) {
                Enfrentamiento tbd = Enfrentamiento.builder()
                        .ronda(ronda)
                        .estado(EstadoEnfrentamiento.PROGRAMADO)
                        .build();
                ronda.getEnfrentamientos().add(tbd);
            }

            bracket.getRondas().add(ronda);
        }

        // Crear la final (Grand Final) si es necesaria
        Ronda finalRonda = Ronda.builder()
                .bracket(bracket)
                .numeroRonda(numRondasWinners + numRondasLosers + 1)
                .nombre("Grand Final")
                .estado(EstadoRonda.PENDIENTE)
                .enfrentamientos(new ArrayList<>())
                .build();

        Enfrentamiento finalMatch = Enfrentamiento.builder()
                .ronda(finalRonda)
                .estado(EstadoEnfrentamiento.PROGRAMADO)
                .build();
        finalRonda.getEnfrentamientos().add(finalMatch);
        bracket.getRondas().add(finalRonda);
    }

    @Override
    public void avanzarRonda(Bracket bracket) {
        List<Ronda> rondas = bracket.getRondas();
        int numRondasWinners = (int) (Math.log(bracket.getTorneo().getMaxParticipantes()) / Math.log(2));

        // Procesar avance en el winners bracket
        for (int i = 0; i < numRondasWinners - 1; i++) {
            Ronda rondaActual = rondas.get(i);
            Ronda rondaSiguiente = rondas.get(i + 1);

            if (rondaActual.getEstado() == EstadoRonda.EN_PROCESO) {
                boolean todosFinalizados = rondaActual.getEnfrentamientos().stream()
                        .allMatch(e -> e.getEstado() == EstadoEnfrentamiento.FINALIZADO);

                if (todosFinalizados) {
                    // Mover ganadores a la siguiente ronda del winners bracket
                    for (int j = 0; j < rondaActual.getEnfrentamientos().size(); j++) {
                        Enfrentamiento enfrentamientoActual = rondaActual.getEnfrentamientos().get(j);
                        int matchIndexSiguiente = j / 2;

                        if (matchIndexSiguiente < rondaSiguiente.getEnfrentamientos().size()) {
                            Enfrentamiento siguienteMatch = rondaSiguiente.getEnfrentamientos().get(matchIndexSiguiente);
                            if (j % 2 == 0) {
                                siguienteMatch.setParticipante1(enfrentamientoActual.getGanador());
                            } else {
                                siguienteMatch.setParticipante2(enfrentamientoActual.getGanador());
                            }
                        }

                        // Enviar perdedores al losers bracket
                        enviarAlLosersBracket(bracket, enfrentamientoActual, i);
                    }

                    rondaActual.setEstado(EstadoRonda.FINALIZADO);
                    rondaSiguiente.setEstado(EstadoRonda.EN_PROCESO);
                    break;
                }
            }
        }

        // Procesar la última ronda del winners bracket
        Ronda ultimaRondaWinners = rondas.get(numRondasWinners - 1);
        if (ultimaRondaWinners.getEstado() == EstadoRonda.EN_PROCESO) {
            boolean todosFinalizados = ultimaRondaWinners.getEnfrentamientos().stream()
                    .allMatch(e -> e.getEstado() == EstadoEnfrentamiento.FINALIZADO);

            if (todosFinalizados) {
                ultimaRondaWinners.setEstado(EstadoRonda.FINALIZADO);

                // El ganador del winners bracket va a la Grand Final
                Enfrentamiento finalWinners = ultimaRondaWinners.getEnfrentamientos().get(0);
                Ronda grandFinal = rondas.get(rondas.size() - 1);
                Enfrentamiento finalMatch = grandFinal.getEnfrentamientos().get(0);
                finalMatch.setParticipante1(finalWinners.getGanador());
                grandFinal.setEstado(EstadoRonda.EN_PROCESO);
            }
        }

        // Procesar avance en el losers bracket
        procesarLosersBracket(bracket, rondas, numRondasWinners);

        // Verificar si el torneo terminó (Grand Final finalizada)
        Ronda grandFinal = rondas.get(rondas.size() - 1);
        if (grandFinal.getEstado() == EstadoRonda.EN_PROCESO) {
            boolean finalizado = grandFinal.getEnfrentamientos().stream()
                    .allMatch(e -> e.getEstado() == EstadoEnfrentamiento.FINALIZADO);
            if (finalizado) {
                grandFinal.setEstado(EstadoRonda.FINALIZADO);
                bracket.setEstado(EstadoBracket.FINALIZADO);
                bracket.getTorneo().setEstado(com.example.torneos.enums.EstadoTorneo.FINALIZADO);
            }
        }
    }

    private void enviarAlLosersBracket(Bracket bracket, Enfrentamiento enfrentamientoWinners, int rondaWinnersIndex) {
        Usuario perdedor = enfrentamientoWinners.getParticipante1().equals(enfrentamientoWinners.getGanador())
                ? enfrentamientoWinners.getParticipante2()
                : enfrentamientoWinners.getParticipante1();

        if (perdedor != null) {
            int numRondasWinners = (int) (Math.log(bracket.getTorneo().getMaxParticipantes()) / Math.log(2));
            int rondaLosersIndex = numRondasWinners + rondaWinnersIndex;

            List<Ronda> rondas = bracket.getRondas();
            if (rondaLosersIndex < rondas.size()) {
                Ronda rondaLosers = rondas.get(rondaLosersIndex);

                // Encontrar un enfrentamiento disponible en la ronda de perdedores
                for (Enfrentamiento e : rondaLosers.getEnfrentamientos()) {
                    if (e.getParticipante1() == null) {
                        e.setParticipante1(perdedor);
                        rondaLosers.setEstado(EstadoRonda.EN_PROCESO);
                        break;
                    } else if (e.getParticipante2() == null) {
                        e.setParticipante2(perdedor);
                        rondaLosers.setEstado(EstadoRonda.EN_PROCESO);
                        break;
                    }
                }
            }
        }
    }

    private void procesarLosersBracket(Bracket bracket, List<Ronda> rondas, int numRondasWinners) {
        int numRondasLosers = 2 * numRondasWinners - 1;

        for (int i = numRondasWinners; i < numRondasWinners + numRondasLosers - 1; i++) {
            Ronda rondaActual = rondas.get(i);
            Ronda rondaSiguiente = rondas.get(i + 1);

            if (rondaActual.getEstado() == EstadoRonda.EN_PROCESO) {
                boolean todosFinalizados = rondaActual.getEnfrentamientos().stream()
                        .allMatch(e -> e.getEstado() == EstadoEnfrentamiento.FINALIZADO);

                if (todosFinalizados) {
                    // Mover ganadores a la siguiente ronda del losers bracket
                    for (int j = 0; j < rondaActual.getEnfrentamientos().size(); j++) {
                        Enfrentamiento enfrentamientoActual = rondaActual.getEnfrentamientos().get(j);
                        int matchIndexSiguiente = j / 2;

                        if (matchIndexSiguiente < rondaSiguiente.getEnfrentamientos().size()) {
                            Enfrentamiento siguienteMatch = rondaSiguiente.getEnfrentamientos().get(matchIndexSiguiente);
                            if (j % 2 == 0) {
                                siguienteMatch.setParticipante1(enfrentamientoActual.getGanador());
                            } else {
                                siguienteMatch.setParticipante2(enfrentamientoActual.getGanador());
                            }
                        }
                    }

                    rondaActual.setEstado(EstadoRonda.FINALIZADO);
                    rondaSiguiente.setEstado(EstadoRonda.EN_PROCESO);
                    break;
                }
            }
        }

        // Procesar la última ronda del losers bracket
        Ronda ultimaRondaLosers = rondas.get(numRondasWinners + numRondasLosers - 1);
        if (ultimaRondaLosers.getEstado() == EstadoRonda.EN_PROCESO) {
            boolean todosFinalizados = ultimaRondaLosers.getEnfrentamientos().stream()
                    .allMatch(e -> e.getEstado() == EstadoEnfrentamiento.FINALIZADO);

            if (todosFinalizados) {
                ultimaRondaLosers.setEstado(EstadoRonda.FINALIZADO);

                // El ganador del losers bracket va a la Grand Final
                Enfrentamiento finalLosers = ultimaRondaLosers.getEnfrentamientos().get(0);
                Ronda grandFinal = rondas.get(rondas.size() - 1);
                Enfrentamiento finalMatch = grandFinal.getEnfrentamientos().get(0);
                finalMatch.setParticipante2(finalLosers.getGanador());
            }
        }
    }
}
