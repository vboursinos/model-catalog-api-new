package ai.turintech.catalog.repository;

import ai.turintech.catalog.domain.FloatParameter;
import ai.turintech.catalog.repository.rowmapper.FloatParameterRowMapper;
import ai.turintech.catalog.repository.rowmapper.ParameterTypeDefinitionRowMapper;
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
 * Spring Data R2DBC custom repository implementation for the FloatParameter entity.
 */
@SuppressWarnings("unused")
class FloatParameterRepositoryInternalImpl extends SimpleR2dbcRepository<FloatParameter, Long> implements FloatParameterRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final ParameterTypeDefinitionRowMapper parametertypedefinitionMapper;
    private final FloatParameterRowMapper floatparameterMapper;

    private static final Table entityTable = Table.aliased("float_parameter", EntityManager.ENTITY_ALIAS);
    private static final Table parameterTypeDefinitionTable = Table.aliased("parameter_type_definition", "parameterTypeDefinition");

    public FloatParameterRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        ParameterTypeDefinitionRowMapper parametertypedefinitionMapper,
        FloatParameterRowMapper floatparameterMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(FloatParameter.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.parametertypedefinitionMapper = parametertypedefinitionMapper;
        this.floatparameterMapper = floatparameterMapper;
    }

    @Override
    public Flux<FloatParameter> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<FloatParameter> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = FloatParameterSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(ParameterTypeDefinitionSqlHelper.getColumns(parameterTypeDefinitionTable, "parameterTypeDefinition"));
        SelectFromAndJoinCondition selectFrom = Select
            .builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(parameterTypeDefinitionTable)
            .on(Column.create("parameter_type_definition_id", entityTable))
            .equals(Column.create("id", parameterTypeDefinitionTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, FloatParameter.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<FloatParameter> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<FloatParameter> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    private FloatParameter process(Row row, RowMetadata metadata) {
        FloatParameter entity = floatparameterMapper.apply(row, "e");
        return entity;
    }

    @Override
    public <S extends FloatParameter> Mono<S> save(S entity) {
        return super.save(entity);
    }
}
