package io.dashboardhub.pipelinedashboard.repository;

import io.dashboardhub.pipelinedashboard.domain.Repo;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the Repo entity.
 */
@SuppressWarnings("unused")
public interface RepoRepository extends JpaRepository<Repo,Long> {

    @Query("SELECT r FROM Repo r JOIN r.project p JOIN p.user u WHERE u.id = :userId")
    List<Repo> findAllByUserId(@Param("userId") Long userId);
}
