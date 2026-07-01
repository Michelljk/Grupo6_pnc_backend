package com.example.torneos.service.strategy;
import com.example.torneos.entity.Bracket;
import com.example.torneos.entity.Usuario;
import com.example.torneos.entity.Torneo;
import java.util.List;
public interface FormatoStrategy {
    void validarConfiguracion(Torneo torneo);
    Bracket generarBracketInicial(Torneo torneo, List<Usuario> participantes);
    void avanzarRonda(Bracket bracket);
}