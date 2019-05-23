package br.com.donazione.api.web.rest;

import br.com.donazione.api.ApidonazioneApp;

import br.com.donazione.api.domain.Campanha;
import br.com.donazione.api.domain.Voluntario;
import br.com.donazione.api.repository.CampanhaRepository;
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
 * Test class for the CampanhaResource REST controller.
 *
 * @see CampanhaResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ApidonazioneApp.class)
public class CampanhaResourceIntTest {

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRICAO = "AAAAAAAAAA";
    private static final String UPDATED_DESCRICAO = "BBBBBBBBBB";

    private static final String DEFAULT_SLOGAN = "AAAAAAAAAA";
    private static final String UPDATED_SLOGAN = "BBBBBBBBBB";

    private static final Instant DEFAULT_DATA_HORA_INICIO = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATA_HORA_INICIO = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_DATA_HORA_FIM = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATA_HORA_FIM = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private CampanhaRepository campanhaRepository;

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

    private MockMvc restCampanhaMockMvc;

    private Campanha campanha;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CampanhaResource campanhaResource = new CampanhaResource(campanhaRepository);
        this.restCampanhaMockMvc = MockMvcBuilders.standaloneSetup(campanhaResource)
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
    public static Campanha createEntity(EntityManager em) {
        Campanha campanha = new Campanha()
            .nome(DEFAULT_NOME)
            .descricao(DEFAULT_DESCRICAO)
            .slogan(DEFAULT_SLOGAN)
            .dataHoraInicio(DEFAULT_DATA_HORA_INICIO)
            .dataHoraFim(DEFAULT_DATA_HORA_FIM);
        // Add required entity
        Voluntario voluntario = VoluntarioResourceIntTest.createEntity(em);
        em.persist(voluntario);
        em.flush();
        campanha.setCoordenador(voluntario);
        return campanha;
    }

    @Before
    public void initTest() {
        campanha = createEntity(em);
    }

