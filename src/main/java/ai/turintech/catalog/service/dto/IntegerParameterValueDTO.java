package ai.turintech.catalog.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link ai.turintech.catalog.domain.IntegerParameterValue} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class IntegerParameterValueDTO implements Serializable {

    private Long id;

    @NotNull(message = "must not be null")
    private Integer lower;

    @NotNull(message = "must not be null")
    private Integer upper;

    private IntegerParameterDTO integerParameter;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getLower() {
        return lower;
    }

    public void setLower(Integer lower) {
        this.lower = lower;
    }

    public Integer getUpper() {
        return upper;
    }

    public void setUpper(Integer upper) {
        this.upper = upper;
    }

    public IntegerParameterDTO getIntegerParameter() {
        return integerParameter;
    }

    public void setIntegerParameter(IntegerParameterDTO integerParameter) {
        this.integerParameter = integerParameter;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof IntegerParameterValueDTO)) {
            return false;
        }

        IntegerParameterValueDTO integerParameterValueDTO = (IntegerParameterValueDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, integerParameterValueDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "IntegerParameterValueDTO{" +
            "id=" + getId() +
            ", lower=" + getLower() +
            ", upper=" + getUpper() +
            ", integerParameter=" + getIntegerParameter() +
            "}";
    }
}
