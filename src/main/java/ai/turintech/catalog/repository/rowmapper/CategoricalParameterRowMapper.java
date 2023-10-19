package ai.turintech.catalog.repository.rowmapper;

import ai.turintech.catalog.domain.CategoricalParameter;
import io.r2dbc.spi.Row;
import java.util.UUID;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link CategoricalParameter}, with proper type conversions.
 */
@Service
public class CategoricalParameterRowMapper implements BiFunction<Row, String, CategoricalParameter> {

    private final ColumnConverter converter;

    public CategoricalParameterRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link CategoricalParameter} stored in the database.
     */
    @Override
    public CategoricalParameter apply(Row row, String prefix) {
        CategoricalParameter entity = new CategoricalParameter();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setDefaultValue(converter.fromRow(row, prefix + "_default_value", String.class));
        entity.setParameterTypeDefinitionId(converter.fromRow(row, prefix + "_parameter_type_definition_id", UUID.class));
        return entity;
    }
}
