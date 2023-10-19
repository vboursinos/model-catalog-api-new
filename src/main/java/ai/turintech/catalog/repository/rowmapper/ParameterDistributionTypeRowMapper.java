package ai.turintech.catalog.repository.rowmapper;

import ai.turintech.catalog.domain.ParameterDistributionType;
import io.r2dbc.spi.Row;
import java.util.UUID;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link ParameterDistributionType}, with proper type conversions.
 */
@Service
public class ParameterDistributionTypeRowMapper implements BiFunction<Row, String, ParameterDistributionType> {

    private final ColumnConverter converter;

    public ParameterDistributionTypeRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link ParameterDistributionType} stored in the database.
     */
    @Override
    public ParameterDistributionType apply(Row row, String prefix) {
        ParameterDistributionType entity = new ParameterDistributionType();
        entity.setId(converter.fromRow(row, prefix + "_id", UUID.class));
        entity.setName(converter.fromRow(row, prefix + "_name", String.class));
        return entity;
    }
}
