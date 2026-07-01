package com.example.torneos.repository;
import com.example.torneos.entity.Disputa;
import com.example.torneos.entity.Resultado;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import com.example.torneos.enums.EstadoDisputa;
import java.util.List;

public interface DisputaRepository extends JpaRepository<Disputa, Long> {
    Optional<Disputa> findByResultado(Resultado r);
    List<Disputa> findByEstado(EstadoDisputa estado);
}