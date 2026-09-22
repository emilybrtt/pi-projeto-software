package br.insper.simulado.dto;

import br.insper.simulado.entity.TipoCurso;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class CursoDto {
    private String nome;
    private TipoCurso tipo;
    private Double valor;
    private String descricao;
    private String professor;
}
