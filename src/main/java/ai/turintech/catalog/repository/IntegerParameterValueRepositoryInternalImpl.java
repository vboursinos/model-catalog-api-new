package ai.turintech.catalog.repository;

import ai.turintech.catalog.domain.IntegerParameterValue;
import ai.turintech.catalog.repository.rowmapper.IntegerParameterRowMapper;
import ai.turintech.catalog.repository.rowmapper.IntegerParameterValueRowMapper;
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
 * Spring Data R2DBC custom repository implementation for the IntegerParameterValue entity.
 */
@SuppressWarnings("unused")
class IntegerParameterValueRepositoryInternalImpl
    extends SimpleR2dbcRepository<IntegerParameterValue, Long>
    implements IntegerParameterValueRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final IntegerParameterRowMapper integerparameterMapper;
    private final IntegerParameterValueRowMapper integerparametervalueMapper;

    private static final Table entityTable = Table.aliased("integer_parameter_value", EntityManager.ENTITY_ALIAS);
    private static final Table integerParameterTable = Table.aliased("integer_parameter", "integerParameter");

    public IntegerParameterValueRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        IntegerParameterRowMapper integerparameterMapper,
        IntegerParameterValueRowMapper integerparametervalueMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(IntegerParameterValue.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.integerparameterMapper = integerparameterMapper;
        this.integerparametervalueMapper = integerparametervalueMapper;
    }

    @Override
    public Flux<IntegerParameterValue> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<IntegerParameterValue> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = IntegerParameterValueSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(IntegerParameterSqlHelper.getColumns(integerParameterTable, "integerParameter"));
        SelectFromAndJoinCondition selectFrom = Select
            .builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(integerParameterTable)
            .on(Column.create("integer_parameter_id", entityTable))
            .equals(Column.create("id", integerParameterTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, IntegerParameterValue.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<IntegerParameterValue> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<IntegerParameterValue> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    private IntegerParameterValue process(Row row, RowMetadata metadata) {
        IntegerParameterValue entity = integerparametervalueMapper.apply(row, "e");
        entity.setIntegerParameter(integerparameterMapper.apply(row, "integerParameter"));
        return entity;
    }

    @Override
    public <S extends IntegerParameterValue> Mono<S> save(S entity) {
        return super.save(entity);
    }
}
