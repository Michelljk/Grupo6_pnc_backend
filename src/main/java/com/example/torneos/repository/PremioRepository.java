package com.example.torneos.repository;

import com.example.torneos.entity.Premio;
import com.example.torneos.enums.TipoPremio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PremioRepository extends JpaRepository<Premio, Long> {
    List<Premio> findByTipo(TipoPremio tipo);
}
