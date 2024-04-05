package com.senai.miniprojeto02.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.senai.miniprojeto02.controllers.dto.request.DisciplinaRequest;
import com.senai.miniprojeto02.controllers.dto.response.DisciplinaResponse;
import com.senai.miniprojeto02.services.DisciplinaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/disciplinas")
public class DisciplinaController {
    private final DisciplinaService service;

    @GetMapping
    public ResponseEntity<List<DisciplinaResponse>> get() throws JsonProcessingException {
        log.info("GET /disciplinas - solicitação recebida para buscar todos os disciplinas.");
        return ResponseEntity.ok().body(service.buscarTodos());
    }

    @GetMapping("{id}")
    public ResponseEntity<DisciplinaResponse> getPorId(@PathVariable Long id) throws JsonProcessingException {
        log.info("GET /disciplinas - solicitação recebida para buscar disciplina pelo id: {}", id);
        return ResponseEntity.ok().body(service.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<DisciplinaResponse> post(@RequestBody DisciplinaRequest disciplinaRequest) throws Exception {
        log.info("POST /disciplinas - solicitação recebida para criar novo disciplina.");
        return ResponseEntity.status(HttpStatus.CREATED).body(service.criar(disciplinaRequest));
    }

    @PutMapping("{id}")
    public ResponseEntity<DisciplinaResponse> put(@PathVariable Long id, @RequestBody DisciplinaRequest disciplinaRequest) throws Exception {
        log.info("PUT /disciplinas - solicitação recebida para atualizar dados de disciplina com id: {}", id);
        return ResponseEntity.ok().body(service.atualizar(id, disciplinaRequest));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) throws JsonProcessingException {
        log.info("DELETE /disciplinas - solicitação recebida para excluir disciplina com id: {}", id);
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
