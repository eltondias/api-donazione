package br.com.donazione.api.web.rest;

import br.com.donazione.api.ApidonazioneApp;

import br.com.donazione.api.domain.RecursoNecessario;
import br.com.donazione.api.domain.Campanha;
import br.com.donazione.api.repository.RecursoNecessarioRepository;
import br.com.donazione.api.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.List;


import static br.com.donazione.api.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the RecursoNecessarioResource REST controller.
 *
 * @see RecursoNecessarioResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ApidonazioneApp.class)
public class RecursoNecessarioResourceIntTest {

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRICAO = "AAAAAAAAAA";
    private static final String UPDATED_DESCRICAO = "BBBBBBBBBB";

    private static final Integer DEFAULT_QUANTIDADE = 1;
    private static final Integer UPDATED_QUANTIDADE = 2;

    private static final Boolean DEFAULT_IS_FINANCEIRO = false;
    private static final Boolean UPDATED_IS_FINANCEIRO = true;

    private static final Double DEFAULT_VALOR = 1D;
    private static final Double UPDATED_VALOR = 2D;

    @Autowired
    private RecursoNecessarioRepository recursoNecessarioRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restRecursoNecessarioMockMvc;

    private RecursoNecessario recursoNecessario;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final RecursoNecessarioResource recursoNecessarioResource = new RecursoNecessarioResource(recursoNecessarioRepository);
        this.restRecursoNecessarioMockMvc = MockMvcBuilders.standaloneSetup(recursoNecessarioResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RecursoNecessario createEntity(EntityManager em) {
        RecursoNecessario recursoNecessario = new RecursoNecessario()
            .nome(DEFAULT_NOME)
            .descricao(DEFAULT_DESCRICAO)
            .quantidade(DEFAULT_QUANTIDADE)
            .isFinanceiro(DEFAULT_IS_FINANCEIRO)
            .valor(DEFAULT_VALOR);
        // Add required entity
        Campanha campanha = CampanhaResourceIntTest.createEntity(em);
        em.persist(campanha);
        em.flush();
        recursoNecessario.setCampanha(campanha);
        return recursoNecessario;
    }

    @Before
    public void initTest() {
        recursoNecessario = createEntity(em);
    }

    @Test
    @Transactional
    public void createRecursoNecessario() throws Exception {
        int databaseSizeBeforeCreate = recursoNecessarioRepository.findAll().size();

        // Create the RecursoNecessario
        restRecursoNecessarioMockMvc.perform(post("/api/recurso-necessarios")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(recursoNecessario)))
            .andExpect(status().isCreated());

        // Validate the RecursoNecessario in the database
        List<RecursoNecessario> recursoNecessarioList = recursoNecessarioRepository.findAll();
        assertThat(recursoNecessarioList).hasSize(databaseSizeBeforeCreate + 1);
        RecursoNecessario testRecursoNecessario = recursoNecessarioList.get(recursoNecessarioList.size() - 1);
        assertThat(testRecursoNecessario.getNome()).isEqualTo(DEFAULT_NOME);
        assertThat(testRecursoNecessario.getDescricao()).isEqualTo(DEFAULT_DESCRICAO);
        assertThat(testRecursoNecessario.getQuantidade()).isEqualTo(DEFAULT_QUANTIDADE);
        assertThat(testRecursoNecessario.isIsFinanceiro()).isEqualTo(DEFAULT_IS_FINANCEIRO);
        assertThat(testRecursoNecessario.getValor()).isEqualTo(DEFAULT_VALOR);
    }

    @Test
    @Transactional
    public void createRecursoNecessarioWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = recursoNecessarioRepository.findAll().size();

