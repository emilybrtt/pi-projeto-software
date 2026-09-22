package br.insper.simulado.entity;

import br.insper.simulado.dto.CursoDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "cursos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Curso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private TipoCurso tipo;

    @Column(nullable = false)
    private String descricao;

    @Column(nullable = false)
    private String professor;

    @Column(nullable = false)
    private Double valor;

    @Column(nullable = false)
    private Boolean deletado;

    public static Curso fromDto(CursoDto dto) {
        Curso curso = new Curso();

        curso.setNome(dto.getNome());
        curso.setTipo(dto.getTipo());
        curso.setDescricao(dto.getDescricao());
        curso.setProfessor(dto.getProfessor());
        curso.setValor(dto.getValor());
        curso.setDeletado(false);

        return curso;
    }

    public String getStatus() {
        return deletado ? "Deletado" : "Ativo";
    }

    public void setStatus() {
        setDeletado(false);
    }
}