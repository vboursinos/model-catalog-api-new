package ai.turintech.modelcatalog.dto;

import java.util.*;

/** A DTO for the CategoricalParameter entity. */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CategoricalParameterDTO extends ParameterTypeDefinitionDTO {
  private String defaultValue;

  private Set<CategoricalParameterValueDTO> categoricalParameterValues = new HashSet<>();;

  public String getDefaultValue() {
    return defaultValue;
  }

  public void setDefaultValue(String defaultValue) {
    this.defaultValue = defaultValue;
  }

  public Set<CategoricalParameterValueDTO> getCategoricalParameterValues() {
    return categoricalParameterValues;
  }

  public void setCategoricalParameterValues(
      Set<CategoricalParameterValueDTO> categoricalParameterValues) {
    this.categoricalParameterValues = categoricalParameterValues;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof CategoricalParameterDTO)) {
      return false;
    }

    CategoricalParameterDTO categoricalParameterDTO = (CategoricalParameterDTO) o;
    if (this.getId() == null) {
      return false;
    }
    return Objects.equals(this.getId(), categoricalParameterDTO.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.getId());
  }

  @Override
  public String toString() {
    return "CategoricalParameterDTO{"
        + "parameterTypeDefinitionId="
        + getId()
        + ", defaultValue='"
        + defaultValue
        + '\''
        + '}';
  }
}
