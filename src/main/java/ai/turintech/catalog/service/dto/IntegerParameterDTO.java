package ai.turintech.catalog.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link ai.turintech.catalog.domain.IntegerParameter} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class IntegerParameterDTO implements Serializable {

    private Long id;

    private Integer defaultValue;

    private ParameterTypeDefinitionDTO parameterTypeDefinition;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(Integer defaultValue) {
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
        if (!(o instanceof IntegerParameterDTO)) {
            return false;
        }

        IntegerParameterDTO integerParameterDTO = (IntegerParameterDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, integerParameterDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "IntegerParameterDTO{" +
            "id=" + getId() +
            ", defaultValue=" + getDefaultValue() +
            ", parameterTypeDefinition=" + getParameterTypeDefinition() +
            "}";
    }
}
