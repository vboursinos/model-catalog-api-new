package ai.turintech.modelcatalog.dto;

import ai.turintech.components.data.common.dto.AbstractUUIDIdentityDTO;
import jakarta.validation.constraints.NotNull;

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
