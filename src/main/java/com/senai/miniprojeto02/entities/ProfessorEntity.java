package com.senai.miniprojeto02.entities;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "professores")
public class ProfessorEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String nome;
}
