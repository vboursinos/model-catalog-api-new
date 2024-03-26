package ai.turintech.modelcatalog.dto;

import ai.turintech.components.data.common.dto.AbstractUUIDIdentityDTO;
import jakarta.validation.constraints.NotNull;
import java.util.Objects;

/** A DTO for the IntegerParameterValue entity. */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class IntegerParameterValueDTO extends AbstractUUIDIdentityDTO {

  @NotNull(message = "must not be null")
  private Integer lower;

  @NotNull(message = "must not be null")
  private Integer upper;

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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof IntegerParameterValueDTO)) {
      return false;
    }

    IntegerParameterValueDTO integerParameterValueDTO = (IntegerParameterValueDTO) o;
    if (this.getId() == null) {
      return false;
    }
    return Objects.equals(this.getId(), integerParameterValueDTO.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.getId());
  }

  // prettier-ignore
  @Override
  public String toString() {
    return "IntegerParameterValueDTO{"
        + "id="
        + getId()
        + ", lower="
        + getLower()
        + ", upper="
        + getUpper()
        + "}";
  }
}
