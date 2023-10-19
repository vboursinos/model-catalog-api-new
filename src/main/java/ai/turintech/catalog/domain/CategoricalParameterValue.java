package ai.turintech.catalog.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A CategoricalParameterValue.
 */
@Table("categorical_parameter_value")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CategoricalParameterValue implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Column("value")
    private String value;

    @Transient
    @JsonIgnoreProperties(value = { "parameterTypeDefinition", "categoricalParameterValues" }, allowSetters = true)
    private CategoricalParameter categoricalParameter;

    @Column("categorical_parameter_id")
    private Long categoricalParameterId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CategoricalParameterValue id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
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
        this.categoricalParameterId = categoricalParameter != null ? categoricalParameter.getId() : null;
    }

    public CategoricalParameterValue categoricalParameter(CategoricalParameter categoricalParameter) {
        this.setCategoricalParameter(categoricalParameter);
        return this;
    }

    public Long getCategoricalParameterId() {
        return this.categoricalParameterId;
    }

    public void setCategoricalParameterId(Long categoricalParameter) {
        this.categoricalParameterId = categoricalParameter;
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
