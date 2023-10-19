package ai.turintech.catalog.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.*;
import java.io.Serializable;
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
    private Long id;

    @NotNull(message = "must not be null")
    @Column("lower")
    private Integer lower;

    @NotNull(message = "must not be null")
    @Column("upper")
    private Integer upper;

    @Transient
    @JsonIgnoreProperties(value = { "parameterTypeDefinition", "integerParameterValues" }, allowSetters = true)
    private IntegerParameter integerParameter;

    @Column("integer_parameter_id")
    private Long integerParameterId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public IntegerParameterValue id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
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

    public IntegerParameter getIntegerParameter() {
        return this.integerParameter;
    }

    public void setIntegerParameter(IntegerParameter integerParameter) {
        this.integerParameter = integerParameter;
        this.integerParameterId = integerParameter != null ? integerParameter.getId() : null;
    }

    public IntegerParameterValue integerParameter(IntegerParameter integerParameter) {
        this.setIntegerParameter(integerParameter);
        return this;
    }

    public Long getIntegerParameterId() {
        return this.integerParameterId;
    }

    public void setIntegerParameterId(Long integerParameter) {
        this.integerParameterId = integerParameter;
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
            "id=" + getId() +
            ", lower=" + getLower() +
            ", upper=" + getUpper() +
            "}";
    }
}
