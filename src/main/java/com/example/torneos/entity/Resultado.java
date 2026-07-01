package com.example.torneos.entity;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "resultados")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Resultado {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne
    @JoinColumn(name = "enfrentamiento_id", nullable = false)
    private Enfrentamiento enfrentamiento;
    @ManyToOne
    @JoinColumn(name = "ganador_id", nullable = false)
    private Usuario ganador;
    private Integer scoreParticipante1;
    private Integer scoreParticipante2;
    private LocalDateTime fechaRegistro;
    private String evidenciaUrl;
    private Boolean validadoPorAdmin;
    @PrePersist
    protected void onCreate() {
        fechaRegistro = LocalDateTime.now();
        if (validadoPorAdmin == null) validadoPorAdmin = false;
    }
}
