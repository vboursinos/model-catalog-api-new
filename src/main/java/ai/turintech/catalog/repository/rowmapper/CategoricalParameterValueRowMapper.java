package ai.turintech.catalog.repository.rowmapper;

import ai.turintech.catalog.domain.CategoricalParameterValue;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link CategoricalParameterValue}, with proper type conversions.
 */
@Service
public class CategoricalParameterValueRowMapper implements BiFunction<Row, String, CategoricalParameterValue> {

    private final ColumnConverter converter;

    public CategoricalParameterValueRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link CategoricalParameterValue} stored in the database.
     */
    @Override
    public CategoricalParameterValue apply(Row row, String prefix) {
        CategoricalParameterValue entity = new CategoricalParameterValue();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setValue(converter.fromRow(row, prefix + "_value", String.class));
        entity.setCategoricalParameterId(converter.fromRow(row, prefix + "_categorical_parameter_id", Long.class));
        return entity;
    }
}
