package com.senai.miniprojeto02.repositories;

import com.senai.miniprojeto02.entities.ProfessorEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfessorRepository extends JpaRepository<ProfessorEntity, Long> {
}
