package ai.turintech.catalog.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.*;
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
 * A ModelEnsembleType.
 */
@Table("model_ensemble_type")
@JsonIgnoreProperties(value = { "new" })
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ModelEnsembleType implements Serializable, Persistable<UUID> {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private UUID id;

    @NotNull(message = "must not be null")
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

    public ModelEnsembleType id(UUID id) {
        this.setId(id);
        return this;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public ModelEnsembleType name(String name) {
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

    public ModelEnsembleType setIsPersisted() {
        this.isPersisted = true;
        return this;
    }

    public Set<Model> getModels() {
        return this.models;
    }

    public void setModels(Set<Model> models) {
        if (this.models != null) {
            this.models.forEach(i -> i.setEnsembleType(null));
        }
        if (models != null) {
            models.forEach(i -> i.setEnsembleType(this));
        }
        this.models = models;
    }

    public ModelEnsembleType models(Set<Model> models) {
        this.setModels(models);
        return this;
    }

    public ModelEnsembleType addModels(Model model) {
        this.models.add(model);
        model.setEnsembleType(this);
        return this;
    }

    public ModelEnsembleType removeModels(Model model) {
        this.models.remove(model);
        model.setEnsembleType(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ModelEnsembleType)) {
            return false;
        }
        return getId() != null && getId().equals(((ModelEnsembleType) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ModelEnsembleType{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
