package br.com.donazione.api.web.rest;

import br.com.donazione.api.ApidonazioneApp;

import br.com.donazione.api.domain.Acao;
import br.com.donazione.api.repository.AcaoRepository;
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

import br.com.donazione.api.domain.enumeration.SituacaoAcaoEnum;
/**
 * Test class for the AcaoResource REST controller.
 *
 * @see AcaoResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ApidonazioneApp.class)
public class AcaoResourceIntTest {

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRICAO = "AAAAAAAAAA";
    private static final String UPDATED_DESCRICAO = "BBBBBBBBBB";

    private static final String DEFAULT_META = "AAAAAAAAAA";
    private static final String UPDATED_META = "BBBBBBBBBB";

    private static final String DEFAULT_BANNER = "AAAAAAAAAA";
    private static final String UPDATED_BANNER = "BBBBBBBBBB";

    private static final Instant DEFAULT_DATA_HORA_INICIO = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATA_HORA_INICIO = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_DATA_HORA_FIM = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATA_HORA_FIM = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Double DEFAULT_CUSTOS = 1D;
    private static final Double UPDATED_CUSTOS = 2D;

    private static final SituacaoAcaoEnum DEFAULT_SITUACAO_ACAO = SituacaoAcaoEnum.PRE_ACAO;
    private static final SituacaoAcaoEnum UPDATED_SITUACAO_ACAO = SituacaoAcaoEnum.EM_ACAO;

    @Autowired
    private AcaoRepository acaoRepository;

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

    private MockMvc restAcaoMockMvc;

    private Acao acao;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final AcaoResource acaoResource = new AcaoResource(acaoRepository);
        this.restAcaoMockMvc = MockMvcBuilders.standaloneSetup(acaoResource)
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
    public static Acao createEntity(EntityManager em) {
        Acao acao = new Acao()
            .nome(DEFAULT_NOME)
            .descricao(DEFAULT_DESCRICAO)
            .meta(DEFAULT_META)
            .banner(DEFAULT_BANNER)
            .dataHoraInicio(DEFAULT_DATA_HORA_INICIO)
            .dataHoraFim(DEFAULT_DATA_HORA_FIM)
            .custos(DEFAULT_CUSTOS)
            .situacaoAcao(DEFAULT_SITUACAO_ACAO);
        return acao;
    }

    @Before
    public void initTest() {
        acao = createEntity(em);
    }

    @Test
    @Transactional
    public void createAcao() throws Exception {
        int databaseSizeBeforeCreate = acaoRepository.findAll().size();

        // Create the Acao
        restAcaoMockMvc.perform(post("/api/acaos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(acao)))
            .andExpect(status().isCreated());

        // Validate the Acao in the database
        List<Acao> acaoList = acaoRepository.findAll();
        assertThat(acaoList).hasSize(databaseSizeBeforeCreate + 1);
        Acao testAcao = acaoList.get(acaoList.size() - 1);
        assertThat(testAcao.getNome()).isEqualTo(DEFAULT_NOME);
        assertThat(testAcao.getDescricao()).isEqualTo(DEFAULT_DESCRICAO);
        assertThat(testAcao.getMeta()).isEqualTo(DEFAULT_META);
        assertThat(testAcao.getBanner()).isEqualTo(DEFAULT_BANNER);
        assertThat(testAcao.getDataHoraInicio()).isEqualTo(DEFAULT_DATA_HORA_INICIO);
        assertThat(testAcao.getDataHoraFim()).isEqualTo(DEFAULT_DATA_HORA_FIM);
        assertThat(testAcao.getCustos()).isEqualTo(DEFAULT_CUSTOS);
        assertThat(testAcao.getSituacaoAcao()).isEqualTo(DEFAULT_SITUACAO_ACAO);
    }

    @Test
    @Transactional
    public void createAcaoWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = acaoRepository.findAll().size();

        // Create the Acao with an existing ID
        acao.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAcaoMockMvc.perform(post("/api/acaos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(acao)))
            .andExpect(status().isBadRequest());

        // Validate the Acao in the database
        List<Acao> acaoList = acaoRepository.findAll();
        assertThat(acaoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNomeIsRequired() throws Exception {
        int databaseSizeBeforeTest = acaoRepository.findAll().size();
        // set the field null
        acao.setNome(null);

        // Create the Acao, which fails.

        restAcaoMockMvc.perform(post("/api/acaos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(acao)))
            .andExpect(status().isBadRequest());

        List<Acao> acaoList = acaoRepository.findAll();
        assertThat(acaoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDescricaoIsRequired() throws Exception {
        int databaseSizeBeforeTest = acaoRepository.findAll().size();
        // set the field null
        acao.setDescricao(null);

        // Create the Acao, which fails.

        restAcaoMockMvc.perform(post("/api/acaos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(acao)))
            .andExpect(status().isBadRequest());

        List<Acao> acaoList = acaoRepository.findAll();
        assertThat(acaoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDataHoraInicioIsRequired() throws Exception {
        int databaseSizeBeforeTest = acaoRepository.findAll().size();
        // set the field null
        acao.setDataHoraInicio(null);

        // Create the Acao, which fails.

        restAcaoMockMvc.perform(post("/api/acaos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(acao)))
            .andExpect(status().isBadRequest());

        List<Acao> acaoList = acaoRepository.findAll();
        assertThat(acaoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDataHoraFimIsRequired() throws Exception {
        int databaseSizeBeforeTest = acaoRepository.findAll().size();
        // set the field null
        acao.setDataHoraFim(null);

        // Create the Acao, which fails.

        restAcaoMockMvc.perform(post("/api/acaos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(acao)))
            .andExpect(status().isBadRequest());

        List<Acao> acaoList = acaoRepository.findAll();
        assertThat(acaoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSituacaoAcaoIsRequired() throws Exception {
        int databaseSizeBeforeTest = acaoRepository.findAll().size();
        // set the field null
        acao.setSituacaoAcao(null);

        // Create the Acao, which fails.

        restAcaoMockMvc.perform(post("/api/acaos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(acao)))
            .andExpect(status().isBadRequest());

        List<Acao> acaoList = acaoRepository.findAll();
        assertThat(acaoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllAcaos() throws Exception {
        // Initialize the database
        acaoRepository.saveAndFlush(acao);

        // Get all the acaoList
        restAcaoMockMvc.perform(get("/api/acaos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(acao.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME.toString())))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO.toString())))
            .andExpect(jsonPath("$.[*].meta").value(hasItem(DEFAULT_META.toString())))
            .andExpect(jsonPath("$.[*].banner").value(hasItem(DEFAULT_BANNER.toString())))
            .andExpect(jsonPath("$.[*].dataHoraInicio").value(hasItem(DEFAULT_DATA_HORA_INICIO.toString())))
            .andExpect(jsonPath("$.[*].dataHoraFim").value(hasItem(DEFAULT_DATA_HORA_FIM.toString())))
            .andExpect(jsonPath("$.[*].custos").value(hasItem(DEFAULT_CUSTOS.doubleValue())))
            .andExpect(jsonPath("$.[*].situacaoAcao").value(hasItem(DEFAULT_SITUACAO_ACAO.toString())));
    }
    
    @Test
    @Transactional
    public void getAcao() throws Exception {
        // Initialize the database
        acaoRepository.saveAndFlush(acao);

        // Get the acao
        restAcaoMockMvc.perform(get("/api/acaos/{id}", acao.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(acao.getId().intValue()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME.toString()))
            .andExpect(jsonPath("$.descricao").value(DEFAULT_DESCRICAO.toString()))
            .andExpect(jsonPath("$.meta").value(DEFAULT_META.toString()))
            .andExpect(jsonPath("$.banner").value(DEFAULT_BANNER.toString()))
            .andExpect(jsonPath("$.dataHoraInicio").value(DEFAULT_DATA_HORA_INICIO.toString()))
            .andExpect(jsonPath("$.dataHoraFim").value(DEFAULT_DATA_HORA_FIM.toString()))
            .andExpect(jsonPath("$.custos").value(DEFAULT_CUSTOS.doubleValue()))
            .andExpect(jsonPath("$.situacaoAcao").value(DEFAULT_SITUACAO_ACAO.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingAcao() throws Exception {
        // Get the acao
        restAcaoMockMvc.perform(get("/api/acaos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAcao() throws Exception {
        // Initialize the database
        acaoRepository.saveAndFlush(acao);

        int databaseSizeBeforeUpdate = acaoRepository.findAll().size();

        // Update the acao
        Acao updatedAcao = acaoRepository.findById(acao.getId()).get();
        // Disconnect from session so that the updates on updatedAcao are not directly saved in db
        em.detach(updatedAcao);
        updatedAcao
            .nome(UPDATED_NOME)
            .descricao(UPDATED_DESCRICAO)
            .meta(UPDATED_META)
            .banner(UPDATED_BANNER)
            .dataHoraInicio(UPDATED_DATA_HORA_INICIO)
            .dataHoraFim(UPDATED_DATA_HORA_FIM)
            .custos(UPDATED_CUSTOS)
            .situacaoAcao(UPDATED_SITUACAO_ACAO);

        restAcaoMockMvc.perform(put("/api/acaos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedAcao)))
            .andExpect(status().isOk());

        // Validate the Acao in the database
        List<Acao> acaoList = acaoRepository.findAll();
        assertThat(acaoList).hasSize(databaseSizeBeforeUpdate);
        Acao testAcao = acaoList.get(acaoList.size() - 1);
        assertThat(testAcao.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testAcao.getDescricao()).isEqualTo(UPDATED_DESCRICAO);
        assertThat(testAcao.getMeta()).isEqualTo(UPDATED_META);
        assertThat(testAcao.getBanner()).isEqualTo(UPDATED_BANNER);
        assertThat(testAcao.getDataHoraInicio()).isEqualTo(UPDATED_DATA_HORA_INICIO);
        assertThat(testAcao.getDataHoraFim()).isEqualTo(UPDATED_DATA_HORA_FIM);
        assertThat(testAcao.getCustos()).isEqualTo(UPDATED_CUSTOS);
        assertThat(testAcao.getSituacaoAcao()).isEqualTo(UPDATED_SITUACAO_ACAO);
    }

    @Test
    @Transactional
    public void updateNonExistingAcao() throws Exception {
        int databaseSizeBeforeUpdate = acaoRepository.findAll().size();

        // Create the Acao

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAcaoMockMvc.perform(put("/api/acaos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(acao)))
            .andExpect(status().isBadRequest());

        // Validate the Acao in the database
        List<Acao> acaoList = acaoRepository.findAll();
        assertThat(acaoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteAcao() throws Exception {
        // Initialize the database
        acaoRepository.saveAndFlush(acao);

        int databaseSizeBeforeDelete = acaoRepository.findAll().size();

        // Delete the acao
        restAcaoMockMvc.perform(delete("/api/acaos/{id}", acao.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Acao> acaoList = acaoRepository.findAll();
        assertThat(acaoList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Acao.class);
        Acao acao1 = new Acao();
        acao1.setId(1L);
        Acao acao2 = new Acao();
        acao2.setId(acao1.getId());
        assertThat(acao1).isEqualTo(acao2);
        acao2.setId(2L);
        assertThat(acao1).isNotEqualTo(acao2);
        acao1.setId(null);
        assertThat(acao1).isNotEqualTo(acao2);
    }
}
