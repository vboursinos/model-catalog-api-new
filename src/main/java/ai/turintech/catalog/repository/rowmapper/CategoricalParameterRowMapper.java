package ai.turintech.catalog.repository.rowmapper;

import ai.turintech.catalog.domain.CategoricalParameter;
import io.r2dbc.spi.Row;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.function.BiFunction;

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
     *
     * @return the {@link CategoricalParameter} stored in the database.
     */
    @Override
    public CategoricalParameter apply(Row row, String prefix) {
        if (converter.fromRow(row, prefix + "_id", UUID.class) != null) {
            CategoricalParameter entity = new CategoricalParameter();
            entity.setParameterTypeDefinitionId(converter.fromRow(row, prefix + "_id", UUID.class));
            entity.setDefaultValue(converter.fromRow(row, prefix + "_default_value", String.class));
            return entity;
        } else {
            return null;
        }
    }
}
