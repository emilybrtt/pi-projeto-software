package br.insper.tarefa.controller;

import br.insper.tarefa.dto.TarefaDto;
import br.insper.tarefa.entity.PrioridadeTarefa;
import br.insper.tarefa.entity.Tarefa;
import br.insper.tarefa.entity.StatusTarefa;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import tools.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
public class TarefaControllerTests {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine")
            .withDatabaseName("simulado_test")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void test_shouldCreateTarefa() throws Exception {

        TarefaDto dto = new TarefaDto();
        dto.setTitulo("Corrigir trabalho");
        dto.setStatus(StatusTarefa.DONE);
        dto.setPrioridade(PrioridadeTarefa.MEDIA);
        dto.setDescricao("Corrigir a tarefa de Software");

        // chamada
        MvcResult result = mockMvc.perform(
                        post("/api/tarefas")
                                .contentType("application/json")
                                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andReturn();

        // asserts
        Tarefa tarefa = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                Tarefa.class);
        Assertions.assertNotNull(tarefa.getId());
        Assertions.assertEquals(StatusTarefa.DONE, tarefa.getStatus());
        Assertions.assertEquals("Corrigir trabalho", tarefa.getTitulo());
        Assertions.assertFalse(tarefa.getDeletado());
    }

    @Test
    public void test_shouldListarTodosOsTarefas() throws Exception {
        // Criar um curso primeiro para garantir que a lista não vem vazia
        TarefaDto dto = new TarefaDto();
        dto.setTitulo("Estudar");
        dto.setStatus(StatusTarefa.TODO);
        dto.setPrioridade(PrioridadeTarefa.ALTA);
        dto.setDescricao("Revisar Machine Learning");

        mockMvc.perform(post("/api/tarefas")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());

        // Testar a listagem
        MvcResult result = mockMvc.perform(get("/api/tarefas"))
                .andExpect(status().isOk())
                .andReturn();

        Tarefa[] tarefas = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                Tarefa[].class);

        Assertions.assertTrue(tarefas.length > 0);
    }

    @Test
    public void test_shouldNotListarTarefasDeletados() throws Exception {
        // Criar um curso
        TarefaDto dto = new TarefaDto();
        dto.setTitulo("Revisar Física");
        dto.setStatus(StatusTarefa.DOING);
        dto.setPrioridade(PrioridadeTarefa.MEDIA);
        dto.setDescricao("Comprar um curso de Física e aprender");

        MvcResult createResult = mockMvc.perform(post("/api/tarefas")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andReturn();

        Tarefa tarefaCriado = objectMapper.readValue(
                createResult.getResponse().getContentAsString(),
                Tarefa.class);

        // Deletar o curso
        mockMvc.perform(delete("/api/tarefas/{id}", tarefaCriado.getId()))
                .andExpect(status().isNoContent());

        // Listar tarefas e verificar que o deletado não aparece
        MvcResult listResult = mockMvc.perform(get("/api/tarefas"))
                .andExpect(status().isOk())
                .andReturn();

        Tarefa[] tarefas = objectMapper.readValue(
                listResult.getResponse().getContentAsString(),
                Tarefa[].class);

        for (Tarefa c : tarefas) {
            Assertions.assertNotEquals(tarefaCriado.getId(), c.getId());
        }
    }

    @Test
    public void test_shouldFilterTarefasByNome() throws Exception {
        // Criar tarefas com nomes diferentes
        TarefaDto dto1 = new TarefaDto();
        dto1.setTitulo("Inscrição Ninja");
        dto1.setStatus(StatusTarefa.TODO);
        dto1.setPrioridade(PrioridadeTarefa.MEDIA);
        dto1.setDescricao("Mandar e-mail pros professores");

        TarefaDto dto2 = new TarefaDto();
        dto2.setTitulo("Fazer janta");
        dto2.setStatus(StatusTarefa.DONE);
        dto2.setPrioridade(PrioridadeTarefa.BAIXA);
        dto2.setDescricao("Ir no mercado e preparar algo");

        TarefaDto dto3 = new TarefaDto();
        dto3.setTitulo("Levar o peixe na natação");
        dto3.setStatus(StatusTarefa.TODO);
        dto3.setPrioridade(PrioridadeTarefa.MEDIA);
        dto3.setDescricao("Levar o peixe pra ele não se afogar");

        mockMvc.perform(post("/api/tarefas")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(dto1)))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/tarefas")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(dto2)))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/tarefas")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(dto3)))
                .andExpect(status().isCreated());
    }

    @Test
    public void test_shouldReturnTarefaQuandoObterTarefaExistente() throws Exception {
        // Preparação: Criar um curso
        TarefaDto dto = new TarefaDto();
        dto.setTitulo("Comprar ingresso");
        dto.setStatus(StatusTarefa.TODO);
        dto.setPrioridade(PrioridadeTarefa.ALTA);
        dto.setDescricao("Comprar ingresso do show do Foo Fighters");

        MvcResult postResult = mockMvc.perform(post("/api/tarefas")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andReturn();

        Tarefa tarefaCriado = objectMapper.readValue(
                postResult.getResponse().getContentAsString(),
                Tarefa.class);

        // Ação: Obter o curso pelo ID gerado
        MvcResult getResult = mockMvc.perform(get("/api/tarefas/{id}", tarefaCriado.getId()))
                .andExpect(status().isOk())
                .andReturn();

        Tarefa tarefaObtido = objectMapper.readValue(
                getResult.getResponse().getContentAsString(),
                Tarefa.class);

        // Validação
        Assertions.assertEquals(tarefaCriado.getId(), tarefaObtido.getId());
        Assertions.assertEquals("Comprar ingresso", tarefaObtido.getTitulo());
    }

    @Test
    public void test_shouldReturnNotFoundWhenObterTarefaInexistente() throws Exception {
        mockMvc.perform(get("/api/tarefas/{id}", 99999L))
                .andExpect(status().isNotFound());
    }

    @Test
    public void test_shouldReturnNotFoundWhenDeletarTarefaInexistente() throws Exception {
        mockMvc.perform(delete("/api/tarefas/{id}", 99999L))
                .andExpect(status().isNotFound());
    }

    @Test
    public void test_shouldReturnNoContentWhenDeletarTarefaExistente() throws Exception {
        TarefaDto dto = new TarefaDto();
        dto.setTitulo("Ir na Zara");
        dto.setStatus(StatusTarefa.DONE);
        dto.setPrioridade(PrioridadeTarefa.MEDIA);
        dto.setDescricao("Comprar calça nova");

        MvcResult result = mockMvc.perform(
                        post("/api/tarefas")
                                .contentType("application/json")
                                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andReturn();

        Tarefa tarefa = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                Tarefa.class);

        mockMvc.perform(delete("/api/tarefas/{id}", tarefa.getId()))
                .andExpect(status().isNoContent());

        // Verifica que o curso foi logicamente deletado, não fisicamente removido
        MvcResult getResult = mockMvc.perform(get("/api/tarefas/{id}", tarefa.getId()))
                .andExpect(status().isOk())
                .andReturn();

        Tarefa tarefaDepois = objectMapper.readValue(
                getResult.getResponse().getContentAsString(),
                Tarefa.class);

        Assertions.assertTrue(tarefaDepois.getDeletado());
    }

}