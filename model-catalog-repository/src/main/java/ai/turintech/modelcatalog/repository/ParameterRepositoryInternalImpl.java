package ai.turintech.modelcatalog.repository;

import ai.turintech.modelcatalog.entity.Parameter;
import ai.turintech.modelcatalog.repository.rowmapper.ParameterRowMapper;
import ai.turintech.modelcatalog.repository.rowmapper.ParameterTypeDefinitionRowMapper;
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
 * Spring Data R2DBC custom repository implementation for the Parameter entity.
 */
@SuppressWarnings("unused")
class ParameterRepositoryInternalImpl extends SimpleR2dbcRepository<Parameter, UUID> implements ParameterRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final ParameterTypeDefinitionRowMapper parametertypedefinitionMapper;
    private final ParameterRowMapper parameterMapper;

    private static final Table entityTable = Table.aliased("parameter", EntityManager.ENTITY_ALIAS);
    private static final Table definitionsTable = Table.aliased("parameter_type_definition", "definitions");

    public ParameterRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        ParameterTypeDefinitionRowMapper parametertypedefinitionMapper,
        ParameterRowMapper parameterMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(Parameter.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.parametertypedefinitionMapper = parametertypedefinitionMapper;
        this.parameterMapper = parameterMapper;
    }

    @Override
    public Flux<Parameter> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<Parameter> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = ParameterSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(ParameterTypeDefinitionSqlHelper.getColumns(definitionsTable, "definitions"));
        SelectFromAndJoinCondition selectFrom = Select
            .builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(definitionsTable)
            .on(Column.create("definitions_id", entityTable))
            .equals(Column.create("id", definitionsTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, Parameter.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<Parameter> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<Parameter> findById(UUID id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(StringUtils.wrap(id.toString(), "'")));
        return createQuery(null, whereClause).one();
    }

    private Parameter process(Row row, RowMetadata metadata) {
        Parameter entity = parameterMapper.apply(row, "e");
        entity.setDefinitions(parametertypedefinitionMapper.apply(row, "definitions"));
        return entity;
    }

    @Override
    public <S extends Parameter> Mono<S> save(S entity) {
        return super.save(entity);
    }
}
