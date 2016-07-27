package io.dashboardhub.pipelinedashboard.domain;

import com.fasterxml.jackson.annotation.*;
import io.dashboardhub.pipelinedashboard.domain.util.UUIDGenerator;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A Repo.
 */
@Entity
@Table(name = "repo")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "repo")
public class Repo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "uuid", length = 64, nullable = false)
    private String uuid = new UUIDGenerator().generate();

    @Column(name = "created_on")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private ZonedDateTime createdOn;

    @NotNull
    @Size(min = 3, max = 32)
    @Column(name = "owner", length = 32, nullable = false)
    private String owner;

    @NotNull
    @Size(min = 3, max = 32)
    @Column(name = "name", length = 32, nullable = false)
    private String name;

    @ManyToOne
    @NotNull
//    @JsonManagedReference
    private Project project;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public ZonedDateTime getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(ZonedDateTime createdOn) {
        this.createdOn = createdOn;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Repo repo = (Repo) o;
        if(repo.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, repo.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Repo{" +
            "id=" + id +
            ", createdOn='" + createdOn + "'" +
            ", owner='" + owner + "'" +
            ", name='" + name + "'" +
            '}';
    }
}
