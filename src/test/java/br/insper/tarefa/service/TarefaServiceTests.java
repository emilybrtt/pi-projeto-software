package br.insper.tarefa.service;

import br.insper.tarefa.dto.TarefaDto;
import br.insper.tarefa.entity.PrioridadeTarefa;
import br.insper.tarefa.entity.Tarefa;
import br.insper.tarefa.entity.StatusTarefa;
import br.insper.tarefa.repository.TarefaRepository;
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
import br.insper.tarefa.exception.ValidacaoTarefaException;

@ExtendWith(MockitoExtension.class)
public class TarefaServiceTests {

    @InjectMocks
    private TarefaService tarefaService;

    @Mock
    private TarefaRepository tarefaRepository;


    @Test
    public void test_shouldReturnTarefaWhenCallObterPorId() {

        TarefaDto dto = new TarefaDto();
        dto.setTitulo("Fazer Miojo");
        dto.setStatus(StatusTarefa.TODO);
        dto.setPrioridade(PrioridadeTarefa.BAIXA);
        dto.setDescricao("De tomate da Turma da Monica");

        Tarefa tarefa = Tarefa.fromDto(dto);

        // mocks
        Mockito.when(tarefaRepository.findById(1L))
                .thenReturn(Optional.of(tarefa));

        // chamada
        Optional<Tarefa> op = tarefaService.obterPorId(1L);

        // asserts
        Assertions.assertTrue(op.isPresent());
        Assertions.assertEquals("Fazer Miojo", op.get().getTitulo());
        Assertions.assertEquals("De tomate da Turma da Monica", op.get().getDescricao());
        Assertions.assertEquals(StatusTarefa.TODO, op.get().getStatus());
        Assertions.assertEquals(PrioridadeTarefa.BAIXA, op.get().getPrioridade());
    }


    @Test
    public void test_shouldReturnTwoTarefasWhenListarTodos() {
        List<Tarefa> tarefas = new ArrayList<>();
        tarefas.add(new Tarefa());
        tarefas.add(new Tarefa());

        // cria os mocks
        Mockito.when(tarefaRepository.findByDeletadoFalse())
                .thenReturn(tarefas);

        // chama o metodo testado
        List<Tarefa> response = tarefaService.listarTodos();

        // asserts
        Assertions.assertEquals(2, response.size());
    }


    @Test
    public void test_shouldCreateTarefaWhenPrioridadeTarefaIsMedia() {
        // mocks
        TarefaDto dto = new TarefaDto();
        dto.setTitulo("Arrumar quarto");
        dto.setPrioridade(PrioridadeTarefa.MEDIA);
        dto.setStatus(StatusTarefa.TODO);
        dto.setDescricao("Lavar roupas e passar pano no chão");

        Tarefa tarefa = Tarefa.fromDto(dto);

        Mockito.when(tarefaRepository.save(Mockito.any()))
                .thenReturn(tarefa);

        // chamada
        Tarefa response = tarefaService.criar(dto);

        // asserts
        Assertions.assertEquals(StatusTarefa.TODO, response.getStatus());
        Assertions.assertEquals(PrioridadeTarefa.MEDIA, response.getPrioridade());
        Assertions.assertEquals("Arrumar quarto", response.getTitulo());
        Assertions.assertEquals("Lavar roupas e passar pano no chão", response.getDescricao());
    }


    @Test
    public void test_shouldReturnTrueWhenDeletarExistingTarefa() {
        // mocks
        TarefaDto dto = new TarefaDto();
        dto.setTitulo("Estudar LingPar");
        dto.setDescricao("Fazer roteiro 5 e resumos");
        dto.setPrioridade(PrioridadeTarefa.ALTA);
        dto.setStatus(StatusTarefa.DONE);

        Tarefa tarefa = Tarefa.fromDto(dto);

        Mockito.when(tarefaRepository.findById(1L))
                .thenReturn(Optional.of(tarefa));
        Mockito.when(tarefaRepository.save(Mockito.any()))
                .thenReturn(tarefa);

        // chamada
        boolean response = tarefaService.deletar(1L);

        // asserts
        Assertions.assertTrue(response);
        Mockito.verify(tarefaRepository, Mockito.times(1)).save(Mockito.any());
        Assertions.assertTrue(tarefa.getDeletado());
    }


    @Test
    public void test_shouldReturnFalseWhenDeletarNonExistingTarefa() {
        // mocks
        Mockito.when(tarefaRepository.findById(1L))
                .thenReturn(Optional.empty());

        // chamada
        boolean response = tarefaService.deletar(1L);

        // asserts
        Assertions.assertFalse(response);
        Mockito.verify(tarefaRepository, Mockito.times(0)).save(Mockito.any());
    }
}