package ai.turintech.catalog.repository.rowmapper;

import ai.turintech.catalog.domain.FloatParameterRange;
import io.r2dbc.spi.Row;

import java.util.UUID;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link FloatParameterRange}, with proper type conversions.
 */
@Service
public class FloatParameterRangeRowMapper implements BiFunction<Row, String, FloatParameterRange> {

    private final ColumnConverter converter;

    public FloatParameterRangeRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link FloatParameterRange} stored in the database.
     */
    @Override
    public FloatParameterRange apply(Row row, String prefix) {
        FloatParameterRange entity = new FloatParameterRange();
        entity.setId(converter.fromRow(row, prefix + "_id", UUID.class));
        entity.setIsLeftOpen(converter.fromRow(row, prefix + "_is_left_open", Boolean.class));
        entity.setIsRightOpen(converter.fromRow(row, prefix + "_is_right_open", Boolean.class));
        entity.setLower(converter.fromRow(row, prefix + "_lower", Double.class));
        entity.setUpper(converter.fromRow(row, prefix + "_upper", Double.class));
        entity.setParameterTypeDefinitionId(converter.fromRow(row, prefix + "_parameter_type_definition_id", UUID.class));
        return entity;
    }
}
