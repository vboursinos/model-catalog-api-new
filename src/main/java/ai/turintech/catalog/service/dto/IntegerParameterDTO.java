package ai.turintech.catalog.service.dto;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * A DTO for the {@link ai.turintech.catalog.domain.IntegerParameter} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class IntegerParameterDTO implements Serializable {

    private UUID parameterTypeDefinitionId;

    private Integer defaultValue;

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
