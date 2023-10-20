package ai.turintech.catalog.service.dto;

import ai.turintech.catalog.domain.ParameterTypeDefinition;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * A DTO for the {@link ai.turintech.catalog.domain.Parameter} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ParameterDTO implements Serializable {

    private UUID id;

    @NotNull(message = "must not be null")
    private String name;

    @NotNull(message = "must not be null")
    private String label;

    private String description;

    @NotNull(message = "must not be null")
    private Boolean enabled;

    @NotNull(message = "must not be null")
    private Boolean fixedValue;

    @NotNull(message = "must not be null")
    private Integer ordering;

    @NotNull
    private UUID modelId;

    @NotNull
    private List<ParameterTypeDefinitionDTO> definitions = new ArrayList<>();


    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Boolean getFixedValue() {
        return fixedValue;
    }

    public void setFixedValue(Boolean fixedValue) {
        this.fixedValue = fixedValue;
    }

    public Integer getOrdering() {
        return ordering;
    }

    public void setOrdering(Integer ordering) {
        this.ordering = ordering;
    }

    public UUID getModelId() {
        return modelId;
    }

    public void setModelId(UUID modelId) {
        this.modelId = modelId;
    }

    public List<ParameterTypeDefinitionDTO> getDefinitions() {
        return definitions;
    }

    public void setDefinitions(List<ParameterTypeDefinitionDTO> definitions) {
        this.definitions = definitions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ParameterDTO)) {
            return false;
        }

        ParameterDTO parameterDTO = (ParameterDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, parameterDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ParameterDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", label='" + label + '\'' +
                ", description='" + description + '\'' +
                ", enabled=" + enabled +
                ", fixedValue=" + fixedValue +
                ", ordering=" + ordering +
                ", modelId=" + modelId +
                ", definitions=" + definitions +
                '}';
    }
}
