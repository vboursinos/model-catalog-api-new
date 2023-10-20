package ai.turintech.catalog.service.dto;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * A DTO for the {@link ai.turintech.catalog.domain.BooleanParameter} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class BooleanParameterDTO implements Serializable {

    private UUID parameterTypeDefinitionId;

    private Boolean defaultValue;

    public UUID getParameterTypeDefinitionId() {
        return parameterTypeDefinitionId;
    }

    public void setParameterTypeDefinitionId(UUID parameterTypeDefinitionId) {
        this.parameterTypeDefinitionId = parameterTypeDefinitionId;
    }

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
        if (this.getParameterTypeDefinitionId() == null) {
            return false;
        }
        return Objects.equals(this.parameterTypeDefinitionId, booleanParameterDTO.parameterTypeDefinitionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.parameterTypeDefinitionId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BooleanParameterDTO{" +
                "parameterTypeDefinitionId=" + parameterTypeDefinitionId +
                ", defaultValue=" + defaultValue +
                '}';
    }
}
