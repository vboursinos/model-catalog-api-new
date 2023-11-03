package ai.turintech.catalog.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.io.Serializable;
import java.util.UUID;

/**
 * A ParameterTypeDefinition.
 */
@Entity
@Table(name = "parameter_type_definition")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ParameterTypeDefinition implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    @Column(name = "id")
    private UUID id;

    @NotNull
    @Column(name = "ordering", nullable = false)
    private Integer ordering;

    @JsonIgnoreProperties(value = { "parameterTypeDefinition", "integerParameterValues" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "parameterTypeDefinition")
    @JoinColumn(name = "parameter_type_definition_id", referencedColumnName = "id")
    private IntegerParameter integerParameter;

    @JsonIgnoreProperties(value = { "parameterTypeDefinition", "floatParameterRanges" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "parameterTypeDefinition")
    @JoinColumn(name = "parameter_type_definition_id", referencedColumnName = "id")
    private FloatParameter floatParameter;

    @JsonIgnoreProperties(value = { "parameterTypeDefinition", "categoricalParameterValues" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "parameterTypeDefinition")
    @JoinColumn(name = "parameter_type_definition_id", referencedColumnName = "id")
    private CategoricalParameter categoricalParameter;

    @JsonIgnoreProperties(value = { "parameterTypeDefinition" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "parameterTypeDefinition")
    @JoinColumn(name = "parameter_type_definition_id", referencedColumnName = "id")
    private BooleanParameter booleanParameter;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "definitions" }, allowSetters = true)
    @JoinColumn(name = "parameter_distribution_type_id", referencedColumnName = "id")
    private ParameterDistributionType distribution;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "definitions", "model" }, allowSetters = true)
    @JoinColumn(name = "parameter_id", referencedColumnName = "id")
    private Parameter parameter;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "definitions" }, allowSetters = true)
    @JoinColumn(name = "parameter_type_id", referencedColumnName = "id")
    private ParameterType type;

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
    }

    public ParameterTypeDefinition type(ParameterType parameterType) {
        this.setType(parameterType);
        return this;
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
            "id=" + getId() +
            ", ordering=" + getOrdering() +
            "}";
    }
}
