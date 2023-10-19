package ai.turintech.catalog.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * A DTO for the {@link ai.turintech.catalog.domain.ParameterType} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ParameterTypeDTO implements Serializable {

    private UUID id;

    @NotNull(message = "must not be null")
    private String name;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ParameterTypeDTO)) {
            return false;
        }

        ParameterTypeDTO parameterTypeDTO = (ParameterTypeDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, parameterTypeDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ParameterTypeDTO{" +
            "id='" + getId() + "'" +
            ", name='" + getName() + "'" +
            "}";
    }
}
