package ai.turintech.catalog.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.UUID;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A ParameterTypeDefinition.
 */
@Table("parameter_type_definition")
@JsonIgnoreProperties(value = { "new" })
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ParameterTypeDefinition implements Serializable, Persistable<UUID> {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private UUID id;

    @NotNull(message = "must not be null")
    @Column("ordering")
    private Integer ordering;

    @Transient
    private boolean isPersisted;

    @Transient
    private IntegerParameter integerParameter;

    @Transient
    private FloatParameter floatParameter;

    @Transient
    private CategoricalParameter categoricalParameter;

    @Transient
    private BooleanParameter booleanParameter;

    @Transient
    @JsonIgnoreProperties(value = { "definitions" }, allowSetters = true)
    private ParameterDistributionType distribution;

    @Transient
    @JsonIgnoreProperties(value = { "definitions", "model" }, allowSetters = true)
    private Parameter parameter;

    @Transient
    @JsonIgnoreProperties(value = { "definitions" }, allowSetters = true)
    private ParameterType type;

    @Column("parameter_distribution_type_id")
    private UUID distributionId;

    @Column("parameter_id")
    private UUID parameterId;

    @Column("parameter_type_id")
    private UUID typeId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public UUID getId() {
        return this.id;
    }

    public ParameterTypeDefinition id(UUID id) {
        this.setId(id);
        return this;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Integer getOrdering() {
        return this.ordering;
    }

    public ParameterTypeDefinition ordering(Integer ordering) {
        this.setOrdering(ordering);
        return this;
    }

    public void setOrdering(Integer ordering) {
        this.ordering = ordering;
    }

    @Transient
    @Override
    public boolean isNew() {
        return !this.isPersisted;
    }

    public ParameterTypeDefinition setIsPersisted() {
        this.isPersisted = true;
        return this;
    }

    public IntegerParameter getIntegerParameter() {
        return this.integerParameter;
    }

    public void setIntegerParameter(IntegerParameter integerParameter) {
        if (this.integerParameter != null) {
            this.integerParameter.setParameterTypeDefinition(null);
        }
        if (integerParameter != null) {
            integerParameter.setParameterTypeDefinition(this);
        }
        this.integerParameter = integerParameter;
    }

    public ParameterTypeDefinition integerParameter(IntegerParameter integerParameter) {
        this.setIntegerParameter(integerParameter);
        return this;
    }

    public FloatParameter getFloatParameter() {
        return this.floatParameter;
    }

    public void setFloatParameter(FloatParameter floatParameter) {
        if (this.floatParameter != null) {
            this.floatParameter.setParameterTypeDefinition(null);
        }
        if (floatParameter != null) {
            floatParameter.setParameterTypeDefinition(this);
        }
        this.floatParameter = floatParameter;
    }

    public ParameterTypeDefinition floatParameter(FloatParameter floatParameter) {
        this.setFloatParameter(floatParameter);
        return this;
    }

    public CategoricalParameter getCategoricalParameter() {
        return this.categoricalParameter;
    }

    public void setCategoricalParameter(CategoricalParameter categoricalParameter) {
        if (this.categoricalParameter != null) {
            this.categoricalParameter.setParameterTypeDefinition(null);
        }
        if (categoricalParameter != null) {
            categoricalParameter.setParameterTypeDefinition(this);
        }
        this.categoricalParameter = categoricalParameter;
    }

    public ParameterTypeDefinition categoricalParameter(CategoricalParameter categoricalParameter) {
        this.setCategoricalParameter(categoricalParameter);
        return this;
    }

    public BooleanParameter getBooleanParameter() {
        return this.booleanParameter;
    }

    public void setBooleanParameter(BooleanParameter booleanParameter) {
        if (this.booleanParameter != null) {
            this.booleanParameter.setParameterTypeDefinition(null);
        }
        if (booleanParameter != null) {
            booleanParameter.setParameterTypeDefinition(this);
        }
        this.booleanParameter = booleanParameter;
    }

    public ParameterTypeDefinition booleanParameter(BooleanParameter booleanParameter) {
        this.setBooleanParameter(booleanParameter);
        return this;
    }

    public ParameterDistributionType getDistribution() {
        return this.distribution;
    }

    public void setDistribution(ParameterDistributionType parameterDistributionType) {
        this.distribution = parameterDistributionType;
        this.distributionId = parameterDistributionType != null ? parameterDistributionType.getId() : null;
    }

    public ParameterTypeDefinition distribution(ParameterDistributionType parameterDistributionType) {
        this.setDistribution(parameterDistributionType);
        return this;
    }

    public Parameter getParameter() {
        return this.parameter;
    }

    public void setParameter(Parameter parameter) {
        this.parameter = parameter;
        this.parameterId = parameter != null ? parameter.getId() : null;
    }

    public ParameterTypeDefinition parameter(Parameter parameter) {
        this.setParameter(parameter);
        return this;
    }

    public ParameterType getType() {
        return this.type;
    }

    public void setType(ParameterType parameterType) {
        this.type = parameterType;
        this.typeId = parameterType != null ? parameterType.getId() : null;
    }

    public ParameterTypeDefinition type(ParameterType parameterType) {
        this.setType(parameterType);
        return this;
    }

    public UUID getDistributionId() {
        return this.distributionId;
    }

    public void setDistributionId(UUID parameterDistributionType) {
        this.distributionId = parameterDistributionType;
    }

    public UUID getParameterId() {
        return this.parameterId;
    }

    public void setParameterId(UUID parameter) {
        this.parameterId = parameter;
    }

    public UUID getTypeId() {
        return this.typeId;
    }

    public void setTypeId(UUID parameterType) {
        this.typeId = parameterType;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ParameterTypeDefinition)) {
            return false;
        }
        return getId() != null && getId().equals(((ParameterTypeDefinition) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore

    @Override
    public String toString() {
        return "ParameterTypeDefinition{" +
                "id=" + id +
                ", ordering=" + ordering +
                ", integerParameter=" + integerParameter +
                ", floatParameter=" + floatParameter +
                ", categoricalParameter=" + categoricalParameter +
                ", booleanParameter=" + booleanParameter +
                ", distributionId=" + distributionId +
                ", parameterId=" + parameterId +
                ", typeId=" + typeId +
                '}';
    }
}
