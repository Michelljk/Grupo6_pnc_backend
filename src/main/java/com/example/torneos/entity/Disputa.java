package com.example.torneos.entity;
import com.example.torneos.enums.EstadoDisputa;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "disputas")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Disputa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne
    @JoinColumn(name = "resultado_id", nullable = false)
    private Resultado resultado;
    @ManyToOne
    @JoinColumn(name = "usuario_reporta_id", nullable = false)
    private Usuario usuarioReporta;
    private String descripcion;
    private LocalDateTime fechaReporte;
    @Enumerated(EnumType.STRING)
    private EstadoDisputa estado;
    private String resolucion;
    @ManyToOne
    @JoinColumn(name = "administrador_resuelve_id")
    private Usuario administradorResuelve;
    @PrePersist
    protected void onCreate() {
        fechaReporte = LocalDateTime.now();
    }
}
