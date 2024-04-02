package com.senai.miniprojeto02.controllers;

import com.senai.miniprojeto02.controllers.dto.request.AlunoRequest;
import com.senai.miniprojeto02.controllers.dto.response.AlunoResponse;
import com.senai.miniprojeto02.services.AlunoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("alunos")
public class AlunoController {
    private final AlunoService service;

    @GetMapping
    public ResponseEntity<List<AlunoResponse>> get() {
        return ResponseEntity.ok().body(service.buscarTodos());
    }

    @GetMapping("{id}")
    public ResponseEntity<AlunoResponse> getPorId(@PathVariable Long id){
        return ResponseEntity.ok().body(service.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<AlunoResponse> post(@RequestBody AlunoRequest alunoRequest) throws Exception {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.criar(alunoRequest));
    }

    @PutMapping("{id}")
    public ResponseEntity<AlunoResponse> put(@PathVariable Long id, @RequestBody AlunoRequest alunoRequest){
        return ResponseEntity.ok().body(service.atualizar(id, alunoRequest));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        service.excluir(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Aluno deletado com sucesso.");
    }
}
