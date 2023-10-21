package ai.turintech.catalog.service.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * A DTO for the {@link ai.turintech.catalog.domain.CategoricalParameter} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CategoricalParameterDTO implements Serializable {

    private UUID parameterTypeDefinitionId;

    private String defaultValue;

    private List<CategoricalParameterValueDTO> categoricalParameterValues = new ArrayList<>();;

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

    public List<CategoricalParameterValueDTO> getCategoricalParameterValues() {
        return categoricalParameterValues;
    }

    public void setCategoricalParameterValues(List<CategoricalParameterValueDTO> categoricalParameterValues) {
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
        return Objects.equals(this.parameterTypeDefinitionId, categoricalParameterDTO.parameterTypeDefinitionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.parameterTypeDefinitionId);
    }

    @Override
    public String toString() {
        return "CategoricalParameterDTO{" +
                "parameterTypeDefinitionId=" + parameterTypeDefinitionId +
                ", defaultValue='" + defaultValue + '\'' +
                '}';
    }
}
