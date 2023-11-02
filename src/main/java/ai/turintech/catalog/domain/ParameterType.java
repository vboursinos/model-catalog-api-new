package ai.turintech.catalog.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * A ParameterType.
 */
@Entity
@Table(name = "parameter_type")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ParameterType implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    @Column(name = "id")
    private UUID id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "type")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(
        value = { "integerParameter", "floatParameter", "categoricalParameter", "booleanParameter", "distribution", "parameter", "type" },
        allowSetters = true
    )
    private Set<ParameterTypeDefinition> definitions = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public UUID getId() {
        return this.id;
    }

    public ParameterType id(UUID id) {
        this.setId(id);
        return this;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public ParameterType name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<ParameterTypeDefinition> getDefinitions() {
        return this.definitions;
    }

    public void setDefinitions(Set<ParameterTypeDefinition> parameterTypeDefinitions) {
        if (this.definitions != null) {
            this.definitions.forEach(i -> i.setType(null));
        }
        if (parameterTypeDefinitions != null) {
            parameterTypeDefinitions.forEach(i -> i.setType(this));
        }
        this.definitions = parameterTypeDefinitions;
    }

    public ParameterType definitions(Set<ParameterTypeDefinition> parameterTypeDefinitions) {
        this.setDefinitions(parameterTypeDefinitions);
        return this;
    }

    public ParameterType addDefinitions(ParameterTypeDefinition parameterTypeDefinition) {
        this.definitions.add(parameterTypeDefinition);
        parameterTypeDefinition.setType(this);
        return this;
    }

    public ParameterType removeDefinitions(ParameterTypeDefinition parameterTypeDefinition) {
        this.definitions.remove(parameterTypeDefinition);
        parameterTypeDefinition.setType(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ParameterType)) {
            return false;
        }
        return getId() != null && getId().equals(((ParameterType) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ParameterType{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
