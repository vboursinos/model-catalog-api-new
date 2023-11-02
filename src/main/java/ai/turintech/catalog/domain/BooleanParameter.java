package ai.turintech.catalog.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.io.Serializable;
import java.util.UUID;

/**
 * A BooleanParameter.
 */
@Entity
@Table(name = "boolean_parameter")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class BooleanParameter implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "default_value")
    private Boolean defaultValue;

    @Id
    @JsonIgnoreProperties(
        value = { "integerParameter", "floatParameter", "categoricalParameter", "booleanParameter", "distribution", "parameter", "type" },
        allowSetters = true
    )
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private ParameterTypeDefinition parameterTypeDefinition;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Boolean getDefaultValue() {
        return this.defaultValue;
    }

    public BooleanParameter defaultValue(Boolean defaultValue) {
        this.setDefaultValue(defaultValue);
        return this;
    }

    public void setDefaultValue(Boolean defaultValue) {
        this.defaultValue = defaultValue;
    }

    public ParameterTypeDefinition getParameterTypeDefinition() {
        return this.parameterTypeDefinition;
    }

    public void setParameterTypeDefinition(ParameterTypeDefinition parameterTypeDefinition) {
        this.parameterTypeDefinition = parameterTypeDefinition;
    }

    public BooleanParameter parameterTypeDefinition(ParameterTypeDefinition parameterTypeDefinition) {
        this.setParameterTypeDefinition(parameterTypeDefinition);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public String toString() {
        return "BooleanParameter{" +
                "defaultValue=" + defaultValue +
                ", parameterTypeDefinition=" + parameterTypeDefinition +
                '}';
    }
}
