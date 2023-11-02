package ai.turintech.catalog.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * A FloatParameter.
 */
@Entity
@Table(name = "float_parameter")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FloatParameter implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "default_value")
    private Double defaultValue;

    @Id
    @JsonIgnoreProperties(
        value = { "integerParameter", "floatParameter", "categoricalParameter", "booleanParameter", "distribution", "parameter", "type" },
        allowSetters = true
    )
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private ParameterTypeDefinition parameterTypeDefinition;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "floatParameter")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "floatParameter" }, allowSetters = true)
    private Set<FloatParameterRange> floatParameterRanges = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Double getDefaultValue() {
        return this.defaultValue;
    }

    public FloatParameter defaultValue(Double defaultValue) {
        this.setDefaultValue(defaultValue);
        return this;
    }

    public void setDefaultValue(Double defaultValue) {
        this.defaultValue = defaultValue;
    }

    public ParameterTypeDefinition getParameterTypeDefinition() {
        return this.parameterTypeDefinition;
    }

    public void setParameterTypeDefinition(ParameterTypeDefinition parameterTypeDefinition) {
        this.parameterTypeDefinition = parameterTypeDefinition;
    }

    public FloatParameter parameterTypeDefinition(ParameterTypeDefinition parameterTypeDefinition) {
        this.setParameterTypeDefinition(parameterTypeDefinition);
        return this;
    }

    public Set<FloatParameterRange> getFloatParameterRanges() {
        return this.floatParameterRanges;
    }

    public void setFloatParameterRanges(Set<FloatParameterRange> floatParameterRanges) {
        if (this.floatParameterRanges != null) {
            this.floatParameterRanges.forEach(i -> i.setFloatParameter(null));
        }
        if (floatParameterRanges != null) {
            floatParameterRanges.forEach(i -> i.setFloatParameter(this));
        }
        this.floatParameterRanges = floatParameterRanges;
    }

    public FloatParameter floatParameterRanges(Set<FloatParameterRange> floatParameterRanges) {
        this.setFloatParameterRanges(floatParameterRanges);
        return this;
    }

    public FloatParameter addFloatParameterRange(FloatParameterRange floatParameterRange) {
        this.floatParameterRanges.add(floatParameterRange);
        floatParameterRange.setFloatParameter(this);
        return this;
    }

    public FloatParameter removeFloatParameterRange(FloatParameterRange floatParameterRange) {
        this.floatParameterRanges.remove(floatParameterRange);
        floatParameterRange.setFloatParameter(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here


    @Override
    public String toString() {
        return "FloatParameter{" +
                "defaultValue=" + defaultValue +
                ", parameterTypeDefinition=" + parameterTypeDefinition +
                '}';
    }
}
