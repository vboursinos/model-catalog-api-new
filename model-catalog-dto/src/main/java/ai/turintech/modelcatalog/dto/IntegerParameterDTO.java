package ai.turintech.modelcatalog.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * A DTO for the IntegerParameter entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class IntegerParameterDTO implements Serializable {

    private UUID parameterTypeDefinitionId;

    private Integer defaultValue;

    private List<IntegerParameterValueDTO> integerParameterValues = new ArrayList<>();

    public UUID getParameterTypeDefinitionId() {
        return parameterTypeDefinitionId;
    }

    public void setParameterTypeDefinitionId(UUID parameterTypeDefinitionId) {
        this.parameterTypeDefinitionId = parameterTypeDefinitionId;
    }

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
        if (this.parameterTypeDefinitionId == null) {
            return false;
        }
        return Objects.equals(this.parameterTypeDefinitionId, integerParameterDTO.parameterTypeDefinitionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.parameterTypeDefinitionId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "IntegerParameterDTO{" +
            "parameterTypeDefinitionId" + getParameterTypeDefinitionId() +
            ", defaultValue=" + getDefaultValue() +
            "}";
    }
}
