package ai.turintech.catalog.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.*;

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
    @Column("parameter_type_definition_id")
    private UUID parameterTypeDefinitionId;

    @Column("default_value")
    private Double defaultValue;

    @Transient
    @JsonIgnoreProperties(value = { "floatParameter" }, allowSetters = true)
    private List<FloatParameterRange> floatParameterRanges = new ArrayList<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public FloatParameter id(UUID id) {
        this.setParameterTypeDefinitionId(id);
        return this;
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

    public List<FloatParameterRange> getFloatParameterRanges() {
        return this.floatParameterRanges;
    }

    public void setFloatParameterRanges(List<FloatParameterRange> floatParameterRanges) {
        this.floatParameterRanges = floatParameterRanges;
    }

    public FloatParameter floatParameterRanges(List<FloatParameterRange> floatParameterRanges) {
        this.setFloatParameterRanges(floatParameterRanges);
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
        return getParameterTypeDefinitionId() != null && getParameterTypeDefinitionId().equals(((FloatParameter) o).getParameterTypeDefinitionId());
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
                "parameterTypeDefinitionId=" + parameterTypeDefinitionId +
                ", defaultValue=" + defaultValue +
                ", floatParameterRanges=" + floatParameterRanges +
                '}';
    }
}
