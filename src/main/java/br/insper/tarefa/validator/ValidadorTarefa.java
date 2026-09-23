package br.insper.tarefa.validator;

import org.springframework.stereotype.Component;

import br.insper.tarefa.dto.TarefaDto;
import br.insper.tarefa.exception.ValidacaoTarefaException;

@Component
public class ValidadorTarefa {

    public void validarCamposObrigatorios(TarefaDto dto) {
        if (dto.getTitulo() == null || dto.getTitulo().isBlank()) {
            throw new ValidacaoTarefaException("Título é obrigatório");
        }

        if (dto.getStatus() == null) {
            throw new ValidacaoTarefaException("Status é obrigatório");
        }

        if (dto.getDescricao() == null || dto.getDescricao().isBlank()) {
            throw new ValidacaoTarefaException("Descrição é obrigatória");
        }

        if (dto.getPrioridade() == null){
            throw new ValidacaoTarefaException("Prioridade é obrigatório");
        }
    }
}