package br.insper.tarefa.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "auditoria")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class RegistroAuditoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private TipoOperacao operacao;

    @Column(name = "data", nullable = false, updatable = false)
    private LocalDateTime data;

    @Column(name="id_tarefa", nullable = false)
    private Long idTarefa;

}
