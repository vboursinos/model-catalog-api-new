package ai.turintech.catalog.anotatation;

import ai.turintech.catalog.service.dto.RelationshipTypeDTO;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Target({java.lang.annotation.ElementType.FIELD})
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
public @interface Relationship {
    RelationshipTypeDTO type();
    String toTable();
    String fromColumn();
    String toColumn();
    String toColumnPrefix() default "";
}
