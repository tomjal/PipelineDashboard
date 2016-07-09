package io.dashboardhub.pipelinedashboard.web.rest;

import com.codahale.metrics.annotation.Timed;
import io.dashboardhub.pipelinedashboard.domain.Repo;
import io.dashboardhub.pipelinedashboard.service.RepoService;
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
 * REST controller for managing Repo.
 */
@RestController
@RequestMapping("/api")
public class RepoResource {

    private final Logger log = LoggerFactory.getLogger(RepoResource.class);
        
    @Inject
    private RepoService repoService;
    
    /**
     * POST  /repos : Create a new repo.
     *
     * @param repo the repo to create
     * @return the ResponseEntity with status 201 (Created) and with body the new repo, or with status 400 (Bad Request) if the repo has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/repos",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Repo> createRepo(@Valid @RequestBody Repo repo) throws URISyntaxException {
        log.debug("REST request to save Repo : {}", repo);
        if (repo.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("repo", "idexists", "A new repo cannot already have an ID")).body(null);
        }
        Repo result = repoService.save(repo);
        return ResponseEntity.created(new URI("/api/repos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("repo", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /repos : Updates an existing repo.
     *
     * @param repo the repo to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated repo,
     * or with status 400 (Bad Request) if the repo is not valid,
     * or with status 500 (Internal Server Error) if the repo couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/repos",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Repo> updateRepo(@Valid @RequestBody Repo repo) throws URISyntaxException {
        log.debug("REST request to update Repo : {}", repo);
        if (repo.getId() == null) {
            return createRepo(repo);
        }
        Repo result = repoService.save(repo);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("repo", repo.getId().toString()))
            .body(result);
    }

    /**
     * GET  /repos : get all the repos.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of repos in body
     */
    @RequestMapping(value = "/repos",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Repo> getAllRepos() {
        log.debug("REST request to get all Repos");
        return repoService.findAll();
    }

    /**
     * GET  /repos/:id : get the "id" repo.
     *
     * @param id the id of the repo to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the repo, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/repos/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Repo> getRepo(@PathVariable Long id) {
        log.debug("REST request to get Repo : {}", id);
        Repo repo = repoService.findOne(id);
        return Optional.ofNullable(repo)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /repos/:id : delete the "id" repo.
     *
     * @param id the id of the repo to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/repos/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteRepo(@PathVariable Long id) {
        log.debug("REST request to delete Repo : {}", id);
        repoService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("repo", id.toString())).build();
    }

    /**
     * SEARCH  /_search/repos?query=:query : search for the repo corresponding
     * to the query.
     *
     * @param query the query of the repo search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/repos",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Repo> searchRepos(@RequestParam String query) {
        log.debug("REST request to search Repos for query {}", query);
        return repoService.search(query);
    }


}
