package ai.turintech.catalog.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link ai.turintech.catalog.domain.CategoricalParameter} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CategoricalParameterDTO implements Serializable {

    private Long id;

    private String defaultValue;

    private ParameterTypeDefinitionDTO parameterTypeDefinition;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
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
        if (!(o instanceof CategoricalParameterDTO)) {
            return false;
        }

        CategoricalParameterDTO categoricalParameterDTO = (CategoricalParameterDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, categoricalParameterDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CategoricalParameterDTO{" +
            "id=" + getId() +
            ", defaultValue='" + getDefaultValue() + "'" +
            ", parameterTypeDefinition=" + getParameterTypeDefinition() +
            "}";
    }
}
