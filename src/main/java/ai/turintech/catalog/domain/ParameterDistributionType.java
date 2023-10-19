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
 * A ParameterDistributionType.
 */
@Table("parameter_distribution_type")
@JsonIgnoreProperties(value = { "new" })
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ParameterDistributionType implements Serializable, Persistable<UUID> {

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
        value = { "integerParameter", "floatParameter", "categoricalParameter", "booleanParameter", "distribution", "parameter", "type" },
        allowSetters = true
    )
    private Set<ParameterTypeDefinition> definitions = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public UUID getId() {
        return this.id;
    }

    public ParameterDistributionType id(UUID id) {
        this.setId(id);
        return this;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public ParameterDistributionType name(String name) {
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

    public ParameterDistributionType setIsPersisted() {
        this.isPersisted = true;
        return this;
    }

    public Set<ParameterTypeDefinition> getDefinitions() {
        return this.definitions;
    }

    public void setDefinitions(Set<ParameterTypeDefinition> parameterTypeDefinitions) {
        if (this.definitions != null) {
            this.definitions.forEach(i -> i.setDistribution(null));
        }
        if (parameterTypeDefinitions != null) {
            parameterTypeDefinitions.forEach(i -> i.setDistribution(this));
        }
        this.definitions = parameterTypeDefinitions;
    }

    public ParameterDistributionType definitions(Set<ParameterTypeDefinition> parameterTypeDefinitions) {
        this.setDefinitions(parameterTypeDefinitions);
        return this;
    }

    public ParameterDistributionType addDefinitions(ParameterTypeDefinition parameterTypeDefinition) {
        this.definitions.add(parameterTypeDefinition);
        parameterTypeDefinition.setDistribution(this);
        return this;
    }

    public ParameterDistributionType removeDefinitions(ParameterTypeDefinition parameterTypeDefinition) {
        this.definitions.remove(parameterTypeDefinition);
        parameterTypeDefinition.setDistribution(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ParameterDistributionType)) {
            return false;
        }
        return getId() != null && getId().equals(((ParameterDistributionType) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ParameterDistributionType{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
