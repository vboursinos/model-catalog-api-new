package ai.turintech.catalog.repository;

import ai.turintech.catalog.domain.MlTaskType;
import ai.turintech.catalog.repository.rowmapper.MlTaskTypeRowMapper;
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
import org.springframework.data.relational.core.sql.Comparison;
import org.springframework.data.relational.core.sql.Condition;
import org.springframework.data.relational.core.sql.Conditions;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Select;
import org.springframework.data.relational.core.sql.SelectBuilder.SelectFromAndJoin;
import org.springframework.data.relational.core.sql.Table;
import org.springframework.data.relational.repository.support.MappingRelationalEntityInformation;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.r2dbc.core.RowsFetchSpec;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC custom repository implementation for the MlTaskType entity.
 */
@SuppressWarnings("unused")
class MlTaskTypeRepositoryInternalImpl extends SimpleR2dbcRepository<MlTaskType, UUID> implements MlTaskTypeRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final MlTaskTypeRowMapper mltasktypeMapper;

    private static final Table entityTable = Table.aliased("ml_task_type", EntityManager.ENTITY_ALIAS);

    public MlTaskTypeRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        MlTaskTypeRowMapper mltasktypeMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(MlTaskType.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.mltasktypeMapper = mltasktypeMapper;
    }

    @Override
    public Flux<MlTaskType> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<MlTaskType> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = MlTaskTypeSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        SelectFromAndJoin selectFrom = Select.builder().select(columns).from(entityTable);
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, MlTaskType.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<MlTaskType> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<MlTaskType> findById(UUID id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(StringUtils.wrap(id.toString(), "'")));
        return createQuery(null, whereClause).one();
    }

    private MlTaskType process(Row row, RowMetadata metadata) {
        MlTaskType entity = mltasktypeMapper.apply(row, "e");
        return entity;
    }

    @Override
    public <S extends MlTaskType> Mono<S> save(S entity) {
        return super.save(entity);
    }
}
