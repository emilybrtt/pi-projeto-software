package br.insper.simulado.processor;

import br.insper.simulado.entity.Curso;
import org.springframework.stereotype.Service;

@Service("POS_GRADUACAO")
public class ProcessadorPosGraduacao implements Processador {

    @Override
    public boolean processar(Curso curso) {
        try {
            System.out.println("Processando curso de Pós-Graduação...");
            System.out.println("Nome: " + curso.getNome());
            System.out.println("Professor: " + curso.getProfessor());
            System.out.println("Valor: " + curso.getValor());

            verificarCurso(curso);

            System.out.println("Curso de Pós-Graduação processado com sucesso!");
            return true;

        } catch (Exception e) {
            System.out.println("Erro ao processar curso: " + e.getMessage());
            return false;
        }
    }

    private void verificarCurso(Curso curso) {
        System.out.println("Verificando informações do curso...");
        System.out.println("Descrição: " + curso.getDescricao());
        System.out.println("Curso verificado!");
    }
}