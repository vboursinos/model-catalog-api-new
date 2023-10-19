package ai.turintech.catalog.repository;

import ai.turintech.catalog.domain.Metric;
import ai.turintech.catalog.domain.Model;
import ai.turintech.catalog.domain.ModelGroupType;
import ai.turintech.catalog.repository.rowmapper.MlTaskTypeRowMapper;
import ai.turintech.catalog.repository.rowmapper.ModelEnsembleTypeRowMapper;
import ai.turintech.catalog.repository.rowmapper.ModelFamilyTypeRowMapper;
import ai.turintech.catalog.repository.rowmapper.ModelRowMapper;
import ai.turintech.catalog.repository.rowmapper.ModelStructureTypeRowMapper;
import ai.turintech.catalog.repository.rowmapper.ModelTypeRowMapper;
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
 * Spring Data R2DBC custom repository implementation for the Model entity.
 */
@SuppressWarnings("unused")
class ModelRepositoryInternalImpl extends SimpleR2dbcRepository<Model, UUID> implements ModelRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final MlTaskTypeRowMapper mltasktypeMapper;
    private final ModelStructureTypeRowMapper modelstructuretypeMapper;
    private final ModelTypeRowMapper modeltypeMapper;
    private final ModelFamilyTypeRowMapper modelfamilytypeMapper;
    private final ModelEnsembleTypeRowMapper modelensembletypeMapper;
    private final ModelRowMapper modelMapper;

    private static final Table entityTable = Table.aliased("model", EntityManager.ENTITY_ALIAS);
    private static final Table mlTaskTable = Table.aliased("ml_task_type", "mlTask");
    private static final Table structureTable = Table.aliased("model_structure_type", "e_structure");
    private static final Table typeTable = Table.aliased("model_type", "model_type");
    private static final Table familyTypeTable = Table.aliased("model_family_type", "familyType");
    private static final Table ensembleTypeTable = Table.aliased("model_ensemble_type", "ensembleType");

    private static final EntityManager.LinkTable groupsLink = new EntityManager.LinkTable("rel_model__groups", "model_id", "groups_id");
    private static final EntityManager.LinkTable incompatibleMetricsLink = new EntityManager.LinkTable(
        "rel_model__incompatible_metrics",
        "model_id",
        "incompatible_metrics_id"
    );

    public ModelRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        MlTaskTypeRowMapper mltasktypeMapper,
        ModelStructureTypeRowMapper modelstructuretypeMapper,
        ModelTypeRowMapper modeltypeMapper,
        ModelFamilyTypeRowMapper modelfamilytypeMapper,
        ModelEnsembleTypeRowMapper modelensembletypeMapper,
        ModelRowMapper modelMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(Model.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.mltasktypeMapper = mltasktypeMapper;
        this.modelstructuretypeMapper = modelstructuretypeMapper;
        this.modeltypeMapper = modeltypeMapper;
        this.modelfamilytypeMapper = modelfamilytypeMapper;
        this.modelensembletypeMapper = modelensembletypeMapper;
        this.modelMapper = modelMapper;
    }

    @Override
    public Flux<Model> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<Model> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = ModelSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(MlTaskTypeSqlHelper.getColumns(mlTaskTable, "mlTask"));
        columns.addAll(ModelStructureTypeSqlHelper.getColumns(structureTable, "structure"));
        columns.addAll(ModelTypeSqlHelper.getColumns(typeTable, "modelType"));
        columns.addAll(ModelFamilyTypeSqlHelper.getColumns(familyTypeTable, "familyType"));
        columns.addAll(ModelEnsembleTypeSqlHelper.getColumns(ensembleTypeTable, "ensembleType"));
        SelectFromAndJoinCondition selectFrom = Select
            .builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(mlTaskTable)
            .on(Column.create("ml_task_id", entityTable))
            .equals(Column.create("id", mlTaskTable))
            .leftOuterJoin(structureTable)
            .on(Column.create("structure_id", entityTable))
            .equals(Column.create("id", structureTable))
            .leftOuterJoin(typeTable)
            .on(Column.create("model_type_id", entityTable))
            .equals(Column.create("id", typeTable))
            .leftOuterJoin(familyTypeTable)
            .on(Column.create("family_type_id", entityTable))
            .equals(Column.create("id", familyTypeTable))
            .leftOuterJoin(ensembleTypeTable)
            .on(Column.create("ensemble_type_id", entityTable))
            .equals(Column.create("id", ensembleTypeTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, Model.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<Model> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<Model> findById(UUID id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(StringUtils.wrap(id.toString(), "'")));
        return createQuery(null, whereClause).one();
    }

    @Override
    public Mono<Model> findOneWithEagerRelationships(UUID id) {
        return findById(id);
    }

    @Override
    public Flux<Model> findAllWithEagerRelationships() {
        return findAll();
    }

    @Override
    public Flux<Model> findAllWithEagerRelationships(Pageable page) {
        Flux<Model> models = findAllBy(page);
        return models;
    }

    private Model process(Row row, RowMetadata metadata) {
        Model entity = modelMapper.apply(row, "e");
        entity.setMlTask(mltasktypeMapper.apply(row, "mlTask"));
        entity.setStructure(modelstructuretypeMapper.apply(row, "structure"));
        entity.setType(modeltypeMapper.apply(row, "modelType"));
        entity.setFamilyType(modelfamilytypeMapper.apply(row, "familyType"));
        entity.setEnsembleType(modelensembletypeMapper.apply(row, "ensembleType"));
        return entity;
    }

    @Override
    public <S extends Model> Mono<S> save(S entity) {
        return super.save(entity).flatMap((S e) -> updateRelations(e));
    }

    protected <S extends Model> Mono<S> updateRelations(S entity) {
        Mono<Void> result = entityManager
            .updateLinkTable(groupsLink, entity.getId(), entity.getGroups().stream().map(ModelGroupType::getId))
            .then();
        result =
            result.and(
                entityManager.updateLinkTable(
                    incompatibleMetricsLink,
                    entity.getId(),
                    entity.getIncompatibleMetrics().stream().map(Metric::getId)
                )
            );
        return result.thenReturn(entity);
    }

    @Override
    public Mono<Void> deleteById(UUID entityId) {
        return deleteRelations(entityId).then(super.deleteById(entityId));
    }

    protected Mono<Void> deleteRelations(UUID entityId) {
        return entityManager
            .deleteFromLinkTable(groupsLink, entityId)
            .and(entityManager.deleteFromLinkTable(incompatibleMetricsLink, entityId));
    }
}