        // Create the RecursoNecessario with an existing ID
        recursoNecessario.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restRecursoNecessarioMockMvc.perform(post("/api/recurso-necessarios")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(recursoNecessario)))
            .andExpect(status().isBadRequest());

        // Validate the RecursoNecessario in the database
        List<RecursoNecessario> recursoNecessarioList = recursoNecessarioRepository.findAll();
        assertThat(recursoNecessarioList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNomeIsRequired() throws Exception {
        int databaseSizeBeforeTest = recursoNecessarioRepository.findAll().size();
        // set the field null
        recursoNecessario.setNome(null);

        // Create the RecursoNecessario, which fails.

        restRecursoNecessarioMockMvc.perform(post("/api/recurso-necessarios")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(recursoNecessario)))
            .andExpect(status().isBadRequest());

        List<RecursoNecessario> recursoNecessarioList = recursoNecessarioRepository.findAll();
        assertThat(recursoNecessarioList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDescricaoIsRequired() throws Exception {
        int databaseSizeBeforeTest = recursoNecessarioRepository.findAll().size();
        // set the field null
        recursoNecessario.setDescricao(null);

        // Create the RecursoNecessario, which fails.

        restRecursoNecessarioMockMvc.perform(post("/api/recurso-necessarios")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(recursoNecessario)))
            .andExpect(status().isBadRequest());

        List<RecursoNecessario> recursoNecessarioList = recursoNecessarioRepository.findAll();
        assertThat(recursoNecessarioList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkQuantidadeIsRequired() throws Exception {
        int databaseSizeBeforeTest = recursoNecessarioRepository.findAll().size();
        // set the field null
        recursoNecessario.setQuantidade(null);

        // Create the RecursoNecessario, which fails.

        restRecursoNecessarioMockMvc.perform(post("/api/recurso-necessarios")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(recursoNecessario)))
            .andExpect(status().isBadRequest());

        List<RecursoNecessario> recursoNecessarioList = recursoNecessarioRepository.findAll();
        assertThat(recursoNecessarioList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkIsFinanceiroIsRequired() throws Exception {
        int databaseSizeBeforeTest = recursoNecessarioRepository.findAll().size();
        // set the field null
        recursoNecessario.setIsFinanceiro(null);

        // Create the RecursoNecessario, which fails.

        restRecursoNecessarioMockMvc.perform(post("/api/recurso-necessarios")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(recursoNecessario)))
            .andExpect(status().isBadRequest());

        List<RecursoNecessario> recursoNecessarioList = recursoNecessarioRepository.findAll();
        assertThat(recursoNecessarioList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkValorIsRequired() throws Exception {
        int databaseSizeBeforeTest = recursoNecessarioRepository.findAll().size();
        // set the field null
        recursoNecessario.setValor(null);

        // Create the RecursoNecessario, which fails.

        restRecursoNecessarioMockMvc.perform(post("/api/recurso-necessarios")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(recursoNecessario)))
            .andExpect(status().isBadRequest());

        List<RecursoNecessario> recursoNecessarioList = recursoNecessarioRepository.findAll();
        assertThat(recursoNecessarioList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllRecursoNecessarios() throws Exception {
        // Initialize the database
        recursoNecessarioRepository.saveAndFlush(recursoNecessario);

        // Get all the recursoNecessarioList
        restRecursoNecessarioMockMvc.perform(get("/api/recurso-necessarios?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(recursoNecessario.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME.toString())))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO.toString())))
            .andExpect(jsonPath("$.[*].quantidade").value(hasItem(DEFAULT_QUANTIDADE)))
            .andExpect(jsonPath("$.[*].isFinanceiro").value(hasItem(DEFAULT_IS_FINANCEIRO.booleanValue())))
            .andExpect(jsonPath("$.[*].valor").value(hasItem(DEFAULT_VALOR.doubleValue())));
    }
    
    @Test
    @Transactional
    public void getRecursoNecessario() throws Exception {
        // Initialize the database
        recursoNecessarioRepository.saveAndFlush(recursoNecessario);

        // Get the recursoNecessario
        restRecursoNecessarioMockMvc.perform(get("/api/recurso-necessarios/{id}", recursoNecessario.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(recursoNecessario.getId().intValue()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME.toString()))
            .andExpect(jsonPath("$.descricao").value(DEFAULT_DESCRICAO.toString()))
            .andExpect(jsonPath("$.quantidade").value(DEFAULT_QUANTIDADE))
            .andExpect(jsonPath("$.isFinanceiro").value(DEFAULT_IS_FINANCEIRO.booleanValue()))
            .andExpect(jsonPath("$.valor").value(DEFAULT_VALOR.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingRecursoNecessario() throws Exception {
        // Get the recursoNecessario
        restRecursoNecessarioMockMvc.perform(get("/api/recurso-necessarios/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRecursoNecessario() throws Exception {
        // Initialize the database
        recursoNecessarioRepository.saveAndFlush(recursoNecessario);

        int databaseSizeBeforeUpdate = recursoNecessarioRepository.findAll().size();

        // Update the recursoNecessario
        RecursoNecessario updatedRecursoNecessario = recursoNecessarioRepository.findById(recursoNecessario.getId()).get();
        // Disconnect from session so that the updates on updatedRecursoNecessario are not directly saved in db
        em.detach(updatedRecursoNecessario);
        updatedRecursoNecessario
            .nome(UPDATED_NOME)
            .descricao(UPDATED_DESCRICAO)
            .quantidade(UPDATED_QUANTIDADE)
            .isFinanceiro(UPDATED_IS_FINANCEIRO)
            .valor(UPDATED_VALOR);

        restRecursoNecessarioMockMvc.perform(put("/api/recurso-necessarios")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedRecursoNecessario)))
            .andExpect(status().isOk());

        // Validate the RecursoNecessario in the database
        List<RecursoNecessario> recursoNecessarioList = recursoNecessarioRepository.findAll();
        assertThat(recursoNecessarioList).hasSize(databaseSizeBeforeUpdate);
        RecursoNecessario testRecursoNecessario = recursoNecessarioList.get(recursoNecessarioList.size() - 1);
        assertThat(testRecursoNecessario.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testRecursoNecessario.getDescricao()).isEqualTo(UPDATED_DESCRICAO);
        assertThat(testRecursoNecessario.getQuantidade()).isEqualTo(UPDATED_QUANTIDADE);
        assertThat(testRecursoNecessario.isIsFinanceiro()).isEqualTo(UPDATED_IS_FINANCEIRO);
        assertThat(testRecursoNecessario.getValor()).isEqualTo(UPDATED_VALOR);
    }

    @Test
    @Transactional
    public void updateNonExistingRecursoNecessario() throws Exception {
        int databaseSizeBeforeUpdate = recursoNecessarioRepository.findAll().size();

        // Create the RecursoNecessario

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRecursoNecessarioMockMvc.perform(put("/api/recurso-necessarios")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(recursoNecessario)))
            .andExpect(status().isBadRequest());

        // Validate the RecursoNecessario in the database
        List<RecursoNecessario> recursoNecessarioList = recursoNecessarioRepository.findAll();
        assertThat(recursoNecessarioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteRecursoNecessario() throws Exception {
        // Initialize the database
        recursoNecessarioRepository.saveAndFlush(recursoNecessario);

        int databaseSizeBeforeDelete = recursoNecessarioRepository.findAll().size();

        // Delete the recursoNecessario
        restRecursoNecessarioMockMvc.perform(delete("/api/recurso-necessarios/{id}", recursoNecessario.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<RecursoNecessario> recursoNecessarioList = recursoNecessarioRepository.findAll();
        assertThat(recursoNecessarioList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RecursoNecessario.class);
        RecursoNecessario recursoNecessario1 = new RecursoNecessario();
        recursoNecessario1.setId(1L);
        RecursoNecessario recursoNecessario2 = new RecursoNecessario();
        recursoNecessario2.setId(recursoNecessario1.getId());
        assertThat(recursoNecessario1).isEqualTo(recursoNecessario2);
        recursoNecessario2.setId(2L);
        assertThat(recursoNecessario1).isNotEqualTo(recursoNecessario2);
        recursoNecessario1.setId(null);
        assertThat(recursoNecessario1).isNotEqualTo(recursoNecessario2);
    }
}
