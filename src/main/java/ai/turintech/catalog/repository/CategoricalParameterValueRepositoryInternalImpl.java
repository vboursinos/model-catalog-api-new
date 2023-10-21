package ai.turintech.catalog.repository;

import ai.turintech.catalog.domain.CategoricalParameterValue;
import ai.turintech.catalog.repository.rowmapper.CategoricalParameterRowMapper;
import ai.turintech.catalog.repository.rowmapper.CategoricalParameterValueRowMapper;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import java.util.List;
import java.util.UUID;

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
 * Spring Data R2DBC custom repository implementation for the CategoricalParameterValue entity.
 */
@SuppressWarnings("unused")
class CategoricalParameterValueRepositoryInternalImpl
    extends SimpleR2dbcRepository<CategoricalParameterValue, UUID>
    implements CategoricalParameterValueRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final CategoricalParameterRowMapper categoricalparameterMapper;
    private final CategoricalParameterValueRowMapper categoricalparametervalueMapper;

    private static final Table entityTable = Table.aliased("categorical_parameter_value", EntityManager.ENTITY_ALIAS);
    private static final Table categoricalParameterTable = Table.aliased("categorical_parameter", "categoricalParameter");

    public CategoricalParameterValueRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        CategoricalParameterRowMapper categoricalparameterMapper,
        CategoricalParameterValueRowMapper categoricalparametervalueMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(
                converter.getMappingContext().getRequiredPersistentEntity(CategoricalParameterValue.class)
            ),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.categoricalparameterMapper = categoricalparameterMapper;
        this.categoricalparametervalueMapper = categoricalparametervalueMapper;
    }

    @Override
    public Flux<CategoricalParameterValue> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<CategoricalParameterValue> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = CategoricalParameterValueSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(CategoricalParameterSqlHelper.getColumns(categoricalParameterTable, "categoricalParameter"));
        SelectFromAndJoinCondition selectFrom = Select
            .builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(categoricalParameterTable)
            .on(Column.create("categorical_parameter_id", entityTable))
            .equals(Column.create("id", categoricalParameterTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, CategoricalParameterValue.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<CategoricalParameterValue> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<CategoricalParameterValue> findById(UUID id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    private CategoricalParameterValue process(Row row, RowMetadata metadata) {
        CategoricalParameterValue entity = categoricalparametervalueMapper.apply(row, "e");
        return entity;
    }

    @Override
    public <S extends CategoricalParameterValue> Mono<S> save(S entity) {
        return super.save(entity);
    }
}
