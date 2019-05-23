package br.com.donazione.api.web.rest;

import br.com.donazione.api.ApidonazioneApp;

import br.com.donazione.api.domain.RedeSocial;
import br.com.donazione.api.domain.Voluntario;
import br.com.donazione.api.repository.RedeSocialRepository;
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

import br.com.donazione.api.domain.enumeration.TipoRedeSocial;
/**
 * Test class for the RedeSocialResource REST controller.
 *
 * @see RedeSocialResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ApidonazioneApp.class)
public class RedeSocialResourceIntTest {

    private static final TipoRedeSocial DEFAULT_TIPO_REDE_SOCIAL = TipoRedeSocial.FACEBOOK;
    private static final TipoRedeSocial UPDATED_TIPO_REDE_SOCIAL = TipoRedeSocial.TWITER;

    private static final String DEFAULT_URL = "AAAAAAAAAA";
    private static final String UPDATED_URL = "BBBBBBBBBB";

    @Autowired
    private RedeSocialRepository redeSocialRepository;

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

    private MockMvc restRedeSocialMockMvc;

    private RedeSocial redeSocial;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final RedeSocialResource redeSocialResource = new RedeSocialResource(redeSocialRepository);
        this.restRedeSocialMockMvc = MockMvcBuilders.standaloneSetup(redeSocialResource)
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
    public static RedeSocial createEntity(EntityManager em) {
        RedeSocial redeSocial = new RedeSocial()
            .tipoRedeSocial(DEFAULT_TIPO_REDE_SOCIAL)
            .url(DEFAULT_URL);
        // Add required entity
        Voluntario voluntario = VoluntarioResourceIntTest.createEntity(em);
        em.persist(voluntario);
        em.flush();
        redeSocial.setVoluntario(voluntario);
        return redeSocial;
    }

    @Before
    public void initTest() {
        redeSocial = createEntity(em);
    }

    @Test
    @Transactional
    public void createRedeSocial() throws Exception {
        int databaseSizeBeforeCreate = redeSocialRepository.findAll().size();

        // Create the RedeSocial
        restRedeSocialMockMvc.perform(post("/api/rede-socials")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(redeSocial)))
            .andExpect(status().isCreated());

        // Validate the RedeSocial in the database
        List<RedeSocial> redeSocialList = redeSocialRepository.findAll();
        assertThat(redeSocialList).hasSize(databaseSizeBeforeCreate + 1);
        RedeSocial testRedeSocial = redeSocialList.get(redeSocialList.size() - 1);
        assertThat(testRedeSocial.getTipoRedeSocial()).isEqualTo(DEFAULT_TIPO_REDE_SOCIAL);
        assertThat(testRedeSocial.getUrl()).isEqualTo(DEFAULT_URL);
    }

    @Test
    @Transactional
    public void createRedeSocialWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = redeSocialRepository.findAll().size();

        // Create the RedeSocial with an existing ID
        redeSocial.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restRedeSocialMockMvc.perform(post("/api/rede-socials")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(redeSocial)))
            .andExpect(status().isBadRequest());

        // Validate the RedeSocial in the database
        List<RedeSocial> redeSocialList = redeSocialRepository.findAll();
        assertThat(redeSocialList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllRedeSocials() throws Exception {
        // Initialize the database
        redeSocialRepository.saveAndFlush(redeSocial);

        // Get all the redeSocialList
        restRedeSocialMockMvc.perform(get("/api/rede-socials?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(redeSocial.getId().intValue())))
            .andExpect(jsonPath("$.[*].tipoRedeSocial").value(hasItem(DEFAULT_TIPO_REDE_SOCIAL.toString())))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL.toString())));
    }
    
    @Test
    @Transactional
    public void getRedeSocial() throws Exception {
        // Initialize the database
        redeSocialRepository.saveAndFlush(redeSocial);

        // Get the redeSocial
        restRedeSocialMockMvc.perform(get("/api/rede-socials/{id}", redeSocial.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(redeSocial.getId().intValue()))
            .andExpect(jsonPath("$.tipoRedeSocial").value(DEFAULT_TIPO_REDE_SOCIAL.toString()))
            .andExpect(jsonPath("$.url").value(DEFAULT_URL.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingRedeSocial() throws Exception {
        // Get the redeSocial
        restRedeSocialMockMvc.perform(get("/api/rede-socials/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRedeSocial() throws Exception {
        // Initialize the database
        redeSocialRepository.saveAndFlush(redeSocial);

        int databaseSizeBeforeUpdate = redeSocialRepository.findAll().size();

        // Update the redeSocial
        RedeSocial updatedRedeSocial = redeSocialRepository.findById(redeSocial.getId()).get();
        // Disconnect from session so that the updates on updatedRedeSocial are not directly saved in db
        em.detach(updatedRedeSocial);
        updatedRedeSocial
            .tipoRedeSocial(UPDATED_TIPO_REDE_SOCIAL)
            .url(UPDATED_URL);

        restRedeSocialMockMvc.perform(put("/api/rede-socials")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedRedeSocial)))
            .andExpect(status().isOk());

        // Validate the RedeSocial in the database
        List<RedeSocial> redeSocialList = redeSocialRepository.findAll();
        assertThat(redeSocialList).hasSize(databaseSizeBeforeUpdate);
        RedeSocial testRedeSocial = redeSocialList.get(redeSocialList.size() - 1);
        assertThat(testRedeSocial.getTipoRedeSocial()).isEqualTo(UPDATED_TIPO_REDE_SOCIAL);
        assertThat(testRedeSocial.getUrl()).isEqualTo(UPDATED_URL);
    }

    @Test
    @Transactional
    public void updateNonExistingRedeSocial() throws Exception {
        int databaseSizeBeforeUpdate = redeSocialRepository.findAll().size();

        // Create the RedeSocial

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRedeSocialMockMvc.perform(put("/api/rede-socials")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(redeSocial)))
            .andExpect(status().isBadRequest());

        // Validate the RedeSocial in the database
        List<RedeSocial> redeSocialList = redeSocialRepository.findAll();
        assertThat(redeSocialList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteRedeSocial() throws Exception {
        // Initialize the database
        redeSocialRepository.saveAndFlush(redeSocial);

        int databaseSizeBeforeDelete = redeSocialRepository.findAll().size();

        // Delete the redeSocial
        restRedeSocialMockMvc.perform(delete("/api/rede-socials/{id}", redeSocial.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<RedeSocial> redeSocialList = redeSocialRepository.findAll();
        assertThat(redeSocialList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RedeSocial.class);
        RedeSocial redeSocial1 = new RedeSocial();
        redeSocial1.setId(1L);
        RedeSocial redeSocial2 = new RedeSocial();
        redeSocial2.setId(redeSocial1.getId());
        assertThat(redeSocial1).isEqualTo(redeSocial2);
        redeSocial2.setId(2L);
        assertThat(redeSocial1).isNotEqualTo(redeSocial2);
        redeSocial1.setId(null);
        assertThat(redeSocial1).isNotEqualTo(redeSocial2);
    }
}
