package ai.turintech.catalog.repository.rowmapper;

import ai.turintech.catalog.domain.Metric;
import io.r2dbc.spi.Row;
import java.util.UUID;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Metric}, with proper type conversions.
 */
@Service
public class MetricRowMapper implements BiFunction<Row, String, Metric> {

    private final ColumnConverter converter;

    public MetricRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Metric} stored in the database.
     */
    @Override
    public Metric apply(Row row, String prefix) {
        Metric entity = new Metric();
        entity.setId(converter.fromRow(row, prefix + "_id", UUID.class));
        entity.setMetric(converter.fromRow(row, prefix + "_metric", String.class));
        return entity;
    }
}
