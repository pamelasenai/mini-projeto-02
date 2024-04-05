package com.senai.miniprojeto02.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.senai.miniprojeto02.controllers.dto.request.NotaRequest;
import com.senai.miniprojeto02.controllers.dto.response.NotaResponse;
import com.senai.miniprojeto02.services.NotaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/notas")
public class NotaController {
    private final NotaService service;

    @GetMapping()
    public ResponseEntity<List<NotaResponse>> get() throws JsonProcessingException {
        log.info("GET /notas - solicitação recebida para buscar todas as notas.");
        return ResponseEntity.ok().body(service.buscarTodos());
    }

    @GetMapping("{id}")
    public ResponseEntity<NotaResponse> getPorId(@PathVariable Long id) throws JsonProcessingException {
        log.info("GET /notas - solicitação recebida para buscar nota pelo id: {}", id);
        return ResponseEntity.ok().body(service.buscarPorId(id));
    }

    @GetMapping("/matricula/{matriculaId}")
    public ResponseEntity<List<NotaResponse>> getPorDisciplinaId(
            @PathVariable Long matriculaId
    ) throws JsonProcessingException {
        log.info(
                "GET /notas/matricula - solicitação recebida para buscar notas da matrícula id: {}",
                matriculaId
        );
        return ResponseEntity.ok().body(service.buscarPorMatriculaId(matriculaId));
    }

    @PostMapping
    public ResponseEntity<NotaResponse> post(@RequestBody NotaRequest notaRequest) throws Exception {
        log.info("POST /notas - solicitação recebida para criar nova nota.");
        return ResponseEntity.status(HttpStatus.CREATED).body(service.criar(notaRequest));
    }

    @PutMapping("{id}")
    public ResponseEntity<NotaResponse> put(
            @PathVariable Long id,
            @RequestBody NotaRequest notaRequest
    ) throws Exception {
        log.info("PUT /notas - solicitação recebida para atualizar dados da nota com id: {}", id);
        return ResponseEntity.ok().body(service.atualizar(id, notaRequest));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) throws Exception {
        log.info("DELETE /notas - solicitação recebida para excluir nota com id: {}", id);
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
