package com.senai.miniprojeto02.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.senai.miniprojeto02.controllers.dto.request.AlunoRequest;
import com.senai.miniprojeto02.controllers.dto.response.AlunoResponse;
import com.senai.miniprojeto02.services.AlunoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/alunos")
public class AlunoController {
    private final AlunoService service;

    @GetMapping
    public ResponseEntity<List<AlunoResponse>> get() throws JsonProcessingException {
        log.info("GET /alunos - solicitação recebida para buscar todos os alunos.");
        return ResponseEntity.ok().body(service.buscarTodos());
    }

    @GetMapping("{id}")
    public ResponseEntity<AlunoResponse> getPorId(@PathVariable Long id) throws JsonProcessingException {
        log.info("GET /alunos - solicitação recebida para buscar aluno pelo id: {}", id);
        return ResponseEntity.ok().body(service.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<AlunoResponse> post(@RequestBody AlunoRequest alunoRequest) throws Exception {
        log.info("POST /alunos - solicitação recebida para criar novo aluno.");
        return ResponseEntity.status(HttpStatus.CREATED).body(service.criar(alunoRequest));
    }

    @PutMapping("{id}")
    public ResponseEntity<AlunoResponse> put(@PathVariable Long id, @RequestBody AlunoRequest alunoRequest) throws Exception {
        log.info("PUT /alunos - solicitação recebida para atualizar dados de aluno com id: {}", id);
        return ResponseEntity.ok().body(service.atualizar(id, alunoRequest));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) throws JsonProcessingException {
        log.info("DELETE /alunos - solicitação recebida para excluir aluno com id: {}", id);
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
