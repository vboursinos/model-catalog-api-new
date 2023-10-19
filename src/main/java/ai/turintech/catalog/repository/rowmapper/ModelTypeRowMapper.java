package ai.turintech.catalog.repository.rowmapper;

import ai.turintech.catalog.domain.ModelType;
import io.r2dbc.spi.Row;
import java.util.UUID;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link ModelType}, with proper type conversions.
 */
@Service
public class ModelTypeRowMapper implements BiFunction<Row, String, ModelType> {

    private final ColumnConverter converter;

    public ModelTypeRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link ModelType} stored in the database.
     */
    @Override
    public ModelType apply(Row row, String prefix) {
        ModelType entity = new ModelType();
        entity.setId(converter.fromRow(row, prefix + "_id", UUID.class));
        entity.setName(converter.fromRow(row, prefix + "_name", String.class));
        return entity;
    }
}
