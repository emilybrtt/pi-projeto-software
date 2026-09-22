package br.insper.simulado.controller;

import br.insper.simulado.dto.CursoDto;
import br.insper.simulado.entity.Curso;
import br.insper.simulado.entity.TipoCurso;
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
public class CursoControllerTests {

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
    public void test_shouldCreateCurso() throws Exception {

        CursoDto dto = new CursoDto();
        dto.setNome("Engenharia de Software");
        dto.setTipo(TipoCurso.GRADUACAO);
        dto.setValor(1000.0);
        dto.setDescricao("Curso de Graduação em Engenharia de Software");
        dto.setProfessor("Fulano de Tal");

        // chamada
        MvcResult result = mockMvc.perform(
                        post("/api/cursos")
                                .contentType("application/json")
                                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andReturn();

        // asserts
        Curso curso = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                Curso.class);
        Assertions.assertNotNull(curso.getId());
        Assertions.assertEquals(TipoCurso.GRADUACAO, curso.getTipo());
        Assertions.assertEquals("Engenharia de Software", curso.getNome());
        Assertions.assertFalse(curso.getDeletado());
    }

    @Test
    public void test_shouldListarTodosOsCursos() throws Exception {
        // Criar um curso primeiro para garantir que a lista não vem vazia
        CursoDto dto = new CursoDto();
        dto.setNome("Medicina");
        dto.setTipo(TipoCurso.GRADUACAO);
        dto.setValor(5000.0);
        dto.setDescricao("Curso de graduação em Medicina");
        dto.setProfessor("Dra. Silvia");

        mockMvc.perform(post("/api/cursos")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());

        // Testar a listagem
        MvcResult result = mockMvc.perform(get("/api/cursos"))
                .andExpect(status().isOk())
                .andReturn();

        Curso[] cursos = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                Curso[].class);

        Assertions.assertTrue(cursos.length > 0);
    }

    @Test
    public void test_shouldReturnCursoQuandoObterCursoExistente() throws Exception {
        // Preparação: Criar um curso
        CursoDto dto = new CursoDto();
        dto.setNome("Arquitetura");
        dto.setTipo(TipoCurso.GRADUACAO);
        dto.setValor(1200.0);
        dto.setDescricao("Curso de Arquitetura");
        dto.setProfessor("Maria");

        MvcResult postResult = mockMvc.perform(post("/api/cursos")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andReturn();

        Curso cursoCriado = objectMapper.readValue(
                postResult.getResponse().getContentAsString(),
                Curso.class);

        // Ação: Obter o curso pelo ID gerado
        MvcResult getResult = mockMvc.perform(get("/api/cursos/{id}", cursoCriado.getId()))
                .andExpect(status().isOk())
                .andReturn();

        Curso cursoObtido = objectMapper.readValue(
                getResult.getResponse().getContentAsString(),
                Curso.class);

        // Validação
        Assertions.assertEquals(cursoCriado.getId(), cursoObtido.getId());
        Assertions.assertEquals("Arquitetura", cursoObtido.getNome());
    }

    @Test
    public void test_shouldReturnNotFoundWhenObterCursoInexistente() throws Exception {
        mockMvc.perform(get("/api/cursos/{id}", 99999L))
                .andExpect(status().isNotFound());
    }

    @Test
    public void test_shouldReturnNotFoundWhenDeletarCursoInexistente() throws Exception {
        mockMvc.perform(delete("/api/cursos/{id}", 99999L))
                .andExpect(status().isNotFound());
    }

    @Test
    public void test_shouldReturnNoContentWhenDeletarCursoExistente() throws Exception {
        CursoDto dto = new CursoDto();
        dto.setNome("Ciência da Computação");
        dto.setTipo(TipoCurso.MBA);
        dto.setValor(2000.0);
        dto.setDescricao("MBA em Ciência da Computação");
        dto.setProfessor("Ciclano de Tal");

        MvcResult result = mockMvc.perform(
                        post("/api/cursos")
                                .contentType("application/json")
                                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andReturn();

        Curso curso = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                Curso.class);

        mockMvc.perform(delete("/api/cursos/{id}", curso.getId()))
                .andExpect(status().isNoContent());
    }

    @Test
    public void test_shouldProcessarCursoExistente() throws Exception {
        // Preparação
        CursoDto dto = new CursoDto();
        dto.setNome("Direito");
        dto.setTipo(TipoCurso.GRADUACAO);
        dto.setValor(2000.0);
        dto.setDescricao("Curso de Direito");
        dto.setProfessor("Pedro");

        MvcResult postResult = mockMvc.perform(post("/api/cursos")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andReturn();

        Curso cursoCriado = objectMapper.readValue(
                postResult.getResponse().getContentAsString(),
                Curso.class);

        // Ação e Validação do endpoint de processamento
        mockMvc.perform(post("/api/cursos/{id}/processar", cursoCriado.getId()))
                .andExpect(status().isOk());
    }
}