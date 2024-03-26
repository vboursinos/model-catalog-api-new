package ai.turintech.modelcatalog.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/** A DTO for the IntegerParameter entity. */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class IntegerParameterDTO extends ParameterTypeDefinitionDTO {

  private Integer defaultValue;

  private List<IntegerParameterValueDTO> integerParameterValues = new ArrayList<>();

  public Integer getDefaultValue() {
    return defaultValue;
  }

  public void setDefaultValue(Integer defaultValue) {
    this.defaultValue = defaultValue;
  }

  public List<IntegerParameterValueDTO> getIntegerParameterValues() {
    return integerParameterValues;
  }

  public void setIntegerParameterValues(List<IntegerParameterValueDTO> integerParameterValues) {
    this.integerParameterValues = integerParameterValues;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof IntegerParameterDTO)) {
      return false;
    }

    IntegerParameterDTO integerParameterDTO = (IntegerParameterDTO) o;
    if (this.getId() == null) {
      return false;
    }
    return Objects.equals(this.getId(), integerParameterDTO.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.getId());
  }

  // prettier-ignore
  @Override
  public String toString() {
    return "IntegerParameterDTO{"
        + "parameterTypeDefinitionId="
        + getId()
        + ", defaultValue="
        + getDefaultValue()
        + "}";
  }
}
