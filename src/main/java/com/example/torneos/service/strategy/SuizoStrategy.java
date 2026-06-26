package com.example.torneos.service.strategy;

import com.example.torneos.entity.Bracket;
import com.example.torneos.entity.Usuario;
import org.springframework.stereotype.Component;
import com.example.torneos.entity.Torneo;

import java.util.List;

@Component
public class SuizoStrategy implements FormatoStrategy {

    @Override
    public void validarConfiguracion(Torneo torneo) {
        int n = torneo.getMaxParticipantes();
        if (n < 6 || n % 2 != 0) {
            throw new IllegalArgumentException("El formato Suizo requiere un número par de participantes y un mínimo de 6");
        }
    }

    @Override
    public Bracket generarBracketInicial(Torneo torneo, List<Usuario> participantes) {
        return null;
    }

    @Override
    public void avanzarRonda(Bracket bracket) {

    }

}