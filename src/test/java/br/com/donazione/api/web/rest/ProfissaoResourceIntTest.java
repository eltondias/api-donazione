package br.com.donazione.api.web.rest;

import br.com.donazione.api.ApidonazioneApp;

import br.com.donazione.api.domain.Profissao;
import br.com.donazione.api.repository.ProfissaoRepository;
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
 * Test class for the ProfissaoResource REST controller.
 *
 * @see ProfissaoResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ApidonazioneApp.class)
public class ProfissaoResourceIntTest {

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRICAO = "AAAAAAAAAA";
    private static final String UPDATED_DESCRICAO = "BBBBBBBBBB";

    @Autowired
    private ProfissaoRepository profissaoRepository;

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

    private MockMvc restProfissaoMockMvc;

    private Profissao profissao;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ProfissaoResource profissaoResource = new ProfissaoResource(profissaoRepository);
        this.restProfissaoMockMvc = MockMvcBuilders.standaloneSetup(profissaoResource)
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
    public static Profissao createEntity(EntityManager em) {
        Profissao profissao = new Profissao()
            .nome(DEFAULT_NOME)
            .descricao(DEFAULT_DESCRICAO);
        return profissao;
    }

    @Before
    public void initTest() {
        profissao = createEntity(em);
    }

    @Test
    @Transactional
    public void createProfissao() throws Exception {
        int databaseSizeBeforeCreate = profissaoRepository.findAll().size();

        // Create the Profissao
        restProfissaoMockMvc.perform(post("/api/profissaos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(profissao)))
            .andExpect(status().isCreated());

        // Validate the Profissao in the database
        List<Profissao> profissaoList = profissaoRepository.findAll();
        assertThat(profissaoList).hasSize(databaseSizeBeforeCreate + 1);
        Profissao testProfissao = profissaoList.get(profissaoList.size() - 1);
        assertThat(testProfissao.getNome()).isEqualTo(DEFAULT_NOME);
        assertThat(testProfissao.getDescricao()).isEqualTo(DEFAULT_DESCRICAO);
    }

    @Test
    @Transactional
    public void createProfissaoWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = profissaoRepository.findAll().size();

        // Create the Profissao with an existing ID
        profissao.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restProfissaoMockMvc.perform(post("/api/profissaos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(profissao)))
            .andExpect(status().isBadRequest());

        // Validate the Profissao in the database
        List<Profissao> profissaoList = profissaoRepository.findAll();
        assertThat(profissaoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNomeIsRequired() throws Exception {
        int databaseSizeBeforeTest = profissaoRepository.findAll().size();
        // set the field null
        profissao.setNome(null);

        // Create the Profissao, which fails.

        restProfissaoMockMvc.perform(post("/api/profissaos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(profissao)))
            .andExpect(status().isBadRequest());

        List<Profissao> profissaoList = profissaoRepository.findAll();
        assertThat(profissaoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDescricaoIsRequired() throws Exception {
        int databaseSizeBeforeTest = profissaoRepository.findAll().size();
        // set the field null
        profissao.setDescricao(null);

        // Create the Profissao, which fails.

        restProfissaoMockMvc.perform(post("/api/profissaos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(profissao)))
            .andExpect(status().isBadRequest());

        List<Profissao> profissaoList = profissaoRepository.findAll();
        assertThat(profissaoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllProfissaos() throws Exception {
        // Initialize the database
        profissaoRepository.saveAndFlush(profissao);

        // Get all the profissaoList
        restProfissaoMockMvc.perform(get("/api/profissaos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(profissao.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME.toString())))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO.toString())));
    }
    
    @Test
    @Transactional
    public void getProfissao() throws Exception {
        // Initialize the database
        profissaoRepository.saveAndFlush(profissao);

        // Get the profissao
        restProfissaoMockMvc.perform(get("/api/profissaos/{id}", profissao.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(profissao.getId().intValue()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME.toString()))
            .andExpect(jsonPath("$.descricao").value(DEFAULT_DESCRICAO.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingProfissao() throws Exception {
        // Get the profissao
        restProfissaoMockMvc.perform(get("/api/profissaos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProfissao() throws Exception {
        // Initialize the database
        profissaoRepository.saveAndFlush(profissao);

        int databaseSizeBeforeUpdate = profissaoRepository.findAll().size();

        // Update the profissao
        Profissao updatedProfissao = profissaoRepository.findById(profissao.getId()).get();
        // Disconnect from session so that the updates on updatedProfissao are not directly saved in db
        em.detach(updatedProfissao);
        updatedProfissao
            .nome(UPDATED_NOME)
            .descricao(UPDATED_DESCRICAO);

        restProfissaoMockMvc.perform(put("/api/profissaos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedProfissao)))
            .andExpect(status().isOk());

        // Validate the Profissao in the database
        List<Profissao> profissaoList = profissaoRepository.findAll();
        assertThat(profissaoList).hasSize(databaseSizeBeforeUpdate);
        Profissao testProfissao = profissaoList.get(profissaoList.size() - 1);
        assertThat(testProfissao.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testProfissao.getDescricao()).isEqualTo(UPDATED_DESCRICAO);
    }

    @Test
    @Transactional
    public void updateNonExistingProfissao() throws Exception {
        int databaseSizeBeforeUpdate = profissaoRepository.findAll().size();

        // Create the Profissao

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProfissaoMockMvc.perform(put("/api/profissaos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(profissao)))
            .andExpect(status().isBadRequest());

        // Validate the Profissao in the database
        List<Profissao> profissaoList = profissaoRepository.findAll();
        assertThat(profissaoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteProfissao() throws Exception {
        // Initialize the database
        profissaoRepository.saveAndFlush(profissao);

        int databaseSizeBeforeDelete = profissaoRepository.findAll().size();

        // Delete the profissao
        restProfissaoMockMvc.perform(delete("/api/profissaos/{id}", profissao.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Profissao> profissaoList = profissaoRepository.findAll();
        assertThat(profissaoList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Profissao.class);
        Profissao profissao1 = new Profissao();
        profissao1.setId(1L);
        Profissao profissao2 = new Profissao();
        profissao2.setId(profissao1.getId());
        assertThat(profissao1).isEqualTo(profissao2);
        profissao2.setId(2L);
        assertThat(profissao1).isNotEqualTo(profissao2);
        profissao1.setId(null);
        assertThat(profissao1).isNotEqualTo(profissao2);
    }
}
