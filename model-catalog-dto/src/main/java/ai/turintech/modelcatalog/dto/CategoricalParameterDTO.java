package ai.turintech.modelcatalog.dto;

import ai.turintech.components.data.common.dto.AbstractDTO;

import java.io.Serializable;
import java.util.*;

/** A DTO for the CategoricalParameter entity. */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CategoricalParameterDTO extends AbstractDTO {

  private UUID parameterTypeDefinitionId;

  private String defaultValue;

  private Set<CategoricalParameterValueDTO> categoricalParameterValues = new HashSet<>();;

  public String getDefaultValue() {
    return defaultValue;
  }

  public void setDefaultValue(String defaultValue) {
    this.defaultValue = defaultValue;
  }

  public UUID getParameterTypeDefinitionId() {
    return parameterTypeDefinitionId;
  }

  public void setParameterTypeDefinitionId(UUID parameterTypeDefinitionId) {
    this.parameterTypeDefinitionId = parameterTypeDefinitionId;
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
    if (this.parameterTypeDefinitionId == null) {
      return false;
    }
    return Objects.equals(
        this.parameterTypeDefinitionId, categoricalParameterDTO.parameterTypeDefinitionId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.parameterTypeDefinitionId);
  }

  @Override
  public String toString() {
    return "CategoricalParameterDTO{"
        + "parameterTypeDefinitionId="
        + parameterTypeDefinitionId
        + ", defaultValue='"
        + defaultValue
        + '\''
        + '}';
  }
}
