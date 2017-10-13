package com.confinabox.cfp.web.rest;

import com.confinabox.cfp.CfpApp;

import com.confinabox.cfp.domain.Speacker;
import com.confinabox.cfp.repository.SpeackerRepository;
import com.confinabox.cfp.web.rest.errors.ExceptionTranslator;

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
import org.springframework.util.Base64Utils;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the SpeackerResource REST controller.
 *
 * @see SpeackerResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CfpApp.class)
public class SpeackerResourceIntTest {

    private static final String DEFAULT_BIO = "AAAAAAAAAA";
    private static final String UPDATED_BIO = "BBBBBBBBBB";

    private static final String DEFAULT_QUALIFICATIONS = "AAAAAAAAAA";
    private static final String UPDATED_QUALIFICATIONS = "BBBBBBBBBB";

    private static final String DEFAULT_COMPANY = "AAAAAAAAAA";
    private static final String UPDATED_COMPANY = "BBBBBBBBBB";

    @Autowired
    private SpeackerRepository speackerRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restSpeackerMockMvc;

    private Speacker speacker;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SpeackerResource speackerResource = new SpeackerResource(speackerRepository);
        this.restSpeackerMockMvc = MockMvcBuilders.standaloneSetup(speackerResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Speacker createEntity(EntityManager em) {
        Speacker speacker = new Speacker()
            .bio(DEFAULT_BIO)
            .qualifications(DEFAULT_QUALIFICATIONS)
            .company(DEFAULT_COMPANY);
        return speacker;
    }

    @Before
    public void initTest() {
        speacker = createEntity(em);
    }

    @Test
    @Transactional
    public void createSpeacker() throws Exception {
        int databaseSizeBeforeCreate = speackerRepository.findAll().size();

        // Create the Speacker
        restSpeackerMockMvc.perform(post("/api/speackers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(speacker)))
            .andExpect(status().isCreated());

        // Validate the Speacker in the database
        List<Speacker> speackerList = speackerRepository.findAll();
        assertThat(speackerList).hasSize(databaseSizeBeforeCreate + 1);
        Speacker testSpeacker = speackerList.get(speackerList.size() - 1);
        assertThat(testSpeacker.getBio()).isEqualTo(DEFAULT_BIO);
        assertThat(testSpeacker.getQualifications()).isEqualTo(DEFAULT_QUALIFICATIONS);
        assertThat(testSpeacker.getCompany()).isEqualTo(DEFAULT_COMPANY);
    }

    @Test
    @Transactional
    public void createSpeackerWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = speackerRepository.findAll().size();

        // Create the Speacker with an existing ID
        speacker.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSpeackerMockMvc.perform(post("/api/speackers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(speacker)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Speacker> speackerList = speackerRepository.findAll();
        assertThat(speackerList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllSpeackers() throws Exception {
        // Initialize the database
        speackerRepository.saveAndFlush(speacker);

        // Get all the speackerList
        restSpeackerMockMvc.perform(get("/api/speackers?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(speacker.getId().intValue())))
            .andExpect(jsonPath("$.[*].bio").value(hasItem(DEFAULT_BIO.toString())))
            .andExpect(jsonPath("$.[*].qualifications").value(hasItem(DEFAULT_QUALIFICATIONS.toString())))
            .andExpect(jsonPath("$.[*].company").value(hasItem(DEFAULT_COMPANY.toString())));
    }

    @Test
    @Transactional
    public void getSpeacker() throws Exception {
        // Initialize the database
        speackerRepository.saveAndFlush(speacker);

        // Get the speacker
        restSpeackerMockMvc.perform(get("/api/speackers/{id}", speacker.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(speacker.getId().intValue()))
            .andExpect(jsonPath("$.bio").value(DEFAULT_BIO.toString()))
            .andExpect(jsonPath("$.qualifications").value(DEFAULT_QUALIFICATIONS.toString()))
            .andExpect(jsonPath("$.company").value(DEFAULT_COMPANY.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingSpeacker() throws Exception {
        // Get the speacker
        restSpeackerMockMvc.perform(get("/api/speackers/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSpeacker() throws Exception {
        // Initialize the database
        speackerRepository.saveAndFlush(speacker);
        int databaseSizeBeforeUpdate = speackerRepository.findAll().size();

        // Update the speacker
        Speacker updatedSpeacker = speackerRepository.findOne(speacker.getId());
        updatedSpeacker
            .bio(UPDATED_BIO)
            .qualifications(UPDATED_QUALIFICATIONS)
            .company(UPDATED_COMPANY);

        restSpeackerMockMvc.perform(put("/api/speackers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedSpeacker)))
            .andExpect(status().isOk());

        // Validate the Speacker in the database
        List<Speacker> speackerList = speackerRepository.findAll();
        assertThat(speackerList).hasSize(databaseSizeBeforeUpdate);
        Speacker testSpeacker = speackerList.get(speackerList.size() - 1);
        assertThat(testSpeacker.getBio()).isEqualTo(UPDATED_BIO);
        assertThat(testSpeacker.getQualifications()).isEqualTo(UPDATED_QUALIFICATIONS);
        assertThat(testSpeacker.getCompany()).isEqualTo(UPDATED_COMPANY);
    }

    @Test
    @Transactional
    public void updateNonExistingSpeacker() throws Exception {
        int databaseSizeBeforeUpdate = speackerRepository.findAll().size();

        // Create the Speacker

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restSpeackerMockMvc.perform(put("/api/speackers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(speacker)))
            .andExpect(status().isCreated());

        // Validate the Speacker in the database
        List<Speacker> speackerList = speackerRepository.findAll();
        assertThat(speackerList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteSpeacker() throws Exception {
        // Initialize the database
        speackerRepository.saveAndFlush(speacker);
        int databaseSizeBeforeDelete = speackerRepository.findAll().size();

        // Get the speacker
        restSpeackerMockMvc.perform(delete("/api/speackers/{id}", speacker.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Speacker> speackerList = speackerRepository.findAll();
        assertThat(speackerList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Speacker.class);
        Speacker speacker1 = new Speacker();
        speacker1.setId(1L);
        Speacker speacker2 = new Speacker();
        speacker2.setId(speacker1.getId());
        assertThat(speacker1).isEqualTo(speacker2);
        speacker2.setId(2L);
        assertThat(speacker1).isNotEqualTo(speacker2);
        speacker1.setId(null);
        assertThat(speacker1).isNotEqualTo(speacker2);
    }
}
