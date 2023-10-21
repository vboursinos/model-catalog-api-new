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
 * A IntegerParameterValue.
 */
@Table("integer_parameter_value")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class IntegerParameterValue implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private UUID id;

    @NotNull(message = "must not be null")
    @Column("lower")
    private Integer lower;

    @NotNull(message = "must not be null")
    @Column("upper")
    private Integer upper;

    @Column("parameter_type_definition_id")
    private UUID parameterTypeDefinitionId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public UUID getId() {
        return this.id;
    }

    public IntegerParameterValue id(UUID id) {
        this.setId(id);
        return this;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Integer getLower() {
        return this.lower;
    }

    public IntegerParameterValue lower(Integer lower) {
        this.setLower(lower);
        return this;
    }

    public void setLower(Integer lower) {
        this.lower = lower;
    }

    public Integer getUpper() {
        return this.upper;
    }

    public IntegerParameterValue upper(Integer upper) {
        this.setUpper(upper);
        return this;
    }

    public void setUpper(Integer upper) {
        this.upper = upper;
    }

    public UUID getParameterTypeDefinitionId() {
        return parameterTypeDefinitionId;
    }

    public void setParameterTypeDefinitionId(UUID parameterTypeDefinitionId) {
        this.parameterTypeDefinitionId = parameterTypeDefinitionId;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof IntegerParameterValue)) {
            return false;
        }
        return getId() != null && getId().equals(((IntegerParameterValue) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore

    @Override
    public String toString() {
        return "IntegerParameterValue{" +
                "id=" + id +
                ", lower=" + lower +
                ", upper=" + upper +
                ", parameterTypeDefinitionId=" + parameterTypeDefinitionId +
                '}';
    }
}
