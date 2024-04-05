package com.senai.miniprojeto02.repositories;

import com.senai.miniprojeto02.entities.NotaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotaRepository extends JpaRepository<NotaEntity, Long> {
    List<NotaEntity> findByDisciplinaId(Long disciplinaId);
}
