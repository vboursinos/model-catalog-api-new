package ai.turintech.catalog.repository.rowmapper;

import ai.turintech.catalog.domain.ModelStructureType;
import io.r2dbc.spi.Row;
import java.util.UUID;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link ModelStructureType}, with proper type conversions.
 */
@Service
public class ModelStructureTypeRowMapper implements BiFunction<Row, String, ModelStructureType> {

    private final ColumnConverter converter;

    public ModelStructureTypeRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link ModelStructureType} stored in the database.
     */
    @Override
    public ModelStructureType apply(Row row, String prefix) {
        ModelStructureType entity = new ModelStructureType();
        entity.setId(converter.fromRow(row, prefix + "_id", UUID.class));
        entity.setName(converter.fromRow(row, prefix + "_name", String.class));
        return entity;
    }
}
