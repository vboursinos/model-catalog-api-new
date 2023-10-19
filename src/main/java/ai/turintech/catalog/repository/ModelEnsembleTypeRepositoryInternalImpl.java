package ai.turintech.catalog.repository;

import ai.turintech.catalog.domain.ModelEnsembleType;
import ai.turintech.catalog.repository.rowmapper.ModelEnsembleTypeRowMapper;
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
 * Spring Data R2DBC custom repository implementation for the ModelEnsembleType entity.
 */
@SuppressWarnings("unused")
class ModelEnsembleTypeRepositoryInternalImpl
    extends SimpleR2dbcRepository<ModelEnsembleType, UUID>
    implements ModelEnsembleTypeRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final ModelEnsembleTypeRowMapper modelensembletypeMapper;

    private static final Table entityTable = Table.aliased("model_ensemble_type", EntityManager.ENTITY_ALIAS);

    public ModelEnsembleTypeRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        ModelEnsembleTypeRowMapper modelensembletypeMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(ModelEnsembleType.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.modelensembletypeMapper = modelensembletypeMapper;
    }

    @Override
    public Flux<ModelEnsembleType> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<ModelEnsembleType> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = ModelEnsembleTypeSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        SelectFromAndJoin selectFrom = Select.builder().select(columns).from(entityTable);
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, ModelEnsembleType.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<ModelEnsembleType> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<ModelEnsembleType> findById(UUID id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(StringUtils.wrap(id.toString(), "'")));
        return createQuery(null, whereClause).one();
    }

    private ModelEnsembleType process(Row row, RowMetadata metadata) {
        ModelEnsembleType entity = modelensembletypeMapper.apply(row, "e");
        return entity;
    }

    @Override
    public <S extends ModelEnsembleType> Mono<S> save(S entity) {
        return super.save(entity);
    }
}
