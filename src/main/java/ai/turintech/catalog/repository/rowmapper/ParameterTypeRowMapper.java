package ai.turintech.catalog.repository.rowmapper;

import ai.turintech.catalog.domain.ParameterType;
import io.r2dbc.spi.Row;
import java.util.UUID;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link ParameterType}, with proper type conversions.
 */
@Service
public class ParameterTypeRowMapper implements BiFunction<Row, String, ParameterType> {

    private final ColumnConverter converter;

    public ParameterTypeRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link ParameterType} stored in the database.
     */
    @Override
    public ParameterType apply(Row row, String prefix) {
        ParameterType entity = new ParameterType();
        entity.setId(converter.fromRow(row, prefix + "_id", UUID.class));
        entity.setName(converter.fromRow(row, prefix + "_name", String.class));
        return entity;
    }
}
