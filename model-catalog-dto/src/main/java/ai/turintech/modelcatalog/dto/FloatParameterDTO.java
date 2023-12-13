package ai.turintech.modelcatalog.dto;

import ai.turintech.components.data.common.dto.AbstractUUIDIdentityDTO;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/** A DTO for the FloatParameter entity. */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FloatParameterDTO extends AbstractUUIDIdentityDTO<UUID> {

  private UUID parameterTypeDefinitionId;

  private Double defaultValue;

  private List<FloatParameterRangeDTO> floatParameterRanges = new ArrayList<>();

  public Double getDefaultValue() {
    return defaultValue;
  }

  public void setDefaultValue(Double defaultValue) {
    this.defaultValue = defaultValue;
  }

  public UUID getParameterTypeDefinitionId() {
    return parameterTypeDefinitionId;
  }

  public void setParameterTypeDefinitionId(UUID parameterTypeDefinitionId) {
    this.parameterTypeDefinitionId = parameterTypeDefinitionId;
  }

  public List<FloatParameterRangeDTO> getFloatParameterRanges() {
    return floatParameterRanges;
  }

  public void setFloatParameterRanges(List<FloatParameterRangeDTO> floatParameterRanges) {
    this.floatParameterRanges = floatParameterRanges;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof FloatParameterDTO)) {
      return false;
    }

    FloatParameterDTO floatParameterDTO = (FloatParameterDTO) o;
    if (this.parameterTypeDefinitionId == null) {
      return false;
    }
    return Objects.equals(
        this.parameterTypeDefinitionId, floatParameterDTO.parameterTypeDefinitionId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.parameterTypeDefinitionId);
  }

  // prettier-ignore
  @Override
  public String toString() {
    return "FloatParameterDTO{"
        + "parameterTypeDefinitionId="
        + getParameterTypeDefinitionId()
        + ", defaultValue="
        + getDefaultValue()
        + "}";
  }
}
