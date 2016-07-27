package io.dashboardhub.pipelinedashboard.repository.search;

import io.dashboardhub.pipelinedashboard.domain.Repo;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Repo entity.
 */
public interface RepoSearchRepository extends ElasticsearchRepository<Repo, Long> {
}
