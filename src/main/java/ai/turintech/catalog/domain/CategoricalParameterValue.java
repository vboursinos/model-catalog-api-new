package ai.turintech.catalog.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.io.Serializable;
import java.util.UUID;

/**
 * A CategoricalParameterValue.
 */
@Entity
@Table(name = "categorical_parameter_value")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CategoricalParameterValue implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    @Column(name = "id")
    private UUID id;

    @NotNull
    @Column(name = "value", nullable = false)
    private String value;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "parameterTypeDefinition", "categoricalParameterValues" }, allowSetters = true)
    @JoinColumn(name = "parameter_type_definition_id", referencedColumnName = "parameter_type_definition_id")
    private CategoricalParameter categoricalParameter;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public UUID getId() {
        return this.id;
    }

    public CategoricalParameterValue id(UUID id) {
        this.setId(id);
        return this;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getValue() {
        return this.value;
    }

    public CategoricalParameterValue value(String value) {
        this.setValue(value);
        return this;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public CategoricalParameter getCategoricalParameter() {
        return this.categoricalParameter;
    }

    public void setCategoricalParameter(CategoricalParameter categoricalParameter) {
        this.categoricalParameter = categoricalParameter;
    }

    public CategoricalParameterValue categoricalParameter(CategoricalParameter categoricalParameter) {
        this.setCategoricalParameter(categoricalParameter);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CategoricalParameterValue)) {
            return false;
        }
        return getId() != null && getId().equals(((CategoricalParameterValue) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CategoricalParameterValue{" +
            "id=" + getId() +
            ", value='" + getValue() + "'" +
            "}";
    }
}
