package br.com.donazione.api.web.rest;

import br.com.donazione.api.ApidonazioneApp;

import br.com.donazione.api.domain.Doacao;
import br.com.donazione.api.domain.Voluntario;
import br.com.donazione.api.domain.RecursoNecessario;
import br.com.donazione.api.repository.DoacaoRepository;
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
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;


import static br.com.donazione.api.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the DoacaoResource REST controller.
 *
 * @see DoacaoResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ApidonazioneApp.class)
public class DoacaoResourceIntTest {

    private static final String DEFAULT_DESCRICAO = "AAAAAAAAAA";
    private static final String UPDATED_DESCRICAO = "BBBBBBBBBB";

    private static final Integer DEFAULT_QUANTIDADE = 1;
    private static final Integer UPDATED_QUANTIDADE = 2;

    private static final Boolean DEFAULT_IS_FINANCEIRO = false;
    private static final Boolean UPDATED_IS_FINANCEIRO = true;

    private static final Boolean DEFAULT_IS_ANOMINA = false;
    private static final Boolean UPDATED_IS_ANOMINA = true;

    private static final Instant DEFAULT_DATA_HORA = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATA_HORA = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private DoacaoRepository doacaoRepository;

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

    private MockMvc restDoacaoMockMvc;

    private Doacao doacao;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final DoacaoResource doacaoResource = new DoacaoResource(doacaoRepository);
        this.restDoacaoMockMvc = MockMvcBuilders.standaloneSetup(doacaoResource)
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
    public static Doacao createEntity(EntityManager em) {
        Doacao doacao = new Doacao()
            .descricao(DEFAULT_DESCRICAO)
            .quantidade(DEFAULT_QUANTIDADE)
            .isFinanceiro(DEFAULT_IS_FINANCEIRO)
            .isAnomina(DEFAULT_IS_ANOMINA)
            .dataHora(DEFAULT_DATA_HORA);
        // Add required entity
        Voluntario voluntario = VoluntarioResourceIntTest.createEntity(em);
        em.persist(voluntario);
        em.flush();
        doacao.setDoador(voluntario);
        // Add required entity
        RecursoNecessario recursoNecessario = RecursoNecessarioResourceIntTest.createEntity(em);
        em.persist(recursoNecessario);
        em.flush();
        doacao.setRecursoNecessario(recursoNecessario);
        return doacao;
    }

    @Before
    public void initTest() {
        doacao = createEntity(em);
    }

