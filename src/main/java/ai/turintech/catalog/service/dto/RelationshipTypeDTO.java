package ai.turintech.catalog.service.dto;

public enum RelationshipTypeDTO {
    ONE_TO_ONE("one-to-one"),
    ONE_TO_MANY("one-to-many"),
    MANY_TO_MANY("many-to-many"),
    MANY_TO_ONE("many-to-one");

    private final String value;

    RelationshipTypeDTO(final String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return this.getValue();
    }

}
