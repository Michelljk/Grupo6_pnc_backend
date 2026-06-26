package com.example.torneos.entity;
import com.example.torneos.enums.EstadoRonda;
import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "rondas")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Ronda {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "bracket_id", nullable = false)
    private Bracket bracket;
    private Integer numeroRonda;
    private String nombre;
    @Enumerated(EnumType.STRING)
    private EstadoRonda estado;
    @OneToMany(mappedBy = "ronda", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Enfrentamiento> enfrentamientos = new java.util.ArrayList<>();
    @PrePersist
    protected void onCreate() {
        if (estado == null) estado = EstadoRonda.PENDIENTE;
    }
}
