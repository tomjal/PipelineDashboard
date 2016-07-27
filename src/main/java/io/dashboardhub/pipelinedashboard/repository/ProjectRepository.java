package io.dashboardhub.pipelinedashboard.repository;

import io.dashboardhub.pipelinedashboard.domain.Project;

import io.dashboardhub.pipelinedashboard.domain.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;

import java.util.List;


/**
 * Spring Data JPA repository for the Project entity.
 */
@SuppressWarnings("unused")
public interface ProjectRepository extends JpaRepository<Project,Long> {

    @Query("SELECT p FROM Project p LEFT JOIN p.repos r JOIN p.user u WHERE u.id = :userId")
    List<Project> findAllByUser(@Param("userId") Long userId);
}
