package br.com.donazione.api.web.rest;

import br.com.donazione.api.ApidonazioneApp;

import br.com.donazione.api.domain.ProfissaoNecessariaAcao;
import br.com.donazione.api.domain.Profissao;
import br.com.donazione.api.domain.Acao;
import br.com.donazione.api.repository.ProfissaoNecessariaAcaoRepository;
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
 * Test class for the ProfissaoNecessariaAcaoResource REST controller.
 *
 * @see ProfissaoNecessariaAcaoResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ApidonazioneApp.class)
public class ProfissaoNecessariaAcaoResourceIntTest {

    private static final Integer DEFAULT_QUANTIDADE_MINIMA = 1;
    private static final Integer UPDATED_QUANTIDADE_MINIMA = 2;

    @Autowired
    private ProfissaoNecessariaAcaoRepository profissaoNecessariaAcaoRepository;

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

    private MockMvc restProfissaoNecessariaAcaoMockMvc;

    private ProfissaoNecessariaAcao profissaoNecessariaAcao;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ProfissaoNecessariaAcaoResource profissaoNecessariaAcaoResource = new ProfissaoNecessariaAcaoResource(profissaoNecessariaAcaoRepository);
        this.restProfissaoNecessariaAcaoMockMvc = MockMvcBuilders.standaloneSetup(profissaoNecessariaAcaoResource)
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
    public static ProfissaoNecessariaAcao createEntity(EntityManager em) {
        ProfissaoNecessariaAcao profissaoNecessariaAcao = new ProfissaoNecessariaAcao()
            .quantidadeMinima(DEFAULT_QUANTIDADE_MINIMA);
        // Add required entity
        Profissao profissao = ProfissaoResourceIntTest.createEntity(em);
        em.persist(profissao);
        em.flush();
        profissaoNecessariaAcao.setProfissao(profissao);
        // Add required entity
        Acao acao = AcaoResourceIntTest.createEntity(em);
        em.persist(acao);
        em.flush();
        profissaoNecessariaAcao.setAcao(acao);
        return profissaoNecessariaAcao;
    }

    @Before
    public void initTest() {
        profissaoNecessariaAcao = createEntity(em);
    }

