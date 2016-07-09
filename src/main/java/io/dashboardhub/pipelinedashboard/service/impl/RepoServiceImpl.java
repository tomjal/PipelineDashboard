package io.dashboardhub.pipelinedashboard.service.impl;

import io.dashboardhub.pipelinedashboard.service.RepoService;
import io.dashboardhub.pipelinedashboard.domain.Repo;
import io.dashboardhub.pipelinedashboard.repository.RepoRepository;
import io.dashboardhub.pipelinedashboard.repository.search.RepoSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Repo.
 */
@Service
@Transactional
public class RepoServiceImpl implements RepoService{

    private final Logger log = LoggerFactory.getLogger(RepoServiceImpl.class);
    
    @Inject
    private RepoRepository repoRepository;
    
    @Inject
    private RepoSearchRepository repoSearchRepository;
    
    /**
     * Save a repo.
     * 
     * @param repo the entity to save
     * @return the persisted entity
     */
    public Repo save(Repo repo) {
        log.debug("Request to save Repo : {}", repo);
        Repo result = repoRepository.save(repo);
        repoSearchRepository.save(result);
        return result;
    }

    /**
     *  Get all the repos.
     *  
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public List<Repo> findAll() {
        log.debug("Request to get all Repos");
        List<Repo> result = repoRepository.findAll();
        return result;
    }

    /**
     *  Get one repo by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public Repo findOne(Long id) {
        log.debug("Request to get Repo : {}", id);
        Repo repo = repoRepository.findOne(id);
        return repo;
    }

    /**
     *  Delete the  repo by id.
     *  
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Repo : {}", id);
        repoRepository.delete(id);
        repoSearchRepository.delete(id);
    }

    /**
     * Search for the repo corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<Repo> search(String query) {
        log.debug("Request to search Repos for query {}", query);
        return StreamSupport
            .stream(repoSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
