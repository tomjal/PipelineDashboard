package io.dashboardhub.pipelinedashboard.repository;

import io.dashboardhub.pipelinedashboard.domain.Repo;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Repo entity.
 */
@SuppressWarnings("unused")
public interface RepoRepository extends JpaRepository<Repo,Long> {

}
