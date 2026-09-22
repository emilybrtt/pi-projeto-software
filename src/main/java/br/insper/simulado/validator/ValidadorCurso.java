package br.insper.simulado.validator;

import org.springframework.stereotype.Component;

import br.insper.simulado.dto.CursoDto;
import br.insper.simulado.exception.ValidacaoCursoException;

@Component
public class ValidadorCurso {

    public void validarCamposObrigatorios(CursoDto dto) {
        if (dto.getNome() == null || dto.getNome().isBlank()) {
            throw new ValidacaoCursoException("Nome do curso é obrigatório");
        }

        if (dto.getTipo() == null) {
            throw new ValidacaoCursoException("Tipo de curso é obrigatório");
        }

        // Em Java, Double é comparado diretamente com operadores matemáticos, diferente de BigDecimal
        if (dto.getValor() == null || dto.getValor() <= 0) {
            throw new ValidacaoCursoException("Valor deve ser maior que zero");
        }

        if (dto.getDescricao() == null || dto.getDescricao().isBlank()) {
            throw new ValidacaoCursoException("Descrição é obrigatória");
        }

        if (dto.getProfessor() == null || dto.getProfessor().isBlank()) {
            throw new ValidacaoCursoException("Professor é obrigatório");
        }
    }
}