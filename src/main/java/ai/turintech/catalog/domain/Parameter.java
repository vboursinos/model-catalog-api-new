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
 * A Parameter.
 */
@Table("parameter")
@JsonIgnoreProperties(value = { "new" })
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Parameter implements Serializable, Persistable<UUID> {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private UUID id;

    @NotNull(message = "must not be null")
    @Column("name")
    private String name;

    @NotNull(message = "must not be null")
    @Column("label")
    private String label;

    @Column("description")
    private String description;

    @NotNull(message = "must not be null")
    @Column("enabled")
    private Boolean enabled;

    @NotNull(message = "must not be null")
    @Column("fixed_value")
    private Boolean fixedValue;

    @NotNull(message = "must not be null")
    @Column("ordering")
    private Integer ordering;

    @Transient
    private boolean isPersisted;

    @Transient
    @JsonIgnoreProperties(
        value = { "integerParameter", "floatParameter", "categoricalParameter", "booleanParameter", "distribution", "parameter", "type" },
        allowSetters = true
    )
    private Set<ParameterTypeDefinition> definitions = new HashSet<>();

    @Transient
    @JsonIgnoreProperties(
        value = { "parameters", "groups", "incompatibleMetrics", "mlTask", "structure", "type", "familyType", "ensembleType" },
        allowSetters = true
    )
    private Model model;

    @Column("model_id")
    private UUID modelId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public UUID getId() {
        return this.id;
    }

    public Parameter id(UUID id) {
        this.setId(id);
        return this;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Parameter name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLabel() {
        return this.label;
    }

    public Parameter label(String label) {
        this.setLabel(label);
        return this;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getDescription() {
        return this.description;
    }

    public Parameter description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getEnabled() {
        return this.enabled;
    }

    public Parameter enabled(Boolean enabled) {
        this.setEnabled(enabled);
        return this;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Boolean getFixedValue() {
        return this.fixedValue;
    }

    public Parameter fixedValue(Boolean fixedValue) {
        this.setFixedValue(fixedValue);
        return this;
    }

    public void setFixedValue(Boolean fixedValue) {
        this.fixedValue = fixedValue;
    }

    public Integer getOrdering() {
        return this.ordering;
    }

    public Parameter ordering(Integer ordering) {
        this.setOrdering(ordering);
        return this;
    }

    public void setOrdering(Integer ordering) {
        this.ordering = ordering;
    }

    @Transient
    @Override
    public boolean isNew() {
        return !this.isPersisted;
    }

    public Parameter setIsPersisted() {
        this.isPersisted = true;
        return this;
    }

    public Set<ParameterTypeDefinition> getDefinitions() {
        return this.definitions;
    }

    public void setDefinitions(Set<ParameterTypeDefinition> parameterTypeDefinitions) {
        if (this.definitions != null) {
            this.definitions.forEach(i -> i.setParameter(null));
        }
        if (parameterTypeDefinitions != null) {
            parameterTypeDefinitions.forEach(i -> i.setParameter(this));
        }
        this.definitions = parameterTypeDefinitions;
    }

    public Parameter definitions(Set<ParameterTypeDefinition> parameterTypeDefinitions) {
        this.setDefinitions(parameterTypeDefinitions);
        return this;
    }

    public Parameter addDefinitions(ParameterTypeDefinition parameterTypeDefinition) {
        this.definitions.add(parameterTypeDefinition);
        parameterTypeDefinition.setParameter(this);
        return this;
    }

    public Parameter removeDefinitions(ParameterTypeDefinition parameterTypeDefinition) {
        this.definitions.remove(parameterTypeDefinition);
        parameterTypeDefinition.setParameter(null);
        return this;
    }

    public Model getModel() {
        return this.model;
    }

    public void setModel(Model model) {
        this.model = model;
        this.modelId = model != null ? model.getId() : null;
    }

    public Parameter model(Model model) {
        this.setModel(model);
        return this;
    }

    public UUID getModelId() {
        return this.modelId;
    }

    public void setModelId(UUID model) {
        this.modelId = model;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Parameter)) {
            return false;
        }
        return getId() != null && getId().equals(((Parameter) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Parameter{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", label='" + getLabel() + "'" +
            ", description='" + getDescription() + "'" +
            ", enabled='" + getEnabled() + "'" +
            ", fixedValue='" + getFixedValue() + "'" +
            ", ordering=" + getOrdering() +
            "}";
    }
}
