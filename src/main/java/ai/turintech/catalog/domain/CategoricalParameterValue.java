package ai.turintech.catalog.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A CategoricalParameterValue.
 */
@Table("categorical_parameter_value")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CategoricalParameterValue implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private UUID id;

    @NotNull(message = "must not be null")
    @Column("value")
    private String value;

    @Column("parameter_type_definition_id")
    private UUID parameterTypeDefinitionId;

    // jhipster-needle-entity-add-field - JHipster will add fields here


    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getParameterTypeDefinitionId() {
        return parameterTypeDefinitionId;
    }

    public void setParameterTypeDefinitionId(UUID parameterTypeDefinitionId) {
        this.parameterTypeDefinitionId = parameterTypeDefinitionId;
    }

    public CategoricalParameterValue id(UUID id) {
        this.setId(id);
        return this;
    }


    public String getValue() {
        return this.value;
    }

    public CategoricalParameterValue value(String value) {
        this.setValue(value);
        return this;
    }

    public void setValue(String value) {
        this.value = value;
    }



// jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CategoricalParameterValue)) {
            return false;
        }
        return getId() != null && getId().equals(((CategoricalParameterValue) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore

    @Override
    public String toString() {
        return "CategoricalParameterValue{" +
                "id=" + id +
                ", value='" + value + '\'' +
                ", parameterTypeDefinitionId=" + parameterTypeDefinitionId +
                '}';
    }
}
