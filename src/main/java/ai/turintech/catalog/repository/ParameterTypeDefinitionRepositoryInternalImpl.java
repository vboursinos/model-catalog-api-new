package ai.turintech.catalog.repository;

import ai.turintech.catalog.domain.ParameterTypeDefinition;
import ai.turintech.catalog.repository.rowmapper.ParameterDistributionTypeRowMapper;
import ai.turintech.catalog.repository.rowmapper.ParameterRowMapper;
import ai.turintech.catalog.repository.rowmapper.ParameterTypeDefinitionRowMapper;
import ai.turintech.catalog.repository.rowmapper.ParameterTypeRowMapper;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import java.util.List;
import java.util.UUID;
import org.apache.commons.lang3.StringUtils;
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
 * Spring Data R2DBC custom repository implementation for the ParameterTypeDefinition entity.
 */
@SuppressWarnings("unused")
class ParameterTypeDefinitionRepositoryInternalImpl
    extends SimpleR2dbcRepository<ParameterTypeDefinition, UUID>
    implements ParameterTypeDefinitionRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final ParameterDistributionTypeRowMapper parameterdistributiontypeMapper;
    private final ParameterRowMapper parameterMapper;
    private final ParameterTypeRowMapper parametertypeMapper;
    private final ParameterTypeDefinitionRowMapper parametertypedefinitionMapper;

    private static final Table entityTable = Table.aliased("parameter_type_definition", EntityManager.ENTITY_ALIAS);
    private static final Table distributionTable = Table.aliased("parameter_distribution_type", "distribution");
    private static final Table parameterTable = Table.aliased("parameter", "parameter");
    private static final Table typeTable = Table.aliased("parameter_type", "e_type");

    public ParameterTypeDefinitionRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        ParameterDistributionTypeRowMapper parameterdistributiontypeMapper,
        ParameterRowMapper parameterMapper,
        ParameterTypeRowMapper parametertypeMapper,
        ParameterTypeDefinitionRowMapper parametertypedefinitionMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(
                converter.getMappingContext().getRequiredPersistentEntity(ParameterTypeDefinition.class)
            ),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.parameterdistributiontypeMapper = parameterdistributiontypeMapper;
        this.parameterMapper = parameterMapper;
        this.parametertypeMapper = parametertypeMapper;
        this.parametertypedefinitionMapper = parametertypedefinitionMapper;
    }

    @Override
    public Flux<ParameterTypeDefinition> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<ParameterTypeDefinition> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = ParameterTypeDefinitionSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(ParameterDistributionTypeSqlHelper.getColumns(distributionTable, "distribution"));
        columns.addAll(ParameterSqlHelper.getColumns(parameterTable, "parameter"));
        columns.addAll(ParameterTypeSqlHelper.getColumns(typeTable, "type"));
        SelectFromAndJoinCondition selectFrom = Select
            .builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(distributionTable)
            .on(Column.create("distribution_id", entityTable))
            .equals(Column.create("id", distributionTable))
            .leftOuterJoin(parameterTable)
            .on(Column.create("parameter_id", entityTable))
            .equals(Column.create("id", parameterTable))
            .leftOuterJoin(typeTable)
            .on(Column.create("type_id", entityTable))
            .equals(Column.create("id", typeTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, ParameterTypeDefinition.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<ParameterTypeDefinition> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<ParameterTypeDefinition> findById(UUID id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(StringUtils.wrap(id.toString(), "'")));
        return createQuery(null, whereClause).one();
    }

    private ParameterTypeDefinition process(Row row, RowMetadata metadata) {
        ParameterTypeDefinition entity = parametertypedefinitionMapper.apply(row, "e");
        entity.setDistribution(parameterdistributiontypeMapper.apply(row, "distribution"));
        entity.setType(parametertypeMapper.apply(row, "type"));
        return entity;
    }

    @Override
    public <S extends ParameterTypeDefinition> Mono<S> save(S entity) {
        return super.save(entity);
    }
}
