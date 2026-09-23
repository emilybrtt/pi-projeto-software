package br.insper.tarefa.entity;

import br.insper.tarefa.dto.TarefaDto;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "tarefas")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class Tarefa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titulo;

    @Column(nullable = false)
    private String descricao;

    @Column(nullable = false)
    private StatusTarefa status;

    @Column(nullable = false)
    private PrioridadeTarefa prioridade;

    @CreationTimestamp
    private LocalDateTime dataCriacao;

    @UpdateTimestamp
    private LocalDateTime dataDelecao;

    @Column(nullable = false)
    private Boolean deletado;

    public static Tarefa fromDto(TarefaDto dto) {
        Tarefa t = new Tarefa();

        t.setTitulo(dto.getTitulo());
        t.setDescricao(dto.getDescricao());
        t.setStatus(dto.getStatus());
        t.setPrioridade(dto.getPrioridade());
        t.setDeletado(false);

        return t;
    }

}