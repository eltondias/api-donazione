package br.com.donazione.api.web.rest;

import br.com.donazione.api.ApidonazioneApp;

import br.com.donazione.api.domain.Voluntario;
import br.com.donazione.api.repository.VoluntarioRepository;
import br.com.donazione.api.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
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
import java.util.ArrayList;
import java.util.List;


import static br.com.donazione.api.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.com.donazione.api.domain.enumeration.EstadoVoluntarioEnum;
/**
 * Test class for the VoluntarioResource REST controller.
 *
 * @see VoluntarioResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ApidonazioneApp.class)
public class VoluntarioResourceIntTest {

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    private static final String DEFAULT_URL_FOTO_PERFIL = "AAAAAAAAAA";
    private static final String UPDATED_URL_FOTO_PERFIL = "BBBBBBBBBB";

    private static final String DEFAULT_CPF = "AAAAAAAAAA";
    private static final String UPDATED_CPF = "BBBBBBBBBB";

    private static final String DEFAULT_LOGIN = "AAAAAAAAAA";
    private static final String UPDATED_LOGIN = "BBBBBBBBBB";

    private static final String DEFAULT_SENHA = "AAAAAAAAAA";
    private static final String UPDATED_SENHA = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_ADMIN = false;
    private static final Boolean UPDATED_IS_ADMIN = true;

    private static final Instant DEFAULT_DATA_NASCIMENTO = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATA_NASCIMENTO = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_DATA_CADASTRO = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATA_CADASTRO = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final EstadoVoluntarioEnum DEFAULT_SITUACAO = EstadoVoluntarioEnum.ATIVO;
    private static final EstadoVoluntarioEnum UPDATED_SITUACAO = EstadoVoluntarioEnum.INATIVO;

    @Autowired
    private VoluntarioRepository voluntarioRepository;

    @Mock
    private VoluntarioRepository voluntarioRepositoryMock;

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

    private MockMvc restVoluntarioMockMvc;

    private Voluntario voluntario;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final VoluntarioResource voluntarioResource = new VoluntarioResource(voluntarioRepository);
        this.restVoluntarioMockMvc = MockMvcBuilders.standaloneSetup(voluntarioResource)
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
    public static Voluntario createEntity(EntityManager em) {
        Voluntario voluntario = new Voluntario()
            .nome(DEFAULT_NOME)
            .urlFotoPerfil(DEFAULT_URL_FOTO_PERFIL)
            .cpf(DEFAULT_CPF)
            .login(DEFAULT_LOGIN)
            .senha(DEFAULT_SENHA)
            .isAdmin(DEFAULT_IS_ADMIN)
            .dataNascimento(DEFAULT_DATA_NASCIMENTO)
            .dataCadastro(DEFAULT_DATA_CADASTRO)
            .situacao(DEFAULT_SITUACAO);
        return voluntario;
    }

    @Before
    public void initTest() {
        voluntario = createEntity(em);
    }

    @Test
    @Transactional
    public void createVoluntario() throws Exception {
        int databaseSizeBeforeCreate = voluntarioRepository.findAll().size();

        // Create the Voluntario
        restVoluntarioMockMvc.perform(post("/api/voluntarios")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(voluntario)))
            .andExpect(status().isCreated());

        // Validate the Voluntario in the database
        List<Voluntario> voluntarioList = voluntarioRepository.findAll();
        assertThat(voluntarioList).hasSize(databaseSizeBeforeCreate + 1);
        Voluntario testVoluntario = voluntarioList.get(voluntarioList.size() - 1);
        assertThat(testVoluntario.getNome()).isEqualTo(DEFAULT_NOME);
        assertThat(testVoluntario.getUrlFotoPerfil()).isEqualTo(DEFAULT_URL_FOTO_PERFIL);
        assertThat(testVoluntario.getCpf()).isEqualTo(DEFAULT_CPF);
        assertThat(testVoluntario.getLogin()).isEqualTo(DEFAULT_LOGIN);
        assertThat(testVoluntario.getSenha()).isEqualTo(DEFAULT_SENHA);
        assertThat(testVoluntario.isIsAdmin()).isEqualTo(DEFAULT_IS_ADMIN);
        assertThat(testVoluntario.getDataNascimento()).isEqualTo(DEFAULT_DATA_NASCIMENTO);
        assertThat(testVoluntario.getDataCadastro()).isEqualTo(DEFAULT_DATA_CADASTRO);
        assertThat(testVoluntario.getSituacao()).isEqualTo(DEFAULT_SITUACAO);
    }

    @Test
    @Transactional
    public void createVoluntarioWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = voluntarioRepository.findAll().size();

        // Create the Voluntario with an existing ID
        voluntario.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restVoluntarioMockMvc.perform(post("/api/voluntarios")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(voluntario)))
            .andExpect(status().isBadRequest());

        // Validate the Voluntario in the database
        List<Voluntario> voluntarioList = voluntarioRepository.findAll();
        assertThat(voluntarioList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNomeIsRequired() throws Exception {
        int databaseSizeBeforeTest = voluntarioRepository.findAll().size();
        // set the field null
        voluntario.setNome(null);

        // Create the Voluntario, which fails.

        restVoluntarioMockMvc.perform(post("/api/voluntarios")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(voluntario)))
            .andExpect(status().isBadRequest());

        List<Voluntario> voluntarioList = voluntarioRepository.findAll();
        assertThat(voluntarioList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCpfIsRequired() throws Exception {
        int databaseSizeBeforeTest = voluntarioRepository.findAll().size();
        // set the field null
        voluntario.setCpf(null);

        // Create the Voluntario, which fails.

        restVoluntarioMockMvc.perform(post("/api/voluntarios")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(voluntario)))
            .andExpect(status().isBadRequest());

        List<Voluntario> voluntarioList = voluntarioRepository.findAll();
        assertThat(voluntarioList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLoginIsRequired() throws Exception {
        int databaseSizeBeforeTest = voluntarioRepository.findAll().size();
        // set the field null
        voluntario.setLogin(null);

        // Create the Voluntario, which fails.

        restVoluntarioMockMvc.perform(post("/api/voluntarios")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(voluntario)))
            .andExpect(status().isBadRequest());

        List<Voluntario> voluntarioList = voluntarioRepository.findAll();
        assertThat(voluntarioList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSenhaIsRequired() throws Exception {
        int databaseSizeBeforeTest = voluntarioRepository.findAll().size();
        // set the field null
        voluntario.setSenha(null);

        // Create the Voluntario, which fails.

        restVoluntarioMockMvc.perform(post("/api/voluntarios")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(voluntario)))
            .andExpect(status().isBadRequest());

        List<Voluntario> voluntarioList = voluntarioRepository.findAll();
        assertThat(voluntarioList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkIsAdminIsRequired() throws Exception {
        int databaseSizeBeforeTest = voluntarioRepository.findAll().size();
        // set the field null
        voluntario.setIsAdmin(null);

        // Create the Voluntario, which fails.

        restVoluntarioMockMvc.perform(post("/api/voluntarios")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(voluntario)))
            .andExpect(status().isBadRequest());

        List<Voluntario> voluntarioList = voluntarioRepository.findAll();
        assertThat(voluntarioList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSituacaoIsRequired() throws Exception {
        int databaseSizeBeforeTest = voluntarioRepository.findAll().size();
        // set the field null
        voluntario.setSituacao(null);

        // Create the Voluntario, which fails.

        restVoluntarioMockMvc.perform(post("/api/voluntarios")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(voluntario)))
            .andExpect(status().isBadRequest());

        List<Voluntario> voluntarioList = voluntarioRepository.findAll();
        assertThat(voluntarioList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllVoluntarios() throws Exception {
        // Initialize the database
        voluntarioRepository.saveAndFlush(voluntario);

        // Get all the voluntarioList
        restVoluntarioMockMvc.perform(get("/api/voluntarios?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(voluntario.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME.toString())))
            .andExpect(jsonPath("$.[*].urlFotoPerfil").value(hasItem(DEFAULT_URL_FOTO_PERFIL.toString())))
            .andExpect(jsonPath("$.[*].cpf").value(hasItem(DEFAULT_CPF.toString())))
            .andExpect(jsonPath("$.[*].login").value(hasItem(DEFAULT_LOGIN.toString())))
            .andExpect(jsonPath("$.[*].senha").value(hasItem(DEFAULT_SENHA.toString())))
            .andExpect(jsonPath("$.[*].isAdmin").value(hasItem(DEFAULT_IS_ADMIN.booleanValue())))
            .andExpect(jsonPath("$.[*].dataNascimento").value(hasItem(DEFAULT_DATA_NASCIMENTO.toString())))
            .andExpect(jsonPath("$.[*].dataCadastro").value(hasItem(DEFAULT_DATA_CADASTRO.toString())))
            .andExpect(jsonPath("$.[*].situacao").value(hasItem(DEFAULT_SITUACAO.toString())));
    }
    
    @SuppressWarnings({"unchecked"})
    public void getAllVoluntariosWithEagerRelationshipsIsEnabled() throws Exception {
        VoluntarioResource voluntarioResource = new VoluntarioResource(voluntarioRepositoryMock);
        when(voluntarioRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        MockMvc restVoluntarioMockMvc = MockMvcBuilders.standaloneSetup(voluntarioResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restVoluntarioMockMvc.perform(get("/api/voluntarios?eagerload=true"))
        .andExpect(status().isOk());

        verify(voluntarioRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({"unchecked"})
    public void getAllVoluntariosWithEagerRelationshipsIsNotEnabled() throws Exception {
        VoluntarioResource voluntarioResource = new VoluntarioResource(voluntarioRepositoryMock);
            when(voluntarioRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));
            MockMvc restVoluntarioMockMvc = MockMvcBuilders.standaloneSetup(voluntarioResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restVoluntarioMockMvc.perform(get("/api/voluntarios?eagerload=true"))
        .andExpect(status().isOk());

            verify(voluntarioRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    public void getVoluntario() throws Exception {
        // Initialize the database
        voluntarioRepository.saveAndFlush(voluntario);

        // Get the voluntario
        restVoluntarioMockMvc.perform(get("/api/voluntarios/{id}", voluntario.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(voluntario.getId().intValue()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME.toString()))
            .andExpect(jsonPath("$.urlFotoPerfil").value(DEFAULT_URL_FOTO_PERFIL.toString()))
            .andExpect(jsonPath("$.cpf").value(DEFAULT_CPF.toString()))
            .andExpect(jsonPath("$.login").value(DEFAULT_LOGIN.toString()))
            .andExpect(jsonPath("$.senha").value(DEFAULT_SENHA.toString()))
            .andExpect(jsonPath("$.isAdmin").value(DEFAULT_IS_ADMIN.booleanValue()))
            .andExpect(jsonPath("$.dataNascimento").value(DEFAULT_DATA_NASCIMENTO.toString()))
            .andExpect(jsonPath("$.dataCadastro").value(DEFAULT_DATA_CADASTRO.toString()))
            .andExpect(jsonPath("$.situacao").value(DEFAULT_SITUACAO.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingVoluntario() throws Exception {
        // Get the voluntario
        restVoluntarioMockMvc.perform(get("/api/voluntarios/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateVoluntario() throws Exception {
        // Initialize the database
        voluntarioRepository.saveAndFlush(voluntario);

        int databaseSizeBeforeUpdate = voluntarioRepository.findAll().size();

        // Update the voluntario
        Voluntario updatedVoluntario = voluntarioRepository.findById(voluntario.getId()).get();
        // Disconnect from session so that the updates on updatedVoluntario are not directly saved in db
        em.detach(updatedVoluntario);
        updatedVoluntario
            .nome(UPDATED_NOME)
            .urlFotoPerfil(UPDATED_URL_FOTO_PERFIL)
            .cpf(UPDATED_CPF)
            .login(UPDATED_LOGIN)
            .senha(UPDATED_SENHA)
            .isAdmin(UPDATED_IS_ADMIN)
            .dataNascimento(UPDATED_DATA_NASCIMENTO)
            .dataCadastro(UPDATED_DATA_CADASTRO)
            .situacao(UPDATED_SITUACAO);

        restVoluntarioMockMvc.perform(put("/api/voluntarios")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedVoluntario)))
            .andExpect(status().isOk());

        // Validate the Voluntario in the database
        List<Voluntario> voluntarioList = voluntarioRepository.findAll();
        assertThat(voluntarioList).hasSize(databaseSizeBeforeUpdate);
        Voluntario testVoluntario = voluntarioList.get(voluntarioList.size() - 1);
        assertThat(testVoluntario.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testVoluntario.getUrlFotoPerfil()).isEqualTo(UPDATED_URL_FOTO_PERFIL);
        assertThat(testVoluntario.getCpf()).isEqualTo(UPDATED_CPF);
        assertThat(testVoluntario.getLogin()).isEqualTo(UPDATED_LOGIN);
        assertThat(testVoluntario.getSenha()).isEqualTo(UPDATED_SENHA);
        assertThat(testVoluntario.isIsAdmin()).isEqualTo(UPDATED_IS_ADMIN);
        assertThat(testVoluntario.getDataNascimento()).isEqualTo(UPDATED_DATA_NASCIMENTO);
        assertThat(testVoluntario.getDataCadastro()).isEqualTo(UPDATED_DATA_CADASTRO);
        assertThat(testVoluntario.getSituacao()).isEqualTo(UPDATED_SITUACAO);
    }

    @Test
    @Transactional
    public void updateNonExistingVoluntario() throws Exception {
        int databaseSizeBeforeUpdate = voluntarioRepository.findAll().size();

        // Create the Voluntario

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVoluntarioMockMvc.perform(put("/api/voluntarios")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(voluntario)))
            .andExpect(status().isBadRequest());

        // Validate the Voluntario in the database
        List<Voluntario> voluntarioList = voluntarioRepository.findAll();
        assertThat(voluntarioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteVoluntario() throws Exception {
        // Initialize the database
        voluntarioRepository.saveAndFlush(voluntario);

        int databaseSizeBeforeDelete = voluntarioRepository.findAll().size();

        // Delete the voluntario
        restVoluntarioMockMvc.perform(delete("/api/voluntarios/{id}", voluntario.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Voluntario> voluntarioList = voluntarioRepository.findAll();
        assertThat(voluntarioList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Voluntario.class);
        Voluntario voluntario1 = new Voluntario();
        voluntario1.setId(1L);
        Voluntario voluntario2 = new Voluntario();
        voluntario2.setId(voluntario1.getId());
        assertThat(voluntario1).isEqualTo(voluntario2);
        voluntario2.setId(2L);
        assertThat(voluntario1).isNotEqualTo(voluntario2);
        voluntario1.setId(null);
        assertThat(voluntario1).isNotEqualTo(voluntario2);
    }
}
