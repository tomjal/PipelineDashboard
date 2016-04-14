package io.dashboardhub.pipelinedashboard.service;

import io.dashboardhub.pipelinedashboard.domain.Repository;

import java.util.List;

/**
 * Service Interface for managing Repository.
 */
public interface RepositoryService {

    /**
     * Save a repository.
     * 
     * @param repository the entity to save
     * @return the persisted entity
     */
    Repository save(Repository repository);

    /**
     *  Get all the repositories.
     *  
     *  @return the list of entities
     */
    List<Repository> findAll();

    /**
     *  Get the "id" repository.
     *  
     *  @param id the id of the entity
     *  @return the entity
     */
    Repository findOne(Long id);

    /**
     *  Delete the "id" repository.
     *  
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the repository corresponding to the query.
     * 
     *  @param query the query of the search
     *  @return the list of entities
     */
    List<Repository> search(String query);
}
