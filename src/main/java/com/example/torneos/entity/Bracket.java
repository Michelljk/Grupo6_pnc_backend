package com.example.torneos.entity;
import com.example.torneos.entity.Torneo;
import com.example.torneos.enums.EstadoBracket;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "brackets")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Bracket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "torneo_id", nullable = false)
    private Torneo torneo;
    @Enumerated(EnumType.STRING)
    private EstadoBracket estado;
    private LocalDateTime fechaCreacion;
    @OneToMany(mappedBy = "bracket", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Ronda> rondas=new java.util.ArrayList<>();
    @PrePersist
    protected void onCreate() {
        fechaCreacion = LocalDateTime.now();
        if (estado == null) estado = EstadoBracket.PENDIENTE;
    }
}