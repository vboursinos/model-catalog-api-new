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
    private Set<CategoricalParameterValue> categoricalParameterValues = new HashSet<>();
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

    public Set<CategoricalParameterValue> getCategoricalParameterValues() {
        return this.categoricalParameterValues;
    }

    public void setCategoricalParameterValues(Set<CategoricalParameterValue> categoricalParameterValues) {
        if (this.categoricalParameterValues != null) {
            this.categoricalParameterValues.forEach(i -> i.setCategoricalParameter(null));
        }
        if (categoricalParameterValues != null) {
            categoricalParameterValues.forEach(i -> i.setCategoricalParameter(this));
        }
        this.categoricalParameterValues = categoricalParameterValues;
    }

    public CategoricalParameter categoricalParameterValues(Set<CategoricalParameterValue> categoricalParameterValues) {
        this.setCategoricalParameterValues(categoricalParameterValues);
        return this;
    }

    public CategoricalParameter addCategoricalParameterValue(CategoricalParameterValue categoricalParameterValue) {
        this.categoricalParameterValues.add(categoricalParameterValue);
        categoricalParameterValue.setCategoricalParameter(this);
        return this;
    }

    public CategoricalParameter removeCategoricalParameterValue(CategoricalParameterValue categoricalParameterValue) {
        this.categoricalParameterValues.remove(categoricalParameterValue);
        categoricalParameterValue.setCategoricalParameter(null);
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
            "parameterTypeDefinitionId=" + getParameterTypeDefinitionId() +
            ", defaultValue='" + getDefaultValue() + "'" +
            "}";
    }
}