    @Test
    @Transactional
    public void createProfissaoNecessariaAcao() throws Exception {
        int databaseSizeBeforeCreate = profissaoNecessariaAcaoRepository.findAll().size();

        // Create the ProfissaoNecessariaAcao
        restProfissaoNecessariaAcaoMockMvc.perform(post("/api/profissao-necessaria-acaos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(profissaoNecessariaAcao)))
            .andExpect(status().isCreated());

        // Validate the ProfissaoNecessariaAcao in the database
        List<ProfissaoNecessariaAcao> profissaoNecessariaAcaoList = profissaoNecessariaAcaoRepository.findAll();
        assertThat(profissaoNecessariaAcaoList).hasSize(databaseSizeBeforeCreate + 1);
        ProfissaoNecessariaAcao testProfissaoNecessariaAcao = profissaoNecessariaAcaoList.get(profissaoNecessariaAcaoList.size() - 1);
        assertThat(testProfissaoNecessariaAcao.getQuantidadeMinima()).isEqualTo(DEFAULT_QUANTIDADE_MINIMA);
    }

    @Test
    @Transactional
    public void createProfissaoNecessariaAcaoWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = profissaoNecessariaAcaoRepository.findAll().size();

        // Create the ProfissaoNecessariaAcao with an existing ID
        profissaoNecessariaAcao.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restProfissaoNecessariaAcaoMockMvc.perform(post("/api/profissao-necessaria-acaos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(profissaoNecessariaAcao)))
            .andExpect(status().isBadRequest());

        // Validate the ProfissaoNecessariaAcao in the database
        List<ProfissaoNecessariaAcao> profissaoNecessariaAcaoList = profissaoNecessariaAcaoRepository.findAll();
        assertThat(profissaoNecessariaAcaoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkQuantidadeMinimaIsRequired() throws Exception {
        int databaseSizeBeforeTest = profissaoNecessariaAcaoRepository.findAll().size();
        // set the field null
        profissaoNecessariaAcao.setQuantidadeMinima(null);

        // Create the ProfissaoNecessariaAcao, which fails.

        restProfissaoNecessariaAcaoMockMvc.perform(post("/api/profissao-necessaria-acaos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(profissaoNecessariaAcao)))
            .andExpect(status().isBadRequest());

        List<ProfissaoNecessariaAcao> profissaoNecessariaAcaoList = profissaoNecessariaAcaoRepository.findAll();
        assertThat(profissaoNecessariaAcaoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllProfissaoNecessariaAcaos() throws Exception {
        // Initialize the database
        profissaoNecessariaAcaoRepository.saveAndFlush(profissaoNecessariaAcao);

        // Get all the profissaoNecessariaAcaoList
        restProfissaoNecessariaAcaoMockMvc.perform(get("/api/profissao-necessaria-acaos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(profissaoNecessariaAcao.getId().intValue())))
            .andExpect(jsonPath("$.[*].quantidadeMinima").value(hasItem(DEFAULT_QUANTIDADE_MINIMA)));
    }
    
    @Test
    @Transactional
    public void getProfissaoNecessariaAcao() throws Exception {
        // Initialize the database
        profissaoNecessariaAcaoRepository.saveAndFlush(profissaoNecessariaAcao);

        // Get the profissaoNecessariaAcao
        restProfissaoNecessariaAcaoMockMvc.perform(get("/api/profissao-necessaria-acaos/{id}", profissaoNecessariaAcao.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(profissaoNecessariaAcao.getId().intValue()))
            .andExpect(jsonPath("$.quantidadeMinima").value(DEFAULT_QUANTIDADE_MINIMA));
    }

    @Test
    @Transactional
    public void getNonExistingProfissaoNecessariaAcao() throws Exception {
        // Get the profissaoNecessariaAcao
        restProfissaoNecessariaAcaoMockMvc.perform(get("/api/profissao-necessaria-acaos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProfissaoNecessariaAcao() throws Exception {
        // Initialize the database
        profissaoNecessariaAcaoRepository.saveAndFlush(profissaoNecessariaAcao);

        int databaseSizeBeforeUpdate = profissaoNecessariaAcaoRepository.findAll().size();

        // Update the profissaoNecessariaAcao
        ProfissaoNecessariaAcao updatedProfissaoNecessariaAcao = profissaoNecessariaAcaoRepository.findById(profissaoNecessariaAcao.getId()).get();
        // Disconnect from session so that the updates on updatedProfissaoNecessariaAcao are not directly saved in db
        em.detach(updatedProfissaoNecessariaAcao);
        updatedProfissaoNecessariaAcao
            .quantidadeMinima(UPDATED_QUANTIDADE_MINIMA);

        restProfissaoNecessariaAcaoMockMvc.perform(put("/api/profissao-necessaria-acaos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedProfissaoNecessariaAcao)))
            .andExpect(status().isOk());

        // Validate the ProfissaoNecessariaAcao in the database
        List<ProfissaoNecessariaAcao> profissaoNecessariaAcaoList = profissaoNecessariaAcaoRepository.findAll();
        assertThat(profissaoNecessariaAcaoList).hasSize(databaseSizeBeforeUpdate);
        ProfissaoNecessariaAcao testProfissaoNecessariaAcao = profissaoNecessariaAcaoList.get(profissaoNecessariaAcaoList.size() - 1);
        assertThat(testProfissaoNecessariaAcao.getQuantidadeMinima()).isEqualTo(UPDATED_QUANTIDADE_MINIMA);
    }

    @Test
    @Transactional
    public void updateNonExistingProfissaoNecessariaAcao() throws Exception {
        int databaseSizeBeforeUpdate = profissaoNecessariaAcaoRepository.findAll().size();

        // Create the ProfissaoNecessariaAcao

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProfissaoNecessariaAcaoMockMvc.perform(put("/api/profissao-necessaria-acaos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(profissaoNecessariaAcao)))
            .andExpect(status().isBadRequest());

        // Validate the ProfissaoNecessariaAcao in the database
        List<ProfissaoNecessariaAcao> profissaoNecessariaAcaoList = profissaoNecessariaAcaoRepository.findAll();
        assertThat(profissaoNecessariaAcaoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteProfissaoNecessariaAcao() throws Exception {
        // Initialize the database
        profissaoNecessariaAcaoRepository.saveAndFlush(profissaoNecessariaAcao);

        int databaseSizeBeforeDelete = profissaoNecessariaAcaoRepository.findAll().size();

        // Delete the profissaoNecessariaAcao
        restProfissaoNecessariaAcaoMockMvc.perform(delete("/api/profissao-necessaria-acaos/{id}", profissaoNecessariaAcao.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<ProfissaoNecessariaAcao> profissaoNecessariaAcaoList = profissaoNecessariaAcaoRepository.findAll();
        assertThat(profissaoNecessariaAcaoList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProfissaoNecessariaAcao.class);
        ProfissaoNecessariaAcao profissaoNecessariaAcao1 = new ProfissaoNecessariaAcao();
        profissaoNecessariaAcao1.setId(1L);
        ProfissaoNecessariaAcao profissaoNecessariaAcao2 = new ProfissaoNecessariaAcao();
        profissaoNecessariaAcao2.setId(profissaoNecessariaAcao1.getId());
        assertThat(profissaoNecessariaAcao1).isEqualTo(profissaoNecessariaAcao2);
        profissaoNecessariaAcao2.setId(2L);
        assertThat(profissaoNecessariaAcao1).isNotEqualTo(profissaoNecessariaAcao2);
        profissaoNecessariaAcao1.setId(null);
        assertThat(profissaoNecessariaAcao1).isNotEqualTo(profissaoNecessariaAcao2);
    }
}
