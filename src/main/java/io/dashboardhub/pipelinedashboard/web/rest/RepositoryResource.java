package io.dashboardhub.pipelinedashboard.web.rest;

import com.codahale.metrics.annotation.Timed;
import io.dashboardhub.pipelinedashboard.domain.Repository;
import io.dashboardhub.pipelinedashboard.service.RepositoryService;
import io.dashboardhub.pipelinedashboard.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Repository.
 */
@RestController
@RequestMapping("/api")
public class RepositoryResource {

    private final Logger log = LoggerFactory.getLogger(RepositoryResource.class);
        
    @Inject
    private RepositoryService repositoryService;
    
    /**
     * POST  /repositories : Create a new repository.
     *
     * @param repository the repository to create
     * @return the ResponseEntity with status 201 (Created) and with body the new repository, or with status 400 (Bad Request) if the repository has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/repositories",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Repository> createRepository(@Valid @RequestBody Repository repository) throws URISyntaxException {
        log.debug("REST request to save Repository : {}", repository);
        if (repository.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("repository", "idexists", "A new repository cannot already have an ID")).body(null);
        }
        Repository result = repositoryService.save(repository);
        return ResponseEntity.created(new URI("/api/repositories/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("repository", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /repositories : Updates an existing repository.
     *
     * @param repository the repository to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated repository,
     * or with status 400 (Bad Request) if the repository is not valid,
     * or with status 500 (Internal Server Error) if the repository couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/repositories",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Repository> updateRepository(@Valid @RequestBody Repository repository) throws URISyntaxException {
        log.debug("REST request to update Repository : {}", repository);
        if (repository.getId() == null) {
            return createRepository(repository);
        }
        Repository result = repositoryService.save(repository);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("repository", repository.getId().toString()))
            .body(result);
    }

    /**
     * GET  /repositories : get all the repositories.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of repositories in body
     */
    @RequestMapping(value = "/repositories",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Repository> getAllRepositories() {
        log.debug("REST request to get all Repositories");
        return repositoryService.findAll();
    }

    /**
     * GET  /repositories/:id : get the "id" repository.
     *
     * @param id the id of the repository to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the repository, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/repositories/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Repository> getRepository(@PathVariable Long id) {
        log.debug("REST request to get Repository : {}", id);
        Repository repository = repositoryService.findOne(id);
        return Optional.ofNullable(repository)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /repositories/:id : delete the "id" repository.
     *
     * @param id the id of the repository to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/repositories/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteRepository(@PathVariable Long id) {
        log.debug("REST request to delete Repository : {}", id);
        repositoryService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("repository", id.toString())).build();
    }

    /**
     * SEARCH  /_search/repositories?query=:query : search for the repository corresponding
     * to the query.
     *
     * @param query the query of the repository search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/repositories",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Repository> searchRepositories(@RequestParam String query) {
        log.debug("REST request to search Repositories for query {}", query);
        return repositoryService.search(query);
    }

}
