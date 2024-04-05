package com.senai.miniprojeto02.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.senai.miniprojeto02.controllers.dto.request.MatriculaRequest;
import com.senai.miniprojeto02.controllers.dto.response.MatriculaResponse;
import com.senai.miniprojeto02.controllers.dto.response.MediaGeralResponse;
import com.senai.miniprojeto02.services.MatriculaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/matriculas")
public class MatriculaController {
    private final MatriculaService service;

    @GetMapping()
    public ResponseEntity<List<MatriculaResponse>> get() throws JsonProcessingException {
        log.info("GET /matriculas - solicitação recebida para buscar todas as matrículas.");
        return ResponseEntity.ok().body(service.buscarTodos());
    }

    @GetMapping("{id}")
    public ResponseEntity<MatriculaResponse> getPorId(@PathVariable Long id) throws JsonProcessingException {
        log.info("GET /matriculas - solicitação recebida para buscar matricula pelo id: {}", id);
        return ResponseEntity.ok().body(service.buscarPorId(id));
    }

    @GetMapping("/aluno/{alunoId}")
    public ResponseEntity<List<MatriculaResponse>> getPorAlunoId(
            @PathVariable Long alunoId
    ) throws JsonProcessingException {
        log.info("GET /matriculas/aluno - solicitação recebida para buscar matriculas do aluno id: {}", alunoId);
        return ResponseEntity.ok().body(service.buscarPorAlunoId(alunoId));
    }

    @GetMapping("/aluno/{alunoId}/media-geral")
    public ResponseEntity<List<MediaGeralResponse>> getMediaPorAlunoId(
            @PathVariable Long alunoId
    ) throws Exception {
        log.info(
                "GET /matriculas/aluno/media-geral - solicitação recebida para buscar media geral do aluno id: {}",
                alunoId
        );
        return ResponseEntity.ok().body(service.buscarMediaGeralPorAlunoId(alunoId));
    }

    @GetMapping("/disciplina/{disciplinaId}")
    public ResponseEntity<List<MatriculaResponse>> getPorDisciplinaId(
            @PathVariable Long disciplinaId
    ) throws JsonProcessingException {
        log.info(
                "GET /matriculas/disciplina - solicitação recebida para buscar matriculas da disciplina id: {}",
                disciplinaId
        );
        return ResponseEntity.ok().body(service.buscarPorDisciplinaId(disciplinaId));
    }

    @PostMapping
    public ResponseEntity<MatriculaResponse> post(@RequestBody MatriculaRequest matriculaRequest) throws Exception {
        log.info("POST /matriculas - solicitação recebida para criar nova matrícula.");
        return ResponseEntity.status(HttpStatus.CREATED).body(service.criar(matriculaRequest));
    }

    @PutMapping("{id}")
    public ResponseEntity<MatriculaResponse> put(
            @PathVariable Long id,
            @RequestBody MatriculaRequest matriculaRequest
    ) throws Exception {
        log.info("PUT /matriculas - solicitação recebida para atualizar dados da matrícula com id: {}", id);
        return ResponseEntity.ok().body(service.atualizar(id, matriculaRequest));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) throws Exception {
        log.info("DELETE /matriculas - solicitação recebida para excluir matrícula com id: {}", id);
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
