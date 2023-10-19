package ai.turintech.catalog.repository.rowmapper;

import ai.turintech.catalog.domain.Model;
import io.r2dbc.spi.Row;
import java.util.UUID;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Model}, with proper type conversions.
 */
@Service
public class ModelRowMapper implements BiFunction<Row, String, Model> {

    private final ColumnConverter converter;

    public ModelRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Model} stored in the database.
     */
    @Override
    public Model apply(Row row, String prefix) {
        Model entity = new Model();
        entity.setId(converter.fromRow(row, prefix + "_id", UUID.class));
        entity.setName(converter.fromRow(row, prefix + "_name", String.class));
        entity.setDisplayName(converter.fromRow(row, prefix + "_display_name", String.class));
        entity.setDescription(converter.fromRow(row, prefix + "_description", String.class));
        entity.setAdvantages(converter.fromRow(row, prefix + "_advantages", String.class));
        entity.setDisadvantages(converter.fromRow(row, prefix + "_disadvantages", String.class));
        entity.setEnabled(converter.fromRow(row, prefix + "_enabled", Boolean.class));
        entity.setDecisionTree(converter.fromRow(row, prefix + "_decision_tree", Boolean.class));
        entity.setMlTaskId(converter.fromRow(row, prefix + "_ml_task_id", UUID.class));
        entity.setStructureId(converter.fromRow(row, prefix + "_structure_id", UUID.class));
        entity.setModelTypeId(converter.fromRow(row, prefix + "_model_type_id", UUID.class));
        entity.setFamilyTypeId(converter.fromRow(row, prefix + "_family_type_id", UUID.class));
        entity.setEnsembleTypeId(converter.fromRow(row, prefix + "_ensemble_type_id", UUID.class));
        return entity;
    }
}
