package ai.turintech.catalog.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * A DTO for the {@link ai.turintech.catalog.domain.ModelGroupType} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ModelGroupTypeDTO implements Serializable {

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
        if (!(o instanceof ModelGroupTypeDTO)) {
            return false;
        }

        ModelGroupTypeDTO modelGroupTypeDTO = (ModelGroupTypeDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, modelGroupTypeDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ModelGroupTypeDTO{" +
            "id='" + getId() + "'" +
            ", name='" + getName() + "'" +
            "}";
    }
}
