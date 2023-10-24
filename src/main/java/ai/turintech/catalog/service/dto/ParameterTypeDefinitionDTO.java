package ai.turintech.catalog.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * A DTO for the {@link ai.turintech.catalog.domain.ParameterTypeDefinition} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ParameterTypeDefinitionDTO implements Serializable {

    private UUID id;

    @NotNull(message = "must not be null")
    private Integer ordering;

    @NotNull(message = "must not be null")
    private UUID distributionId;

    @NotNull(message = "must not be null")
    private UUID parameterId;

    @NotNull(message = "must not be null")
    private UUID typeId;
    private ParameterDistributionTypeDTO distribution;

    private CategoricalParameterDTO categoricalParameter;
    private BooleanParameterDTO booleanParameter;
    private FloatParameterDTO floatParameter;
    private IntegerParameterDTO integerParameter;


//    private ParameterDTO parameter;
//
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

    public UUID getDistributionId() {
        return distributionId;
    }

    public void setDistributionId(UUID distributionId) {
        this.distributionId = distributionId;
    }

    public UUID getParameterId() {
        return parameterId;
    }

    public void setParameterId(UUID parameterId) {
        this.parameterId = parameterId;
    }

    public UUID getTypeId() {
        return typeId;
    }

    public void setTypeId(UUID typeId) {
        this.typeId = typeId;
    }

    public ParameterDistributionTypeDTO getDistribution() {
        return distribution;
    }

    public void setDistribution(ParameterDistributionTypeDTO distribution) {
        this.distribution = distribution;
    }

    public ParameterTypeDTO getType() {
        return type;
    }

    public void setType(ParameterTypeDTO type) {
        this.type = type;
    }

    public CategoricalParameterDTO getCategoricalParameter() {
        return categoricalParameter;
    }

    public void setCategoricalParameter(CategoricalParameterDTO categoricalParameter) {
        this.categoricalParameter = categoricalParameter;
    }

    public BooleanParameterDTO getBooleanParameter() {
        return booleanParameter;
    }

    public void setBooleanParameter(BooleanParameterDTO booleanParameter) {
        this.booleanParameter = booleanParameter;
    }

    public FloatParameterDTO getFloatParameter() {
        return floatParameter;
    }

    public void setFloatParameter(FloatParameterDTO floatParameter) {
        this.floatParameter = floatParameter;
    }

    public IntegerParameterDTO getIntegerParameter() {
        return integerParameter;
    }

    public void setIntegerParameter(IntegerParameterDTO integerParameter) {
        this.integerParameter = integerParameter;
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
                "id=" + id +
                ", ordering=" + ordering +
                ", distributionId=" + distributionId +
                ", parameterId=" + parameterId +
                ", typeId=" + typeId +
                ", distribution=" + distribution +
                ", type=" + type +
                '}';
    }
}
