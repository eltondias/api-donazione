package br.com.donazione.api.web.rest;

import br.com.donazione.api.ApidonazioneApp;

import br.com.donazione.api.domain.FormaPagamento;
import br.com.donazione.api.domain.Doacao;
import br.com.donazione.api.repository.FormaPagamentoRepository;
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
 * Test class for the FormaPagamentoResource REST controller.
 *
 * @see FormaPagamentoResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ApidonazioneApp.class)
public class FormaPagamentoResourceIntTest {

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    private static final Instant DEFAULT_DESCRICAO = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DESCRICAO = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private FormaPagamentoRepository formaPagamentoRepository;

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

    private MockMvc restFormaPagamentoMockMvc;

    private FormaPagamento formaPagamento;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final FormaPagamentoResource formaPagamentoResource = new FormaPagamentoResource(formaPagamentoRepository);
        this.restFormaPagamentoMockMvc = MockMvcBuilders.standaloneSetup(formaPagamentoResource)
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
    public static FormaPagamento createEntity(EntityManager em) {
        FormaPagamento formaPagamento = new FormaPagamento()
            .nome(DEFAULT_NOME)
            .descricao(DEFAULT_DESCRICAO);
        // Add required entity
        Doacao doacao = DoacaoResourceIntTest.createEntity(em);
        em.persist(doacao);
        em.flush();
        formaPagamento.setDoacao(doacao);
        return formaPagamento;
    }

    @Before
    public void initTest() {
        formaPagamento = createEntity(em);
    }

    @Test
    @Transactional
    public void createFormaPagamento() throws Exception {
        int databaseSizeBeforeCreate = formaPagamentoRepository.findAll().size();

        // Create the FormaPagamento
        restFormaPagamentoMockMvc.perform(post("/api/forma-pagamentos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(formaPagamento)))
            .andExpect(status().isCreated());

        // Validate the FormaPagamento in the database
        List<FormaPagamento> formaPagamentoList = formaPagamentoRepository.findAll();
        assertThat(formaPagamentoList).hasSize(databaseSizeBeforeCreate + 1);
        FormaPagamento testFormaPagamento = formaPagamentoList.get(formaPagamentoList.size() - 1);
        assertThat(testFormaPagamento.getNome()).isEqualTo(DEFAULT_NOME);
        assertThat(testFormaPagamento.getDescricao()).isEqualTo(DEFAULT_DESCRICAO);
    }

    @Test
    @Transactional
    public void createFormaPagamentoWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = formaPagamentoRepository.findAll().size();

        // Create the FormaPagamento with an existing ID
        formaPagamento.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restFormaPagamentoMockMvc.perform(post("/api/forma-pagamentos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(formaPagamento)))
            .andExpect(status().isBadRequest());

        // Validate the FormaPagamento in the database
        List<FormaPagamento> formaPagamentoList = formaPagamentoRepository.findAll();
        assertThat(formaPagamentoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNomeIsRequired() throws Exception {
        int databaseSizeBeforeTest = formaPagamentoRepository.findAll().size();
        // set the field null
        formaPagamento.setNome(null);

        // Create the FormaPagamento, which fails.

        restFormaPagamentoMockMvc.perform(post("/api/forma-pagamentos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(formaPagamento)))
            .andExpect(status().isBadRequest());

        List<FormaPagamento> formaPagamentoList = formaPagamentoRepository.findAll();
        assertThat(formaPagamentoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllFormaPagamentos() throws Exception {
        // Initialize the database
        formaPagamentoRepository.saveAndFlush(formaPagamento);

        // Get all the formaPagamentoList
        restFormaPagamentoMockMvc.perform(get("/api/forma-pagamentos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(formaPagamento.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME.toString())))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO.toString())));
    }
    
    @Test
    @Transactional
    public void getFormaPagamento() throws Exception {
        // Initialize the database
        formaPagamentoRepository.saveAndFlush(formaPagamento);

        // Get the formaPagamento
        restFormaPagamentoMockMvc.perform(get("/api/forma-pagamentos/{id}", formaPagamento.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(formaPagamento.getId().intValue()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME.toString()))
            .andExpect(jsonPath("$.descricao").value(DEFAULT_DESCRICAO.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingFormaPagamento() throws Exception {
        // Get the formaPagamento
        restFormaPagamentoMockMvc.perform(get("/api/forma-pagamentos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFormaPagamento() throws Exception {
        // Initialize the database
        formaPagamentoRepository.saveAndFlush(formaPagamento);

        int databaseSizeBeforeUpdate = formaPagamentoRepository.findAll().size();

        // Update the formaPagamento
        FormaPagamento updatedFormaPagamento = formaPagamentoRepository.findById(formaPagamento.getId()).get();
        // Disconnect from session so that the updates on updatedFormaPagamento are not directly saved in db
        em.detach(updatedFormaPagamento);
        updatedFormaPagamento
            .nome(UPDATED_NOME)
            .descricao(UPDATED_DESCRICAO);

        restFormaPagamentoMockMvc.perform(put("/api/forma-pagamentos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedFormaPagamento)))
            .andExpect(status().isOk());

        // Validate the FormaPagamento in the database
        List<FormaPagamento> formaPagamentoList = formaPagamentoRepository.findAll();
        assertThat(formaPagamentoList).hasSize(databaseSizeBeforeUpdate);
        FormaPagamento testFormaPagamento = formaPagamentoList.get(formaPagamentoList.size() - 1);
        assertThat(testFormaPagamento.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testFormaPagamento.getDescricao()).isEqualTo(UPDATED_DESCRICAO);
    }

    @Test
    @Transactional
    public void updateNonExistingFormaPagamento() throws Exception {
        int databaseSizeBeforeUpdate = formaPagamentoRepository.findAll().size();

        // Create the FormaPagamento

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFormaPagamentoMockMvc.perform(put("/api/forma-pagamentos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(formaPagamento)))
            .andExpect(status().isBadRequest());

        // Validate the FormaPagamento in the database
        List<FormaPagamento> formaPagamentoList = formaPagamentoRepository.findAll();
        assertThat(formaPagamentoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteFormaPagamento() throws Exception {
        // Initialize the database
        formaPagamentoRepository.saveAndFlush(formaPagamento);

        int databaseSizeBeforeDelete = formaPagamentoRepository.findAll().size();

        // Delete the formaPagamento
        restFormaPagamentoMockMvc.perform(delete("/api/forma-pagamentos/{id}", formaPagamento.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<FormaPagamento> formaPagamentoList = formaPagamentoRepository.findAll();
        assertThat(formaPagamentoList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FormaPagamento.class);
        FormaPagamento formaPagamento1 = new FormaPagamento();
        formaPagamento1.setId(1L);
        FormaPagamento formaPagamento2 = new FormaPagamento();
        formaPagamento2.setId(formaPagamento1.getId());
        assertThat(formaPagamento1).isEqualTo(formaPagamento2);
        formaPagamento2.setId(2L);
        assertThat(formaPagamento1).isNotEqualTo(formaPagamento2);
        formaPagamento1.setId(null);
        assertThat(formaPagamento1).isNotEqualTo(formaPagamento2);
    }
}
