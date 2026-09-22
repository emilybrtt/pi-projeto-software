package br.insper.simulado.processor;

import br.insper.simulado.entity.TipoCurso;
import br.insper.simulado.exception.ValidacaoCursoException;

public class CursoFactory {
    public Processador getProcessador(TipoCurso tipoCurso) {
        if (tipoCurso == TipoCurso.GRADUACAO) {
            return new ProcessadorGraduacao();
        } else if (tipoCurso == TipoCurso.POS_GRADUACAO) {
            return new ProcessadorPosGraduacao();
        } else if (tipoCurso == TipoCurso.MBA) {
            return new ProcessadorMba();
        } else {
            throw new ValidacaoCursoException("Tipo de curso desconhecido: " + tipoCurso);
        }
    }


}
