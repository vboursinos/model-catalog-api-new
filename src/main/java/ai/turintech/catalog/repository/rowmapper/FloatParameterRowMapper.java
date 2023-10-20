package ai.turintech.catalog.repository.rowmapper;

import ai.turintech.catalog.domain.FloatParameter;
import io.r2dbc.spi.Row;
import java.util.UUID;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link FloatParameter}, with proper type conversions.
 */
@Service
public class FloatParameterRowMapper implements BiFunction<Row, String, FloatParameter> {

    private final ColumnConverter converter;

    public FloatParameterRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link FloatParameter} stored in the database.
     */
    @Override
    public FloatParameter apply(Row row, String prefix) {
        FloatParameter entity = new FloatParameter();
        entity.setParameterTypeDefinitionId(converter.fromRow(row, prefix + "_id", UUID.class));
        entity.setDefaultValue(converter.fromRow(row, prefix + "_default_value", Double.class));
        return entity;
    }
}
