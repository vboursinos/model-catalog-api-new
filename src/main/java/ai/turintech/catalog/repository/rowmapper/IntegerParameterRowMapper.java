package ai.turintech.catalog.repository.rowmapper;

import ai.turintech.catalog.domain.IntegerParameter;
import io.r2dbc.spi.Row;
import java.util.UUID;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link IntegerParameter}, with proper type conversions.
 */
@Service
public class IntegerParameterRowMapper implements BiFunction<Row, String, IntegerParameter> {

    private final ColumnConverter converter;

    public IntegerParameterRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link IntegerParameter} stored in the database.
     */
    @Override
    public IntegerParameter apply(Row row, String prefix) {
        IntegerParameter entity = new IntegerParameter();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setDefaultValue(converter.fromRow(row, prefix + "_default_value", Integer.class));
        entity.setParameterTypeDefinitionId(converter.fromRow(row, prefix + "_parameter_type_definition_id", UUID.class));
        return entity;
    }
}
