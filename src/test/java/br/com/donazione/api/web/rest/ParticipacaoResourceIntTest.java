package br.com.donazione.api.web.rest;

import br.com.donazione.api.ApidonazioneApp;

import br.com.donazione.api.domain.Participacao;
import br.com.donazione.api.domain.Voluntario;
import br.com.donazione.api.domain.Acao;
import br.com.donazione.api.repository.ParticipacaoRepository;
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
 * Test class for the ParticipacaoResource REST controller.
 *
 * @see ParticipacaoResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ApidonazioneApp.class)
public class ParticipacaoResourceIntTest {

    private static final Instant DEFAULT_DATA_HORA_EMISSAO_CERTIFICADO = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATA_HORA_EMISSAO_CERTIFICADO = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Double DEFAULT_CARGA_HORARIA = 1D;
    private static final Double UPDATED_CARGA_HORARIA = 2D;

    @Autowired
    private ParticipacaoRepository participacaoRepository;

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

    private MockMvc restParticipacaoMockMvc;

    private Participacao participacao;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ParticipacaoResource participacaoResource = new ParticipacaoResource(participacaoRepository);
        this.restParticipacaoMockMvc = MockMvcBuilders.standaloneSetup(participacaoResource)
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
    public static Participacao createEntity(EntityManager em) {
        Participacao participacao = new Participacao()
            .dataHoraEmissaoCertificado(DEFAULT_DATA_HORA_EMISSAO_CERTIFICADO)
            .cargaHoraria(DEFAULT_CARGA_HORARIA);
        // Add required entity
        Voluntario voluntario = VoluntarioResourceIntTest.createEntity(em);
        em.persist(voluntario);
        em.flush();
        participacao.setVoluntario(voluntario);
        // Add required entity
        Acao acao = AcaoResourceIntTest.createEntity(em);
        em.persist(acao);
        em.flush();
        participacao.setAcao(acao);
        return participacao;
    }

    @Before
    public void initTest() {
        participacao = createEntity(em);
    }

    @Test
    @Transactional
    public void createParticipacao() throws Exception {
        int databaseSizeBeforeCreate = participacaoRepository.findAll().size();

        // Create the Participacao
        restParticipacaoMockMvc.perform(post("/api/participacaos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(participacao)))
            .andExpect(status().isCreated());

        // Validate the Participacao in the database
        List<Participacao> participacaoList = participacaoRepository.findAll();
        assertThat(participacaoList).hasSize(databaseSizeBeforeCreate + 1);
        Participacao testParticipacao = participacaoList.get(participacaoList.size() - 1);
        assertThat(testParticipacao.getDataHoraEmissaoCertificado()).isEqualTo(DEFAULT_DATA_HORA_EMISSAO_CERTIFICADO);
        assertThat(testParticipacao.getCargaHoraria()).isEqualTo(DEFAULT_CARGA_HORARIA);
    }

    @Test
    @Transactional
    public void createParticipacaoWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = participacaoRepository.findAll().size();

        // Create the Participacao with an existing ID
        participacao.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restParticipacaoMockMvc.perform(post("/api/participacaos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(participacao)))
            .andExpect(status().isBadRequest());

        // Validate the Participacao in the database
        List<Participacao> participacaoList = participacaoRepository.findAll();
        assertThat(participacaoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllParticipacaos() throws Exception {
        // Initialize the database
        participacaoRepository.saveAndFlush(participacao);

        // Get all the participacaoList
        restParticipacaoMockMvc.perform(get("/api/participacaos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(participacao.getId().intValue())))
            .andExpect(jsonPath("$.[*].dataHoraEmissaoCertificado").value(hasItem(DEFAULT_DATA_HORA_EMISSAO_CERTIFICADO.toString())))
            .andExpect(jsonPath("$.[*].cargaHoraria").value(hasItem(DEFAULT_CARGA_HORARIA.doubleValue())));
    }
    
    @Test
    @Transactional
    public void getParticipacao() throws Exception {
        // Initialize the database
        participacaoRepository.saveAndFlush(participacao);

        // Get the participacao
        restParticipacaoMockMvc.perform(get("/api/participacaos/{id}", participacao.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(participacao.getId().intValue()))
            .andExpect(jsonPath("$.dataHoraEmissaoCertificado").value(DEFAULT_DATA_HORA_EMISSAO_CERTIFICADO.toString()))
            .andExpect(jsonPath("$.cargaHoraria").value(DEFAULT_CARGA_HORARIA.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingParticipacao() throws Exception {
        // Get the participacao
        restParticipacaoMockMvc.perform(get("/api/participacaos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateParticipacao() throws Exception {
        // Initialize the database
        participacaoRepository.saveAndFlush(participacao);

        int databaseSizeBeforeUpdate = participacaoRepository.findAll().size();

        // Update the participacao
        Participacao updatedParticipacao = participacaoRepository.findById(participacao.getId()).get();
        // Disconnect from session so that the updates on updatedParticipacao are not directly saved in db
        em.detach(updatedParticipacao);
        updatedParticipacao
            .dataHoraEmissaoCertificado(UPDATED_DATA_HORA_EMISSAO_CERTIFICADO)
            .cargaHoraria(UPDATED_CARGA_HORARIA);

        restParticipacaoMockMvc.perform(put("/api/participacaos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedParticipacao)))
            .andExpect(status().isOk());

        // Validate the Participacao in the database
        List<Participacao> participacaoList = participacaoRepository.findAll();
        assertThat(participacaoList).hasSize(databaseSizeBeforeUpdate);
        Participacao testParticipacao = participacaoList.get(participacaoList.size() - 1);
        assertThat(testParticipacao.getDataHoraEmissaoCertificado()).isEqualTo(UPDATED_DATA_HORA_EMISSAO_CERTIFICADO);
        assertThat(testParticipacao.getCargaHoraria()).isEqualTo(UPDATED_CARGA_HORARIA);
    }

    @Test
    @Transactional
    public void updateNonExistingParticipacao() throws Exception {
        int databaseSizeBeforeUpdate = participacaoRepository.findAll().size();

        // Create the Participacao

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restParticipacaoMockMvc.perform(put("/api/participacaos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(participacao)))
            .andExpect(status().isBadRequest());

        // Validate the Participacao in the database
        List<Participacao> participacaoList = participacaoRepository.findAll();
        assertThat(participacaoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteParticipacao() throws Exception {
        // Initialize the database
        participacaoRepository.saveAndFlush(participacao);

        int databaseSizeBeforeDelete = participacaoRepository.findAll().size();

        // Delete the participacao
        restParticipacaoMockMvc.perform(delete("/api/participacaos/{id}", participacao.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Participacao> participacaoList = participacaoRepository.findAll();
        assertThat(participacaoList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Participacao.class);
        Participacao participacao1 = new Participacao();
        participacao1.setId(1L);
        Participacao participacao2 = new Participacao();
        participacao2.setId(participacao1.getId());
        assertThat(participacao1).isEqualTo(participacao2);
        participacao2.setId(2L);
        assertThat(participacao1).isNotEqualTo(participacao2);
        participacao1.setId(null);
        assertThat(participacao1).isNotEqualTo(participacao2);
    }
}
