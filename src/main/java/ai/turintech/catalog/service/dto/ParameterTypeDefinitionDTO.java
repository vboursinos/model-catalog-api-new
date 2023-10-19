package ai.turintech.catalog.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * A DTO for the {@link ai.turintech.catalog.domain.ParameterTypeDefinition} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ParameterTypeDefinitionDTO implements Serializable {

    private UUID id;

    @NotNull(message = "must not be null")
    private Integer ordering;

    private ParameterDistributionTypeDTO distribution;

    private ParameterDTO parameter;

    private ParameterTypeDTO type;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Integer getOrdering() {
        return ordering;
    }

    public void setOrdering(Integer ordering) {
        this.ordering = ordering;
    }

    public ParameterDistributionTypeDTO getDistribution() {
        return distribution;
    }

    public void setDistribution(ParameterDistributionTypeDTO distribution) {
        this.distribution = distribution;
    }

    public ParameterDTO getParameter() {
        return parameter;
    }

    public void setParameter(ParameterDTO parameter) {
        this.parameter = parameter;
    }

    public ParameterTypeDTO getType() {
        return type;
    }

    public void setType(ParameterTypeDTO type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ParameterTypeDefinitionDTO)) {
            return false;
        }

        ParameterTypeDefinitionDTO parameterTypeDefinitionDTO = (ParameterTypeDefinitionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, parameterTypeDefinitionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ParameterTypeDefinitionDTO{" +
            "id='" + getId() + "'" +
            ", ordering=" + getOrdering() +
            ", distribution=" + getDistribution() +
            ", parameter=" + getParameter() +
            ", type=" + getType() +
            "}";
    }
}
