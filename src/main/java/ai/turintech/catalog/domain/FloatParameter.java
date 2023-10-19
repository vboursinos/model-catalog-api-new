package ai.turintech.catalog.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A FloatParameter.
 */
@Table("float_parameter")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FloatParameter implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @Column("default_value")
    private Double defaultValue;

    @Transient
    private ParameterTypeDefinition parameterTypeDefinition;

    @Transient
    @JsonIgnoreProperties(value = { "floatParameter" }, allowSetters = true)
    private Set<FloatParameterRange> floatParameterRanges = new HashSet<>();

    @Column("parameter_type_definition_id")
    private UUID parameterTypeDefinitionId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public FloatParameter id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getDefaultValue() {
        return this.defaultValue;
    }

    public FloatParameter defaultValue(Double defaultValue) {
        this.setDefaultValue(defaultValue);
        return this;
    }

    public void setDefaultValue(Double defaultValue) {
        this.defaultValue = defaultValue;
    }

    public ParameterTypeDefinition getParameterTypeDefinition() {
        return this.parameterTypeDefinition;
    }

    public void setParameterTypeDefinition(ParameterTypeDefinition parameterTypeDefinition) {
        this.parameterTypeDefinition = parameterTypeDefinition;
        this.parameterTypeDefinitionId = parameterTypeDefinition != null ? parameterTypeDefinition.getId() : null;
    }

    public FloatParameter parameterTypeDefinition(ParameterTypeDefinition parameterTypeDefinition) {
        this.setParameterTypeDefinition(parameterTypeDefinition);
        return this;
    }

    public Set<FloatParameterRange> getFloatParameterRanges() {
        return this.floatParameterRanges;
    }

    public void setFloatParameterRanges(Set<FloatParameterRange> floatParameterRanges) {
        if (this.floatParameterRanges != null) {
            this.floatParameterRanges.forEach(i -> i.setFloatParameter(null));
        }
        if (floatParameterRanges != null) {
            floatParameterRanges.forEach(i -> i.setFloatParameter(this));
        }
        this.floatParameterRanges = floatParameterRanges;
    }

    public FloatParameter floatParameterRanges(Set<FloatParameterRange> floatParameterRanges) {
        this.setFloatParameterRanges(floatParameterRanges);
        return this;
    }

    public FloatParameter addFloatParameterRange(FloatParameterRange floatParameterRange) {
        this.floatParameterRanges.add(floatParameterRange);
        floatParameterRange.setFloatParameter(this);
        return this;
    }

    public FloatParameter removeFloatParameterRange(FloatParameterRange floatParameterRange) {
        this.floatParameterRanges.remove(floatParameterRange);
        floatParameterRange.setFloatParameter(null);
        return this;
    }

    public UUID getParameterTypeDefinitionId() {
        return this.parameterTypeDefinitionId;
    }

    public void setParameterTypeDefinitionId(UUID parameterTypeDefinition) {
        this.parameterTypeDefinitionId = parameterTypeDefinition;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FloatParameter)) {
            return false;
        }
        return getId() != null && getId().equals(((FloatParameter) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FloatParameter{" +
            "id=" + getId() +
            ", defaultValue=" + getDefaultValue() +
            "}";
    }
}
