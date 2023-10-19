package ai.turintech.catalog.repository.rowmapper;

import ai.turintech.catalog.domain.BooleanParameter;
import io.r2dbc.spi.Row;
import java.util.UUID;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link BooleanParameter}, with proper type conversions.
 */
@Service
public class BooleanParameterRowMapper implements BiFunction<Row, String, BooleanParameter> {

    private final ColumnConverter converter;

    public BooleanParameterRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link BooleanParameter} stored in the database.
     */
    @Override
    public BooleanParameter apply(Row row, String prefix) {
        BooleanParameter entity = new BooleanParameter();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setDefaultValue(converter.fromRow(row, prefix + "_default_value", Boolean.class));
        entity.setParameterTypeDefinitionId(converter.fromRow(row, prefix + "_parameter_type_definition_id", UUID.class));
        return entity;
    }
}