    @Test
    @Transactional
    public void createCampanha() throws Exception {
        int databaseSizeBeforeCreate = campanhaRepository.findAll().size();

        // Create the Campanha
        restCampanhaMockMvc.perform(post("/api/campanhas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(campanha)))
            .andExpect(status().isCreated());

        // Validate the Campanha in the database
        List<Campanha> campanhaList = campanhaRepository.findAll();
        assertThat(campanhaList).hasSize(databaseSizeBeforeCreate + 1);
        Campanha testCampanha = campanhaList.get(campanhaList.size() - 1);
        assertThat(testCampanha.getNome()).isEqualTo(DEFAULT_NOME);
        assertThat(testCampanha.getDescricao()).isEqualTo(DEFAULT_DESCRICAO);
        assertThat(testCampanha.getSlogan()).isEqualTo(DEFAULT_SLOGAN);
        assertThat(testCampanha.getDataHoraInicio()).isEqualTo(DEFAULT_DATA_HORA_INICIO);
        assertThat(testCampanha.getDataHoraFim()).isEqualTo(DEFAULT_DATA_HORA_FIM);
    }

    @Test
    @Transactional
    public void createCampanhaWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = campanhaRepository.findAll().size();

        // Create the Campanha with an existing ID
        campanha.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCampanhaMockMvc.perform(post("/api/campanhas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(campanha)))
            .andExpect(status().isBadRequest());

        // Validate the Campanha in the database
        List<Campanha> campanhaList = campanhaRepository.findAll();
        assertThat(campanhaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNomeIsRequired() throws Exception {
        int databaseSizeBeforeTest = campanhaRepository.findAll().size();
        // set the field null
        campanha.setNome(null);

        // Create the Campanha, which fails.

        restCampanhaMockMvc.perform(post("/api/campanhas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(campanha)))
            .andExpect(status().isBadRequest());

        List<Campanha> campanhaList = campanhaRepository.findAll();
        assertThat(campanhaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDescricaoIsRequired() throws Exception {
        int databaseSizeBeforeTest = campanhaRepository.findAll().size();
        // set the field null
        campanha.setDescricao(null);

        // Create the Campanha, which fails.

        restCampanhaMockMvc.perform(post("/api/campanhas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(campanha)))
            .andExpect(status().isBadRequest());

        List<Campanha> campanhaList = campanhaRepository.findAll();
        assertThat(campanhaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDataHoraInicioIsRequired() throws Exception {
        int databaseSizeBeforeTest = campanhaRepository.findAll().size();
        // set the field null
        campanha.setDataHoraInicio(null);

        // Create the Campanha, which fails.

        restCampanhaMockMvc.perform(post("/api/campanhas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(campanha)))
            .andExpect(status().isBadRequest());

        List<Campanha> campanhaList = campanhaRepository.findAll();
        assertThat(campanhaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDataHoraFimIsRequired() throws Exception {
        int databaseSizeBeforeTest = campanhaRepository.findAll().size();
        // set the field null
        campanha.setDataHoraFim(null);

        // Create the Campanha, which fails.

        restCampanhaMockMvc.perform(post("/api/campanhas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(campanha)))
            .andExpect(status().isBadRequest());

        List<Campanha> campanhaList = campanhaRepository.findAll();
        assertThat(campanhaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCampanhas() throws Exception {
        // Initialize the database
        campanhaRepository.saveAndFlush(campanha);

        // Get all the campanhaList
        restCampanhaMockMvc.perform(get("/api/campanhas?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(campanha.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME.toString())))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO.toString())))
            .andExpect(jsonPath("$.[*].slogan").value(hasItem(DEFAULT_SLOGAN.toString())))
            .andExpect(jsonPath("$.[*].dataHoraInicio").value(hasItem(DEFAULT_DATA_HORA_INICIO.toString())))
            .andExpect(jsonPath("$.[*].dataHoraFim").value(hasItem(DEFAULT_DATA_HORA_FIM.toString())));
    }
    
    @Test
    @Transactional
    public void getCampanha() throws Exception {
        // Initialize the database
        campanhaRepository.saveAndFlush(campanha);

        // Get the campanha
        restCampanhaMockMvc.perform(get("/api/campanhas/{id}", campanha.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(campanha.getId().intValue()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME.toString()))
            .andExpect(jsonPath("$.descricao").value(DEFAULT_DESCRICAO.toString()))
            .andExpect(jsonPath("$.slogan").value(DEFAULT_SLOGAN.toString()))
            .andExpect(jsonPath("$.dataHoraInicio").value(DEFAULT_DATA_HORA_INICIO.toString()))
            .andExpect(jsonPath("$.dataHoraFim").value(DEFAULT_DATA_HORA_FIM.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCampanha() throws Exception {
        // Get the campanha
        restCampanhaMockMvc.perform(get("/api/campanhas/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCampanha() throws Exception {
        // Initialize the database
        campanhaRepository.saveAndFlush(campanha);

        int databaseSizeBeforeUpdate = campanhaRepository.findAll().size();

        // Update the campanha
        Campanha updatedCampanha = campanhaRepository.findById(campanha.getId()).get();
        // Disconnect from session so that the updates on updatedCampanha are not directly saved in db
        em.detach(updatedCampanha);
        updatedCampanha
            .nome(UPDATED_NOME)
            .descricao(UPDATED_DESCRICAO)
            .slogan(UPDATED_SLOGAN)
            .dataHoraInicio(UPDATED_DATA_HORA_INICIO)
            .dataHoraFim(UPDATED_DATA_HORA_FIM);

        restCampanhaMockMvc.perform(put("/api/campanhas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedCampanha)))
            .andExpect(status().isOk());

        // Validate the Campanha in the database
        List<Campanha> campanhaList = campanhaRepository.findAll();
        assertThat(campanhaList).hasSize(databaseSizeBeforeUpdate);
        Campanha testCampanha = campanhaList.get(campanhaList.size() - 1);
        assertThat(testCampanha.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testCampanha.getDescricao()).isEqualTo(UPDATED_DESCRICAO);
        assertThat(testCampanha.getSlogan()).isEqualTo(UPDATED_SLOGAN);
        assertThat(testCampanha.getDataHoraInicio()).isEqualTo(UPDATED_DATA_HORA_INICIO);
        assertThat(testCampanha.getDataHoraFim()).isEqualTo(UPDATED_DATA_HORA_FIM);
    }

    @Test
    @Transactional
    public void updateNonExistingCampanha() throws Exception {
        int databaseSizeBeforeUpdate = campanhaRepository.findAll().size();

        // Create the Campanha

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCampanhaMockMvc.perform(put("/api/campanhas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(campanha)))
            .andExpect(status().isBadRequest());

        // Validate the Campanha in the database
        List<Campanha> campanhaList = campanhaRepository.findAll();
        assertThat(campanhaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteCampanha() throws Exception {
        // Initialize the database
        campanhaRepository.saveAndFlush(campanha);

        int databaseSizeBeforeDelete = campanhaRepository.findAll().size();

        // Delete the campanha
        restCampanhaMockMvc.perform(delete("/api/campanhas/{id}", campanha.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Campanha> campanhaList = campanhaRepository.findAll();
        assertThat(campanhaList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Campanha.class);
        Campanha campanha1 = new Campanha();
        campanha1.setId(1L);
        Campanha campanha2 = new Campanha();
        campanha2.setId(campanha1.getId());
        assertThat(campanha1).isEqualTo(campanha2);
        campanha2.setId(2L);
        assertThat(campanha1).isNotEqualTo(campanha2);
        campanha1.setId(null);
        assertThat(campanha1).isNotEqualTo(campanha2);
    }
}
