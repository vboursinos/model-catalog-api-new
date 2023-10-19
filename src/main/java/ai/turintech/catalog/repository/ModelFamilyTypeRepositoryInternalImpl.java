package ai.turintech.catalog.repository;

import ai.turintech.catalog.domain.ModelFamilyType;
import ai.turintech.catalog.repository.rowmapper.ModelFamilyTypeRowMapper;
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
 * Spring Data R2DBC custom repository implementation for the ModelFamilyType entity.
 */
@SuppressWarnings("unused")
class ModelFamilyTypeRepositoryInternalImpl
    extends SimpleR2dbcRepository<ModelFamilyType, UUID>
    implements ModelFamilyTypeRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final ModelFamilyTypeRowMapper modelfamilytypeMapper;

    private static final Table entityTable = Table.aliased("model_family_type", EntityManager.ENTITY_ALIAS);

    public ModelFamilyTypeRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        ModelFamilyTypeRowMapper modelfamilytypeMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(ModelFamilyType.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.modelfamilytypeMapper = modelfamilytypeMapper;
    }

    @Override
    public Flux<ModelFamilyType> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<ModelFamilyType> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = ModelFamilyTypeSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        SelectFromAndJoin selectFrom = Select.builder().select(columns).from(entityTable);
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, ModelFamilyType.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<ModelFamilyType> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<ModelFamilyType> findById(UUID id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(StringUtils.wrap(id.toString(), "'")));
        return createQuery(null, whereClause).one();
    }

    private ModelFamilyType process(Row row, RowMetadata metadata) {
        ModelFamilyType entity = modelfamilytypeMapper.apply(row, "e");
        return entity;
    }

    @Override
    public <S extends ModelFamilyType> Mono<S> save(S entity) {
        return super.save(entity);
    }
}
