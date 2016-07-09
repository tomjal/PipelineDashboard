package io.dashboardhub.pipelinedashboard.service;

import io.dashboardhub.pipelinedashboard.domain.Repo;

import java.util.List;

/**
 * Service Interface for managing Repo.
 */
public interface RepoService {

    /**
     * Save a repo.
     * 
     * @param repo the entity to save
     * @return the persisted entity
     */
    Repo save(Repo repo);

    /**
     *  Get all the repos.
     *  
     *  @return the list of entities
     */
    List<Repo> findAll();

    /**
     *  Get the "id" repo.
     *  
     *  @param id the id of the entity
     *  @return the entity
     */
    Repo findOne(Long id);

    /**
     *  Delete the "id" repo.
     *  
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the repo corresponding to the query.
     * 
     *  @param query the query of the search
     *  @return the list of entities
     */
    List<Repo> search(String query);
}
