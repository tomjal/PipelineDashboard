package io.dashboardhub.pipelinedashboard.service.impl;

import io.dashboardhub.pipelinedashboard.service.RepositoryService;
import io.dashboardhub.pipelinedashboard.domain.Repository;
import io.dashboardhub.pipelinedashboard.repository.RepositoryRepository;
import io.dashboardhub.pipelinedashboard.repository.search.RepositorySearchRepository;
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
 * Service Implementation for managing Repository.
 */
@Service
@Transactional
public class RepositoryServiceImpl implements RepositoryService{

    private final Logger log = LoggerFactory.getLogger(RepositoryServiceImpl.class);
    
    @Inject
    private RepositoryRepository repositoryRepository;
    
    @Inject
    private RepositorySearchRepository repositorySearchRepository;
    
    /**
     * Save a repository.
     * 
     * @param repository the entity to save
     * @return the persisted entity
     */
    public Repository save(Repository repository) {
        log.debug("Request to save Repository : {}", repository);
        Repository result = repositoryRepository.save(repository);
        repositorySearchRepository.save(result);
        return result;
    }

    /**
     *  Get all the repositories.
     *  
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public List<Repository> findAll() {
        log.debug("Request to get all Repositories");
        List<Repository> result = repositoryRepository.findAll();
        return result;
    }

    /**
     *  Get one repository by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public Repository findOne(Long id) {
        log.debug("Request to get Repository : {}", id);
        Repository repository = repositoryRepository.findOne(id);
        return repository;
    }

    /**
     *  Delete the  repository by id.
     *  
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Repository : {}", id);
        repositoryRepository.delete(id);
        repositorySearchRepository.delete(id);
    }

    /**
     * Search for the repository corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<Repository> search(String query) {
        log.debug("Request to search Repositories for query {}", query);
        return StreamSupport
            .stream(repositorySearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
