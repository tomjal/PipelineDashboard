package io.dashboardhub.pipelinedashboard.service;

import io.dashboardhub.pipelinedashboard.domain.Repo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<Repo> findAll(Pageable pageable);

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
    Page<Repo> search(String query, Pageable pageable);
}
