package io.dashboardhub.pipelinedashboard.web.rest;

import io.dashboardhub.pipelinedashboard.PipelineDashboardApp;
import io.dashboardhub.pipelinedashboard.domain.Repository;
import io.dashboardhub.pipelinedashboard.repository.RepositoryRepository;
import io.dashboardhub.pipelinedashboard.service.RepositoryService;
import io.dashboardhub.pipelinedashboard.repository.search.RepositorySearchRepository;

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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the RepositoryResource REST controller.
 *
 * @see RepositoryResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = PipelineDashboardApp.class)
@WebAppConfiguration
@IntegrationTest
public class RepositoryResourceIntTest {

    private static final String DEFAULT_URL = "AAAAA";
    private static final String UPDATED_URL = "BBBBB";

    @Inject
    private RepositoryRepository repositoryRepository;

    @Inject
    private RepositoryService repositoryService;

    @Inject
    private RepositorySearchRepository repositorySearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restRepositoryMockMvc;

    private Repository repository;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        RepositoryResource repositoryResource = new RepositoryResource();
        ReflectionTestUtils.setField(repositoryResource, "repositoryService", repositoryService);
        this.restRepositoryMockMvc = MockMvcBuilders.standaloneSetup(repositoryResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        repositorySearchRepository.deleteAll();
        repository = new Repository();
        repository.setUrl(DEFAULT_URL);
    }

    @Test
    @Transactional
    public void createRepository() throws Exception {
        int databaseSizeBeforeCreate = repositoryRepository.findAll().size();

        // Create the Repository

        restRepositoryMockMvc.perform(post("/api/repositories")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(repository)))
                .andExpect(status().isCreated());

        // Validate the Repository in the database
        List<Repository> repositories = repositoryRepository.findAll();
        assertThat(repositories).hasSize(databaseSizeBeforeCreate + 1);
        Repository testRepository = repositories.get(repositories.size() - 1);
        assertThat(testRepository.getUrl()).isEqualTo(DEFAULT_URL);

        // Validate the Repository in ElasticSearch
        Repository repositoryEs = repositorySearchRepository.findOne(testRepository.getId());
        assertThat(repositoryEs).isEqualToComparingFieldByField(testRepository);
    }

    @Test
    @Transactional
    public void checkUrlIsRequired() throws Exception {
        int databaseSizeBeforeTest = repositoryRepository.findAll().size();
        // set the field null
        repository.setUrl(null);

        // Create the Repository, which fails.

        restRepositoryMockMvc.perform(post("/api/repositories")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(repository)))
                .andExpect(status().isBadRequest());

        List<Repository> repositories = repositoryRepository.findAll();
        assertThat(repositories).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllRepositories() throws Exception {
        // Initialize the database
        repositoryRepository.saveAndFlush(repository);

        // Get all the repositories
        restRepositoryMockMvc.perform(get("/api/repositories?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(repository.getId().intValue())))
                .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL.toString())));
    }

    @Test
    @Transactional
    public void getRepository() throws Exception {
        // Initialize the database
        repositoryRepository.saveAndFlush(repository);

        // Get the repository
        restRepositoryMockMvc.perform(get("/api/repositories/{id}", repository.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(repository.getId().intValue()))
            .andExpect(jsonPath("$.url").value(DEFAULT_URL.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingRepository() throws Exception {
        // Get the repository
        restRepositoryMockMvc.perform(get("/api/repositories/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRepository() throws Exception {
        // Initialize the database
        repositoryService.save(repository);

        int databaseSizeBeforeUpdate = repositoryRepository.findAll().size();

        // Update the repository
        Repository updatedRepository = new Repository();
        updatedRepository.setId(repository.getId());
        updatedRepository.setUrl(UPDATED_URL);

        restRepositoryMockMvc.perform(put("/api/repositories")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedRepository)))
                .andExpect(status().isOk());

        // Validate the Repository in the database
        List<Repository> repositories = repositoryRepository.findAll();
        assertThat(repositories).hasSize(databaseSizeBeforeUpdate);
        Repository testRepository = repositories.get(repositories.size() - 1);
        assertThat(testRepository.getUrl()).isEqualTo(UPDATED_URL);

        // Validate the Repository in ElasticSearch
        Repository repositoryEs = repositorySearchRepository.findOne(testRepository.getId());
        assertThat(repositoryEs).isEqualToComparingFieldByField(testRepository);
    }

    @Test
    @Transactional
    public void deleteRepository() throws Exception {
        // Initialize the database
        repositoryService.save(repository);

        int databaseSizeBeforeDelete = repositoryRepository.findAll().size();

        // Get the repository
        restRepositoryMockMvc.perform(delete("/api/repositories/{id}", repository.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean repositoryExistsInEs = repositorySearchRepository.exists(repository.getId());
        assertThat(repositoryExistsInEs).isFalse();

        // Validate the database is empty
        List<Repository> repositories = repositoryRepository.findAll();
        assertThat(repositories).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchRepository() throws Exception {
        // Initialize the database
        repositoryService.save(repository);

        // Search the repository
        restRepositoryMockMvc.perform(get("/api/_search/repositories?query=id:" + repository.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(repository.getId().intValue())))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL.toString())));
    }
}