    @Test
    @Transactional
    public void createDoacao() throws Exception {
        int databaseSizeBeforeCreate = doacaoRepository.findAll().size();

        // Create the Doacao
        restDoacaoMockMvc.perform(post("/api/doacaos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(doacao)))
            .andExpect(status().isCreated());

        // Validate the Doacao in the database
        List<Doacao> doacaoList = doacaoRepository.findAll();
        assertThat(doacaoList).hasSize(databaseSizeBeforeCreate + 1);
        Doacao testDoacao = doacaoList.get(doacaoList.size() - 1);
        assertThat(testDoacao.getDescricao()).isEqualTo(DEFAULT_DESCRICAO);
        assertThat(testDoacao.getQuantidade()).isEqualTo(DEFAULT_QUANTIDADE);
        assertThat(testDoacao.isIsFinanceiro()).isEqualTo(DEFAULT_IS_FINANCEIRO);
        assertThat(testDoacao.isIsAnomina()).isEqualTo(DEFAULT_IS_ANOMINA);
        assertThat(testDoacao.getDataHora()).isEqualTo(DEFAULT_DATA_HORA);
    }

    @Test
    @Transactional
    public void createDoacaoWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = doacaoRepository.findAll().size();

        // Create the Doacao with an existing ID
        doacao.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDoacaoMockMvc.perform(post("/api/doacaos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(doacao)))
            .andExpect(status().isBadRequest());

        // Validate the Doacao in the database
        List<Doacao> doacaoList = doacaoRepository.findAll();
        assertThat(doacaoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkDescricaoIsRequired() throws Exception {
        int databaseSizeBeforeTest = doacaoRepository.findAll().size();
        // set the field null
        doacao.setDescricao(null);

        // Create the Doacao, which fails.

        restDoacaoMockMvc.perform(post("/api/doacaos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(doacao)))
            .andExpect(status().isBadRequest());

        List<Doacao> doacaoList = doacaoRepository.findAll();
        assertThat(doacaoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkQuantidadeIsRequired() throws Exception {
        int databaseSizeBeforeTest = doacaoRepository.findAll().size();
        // set the field null
        doacao.setQuantidade(null);

        // Create the Doacao, which fails.

        restDoacaoMockMvc.perform(post("/api/doacaos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(doacao)))
            .andExpect(status().isBadRequest());

        List<Doacao> doacaoList = doacaoRepository.findAll();
        assertThat(doacaoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkIsFinanceiroIsRequired() throws Exception {
        int databaseSizeBeforeTest = doacaoRepository.findAll().size();
        // set the field null
        doacao.setIsFinanceiro(null);

        // Create the Doacao, which fails.

        restDoacaoMockMvc.perform(post("/api/doacaos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(doacao)))
            .andExpect(status().isBadRequest());

        List<Doacao> doacaoList = doacaoRepository.findAll();
        assertThat(doacaoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkIsAnominaIsRequired() throws Exception {
        int databaseSizeBeforeTest = doacaoRepository.findAll().size();
        // set the field null
        doacao.setIsAnomina(null);

        // Create the Doacao, which fails.

        restDoacaoMockMvc.perform(post("/api/doacaos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(doacao)))
            .andExpect(status().isBadRequest());

        List<Doacao> doacaoList = doacaoRepository.findAll();
        assertThat(doacaoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDataHoraIsRequired() throws Exception {
        int databaseSizeBeforeTest = doacaoRepository.findAll().size();
        // set the field null
        doacao.setDataHora(null);

        // Create the Doacao, which fails.

        restDoacaoMockMvc.perform(post("/api/doacaos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(doacao)))
            .andExpect(status().isBadRequest());

        List<Doacao> doacaoList = doacaoRepository.findAll();
        assertThat(doacaoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllDoacaos() throws Exception {
        // Initialize the database
        doacaoRepository.saveAndFlush(doacao);

        // Get all the doacaoList
        restDoacaoMockMvc.perform(get("/api/doacaos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(doacao.getId().intValue())))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO.toString())))
            .andExpect(jsonPath("$.[*].quantidade").value(hasItem(DEFAULT_QUANTIDADE)))
            .andExpect(jsonPath("$.[*].isFinanceiro").value(hasItem(DEFAULT_IS_FINANCEIRO.booleanValue())))
            .andExpect(jsonPath("$.[*].isAnomina").value(hasItem(DEFAULT_IS_ANOMINA.booleanValue())))
            .andExpect(jsonPath("$.[*].dataHora").value(hasItem(DEFAULT_DATA_HORA.toString())));
    }
    
    @Test
    @Transactional
    public void getDoacao() throws Exception {
        // Initialize the database
        doacaoRepository.saveAndFlush(doacao);

        // Get the doacao
        restDoacaoMockMvc.perform(get("/api/doacaos/{id}", doacao.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(doacao.getId().intValue()))
            .andExpect(jsonPath("$.descricao").value(DEFAULT_DESCRICAO.toString()))
            .andExpect(jsonPath("$.quantidade").value(DEFAULT_QUANTIDADE))
            .andExpect(jsonPath("$.isFinanceiro").value(DEFAULT_IS_FINANCEIRO.booleanValue()))
            .andExpect(jsonPath("$.isAnomina").value(DEFAULT_IS_ANOMINA.booleanValue()))
            .andExpect(jsonPath("$.dataHora").value(DEFAULT_DATA_HORA.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingDoacao() throws Exception {
        // Get the doacao
        restDoacaoMockMvc.perform(get("/api/doacaos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDoacao() throws Exception {
        // Initialize the database
        doacaoRepository.saveAndFlush(doacao);

        int databaseSizeBeforeUpdate = doacaoRepository.findAll().size();

        // Update the doacao
        Doacao updatedDoacao = doacaoRepository.findById(doacao.getId()).get();
        // Disconnect from session so that the updates on updatedDoacao are not directly saved in db
        em.detach(updatedDoacao);
        updatedDoacao
            .descricao(UPDATED_DESCRICAO)
            .quantidade(UPDATED_QUANTIDADE)
            .isFinanceiro(UPDATED_IS_FINANCEIRO)
            .isAnomina(UPDATED_IS_ANOMINA)
            .dataHora(UPDATED_DATA_HORA);

        restDoacaoMockMvc.perform(put("/api/doacaos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedDoacao)))
            .andExpect(status().isOk());

        // Validate the Doacao in the database
        List<Doacao> doacaoList = doacaoRepository.findAll();
        assertThat(doacaoList).hasSize(databaseSizeBeforeUpdate);
        Doacao testDoacao = doacaoList.get(doacaoList.size() - 1);
        assertThat(testDoacao.getDescricao()).isEqualTo(UPDATED_DESCRICAO);
        assertThat(testDoacao.getQuantidade()).isEqualTo(UPDATED_QUANTIDADE);
        assertThat(testDoacao.isIsFinanceiro()).isEqualTo(UPDATED_IS_FINANCEIRO);
        assertThat(testDoacao.isIsAnomina()).isEqualTo(UPDATED_IS_ANOMINA);
        assertThat(testDoacao.getDataHora()).isEqualTo(UPDATED_DATA_HORA);
    }

    @Test
    @Transactional
    public void updateNonExistingDoacao() throws Exception {
        int databaseSizeBeforeUpdate = doacaoRepository.findAll().size();

        // Create the Doacao

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDoacaoMockMvc.perform(put("/api/doacaos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(doacao)))
            .andExpect(status().isBadRequest());

        // Validate the Doacao in the database
        List<Doacao> doacaoList = doacaoRepository.findAll();
        assertThat(doacaoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteDoacao() throws Exception {
        // Initialize the database
        doacaoRepository.saveAndFlush(doacao);

        int databaseSizeBeforeDelete = doacaoRepository.findAll().size();

        // Delete the doacao
        restDoacaoMockMvc.perform(delete("/api/doacaos/{id}", doacao.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Doacao> doacaoList = doacaoRepository.findAll();
        assertThat(doacaoList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Doacao.class);
        Doacao doacao1 = new Doacao();
        doacao1.setId(1L);
        Doacao doacao2 = new Doacao();
        doacao2.setId(doacao1.getId());
        assertThat(doacao1).isEqualTo(doacao2);
        doacao2.setId(2L);
        assertThat(doacao1).isNotEqualTo(doacao2);
        doacao1.setId(null);
        assertThat(doacao1).isNotEqualTo(doacao2);
    }
}
