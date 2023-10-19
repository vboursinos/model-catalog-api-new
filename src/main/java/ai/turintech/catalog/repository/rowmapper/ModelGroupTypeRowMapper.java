package ai.turintech.catalog.repository.rowmapper;

import ai.turintech.catalog.domain.ModelGroupType;
import io.r2dbc.spi.Row;
import java.util.UUID;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link ModelGroupType}, with proper type conversions.
 */
@Service
public class ModelGroupTypeRowMapper implements BiFunction<Row, String, ModelGroupType> {

    private final ColumnConverter converter;

    public ModelGroupTypeRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link ModelGroupType} stored in the database.
     */
    @Override
    public ModelGroupType apply(Row row, String prefix) {
        ModelGroupType entity = new ModelGroupType();
        entity.setId(converter.fromRow(row, prefix + "_id", UUID.class));
        entity.setName(converter.fromRow(row, prefix + "_name", String.class));
        return entity;
    }
}
