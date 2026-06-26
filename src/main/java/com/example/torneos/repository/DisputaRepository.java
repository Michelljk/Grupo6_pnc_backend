package com.example.torneos.repository;
import com.example.torneos.entity.Disputa;
import com.example.torneos.entity.Resultado;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
public interface DisputaRepository extends JpaRepository<Disputa, Long> { Optional<Disputa> findByResultado(Resultado r); }