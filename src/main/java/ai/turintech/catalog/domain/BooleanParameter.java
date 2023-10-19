package ai.turintech.catalog.domain;

import java.io.Serializable;
import java.util.UUID;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A BooleanParameter.
 */
@Table("boolean_parameter")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class BooleanParameter implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @Column("default_value")
    private Boolean defaultValue;

    @Transient
    private ParameterTypeDefinition parameterTypeDefinition;

    @Column("parameter_type_definition_id")
    private UUID parameterTypeDefinitionId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public BooleanParameter id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getDefaultValue() {
        return this.defaultValue;
    }

    public BooleanParameter defaultValue(Boolean defaultValue) {
        this.setDefaultValue(defaultValue);
        return this;
    }

    public void setDefaultValue(Boolean defaultValue) {
        this.defaultValue = defaultValue;
    }

    public ParameterTypeDefinition getParameterTypeDefinition() {
        return this.parameterTypeDefinition;
    }

    public void setParameterTypeDefinition(ParameterTypeDefinition parameterTypeDefinition) {
        this.parameterTypeDefinition = parameterTypeDefinition;
        this.parameterTypeDefinitionId = parameterTypeDefinition != null ? parameterTypeDefinition.getId() : null;
    }

    public BooleanParameter parameterTypeDefinition(ParameterTypeDefinition parameterTypeDefinition) {
        this.setParameterTypeDefinition(parameterTypeDefinition);
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
        if (!(o instanceof BooleanParameter)) {
            return false;
        }
        return getId() != null && getId().equals(((BooleanParameter) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BooleanParameter{" +
            "id=" + getId() +
            ", defaultValue='" + getDefaultValue() + "'" +
            "}";
    }
}
