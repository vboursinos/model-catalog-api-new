package ai.turintech.catalog.service.dto;

import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.util.UUID;

/**
 * A DTO for the {@link ai.turintech.catalog.domain.IntegerParameterValue} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class IntegerParameterValueDTO implements Serializable {

    private UUID id;

    @NotNull(message = "must not be null")
    private Integer lower;

    @NotNull(message = "must not be null")
    private Integer upper;



    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Integer getLower() {
        return lower;
    }

    public void setLower(Integer lower) {
        this.lower = lower;
    }

    public Integer getUpper() {
        return upper;
    }

    public void setUpper(Integer upper) {
        this.upper = upper;
    }


    // prettier-ignore
    @Override
    public String toString() {
        return "IntegerParameterValueDTO{" +
            "id=" + getId() +
            ", lower=" + getLower() +
            ", upper=" + getUpper() +
            "}";
    }
}
