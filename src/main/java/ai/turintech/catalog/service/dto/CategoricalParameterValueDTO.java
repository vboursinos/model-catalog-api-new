package ai.turintech.catalog.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link ai.turintech.catalog.domain.CategoricalParameterValue} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CategoricalParameterValueDTO implements Serializable {

    private Long id;

    @NotNull(message = "must not be null")
    private String value;

    private CategoricalParameterDTO categoricalParameter;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public CategoricalParameterDTO getCategoricalParameter() {
        return categoricalParameter;
    }

    public void setCategoricalParameter(CategoricalParameterDTO categoricalParameter) {
        this.categoricalParameter = categoricalParameter;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CategoricalParameterValueDTO)) {
            return false;
        }

        CategoricalParameterValueDTO categoricalParameterValueDTO = (CategoricalParameterValueDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, categoricalParameterValueDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CategoricalParameterValueDTO{" +
            "id=" + getId() +
            ", value='" + getValue() + "'" +
            ", categoricalParameter=" + getCategoricalParameter() +
            "}";
    }
}
