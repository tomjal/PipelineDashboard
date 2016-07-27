package io.dashboardhub.pipelinedashboard.web.rest;

import io.dashboardhub.pipelinedashboard.PipelinedashboardApp;
import io.dashboardhub.pipelinedashboard.domain.Repo;
import io.dashboardhub.pipelinedashboard.repository.RepoRepository;
import io.dashboardhub.pipelinedashboard.service.RepoService;
import io.dashboardhub.pipelinedashboard.repository.search.RepoSearchRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the RepoResource REST controller.
 *
 * @see RepoResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = PipelinedashboardApp.class)
@WebAppConfiguration
@IntegrationTest
public class RepoResourceIntTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneId.of("Z"));

    private static final String DEFAULT_OWNER = "A";
    private static final String UPDATED_OWNER = "B";
    private static final String DEFAULT_NAME = "A";
    private static final String UPDATED_NAME = "B";

    private static final ZonedDateTime DEFAULT_CREATED_ON = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_CREATED_ON = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_CREATED_ON_STR = dateTimeFormatter.format(DEFAULT_CREATED_ON);

    private static final ZonedDateTime DEFAULT_UPDATED_ON = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_UPDATED_ON = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_UPDATED_ON_STR = dateTimeFormatter.format(DEFAULT_UPDATED_ON);

    @Inject
    private RepoRepository repoRepository;

    @Inject
    private RepoService repoService;

    @Inject
    private RepoSearchRepository repoSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restRepoMockMvc;

    private Repo repo;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        RepoResource repoResource = new RepoResource();
        ReflectionTestUtils.setField(repoResource, "repoService", repoService);
        this.restRepoMockMvc = MockMvcBuilders.standaloneSetup(repoResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        repoSearchRepository.deleteAll();
        repo = new Repo();
        repo.setOwner(DEFAULT_OWNER);
        repo.setName(DEFAULT_NAME);
        repo.setCreatedOn(DEFAULT_CREATED_ON);
        repo.setUpdatedOn(DEFAULT_UPDATED_ON);
    }

    @Test
    @Transactional
    public void createRepo() throws Exception {
        int databaseSizeBeforeCreate = repoRepository.findAll().size();

        // Create the Repo

        restRepoMockMvc.perform(post("/api/repos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(repo)))
                .andExpect(status().isCreated());

        // Validate the Repo in the database
        List<Repo> repos = repoRepository.findAll();
        assertThat(repos).hasSize(databaseSizeBeforeCreate + 1);
        Repo testRepo = repos.get(repos.size() - 1);
        assertThat(testRepo.getOwner()).isEqualTo(DEFAULT_OWNER);
        assertThat(testRepo.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testRepo.getCreatedOn()).isEqualTo(DEFAULT_CREATED_ON);
        assertThat(testRepo.getUpdatedOn()).isEqualTo(DEFAULT_UPDATED_ON);

        // Validate the Repo in ElasticSearch
        Repo repoEs = repoSearchRepository.findOne(testRepo.getId());
        assertThat(repoEs).isEqualToComparingFieldByField(testRepo);
    }

    @Test
    @Transactional
    public void checkOwnerIsRequired() throws Exception {
        int databaseSizeBeforeTest = repoRepository.findAll().size();
        // set the field null
        repo.setOwner(null);

        // Create the Repo, which fails.

        restRepoMockMvc.perform(post("/api/repos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(repo)))
                .andExpect(status().isBadRequest());

        List<Repo> repos = repoRepository.findAll();
        assertThat(repos).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = repoRepository.findAll().size();
        // set the field null
        repo.setName(null);

        // Create the Repo, which fails.

        restRepoMockMvc.perform(post("/api/repos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(repo)))
                .andExpect(status().isBadRequest());

        List<Repo> repos = repoRepository.findAll();
        assertThat(repos).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllRepos() throws Exception {
        // Initialize the database
        repoRepository.saveAndFlush(repo);

        // Get all the repos
        restRepoMockMvc.perform(get("/api/repos?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(repo.getId().intValue())))
                .andExpect(jsonPath("$.[*].owner").value(hasItem(DEFAULT_OWNER.toString())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].createdOn").value(hasItem(DEFAULT_CREATED_ON_STR)))
                .andExpect(jsonPath("$.[*].updatedOn").value(hasItem(DEFAULT_UPDATED_ON_STR)));
    }

    @Test
    @Transactional
    public void getRepo() throws Exception {
        // Initialize the database
        repoRepository.saveAndFlush(repo);

        // Get the repo
        restRepoMockMvc.perform(get("/api/repos/{id}", repo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(repo.getId().intValue()))
            .andExpect(jsonPath("$.owner").value(DEFAULT_OWNER.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.createdOn").value(DEFAULT_CREATED_ON_STR))
            .andExpect(jsonPath("$.updatedOn").value(DEFAULT_UPDATED_ON_STR));
    }

    @Test
    @Transactional
    public void getNonExistingRepo() throws Exception {
        // Get the repo
        restRepoMockMvc.perform(get("/api/repos/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRepo() throws Exception {
        // Initialize the database
        repoService.save(repo);

        int databaseSizeBeforeUpdate = repoRepository.findAll().size();

        // Update the repo
        Repo updatedRepo = new Repo();
        updatedRepo.setId(repo.getId());
        updatedRepo.setOwner(UPDATED_OWNER);
        updatedRepo.setName(UPDATED_NAME);
        updatedRepo.setCreatedOn(UPDATED_CREATED_ON);
        updatedRepo.setUpdatedOn(UPDATED_UPDATED_ON);

        restRepoMockMvc.perform(put("/api/repos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedRepo)))
                .andExpect(status().isOk());

        // Validate the Repo in the database
        List<Repo> repos = repoRepository.findAll();
        assertThat(repos).hasSize(databaseSizeBeforeUpdate);
        Repo testRepo = repos.get(repos.size() - 1);
        assertThat(testRepo.getOwner()).isEqualTo(UPDATED_OWNER);
        assertThat(testRepo.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testRepo.getCreatedOn()).isEqualTo(UPDATED_CREATED_ON);
        assertThat(testRepo.getUpdatedOn()).isEqualTo(UPDATED_UPDATED_ON);

        // Validate the Repo in ElasticSearch
        Repo repoEs = repoSearchRepository.findOne(testRepo.getId());
        assertThat(repoEs).isEqualToComparingFieldByField(testRepo);
    }

    @Test
    @Transactional
    public void deleteRepo() throws Exception {
        // Initialize the database
        repoService.save(repo);

        int databaseSizeBeforeDelete = repoRepository.findAll().size();

        // Get the repo
        restRepoMockMvc.perform(delete("/api/repos/{id}", repo.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean repoExistsInEs = repoSearchRepository.exists(repo.getId());
        assertThat(repoExistsInEs).isFalse();

        // Validate the database is empty
        List<Repo> repos = repoRepository.findAll();
        assertThat(repos).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchRepo() throws Exception {
        // Initialize the database
        repoService.save(repo);

        // Search the repo
        restRepoMockMvc.perform(get("/api/_search/repos?query=id:" + repo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(repo.getId().intValue())))
            .andExpect(jsonPath("$.[*].owner").value(hasItem(DEFAULT_OWNER.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].createdOn").value(hasItem(DEFAULT_CREATED_ON_STR)))
            .andExpect(jsonPath("$.[*].updatedOn").value(hasItem(DEFAULT_UPDATED_ON_STR)));
    }
}
