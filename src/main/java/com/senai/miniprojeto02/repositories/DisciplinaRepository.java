package com.senai.miniprojeto02.repositories;

import com.senai.miniprojeto02.entities.DisciplinaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DisciplinaRepository extends JpaRepository<DisciplinaEntity, Long> {
}
