package ai.turintech.catalog.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A Metric.
 */
@Table("metric")
@JsonIgnoreProperties(value = { "new" })
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Metric implements Serializable, Persistable<UUID> {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private UUID id;

    @Column("name")
    private String name;

    @Transient
    private boolean isPersisted;

    @Transient
    @JsonIgnoreProperties(
        value = { "parameters", "groups", "incompatibleMetrics", "mlTask", "structure", "type", "familyType", "ensembleType" },
        allowSetters = true
    )
    private Set<Model> models = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public UUID getId() {
        return this.id;
    }

    public Metric id(UUID id) {
        this.setId(id);
        return this;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Metric name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Transient
    @Override
    public boolean isNew() {
        return !this.isPersisted;
    }

    public Metric setIsPersisted() {
        this.isPersisted = true;
        return this;
    }

    public Set<Model> getModels() {
        return this.models;
    }

    public void setModels(Set<Model> models) {
        if (this.models != null) {
            this.models.forEach(i -> i.removeIncompatibleMetrics(this));
        }
        if (models != null) {
            models.forEach(i -> i.addIncompatibleMetrics(this));
        }
        this.models = models;
    }

    public Metric models(Set<Model> models) {
        this.setModels(models);
        return this;
    }

    public Metric addModel(Model model) {
        this.models.add(model);
        model.getIncompatibleMetrics().add(this);
        return this;
    }

    public Metric removeModel(Model model) {
        this.models.remove(model);
        model.getIncompatibleMetrics().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Metric)) {
            return false;
        }
        return getId() != null && getId().equals(((Metric) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Metric{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
