package com.example.torneos.service.strategy;
import com.example.torneos.entity.*;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class LigaStrategy implements FormatoStrategy {
    @Override
    public void validarConfiguracion(Torneo torneo) {
        // TODO: Implementar validación específica
    }

    @Override
    public Bracket generarBracketInicial(Torneo torneo, List<Usuario> participantes) {
        // TODO: Implementar generación inicial
        return null;
    }

    @Override
    public void avanzarRonda(Bracket bracket) {
        // TODO: Implementar lógica de avance
    }
}
