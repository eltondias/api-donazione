package br.com.donazione.api.web.rest;

import br.com.donazione.api.ApidonazioneApp;

import br.com.donazione.api.domain.Habilidade;
import br.com.donazione.api.repository.HabilidadeRepository;
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
 * Test class for the HabilidadeResource REST controller.
 *
 * @see HabilidadeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ApidonazioneApp.class)
public class HabilidadeResourceIntTest {

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRICAO = "AAAAAAAAAA";
    private static final String UPDATED_DESCRICAO = "BBBBBBBBBB";

    @Autowired
    private HabilidadeRepository habilidadeRepository;

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

    private MockMvc restHabilidadeMockMvc;

    private Habilidade habilidade;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final HabilidadeResource habilidadeResource = new HabilidadeResource(habilidadeRepository);
        this.restHabilidadeMockMvc = MockMvcBuilders.standaloneSetup(habilidadeResource)
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
    public static Habilidade createEntity(EntityManager em) {
        Habilidade habilidade = new Habilidade()
            .nome(DEFAULT_NOME)
            .descricao(DEFAULT_DESCRICAO);
        return habilidade;
    }

    @Before
    public void initTest() {
        habilidade = createEntity(em);
    }

    @Test
    @Transactional
    public void createHabilidade() throws Exception {
        int databaseSizeBeforeCreate = habilidadeRepository.findAll().size();

        // Create the Habilidade
        restHabilidadeMockMvc.perform(post("/api/habilidades")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(habilidade)))
            .andExpect(status().isCreated());

        // Validate the Habilidade in the database
        List<Habilidade> habilidadeList = habilidadeRepository.findAll();
        assertThat(habilidadeList).hasSize(databaseSizeBeforeCreate + 1);
        Habilidade testHabilidade = habilidadeList.get(habilidadeList.size() - 1);
        assertThat(testHabilidade.getNome()).isEqualTo(DEFAULT_NOME);
        assertThat(testHabilidade.getDescricao()).isEqualTo(DEFAULT_DESCRICAO);
    }

    @Test
    @Transactional
    public void createHabilidadeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = habilidadeRepository.findAll().size();

        // Create the Habilidade with an existing ID
        habilidade.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restHabilidadeMockMvc.perform(post("/api/habilidades")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(habilidade)))
            .andExpect(status().isBadRequest());

        // Validate the Habilidade in the database
        List<Habilidade> habilidadeList = habilidadeRepository.findAll();
        assertThat(habilidadeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNomeIsRequired() throws Exception {
        int databaseSizeBeforeTest = habilidadeRepository.findAll().size();
        // set the field null
        habilidade.setNome(null);

        // Create the Habilidade, which fails.

        restHabilidadeMockMvc.perform(post("/api/habilidades")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(habilidade)))
            .andExpect(status().isBadRequest());

        List<Habilidade> habilidadeList = habilidadeRepository.findAll();
        assertThat(habilidadeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDescricaoIsRequired() throws Exception {
        int databaseSizeBeforeTest = habilidadeRepository.findAll().size();
        // set the field null
        habilidade.setDescricao(null);

        // Create the Habilidade, which fails.

        restHabilidadeMockMvc.perform(post("/api/habilidades")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(habilidade)))
            .andExpect(status().isBadRequest());

        List<Habilidade> habilidadeList = habilidadeRepository.findAll();
        assertThat(habilidadeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllHabilidades() throws Exception {
        // Initialize the database
        habilidadeRepository.saveAndFlush(habilidade);

        // Get all the habilidadeList
        restHabilidadeMockMvc.perform(get("/api/habilidades?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(habilidade.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME.toString())))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO.toString())));
    }
    
    @Test
    @Transactional
    public void getHabilidade() throws Exception {
        // Initialize the database
        habilidadeRepository.saveAndFlush(habilidade);

        // Get the habilidade
        restHabilidadeMockMvc.perform(get("/api/habilidades/{id}", habilidade.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(habilidade.getId().intValue()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME.toString()))
            .andExpect(jsonPath("$.descricao").value(DEFAULT_DESCRICAO.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingHabilidade() throws Exception {
        // Get the habilidade
        restHabilidadeMockMvc.perform(get("/api/habilidades/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateHabilidade() throws Exception {
        // Initialize the database
        habilidadeRepository.saveAndFlush(habilidade);

        int databaseSizeBeforeUpdate = habilidadeRepository.findAll().size();

        // Update the habilidade
        Habilidade updatedHabilidade = habilidadeRepository.findById(habilidade.getId()).get();
        // Disconnect from session so that the updates on updatedHabilidade are not directly saved in db
        em.detach(updatedHabilidade);
        updatedHabilidade
            .nome(UPDATED_NOME)
            .descricao(UPDATED_DESCRICAO);

        restHabilidadeMockMvc.perform(put("/api/habilidades")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedHabilidade)))
            .andExpect(status().isOk());

        // Validate the Habilidade in the database
        List<Habilidade> habilidadeList = habilidadeRepository.findAll();
        assertThat(habilidadeList).hasSize(databaseSizeBeforeUpdate);
        Habilidade testHabilidade = habilidadeList.get(habilidadeList.size() - 1);
        assertThat(testHabilidade.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testHabilidade.getDescricao()).isEqualTo(UPDATED_DESCRICAO);
    }

    @Test
    @Transactional
    public void updateNonExistingHabilidade() throws Exception {
        int databaseSizeBeforeUpdate = habilidadeRepository.findAll().size();

        // Create the Habilidade

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHabilidadeMockMvc.perform(put("/api/habilidades")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(habilidade)))
            .andExpect(status().isBadRequest());

        // Validate the Habilidade in the database
        List<Habilidade> habilidadeList = habilidadeRepository.findAll();
        assertThat(habilidadeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteHabilidade() throws Exception {
        // Initialize the database
        habilidadeRepository.saveAndFlush(habilidade);

        int databaseSizeBeforeDelete = habilidadeRepository.findAll().size();

        // Delete the habilidade
        restHabilidadeMockMvc.perform(delete("/api/habilidades/{id}", habilidade.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Habilidade> habilidadeList = habilidadeRepository.findAll();
        assertThat(habilidadeList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Habilidade.class);
        Habilidade habilidade1 = new Habilidade();
        habilidade1.setId(1L);
        Habilidade habilidade2 = new Habilidade();
        habilidade2.setId(habilidade1.getId());
        assertThat(habilidade1).isEqualTo(habilidade2);
        habilidade2.setId(2L);
        assertThat(habilidade1).isNotEqualTo(habilidade2);
        habilidade1.setId(null);
        assertThat(habilidade1).isNotEqualTo(habilidade2);
    }
}
