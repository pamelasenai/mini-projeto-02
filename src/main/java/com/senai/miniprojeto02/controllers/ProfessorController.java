package com.senai.miniprojeto02.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.senai.miniprojeto02.controllers.dto.request.ProfessorRequest;
import com.senai.miniprojeto02.controllers.dto.response.ProfessorResponse;
import com.senai.miniprojeto02.services.ProfessorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/professores")
public class ProfessorController {
    private final ProfessorService service;

    @GetMapping
    public ResponseEntity<List<ProfessorResponse>> get() throws JsonProcessingException {
        log.info("GET /professores - solicitação recebida para buscar todos os professores.");
        return ResponseEntity.ok().body(service.buscarTodos());
    }

    @GetMapping("{id}")
    public ResponseEntity<ProfessorResponse> getPorId(@PathVariable Long id) throws JsonProcessingException {
        log.info("GET /professores - solicitação recebida para buscar professor pelo id: {}", id);
        return ResponseEntity.ok().body(service.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<ProfessorResponse> post(@RequestBody ProfessorRequest professorRequest) throws Exception {
        log.info("POST /professores - solicitação recebida para criar novo professor.");
        return ResponseEntity.status(HttpStatus.CREATED).body(service.criar(professorRequest));
    }

    @PutMapping("{id}")
    public ResponseEntity<ProfessorResponse> put(@PathVariable Long id, @RequestBody ProfessorRequest professorRequest) throws Exception {
        log.info("PUT /professores - solicitação recebida para atualizar dados de professor com id: {}", id);
        return ResponseEntity.ok().body(service.atualizar(id, professorRequest));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) throws JsonProcessingException {
        log.info("DELETE /professores - solicitação recebida para excluir professor com id: {}", id);
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
