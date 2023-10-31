package ai.turintech.catalog.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.*;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A CategoricalParameter.
 */
@Table("categorical_parameter")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CategoricalParameter implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("parameter_type_definition_id")
    private UUID parameterTypeDefinitionId;

    @Column("default_value")
    private String defaultValue;

    @Transient
    @JsonIgnoreProperties(value = { "categoricalParameter" }, allowSetters = true)
    private List<CategoricalParameterValue> categoricalParameterValues = new ArrayList<>();
    // jhipster-needle-entity-add-field - JHipster will add fields here


    public CategoricalParameter id(UUID id) {
        this.setParameterTypeDefinitionId(id);
        return this;
    }


    public String getDefaultValue() {
        return this.defaultValue;
    }

    public CategoricalParameter defaultValue(String defaultValue) {
        this.setDefaultValue(defaultValue);
        return this;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public List<CategoricalParameterValue> getCategoricalParameterValues() {
        return this.categoricalParameterValues;
    }

    public void setCategoricalParameterValues(List<CategoricalParameterValue> categoricalParameterValues) {
        this.categoricalParameterValues = categoricalParameterValues;
    }

    public CategoricalParameter categoricalParameterValues(List<CategoricalParameterValue> categoricalParameterValues) {
        this.setCategoricalParameterValues(categoricalParameterValues);
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
        if (!(o instanceof CategoricalParameter)) {
            return false;
        }
        return getParameterTypeDefinitionId() != null && getParameterTypeDefinitionId().equals(((CategoricalParameter) o).getParameterTypeDefinitionId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore

    @Override
    public String toString() {
        return "CategoricalParameter{" +
                "parameterTypeDefinitionId=" + parameterTypeDefinitionId +
                ", defaultValue='" + defaultValue + '\'' +
                ", categoricalParameterValues=" + categoricalParameterValues +
                '}';
    }
}