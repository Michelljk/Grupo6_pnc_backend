package com.example.torneos.entity;
import com.example.torneos.enums.EstadoEnfrentamiento;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "enfrentamientos")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Enfrentamiento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "ronda_id", nullable = false)
    private Ronda ronda;
    @ManyToOne
    @JoinColumn(name = "participante1_id")
    private Usuario participante1;
    @ManyToOne
    @JoinColumn(name = "participante2_id")
    private Usuario participante2;
    @ManyToOne
    @JoinColumn(name = "ganador_id")
    private Usuario ganador;
    private Integer scoreParticipante1;
    private Integer scoreParticipante2;
    private LocalDateTime fechaHora;
    @Enumerated(EnumType.STRING)
    private EstadoEnfrentamiento estado;
    @PrePersist
    protected void onCreate() {
        if (estado == null) estado = EstadoEnfrentamiento.PROGRAMADO;
    }
}
