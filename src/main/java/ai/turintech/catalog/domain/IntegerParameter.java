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
 * A IntegerParameter.
 */
@Table("integer_parameter")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class IntegerParameter implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("parameter_type_definition_id")
    private UUID parameterTypeDefinitionId;

    @Column("default_value")
    private Integer defaultValue;

    @Transient
    @JsonIgnoreProperties(value = { "integerParameter" }, allowSetters = true)
    private Set<IntegerParameterValue> integerParameterValues = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public IntegerParameter id(UUID id) {
        this.setParameterTypeDefinitionId(id);
        return this;
    }

    public Integer getDefaultValue() {
        return this.defaultValue;
    }

    public IntegerParameter defaultValue(Integer defaultValue) {
        this.setDefaultValue(defaultValue);
        return this;
    }

    public void setDefaultValue(Integer defaultValue) {
        this.defaultValue = defaultValue;
    }

    public Set<IntegerParameterValue> getIntegerParameterValues() {
        return this.integerParameterValues;
    }

    public void setIntegerParameterValues(Set<IntegerParameterValue> integerParameterValues) {
        if (this.integerParameterValues != null) {
            this.integerParameterValues.forEach(i -> i.setIntegerParameter(null));
        }
        if (integerParameterValues != null) {
            integerParameterValues.forEach(i -> i.setIntegerParameter(this));
        }
        this.integerParameterValues = integerParameterValues;
    }

    public IntegerParameter integerParameterValues(Set<IntegerParameterValue> integerParameterValues) {
        this.setIntegerParameterValues(integerParameterValues);
        return this;
    }

    public IntegerParameter addIntegerParameterValue(IntegerParameterValue integerParameterValue) {
        this.integerParameterValues.add(integerParameterValue);
        integerParameterValue.setIntegerParameter(this);
        return this;
    }

    public IntegerParameter removeIntegerParameterValue(IntegerParameterValue integerParameterValue) {
        this.integerParameterValues.remove(integerParameterValue);
        integerParameterValue.setIntegerParameter(null);
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
        if (!(o instanceof IntegerParameter)) {
            return false;
        }
        return getParameterTypeDefinitionId() != null && getParameterTypeDefinitionId().equals(((IntegerParameter) o).getParameterTypeDefinitionId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "IntegerParameter{" +
            "parameterTypeDefinitionId=" + getParameterTypeDefinitionId() +
            ", defaultValue=" + getDefaultValue() +
            "}";
    }
}
