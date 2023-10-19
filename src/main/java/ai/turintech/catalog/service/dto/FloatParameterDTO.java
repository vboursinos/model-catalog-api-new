package ai.turintech.catalog.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link ai.turintech.catalog.domain.FloatParameter} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FloatParameterDTO implements Serializable {

    private Long id;

    private Double defaultValue;

    private ParameterTypeDefinitionDTO parameterTypeDefinition;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(Double defaultValue) {
        this.defaultValue = defaultValue;
    }

    public ParameterTypeDefinitionDTO getParameterTypeDefinition() {
        return parameterTypeDefinition;
    }

    public void setParameterTypeDefinition(ParameterTypeDefinitionDTO parameterTypeDefinition) {
        this.parameterTypeDefinition = parameterTypeDefinition;
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
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, floatParameterDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FloatParameterDTO{" +
            "id=" + getId() +
            ", defaultValue=" + getDefaultValue() +
            ", parameterTypeDefinition=" + getParameterTypeDefinition() +
            "}";
    }
}
