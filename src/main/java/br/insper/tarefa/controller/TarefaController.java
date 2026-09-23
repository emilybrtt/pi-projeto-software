package br.insper.tarefa.controller;

import br.insper.tarefa.dto.TarefaDto;
import br.insper.tarefa.dto.RespostaTarefaDto;
import br.insper.tarefa.entity.Tarefa;
import br.insper.tarefa.service.TarefaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@RestController
@RequestMapping("/api/tarefas")
public class TarefaController {

    @Autowired
    private TarefaService tarefaService;

    @PostMapping
    public ResponseEntity<Tarefa> criar(@RequestBody TarefaDto dto) {
        Tarefa tarefa = tarefaService.criar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(tarefa);
    }

    @GetMapping
    public ResponseEntity<List<Tarefa>> listar() {
        List<Tarefa> tarefas;
        tarefas = tarefaService.listarTodos();
        return ResponseEntity.ok(tarefas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Tarefa> obter(@PathVariable Long id) {
        Optional<Tarefa> tarefa = tarefaService.obterPorId(id);
        return tarefa.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        boolean sucesso = tarefaService.deletar(id);
        if (sucesso) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}