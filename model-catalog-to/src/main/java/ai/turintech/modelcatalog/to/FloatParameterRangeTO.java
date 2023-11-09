package ai.turintech.modelcatalog.to;

import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@SuppressWarnings("common-java:DuplicatedBlocks")
public class FloatParameterRangeTO implements Serializable {

    private UUID id;

    @NotNull(message = "must not be null")
    private Boolean isLeftOpen;

    @NotNull(message = "must not be null")
    private Boolean isRightOpen;

    @NotNull(message = "must not be null")
    private Double lower;

    @NotNull(message = "must not be null")
    private Double upper;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FloatParameterRangeTO)) {
            return false;
        }

        FloatParameterRangeTO floatParameterRangeDTO = (FloatParameterRangeTO) o;
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
            "}";
    }
}
