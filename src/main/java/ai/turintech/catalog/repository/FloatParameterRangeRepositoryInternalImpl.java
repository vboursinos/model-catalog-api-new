package ai.turintech.catalog.repository;

import ai.turintech.catalog.domain.FloatParameterRange;
import ai.turintech.catalog.repository.rowmapper.FloatParameterRangeRowMapper;
import ai.turintech.catalog.repository.rowmapper.FloatParameterRowMapper;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.convert.R2dbcConverter;
import org.springframework.data.r2dbc.core.R2dbcEntityOperations;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.r2dbc.repository.support.SimpleR2dbcRepository;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Comparison;
import org.springframework.data.relational.core.sql.Condition;
import org.springframework.data.relational.core.sql.Conditions;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Select;
import org.springframework.data.relational.core.sql.SelectBuilder.SelectFromAndJoinCondition;
import org.springframework.data.relational.core.sql.Table;
import org.springframework.data.relational.repository.support.MappingRelationalEntityInformation;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.r2dbc.core.RowsFetchSpec;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC custom repository implementation for the FloatParameterRange entity.
 */
@SuppressWarnings("unused")
class FloatParameterRangeRepositoryInternalImpl
    extends SimpleR2dbcRepository<FloatParameterRange, Long>
    implements FloatParameterRangeRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final FloatParameterRowMapper floatparameterMapper;
    private final FloatParameterRangeRowMapper floatparameterrangeMapper;

    private static final Table entityTable = Table.aliased("float_parameter_range", EntityManager.ENTITY_ALIAS);
    private static final Table floatParameterTable = Table.aliased("float_parameter", "floatParameter");

    public FloatParameterRangeRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        FloatParameterRowMapper floatparameterMapper,
        FloatParameterRangeRowMapper floatparameterrangeMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(FloatParameterRange.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.floatparameterMapper = floatparameterMapper;
        this.floatparameterrangeMapper = floatparameterrangeMapper;
    }

    @Override
    public Flux<FloatParameterRange> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<FloatParameterRange> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = FloatParameterRangeSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(FloatParameterSqlHelper.getColumns(floatParameterTable, "floatParameter"));
        SelectFromAndJoinCondition selectFrom = Select
            .builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(floatParameterTable)
            .on(Column.create("float_parameter_id", entityTable))
            .equals(Column.create("id", floatParameterTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, FloatParameterRange.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<FloatParameterRange> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<FloatParameterRange> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    private FloatParameterRange process(Row row, RowMetadata metadata) {
        FloatParameterRange entity = floatparameterrangeMapper.apply(row, "e");
        entity.setFloatParameter(floatparameterMapper.apply(row, "floatParameter"));
        return entity;
    }

    @Override
    public <S extends FloatParameterRange> Mono<S> save(S entity) {
        return super.save(entity);
    }
}
