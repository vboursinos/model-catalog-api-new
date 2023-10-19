package ai.turintech.catalog.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link ai.turintech.catalog.domain.FloatParameterRange} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FloatParameterRangeDTO implements Serializable {

    private Long id;

    @NotNull(message = "must not be null")
    private Boolean isLeftOpen;

    @NotNull(message = "must not be null")
    private Boolean isRightOpen;

    @NotNull(message = "must not be null")
    private Double lower;

    @NotNull(message = "must not be null")
    private Double upper;

    private FloatParameterDTO floatParameter;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getIsLeftOpen() {
        return isLeftOpen;
    }

    public void setIsLeftOpen(Boolean isLeftOpen) {
        this.isLeftOpen = isLeftOpen;
    }

    public Boolean getIsRightOpen() {
        return isRightOpen;
    }

    public void setIsRightOpen(Boolean isRightOpen) {
        this.isRightOpen = isRightOpen;
    }

    public Double getLower() {
        return lower;
    }

    public void setLower(Double lower) {
        this.lower = lower;
    }

    public Double getUpper() {
        return upper;
    }

    public void setUpper(Double upper) {
        this.upper = upper;
    }

    public FloatParameterDTO getFloatParameter() {
        return floatParameter;
    }

    public void setFloatParameter(FloatParameterDTO floatParameter) {
        this.floatParameter = floatParameter;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FloatParameterRangeDTO)) {
            return false;
        }

        FloatParameterRangeDTO floatParameterRangeDTO = (FloatParameterRangeDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, floatParameterRangeDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FloatParameterRangeDTO{" +
            "id=" + getId() +
            ", isLeftOpen='" + getIsLeftOpen() + "'" +
            ", isRightOpen='" + getIsRightOpen() + "'" +
            ", lower=" + getLower() +
            ", upper=" + getUpper() +
            ", floatParameter=" + getFloatParameter() +
            "}";
    }
}
