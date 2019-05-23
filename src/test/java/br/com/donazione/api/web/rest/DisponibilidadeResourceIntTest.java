package br.com.donazione.api.web.rest;

import br.com.donazione.api.ApidonazioneApp;

import br.com.donazione.api.domain.Disponibilidade;
import br.com.donazione.api.domain.Voluntario;
import br.com.donazione.api.repository.DisponibilidadeRepository;
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

import br.com.donazione.api.domain.enumeration.DiaSemanaEnum;
import br.com.donazione.api.domain.enumeration.TurnoEnum;
/**
 * Test class for the DisponibilidadeResource REST controller.
 *
 * @see DisponibilidadeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ApidonazioneApp.class)
public class DisponibilidadeResourceIntTest {

    private static final Instant DEFAULT_HORA_INICIO = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_HORA_INICIO = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_HORA_FIM = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_HORA_FIM = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final DiaSemanaEnum DEFAULT_DIA_SEMANA = DiaSemanaEnum.SEGUNDA;
    private static final DiaSemanaEnum UPDATED_DIA_SEMANA = DiaSemanaEnum.TERCA;

    private static final TurnoEnum DEFAULT_TURNO = TurnoEnum.MANHA;
    private static final TurnoEnum UPDATED_TURNO = TurnoEnum.TARDE;

    @Autowired
    private DisponibilidadeRepository disponibilidadeRepository;

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

    private MockMvc restDisponibilidadeMockMvc;

    private Disponibilidade disponibilidade;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final DisponibilidadeResource disponibilidadeResource = new DisponibilidadeResource(disponibilidadeRepository);
        this.restDisponibilidadeMockMvc = MockMvcBuilders.standaloneSetup(disponibilidadeResource)
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
    public static Disponibilidade createEntity(EntityManager em) {
        Disponibilidade disponibilidade = new Disponibilidade()
            .horaInicio(DEFAULT_HORA_INICIO)
            .horaFim(DEFAULT_HORA_FIM)
            .diaSemana(DEFAULT_DIA_SEMANA)
            .turno(DEFAULT_TURNO);
        // Add required entity
        Voluntario voluntario = VoluntarioResourceIntTest.createEntity(em);
        em.persist(voluntario);
        em.flush();
        disponibilidade.setVoluntario(voluntario);
        return disponibilidade;
    }

    @Before
    public void initTest() {
        disponibilidade = createEntity(em);
    }

    @Test
    @Transactional
    public void createDisponibilidade() throws Exception {
        int databaseSizeBeforeCreate = disponibilidadeRepository.findAll().size();

        // Create the Disponibilidade
        restDisponibilidadeMockMvc.perform(post("/api/disponibilidades")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(disponibilidade)))
            .andExpect(status().isCreated());

        // Validate the Disponibilidade in the database
        List<Disponibilidade> disponibilidadeList = disponibilidadeRepository.findAll();
        assertThat(disponibilidadeList).hasSize(databaseSizeBeforeCreate + 1);
        Disponibilidade testDisponibilidade = disponibilidadeList.get(disponibilidadeList.size() - 1);
        assertThat(testDisponibilidade.getHoraInicio()).isEqualTo(DEFAULT_HORA_INICIO);
        assertThat(testDisponibilidade.getHoraFim()).isEqualTo(DEFAULT_HORA_FIM);
        assertThat(testDisponibilidade.getDiaSemana()).isEqualTo(DEFAULT_DIA_SEMANA);
        assertThat(testDisponibilidade.getTurno()).isEqualTo(DEFAULT_TURNO);
    }

    @Test
    @Transactional
    public void createDisponibilidadeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = disponibilidadeRepository.findAll().size();

        // Create the Disponibilidade with an existing ID
        disponibilidade.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDisponibilidadeMockMvc.perform(post("/api/disponibilidades")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(disponibilidade)))
            .andExpect(status().isBadRequest());

        // Validate the Disponibilidade in the database
        List<Disponibilidade> disponibilidadeList = disponibilidadeRepository.findAll();
        assertThat(disponibilidadeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkHoraInicioIsRequired() throws Exception {
        int databaseSizeBeforeTest = disponibilidadeRepository.findAll().size();
        // set the field null
        disponibilidade.setHoraInicio(null);

        // Create the Disponibilidade, which fails.

        restDisponibilidadeMockMvc.perform(post("/api/disponibilidades")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(disponibilidade)))
            .andExpect(status().isBadRequest());

        List<Disponibilidade> disponibilidadeList = disponibilidadeRepository.findAll();
        assertThat(disponibilidadeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkHoraFimIsRequired() throws Exception {
        int databaseSizeBeforeTest = disponibilidadeRepository.findAll().size();
        // set the field null
        disponibilidade.setHoraFim(null);

        // Create the Disponibilidade, which fails.

        restDisponibilidadeMockMvc.perform(post("/api/disponibilidades")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(disponibilidade)))
            .andExpect(status().isBadRequest());

        List<Disponibilidade> disponibilidadeList = disponibilidadeRepository.findAll();
        assertThat(disponibilidadeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDiaSemanaIsRequired() throws Exception {
        int databaseSizeBeforeTest = disponibilidadeRepository.findAll().size();
        // set the field null
        disponibilidade.setDiaSemana(null);

        // Create the Disponibilidade, which fails.

        restDisponibilidadeMockMvc.perform(post("/api/disponibilidades")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(disponibilidade)))
            .andExpect(status().isBadRequest());

        List<Disponibilidade> disponibilidadeList = disponibilidadeRepository.findAll();
        assertThat(disponibilidadeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTurnoIsRequired() throws Exception {
        int databaseSizeBeforeTest = disponibilidadeRepository.findAll().size();
        // set the field null
        disponibilidade.setTurno(null);

        // Create the Disponibilidade, which fails.

        restDisponibilidadeMockMvc.perform(post("/api/disponibilidades")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(disponibilidade)))
            .andExpect(status().isBadRequest());

        List<Disponibilidade> disponibilidadeList = disponibilidadeRepository.findAll();
        assertThat(disponibilidadeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllDisponibilidades() throws Exception {
        // Initialize the database
        disponibilidadeRepository.saveAndFlush(disponibilidade);

        // Get all the disponibilidadeList
        restDisponibilidadeMockMvc.perform(get("/api/disponibilidades?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(disponibilidade.getId().intValue())))
            .andExpect(jsonPath("$.[*].horaInicio").value(hasItem(DEFAULT_HORA_INICIO.toString())))
            .andExpect(jsonPath("$.[*].horaFim").value(hasItem(DEFAULT_HORA_FIM.toString())))
            .andExpect(jsonPath("$.[*].diaSemana").value(hasItem(DEFAULT_DIA_SEMANA.toString())))
            .andExpect(jsonPath("$.[*].turno").value(hasItem(DEFAULT_TURNO.toString())));
    }
    
    @Test
    @Transactional
    public void getDisponibilidade() throws Exception {
        // Initialize the database
        disponibilidadeRepository.saveAndFlush(disponibilidade);

        // Get the disponibilidade
        restDisponibilidadeMockMvc.perform(get("/api/disponibilidades/{id}", disponibilidade.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(disponibilidade.getId().intValue()))
            .andExpect(jsonPath("$.horaInicio").value(DEFAULT_HORA_INICIO.toString()))
            .andExpect(jsonPath("$.horaFim").value(DEFAULT_HORA_FIM.toString()))
            .andExpect(jsonPath("$.diaSemana").value(DEFAULT_DIA_SEMANA.toString()))
            .andExpect(jsonPath("$.turno").value(DEFAULT_TURNO.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingDisponibilidade() throws Exception {
        // Get the disponibilidade
        restDisponibilidadeMockMvc.perform(get("/api/disponibilidades/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDisponibilidade() throws Exception {
        // Initialize the database
        disponibilidadeRepository.saveAndFlush(disponibilidade);

        int databaseSizeBeforeUpdate = disponibilidadeRepository.findAll().size();

        // Update the disponibilidade
        Disponibilidade updatedDisponibilidade = disponibilidadeRepository.findById(disponibilidade.getId()).get();
        // Disconnect from session so that the updates on updatedDisponibilidade are not directly saved in db
        em.detach(updatedDisponibilidade);
        updatedDisponibilidade
            .horaInicio(UPDATED_HORA_INICIO)
            .horaFim(UPDATED_HORA_FIM)
            .diaSemana(UPDATED_DIA_SEMANA)
            .turno(UPDATED_TURNO);

        restDisponibilidadeMockMvc.perform(put("/api/disponibilidades")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedDisponibilidade)))
            .andExpect(status().isOk());

        // Validate the Disponibilidade in the database
        List<Disponibilidade> disponibilidadeList = disponibilidadeRepository.findAll();
        assertThat(disponibilidadeList).hasSize(databaseSizeBeforeUpdate);
        Disponibilidade testDisponibilidade = disponibilidadeList.get(disponibilidadeList.size() - 1);
        assertThat(testDisponibilidade.getHoraInicio()).isEqualTo(UPDATED_HORA_INICIO);
        assertThat(testDisponibilidade.getHoraFim()).isEqualTo(UPDATED_HORA_FIM);
        assertThat(testDisponibilidade.getDiaSemana()).isEqualTo(UPDATED_DIA_SEMANA);
        assertThat(testDisponibilidade.getTurno()).isEqualTo(UPDATED_TURNO);
    }

    @Test
    @Transactional
    public void updateNonExistingDisponibilidade() throws Exception {
        int databaseSizeBeforeUpdate = disponibilidadeRepository.findAll().size();

        // Create the Disponibilidade

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDisponibilidadeMockMvc.perform(put("/api/disponibilidades")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(disponibilidade)))
            .andExpect(status().isBadRequest());

        // Validate the Disponibilidade in the database
        List<Disponibilidade> disponibilidadeList = disponibilidadeRepository.findAll();
        assertThat(disponibilidadeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteDisponibilidade() throws Exception {
        // Initialize the database
        disponibilidadeRepository.saveAndFlush(disponibilidade);

        int databaseSizeBeforeDelete = disponibilidadeRepository.findAll().size();

        // Delete the disponibilidade
        restDisponibilidadeMockMvc.perform(delete("/api/disponibilidades/{id}", disponibilidade.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Disponibilidade> disponibilidadeList = disponibilidadeRepository.findAll();
        assertThat(disponibilidadeList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Disponibilidade.class);
        Disponibilidade disponibilidade1 = new Disponibilidade();
        disponibilidade1.setId(1L);
        Disponibilidade disponibilidade2 = new Disponibilidade();
        disponibilidade2.setId(disponibilidade1.getId());
        assertThat(disponibilidade1).isEqualTo(disponibilidade2);
        disponibilidade2.setId(2L);
        assertThat(disponibilidade1).isNotEqualTo(disponibilidade2);
        disponibilidade1.setId(null);
        assertThat(disponibilidade1).isNotEqualTo(disponibilidade2);
    }
}
