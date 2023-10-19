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
    @Column("id")
    private Long id;

    @Column("default_value")
    private String defaultValue;

    @Transient
    private ParameterTypeDefinition parameterTypeDefinition;

    @Transient
    @JsonIgnoreProperties(value = { "categoricalParameter" }, allowSetters = true)
    private Set<CategoricalParameterValue> categoricalParameterValues = new HashSet<>();

    @Column("parameter_type_definition_id")
    private UUID parameterTypeDefinitionId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CategoricalParameter id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
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

    public ParameterTypeDefinition getParameterTypeDefinition() {
        return this.parameterTypeDefinition;
    }

    public void setParameterTypeDefinition(ParameterTypeDefinition parameterTypeDefinition) {
        this.parameterTypeDefinition = parameterTypeDefinition;
        this.parameterTypeDefinitionId = parameterTypeDefinition != null ? parameterTypeDefinition.getId() : null;
    }

    public CategoricalParameter parameterTypeDefinition(ParameterTypeDefinition parameterTypeDefinition) {
        this.setParameterTypeDefinition(parameterTypeDefinition);
        return this;
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
        return getId() != null && getId().equals(((CategoricalParameter) o).getId());
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
            "id=" + getId() +
            ", defaultValue='" + getDefaultValue() + "'" +
            "}";
    }
}
