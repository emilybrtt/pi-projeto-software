package br.insper.simulado.controller;

import br.insper.simulado.dto.CursoDto;
import br.insper.simulado.dto.RespostaCursoDto;
import br.insper.simulado.entity.Curso;
import br.insper.simulado.service.CursoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/cursos")
public class CursoController {

    @Autowired
    private CursoService cursoService;

    @PostMapping
    public ResponseEntity<Curso> criar(@RequestBody CursoDto dto) {
        Curso curso = cursoService.criar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(curso);
    }

    @GetMapping
    public ResponseEntity<List<Curso>> listar(@RequestParam(required = false) String nome) {
        List<Curso> cursos;
        if (nome != null && !nome.isEmpty()) {
            cursos = cursoService.listarPorNome(nome);
        } else {
            cursos = cursoService.listarTodos();
        }
        return ResponseEntity.ok(cursos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Curso> obter(@PathVariable Long id) {
        Optional<Curso> curso = cursoService.obterPorId(id);
        return curso.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        boolean sucesso = cursoService.deletar(id);
        if (sucesso) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/{id}/processar")
    public ResponseEntity<RespostaCursoDto> processar(@PathVariable Long id) {
        boolean sucesso = cursoService.processar(id);
        RespostaCursoDto resposta = new RespostaCursoDto(
                sucesso,
                sucesso ? "Curso processado com sucesso" : "Erro ao processar o curso"
        );
        return ResponseEntity.ok(resposta);
    }

}