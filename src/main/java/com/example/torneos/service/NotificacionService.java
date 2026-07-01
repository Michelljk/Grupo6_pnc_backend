package com.example.torneos.service;

import com.example.torneos.entity.Enfrentamiento;
import com.example.torneos.entity.Usuario;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class NotificacionService {


    public void notificarProximoEnfrentamiento(Usuario usuario, Enfrentamiento enfrentamiento) {
        if (usuario == null || enfrentamiento == null) {
            return;
        }

        String nombreOponente = obtenerNombreOponente(usuario, enfrentamiento);
        String fechaHora = enfrentamiento.getFechaHora() != null ?
                enfrentamiento.getFechaHora().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")) :
                "Por programar";

        String mensaje = String.format(
                "Hola %s,\n\nTienes un próximo enfrentamiento contra %s el %s.\n\nBuena suerte!",
                usuario.getNombre(),
                nombreOponente,
                fechaHora
        );

        enviarNotificacion(usuario.getEmail(), "Próximo Enfrentamiento", mensaje);
    }


    public void notificarResultadoEnfrentamiento(Enfrentamiento enfrentamiento) {
        if (enfrentamiento == null || enfrentamiento.getGanador() == null) {
            return;
        }

        Usuario ganador = enfrentamiento.getGanador();
        Usuario perdedor = enfrentamiento.getParticipante1().equals(ganador) ?
                enfrentamiento.getParticipante2() :
                enfrentamiento.getParticipante1();

        String scoreInfo = String.format("%d - %d",
                enfrentamiento.getScoreParticipante1() != null ? enfrentamiento.getScoreParticipante1() : 0,
                enfrentamiento.getScoreParticipante2() != null ? enfrentamiento.getScoreParticipante2() : 0);

        // Notificar al ganador
        String mensajeGanador = String.format(
                "Felicidades %s!\n\nHas ganado tu enfrentamiento contra %s con un score de %s.",
                ganador.getNombre(),
                obtenerNombreOponente(ganador, enfrentamiento),
                scoreInfo
        );
        enviarNotificacion(ganador.getEmail(), "Resultado del Enfrentamiento", mensajeGanador);

        // Notificar al perdedor
        if (perdedor != null) {
            String mensajePerdedor = String.format(
                    "Hola %s,\n\nLamentablemente has perdido tu enfrentamiento contra %s con un score de %s.\n\nMejor suerte la próxima vez!",
                    perdedor.getNombre(),
                    obtenerNombreOponente(perdedor, enfrentamiento),
                    scoreInfo
            );
            enviarNotificacion(perdedor.getEmail(), "Resultado del Enfrentamiento", mensajePerdedor);
        }
    }


    public void notificarResolucionDisputa(Usuario usuario, String resolucion, String descripcion) {
        String mensaje = String.format(
                "Hola %s,\n\nTu disputa ha sido resuelta.\n\nResolución: %s\n\nDetalles: %s",
                usuario.getNombre(),
                resolucion,
                descripcion
        );

        enviarNotificacion(usuario.getEmail(), "Resolución de Disputa", mensaje);
    }


    public void notificarCambioProgramacion(Usuario usuario, Enfrentamiento enfrentamiento) {
        if (usuario == null || enfrentamiento == null) {
            return;
        }

        String nombreOponente = obtenerNombreOponente(usuario, enfrentamiento);
        String fechaHora = enfrentamiento.getFechaHora() != null ?
                enfrentamiento.getFechaHora().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")) :
                "Por programar";

        String mensaje = String.format(
                "Hola %s,\n\nLa programación de tu enfrentamiento contra %s ha sido actualizada.\n\nNueva fecha y hora: %s",
                usuario.getNombre(),
                nombreOponente,
                fechaHora
        );

        enviarNotificacion(usuario.getEmail(), "Cambio en Programación de Enfrentamiento", mensaje);
    }


    public void notificarInscripcionTorneo(Usuario usuario, String nombreTorneo) {
        String mensaje = String.format(
                "Hola %s,\n\nTe has inscrito exitosamente al torneo: %s\n\nBuena suerte en la competencia!",
                usuario.getNombre(),
                nombreTorneo
        );

        enviarNotificacion(usuario.getEmail(), "Inscripción a Torneo", mensaje);
    }


    public void notificarFinalizacionTorneo(Usuario usuario, String nombreTorneo, String posicion) {
        String mensaje = String.format(
                "Hola %s,\n\nEl torneo %s ha finalizado.\n\nTu posición final: %s",
                usuario.getNombre(),
                nombreTorneo,
                posicion
        );

        enviarNotificacion(usuario.getEmail(), "Torneo Finalizado", mensaje);
    }

    private String obtenerNombreOponente(Usuario usuario, Enfrentamiento enfrentamiento) {
        if (enfrentamiento.getParticipante1().equals(usuario)) {
            return enfrentamiento.getParticipante2() != null ? enfrentamiento.getParticipante2().getNombre() : "Oponente";
        } else {
            return enfrentamiento.getParticipante1() != null ? enfrentamiento.getParticipante1().getNombre() : "Oponente";
        }
    }


    private void enviarNotificacion(String destinatario, String asunto, String mensaje) {
        // Simulación de envío de notificación
        System.out.println("NOTIFICACIÓN");
        System.out.println("Para: " + destinatario);
        System.out.println("Asunto: " + asunto);
        System.out.println("Mensaje: " + mensaje);
        System.out.println("\n");


    }
}
