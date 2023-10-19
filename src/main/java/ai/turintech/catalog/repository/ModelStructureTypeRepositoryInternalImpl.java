package ai.turintech.catalog.repository;

import ai.turintech.catalog.domain.ModelStructureType;
import ai.turintech.catalog.repository.rowmapper.ModelStructureTypeRowMapper;
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
 * Spring Data R2DBC custom repository implementation for the ModelStructureType entity.
 */
@SuppressWarnings("unused")
class ModelStructureTypeRepositoryInternalImpl
    extends SimpleR2dbcRepository<ModelStructureType, UUID>
    implements ModelStructureTypeRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final ModelStructureTypeRowMapper modelstructuretypeMapper;

    private static final Table entityTable = Table.aliased("model_structure_type", EntityManager.ENTITY_ALIAS);

    public ModelStructureTypeRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        ModelStructureTypeRowMapper modelstructuretypeMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(ModelStructureType.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.modelstructuretypeMapper = modelstructuretypeMapper;
    }

    @Override
    public Flux<ModelStructureType> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<ModelStructureType> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = ModelStructureTypeSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        SelectFromAndJoin selectFrom = Select.builder().select(columns).from(entityTable);
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, ModelStructureType.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<ModelStructureType> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<ModelStructureType> findById(UUID id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(StringUtils.wrap(id.toString(), "'")));
        return createQuery(null, whereClause).one();
    }

    private ModelStructureType process(Row row, RowMetadata metadata) {
        ModelStructureType entity = modelstructuretypeMapper.apply(row, "e");
        return entity;
    }

    @Override
    public <S extends ModelStructureType> Mono<S> save(S entity) {
        return super.save(entity);
    }
}
