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
public class SuizoStrategy implements FormatoStrategy {

    @Override
    public void validarConfiguracion(Torneo torneo) {
        int n = torneo.getMaxParticipantes();
        if (n < 4 || n % 2 != 0) {
            throw new IllegalArgumentException("El formato Suizo requiere un número par de participantes y un mínimo de 4");
        }
    }

    @Override
    public Bracket generarBracketInicial(Torneo torneo, List<Usuario> participantes) {
        Bracket bracket = Bracket.builder()
                .torneo(torneo)
                .estado(EstadoBracket.EN_PROCESO)
                .rondas(new ArrayList<>())
                .build();

        List<Usuario> shuffled = new ArrayList<>(participantes);
        Collections.shuffle(shuffled);

        Ronda r1 = Ronda.builder()
                .bracket(bracket)
                .numeroRonda(1)
                .nombre("Ronda Suiza 1")
                .estado(EstadoRonda.EN_PROCESO)
                .enfrentamientos(new ArrayList<>())
                .build();

        for (int i = 0; i < shuffled.size(); i += 2) {
            Enfrentamiento e = Enfrentamiento.builder()
                    .ronda(r1)
                    .participante1(shuffled.get(i))
                    .participante2(shuffled.get(i + 1))
                    .estado(EstadoEnfrentamiento.PROGRAMADO)
                    .build();
            r1.getEnfrentamientos().add(e);
        }

        bracket.getRondas().add(r1);
        return bracket;
    }

    @Override
    public void avanzarRonda(Bracket bracket) {
        List<Ronda> rondas = bracket.getRondas();
        Ronda ultima = rondas.get(rondas.size() - 1);
        if (ultima.getEnfrentamientos().stream().allMatch(e -> e.getEstado() == EstadoEnfrentamiento.FINALIZADO)) {
            ultima.setEstado(EstadoRonda.FINALIZADO);
            // Simulación: Finaliza el torneo después de la primera ronda suiza
            bracket.setEstado(EstadoBracket.FINALIZADO);
            bracket.getTorneo().setEstado(com.example.torneos.enums.EstadoTorneo.FINALIZADO);
        }
    }
}
