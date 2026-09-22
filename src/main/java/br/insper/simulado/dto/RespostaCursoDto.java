package br.insper.simulado.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RespostaCursoDto {
    private Boolean sucesso;
    private String mensagem;
}
