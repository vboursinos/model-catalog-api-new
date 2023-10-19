package ai.turintech.catalog.repository.rowmapper;

import ai.turintech.catalog.domain.ModelFamilyType;
import io.r2dbc.spi.Row;
import java.util.UUID;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link ModelFamilyType}, with proper type conversions.
 */
@Service
public class ModelFamilyTypeRowMapper implements BiFunction<Row, String, ModelFamilyType> {

    private final ColumnConverter converter;

    public ModelFamilyTypeRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link ModelFamilyType} stored in the database.
     */
    @Override
    public ModelFamilyType apply(Row row, String prefix) {
        ModelFamilyType entity = new ModelFamilyType();
        entity.setId(converter.fromRow(row, prefix + "_id", UUID.class));
        entity.setName(converter.fromRow(row, prefix + "_name", String.class));
        return entity;
    }
}
