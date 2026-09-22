package br.insper.simulado;

import br.insper.simulado.dto.CursoDto;
import br.insper.simulado.entity.Curso;
import br.insper.simulado.entity.TipoCurso;
import br.insper.simulado.processor.Processador;
import br.insper.simulado.repository.CursoRepository;
import br.insper.simulado.service.CursoService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import br.insper.simulado.exception.ValidacaoCursoException;

@ExtendWith(MockitoExtension.class)
public class CursoServiceTests {

    @InjectMocks
    private CursoService cursoService;

    @Mock
    private CursoRepository cursoRepository;

    @Mock
    private Map<String, Processador> processadores;

    @Mock
    private Processador processador;


    @Test
    public void test_shouldReturnCursoWhenCallObterPorId() {

        CursoDto dto = new CursoDto();
        dto.setNome("Engenharia de Software");
        dto.setTipo(TipoCurso.GRADUACAO);
        dto.setValor(1000.0);
        dto.setDescricao("Curso de graduação em Engenharia de Software");
        dto.setProfessor("Fulano de Tal");

        Curso curso = Curso.fromDto(dto);

        // mocks
        Mockito.when(cursoRepository.findById(1L))
                .thenReturn(Optional.of(curso));

        // chamada
        Optional<Curso> op = cursoService.obterPorId(1L);

        // asserts
        Assertions.assertTrue(op.isPresent());
        Assertions.assertEquals("Engenharia de Software", op.get().getNome());
        Assertions.assertEquals("Fulano de Tal", op.get().getProfessor());
        Assertions.assertEquals(TipoCurso.GRADUACAO, op.get().getTipo());
    }

    @Test
    public void test_shouldReturnSuccessWhenCallProcessador() {

        CursoDto dto = new CursoDto();
        dto.setNome("Engenharia de Software");
        dto.setTipo(TipoCurso.GRADUACAO);
        dto.setValor(1000.0);
        dto.setDescricao("Curso de graduação em Engenharia de Software");
        dto.setProfessor("Fulano de Tal");

        Curso curso = Curso.fromDto(dto);

        // mocks
        Mockito.when(cursoRepository.findById(1L))
                .thenReturn(Optional.of(curso));
        Mockito.when(processadores.get(Mockito.any()))
                .thenReturn(processador);
        Mockito.when(processador.processar(Mockito.any()))
                .thenReturn(true);
        Mockito.when(cursoRepository.save(Mockito.any()))
                .thenReturn(curso);

        // chamada
        boolean response = cursoService.processar(1L);

        // asserts
        Assertions.assertTrue(response);
    }


    @Test
    public void test_shouldReturnFalseWhenCallProcessador() {

        CursoDto dto = new CursoDto();
        dto.setNome("Engenharia de Software");
        dto.setTipo(TipoCurso.GRADUACAO);
        dto.setValor(1000.0);
        dto.setDescricao("Curso de graduação em Engenharia de Software");
        dto.setProfessor("Fulano de Tal");

        Curso curso = Curso.fromDto(dto);

        // mocks
        Mockito.when(cursoRepository.findById(1L))
                .thenReturn(Optional.of(curso));
        Mockito.when(processadores.get(Mockito.any()))
                .thenReturn(processador);
        Mockito.when(processador.processar(Mockito.any()))
                .thenReturn(false);
        Mockito.when(cursoRepository.save(Mockito.any()))
                .thenReturn(curso);

        // chamada
        boolean response = cursoService.processar(1L);

        // asserts
        Assertions.assertFalse(response);
    }


    @Test
    public void test_shouldReturnTwoCoursesWhenListarTodos() {
        List<Curso> cursos = new ArrayList<>();
        cursos.add(new Curso());
        cursos.add(new Curso());

        // cria os mocks
        Mockito.when(cursoRepository.findAll())
                .thenReturn(cursos);

        // chama o metodo testado
        List<Curso> response = cursoService.listarTodos();

        // asserts
        Assertions.assertEquals(2, response.size());
    }


    @Test
    public void test_shouldCreateCursoWhenTipoCursoIsGraduacao() {
        // mocks
        CursoDto dto = new CursoDto();
        dto.setNome("Engenharia de Software");
        dto.setTipo(TipoCurso.GRADUACAO);
        dto.setValor(1000.0);
        dto.setDescricao("Curso de graduação em Engenharia de Software");
        dto.setProfessor("Fulano de Tal");

        Curso curso = Curso.fromDto(dto);

        Mockito.when(cursoRepository.save(Mockito.any()))
                .thenReturn(curso);

        // chamada
        Curso response = cursoService.criar(dto);

        // asserts
        Assertions.assertEquals(TipoCurso.GRADUACAO, response.getTipo());
        Assertions.assertEquals("Engenharia de Software", response.getNome());
        Assertions.assertEquals("Fulano de Tal", response.getProfessor());
    }


    @Test
    public void test_shouldReturnTrueWhenDeletarExistingCurso() {
        // mocks
        Mockito.when(cursoRepository.existsById(1L))
                .thenReturn(true);

        // chamada
        boolean response = cursoService.deletar(1L);

        // asserts
        Assertions.assertTrue(response);
        Mockito.verify(cursoRepository, Mockito.times(1)).deleteById(1L);
    }


    @Test
    public void test_shouldReturnFalseWhenDeletarNonExistingCurso() {
        // mocks
        Mockito.when(cursoRepository.existsById(1L))
                .thenReturn(false);

        // chamada
        boolean response = cursoService.deletar(1L);

        // asserts
        Assertions.assertFalse(response);
        Mockito.verify(cursoRepository, Mockito.times(0)).deleteById(Mockito.any());
    }

    @Test
    public void test_shouldThrowExceptionWhenProcessarCursoDoesNotExist() {
        // mock
        Mockito.when(cursoRepository.findById(99L))
                .thenReturn(Optional.empty());

        // chamada e assert
        ValidacaoCursoException exception = Assertions.assertThrows(
                ValidacaoCursoException.class,
                () -> cursoService.processar(99L)
        );

        Assertions.assertEquals(
                "Curso com ID 99 não encontrado",
                exception.getMessage()
        );

        Mockito.verify(cursoRepository, Mockito.never())
                .save(Mockito.any());

        Mockito.verifyNoInteractions(processador);
    }

}