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
 * A FloatParameterRange.
 */
@Table("float_parameter_range")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FloatParameterRange implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private UUID id;

    @NotNull(message = "must not be null")
    @Column("is_left_open")
    private Boolean isLeftOpen;

    @NotNull(message = "must not be null")
    @Column("is_right_open")
    private Boolean isRightOpen;

    @NotNull(message = "must not be null")
    @Column("lower")
    private Double lower;

    @NotNull(message = "must not be null")
    @Column("upper")
    private Double upper;

    @Column("parameter_type_definition_id")
    private UUID parameterTypeDefinitionId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public UUID getId() {
        return this.id;
    }

    public FloatParameterRange id(UUID id) {
        this.setId(id);
        return this;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Boolean getIsLeftOpen() {
        return this.isLeftOpen;
    }

    public FloatParameterRange isLeftOpen(Boolean isLeftOpen) {
        this.setIsLeftOpen(isLeftOpen);
        return this;
    }

    public void setIsLeftOpen(Boolean isLeftOpen) {
        this.isLeftOpen = isLeftOpen;
    }

    public Boolean getIsRightOpen() {
        return this.isRightOpen;
    }

    public FloatParameterRange isRightOpen(Boolean isRightOpen) {
        this.setIsRightOpen(isRightOpen);
        return this;
    }

    public void setIsRightOpen(Boolean isRightOpen) {
        this.isRightOpen = isRightOpen;
    }

    public Double getLower() {
        return this.lower;
    }

    public FloatParameterRange lower(Double lower) {
        this.setLower(lower);
        return this;
    }

    public void setLower(Double lower) {
        this.lower = lower;
    }

    public Double getUpper() {
        return this.upper;
    }

    public FloatParameterRange upper(Double upper) {
        this.setUpper(upper);
        return this;
    }

    public void setUpper(Double upper) {
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
        if (!(o instanceof FloatParameterRange)) {
            return false;
        }
        return getId() != null && getId().equals(((FloatParameterRange) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FloatParameterRange{" +
                "id=" + id +
                ", isLeftOpen=" + isLeftOpen +
                ", isRightOpen=" + isRightOpen +
                ", lower=" + lower +
                ", upper=" + upper +
                ", parameterTypeDefinitionId=" + parameterTypeDefinitionId +
                '}';
    }
}
