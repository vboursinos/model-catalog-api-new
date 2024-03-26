package ai.turintech.modelcatalog.dto;

import java.util.Objects;

/** A DTO for the BooleanParameter entity */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class BooleanParameterDTO extends ParameterTypeDefinitionDTO {

  private Boolean defaultValue;

  public Boolean getDefaultValue() {
    return defaultValue;
  }

  public void setDefaultValue(Boolean defaultValue) {
    this.defaultValue = defaultValue;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof BooleanParameterDTO)) {
      return false;
    }

    BooleanParameterDTO booleanParameterDTO = (BooleanParameterDTO) o;
    if (this.getId() == null) {
      return false;
    }
    return Objects.equals(this.getId(), booleanParameterDTO.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.getId());
  }

  // prettier-ignore
  @Override
  public String toString() {
    return "BooleanParameterDTO{"
        + "parameterTypeDefinitionId="
        + getId()
        + ", defaultValue="
        + defaultValue
        + '}';
  }
}
