package ai.turintech.catalog.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link ai.turintech.catalog.domain.BooleanParameter} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class BooleanParameterDTO implements Serializable {

    private Long id;

    private Boolean defaultValue;

    private ParameterTypeDefinitionDTO parameterTypeDefinition;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(Boolean defaultValue) {
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
        if (!(o instanceof BooleanParameterDTO)) {
            return false;
        }

        BooleanParameterDTO booleanParameterDTO = (BooleanParameterDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, booleanParameterDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BooleanParameterDTO{" +
            "id=" + getId() +
            ", defaultValue='" + getDefaultValue() + "'" +
            ", parameterTypeDefinition=" + getParameterTypeDefinition() +
            "}";
    }
}
