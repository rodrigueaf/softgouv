package com.gt.softgouv.web.rest;

import com.gt.softgouv.SoftgouvApp;

import com.gt.softgouv.domain.Cheferie;
import com.gt.softgouv.repository.CheferieRepository;
import com.gt.softgouv.web.rest.errors.ExceptionTranslator;

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

import javax.persistence.EntityManager;
import java.util.List;

import static com.gt.softgouv.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the CheferieResource REST controller.
 *
 * @see CheferieResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SoftgouvApp.class)
public class CheferieResourceIntTest {

    private static final String DEFAULT_NOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM = "BBBBBBBBBB";

    private static final String DEFAULT_PRENOM = "AAAAAAAAAA";
    private static final String UPDATED_PRENOM = "BBBBBBBBBB";

    @Autowired
    private CheferieRepository cheferieRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restCheferieMockMvc;

    private Cheferie cheferie;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CheferieResource cheferieResource = new CheferieResource(cheferieRepository);
        this.restCheferieMockMvc = MockMvcBuilders.standaloneSetup(cheferieResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Cheferie createEntity(EntityManager em) {
        Cheferie cheferie = new Cheferie()
            .nom(DEFAULT_NOM)
            .prenom(DEFAULT_PRENOM);
        return cheferie;
    }

    @Before
    public void initTest() {
        cheferie = createEntity(em);
    }

    @Test
    @Transactional
    public void createCheferie() throws Exception {
        int databaseSizeBeforeCreate = cheferieRepository.findAll().size();

        // Create the Cheferie
        restCheferieMockMvc.perform(post("/api/cheferies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cheferie)))
            .andExpect(status().isCreated());

        // Validate the Cheferie in the database
        List<Cheferie> cheferieList = cheferieRepository.findAll();
        assertThat(cheferieList).hasSize(databaseSizeBeforeCreate + 1);
        Cheferie testCheferie = cheferieList.get(cheferieList.size() - 1);
        assertThat(testCheferie.getNom()).isEqualTo(DEFAULT_NOM);
        assertThat(testCheferie.getPrenom()).isEqualTo(DEFAULT_PRENOM);
    }

    @Test
    @Transactional
    public void createCheferieWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = cheferieRepository.findAll().size();

        // Create the Cheferie with an existing ID
        cheferie.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCheferieMockMvc.perform(post("/api/cheferies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cheferie)))
            .andExpect(status().isBadRequest());

        // Validate the Cheferie in the database
        List<Cheferie> cheferieList = cheferieRepository.findAll();
        assertThat(cheferieList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllCheferies() throws Exception {
        // Initialize the database
        cheferieRepository.saveAndFlush(cheferie);

        // Get all the cheferieList
        restCheferieMockMvc.perform(get("/api/cheferies?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cheferie.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM.toString())))
            .andExpect(jsonPath("$.[*].prenom").value(hasItem(DEFAULT_PRENOM.toString())));
    }

    @Test
    @Transactional
    public void getCheferie() throws Exception {
        // Initialize the database
        cheferieRepository.saveAndFlush(cheferie);

        // Get the cheferie
        restCheferieMockMvc.perform(get("/api/cheferies/{id}", cheferie.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(cheferie.getId().intValue()))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM.toString()))
            .andExpect(jsonPath("$.prenom").value(DEFAULT_PRENOM.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCheferie() throws Exception {
        // Get the cheferie
        restCheferieMockMvc.perform(get("/api/cheferies/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCheferie() throws Exception {
        // Initialize the database
        cheferieRepository.saveAndFlush(cheferie);
        int databaseSizeBeforeUpdate = cheferieRepository.findAll().size();

        // Update the cheferie
        Cheferie updatedCheferie = cheferieRepository.findOne(cheferie.getId());
        // Disconnect from session so that the updates on updatedCheferie are not directly saved in db
        em.detach(updatedCheferie);
        updatedCheferie
            .nom(UPDATED_NOM)
            .prenom(UPDATED_PRENOM);

        restCheferieMockMvc.perform(put("/api/cheferies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedCheferie)))
            .andExpect(status().isOk());

        // Validate the Cheferie in the database
        List<Cheferie> cheferieList = cheferieRepository.findAll();
        assertThat(cheferieList).hasSize(databaseSizeBeforeUpdate);
        Cheferie testCheferie = cheferieList.get(cheferieList.size() - 1);
        assertThat(testCheferie.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testCheferie.getPrenom()).isEqualTo(UPDATED_PRENOM);
    }

    @Test
    @Transactional
    public void updateNonExistingCheferie() throws Exception {
        int databaseSizeBeforeUpdate = cheferieRepository.findAll().size();

        // Create the Cheferie

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restCheferieMockMvc.perform(put("/api/cheferies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cheferie)))
            .andExpect(status().isCreated());

        // Validate the Cheferie in the database
        List<Cheferie> cheferieList = cheferieRepository.findAll();
        assertThat(cheferieList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteCheferie() throws Exception {
        // Initialize the database
        cheferieRepository.saveAndFlush(cheferie);
        int databaseSizeBeforeDelete = cheferieRepository.findAll().size();

        // Get the cheferie
        restCheferieMockMvc.perform(delete("/api/cheferies/{id}", cheferie.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Cheferie> cheferieList = cheferieRepository.findAll();
        assertThat(cheferieList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Cheferie.class);
        Cheferie cheferie1 = new Cheferie();
        cheferie1.setId(1L);
        Cheferie cheferie2 = new Cheferie();
        cheferie2.setId(cheferie1.getId());
        assertThat(cheferie1).isEqualTo(cheferie2);
        cheferie2.setId(2L);
        assertThat(cheferie1).isNotEqualTo(cheferie2);
        cheferie1.setId(null);
        assertThat(cheferie1).isNotEqualTo(cheferie2);
    }
}
