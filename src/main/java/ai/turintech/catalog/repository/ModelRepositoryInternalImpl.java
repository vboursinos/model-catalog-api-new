package ai.turintech.catalog.repository;

import ai.turintech.catalog.domain.*;
import ai.turintech.catalog.repository.rowmapper.*;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;

import java.util.*;
import java.util.stream.Collectors;

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
    private final ParameterRowMapper parameterMapper;
    private final ParameterTypeDefinitionRowMapper parametertypedefinitionMapper;
    private static final Table entityTable = Table.aliased("model", EntityManager.ENTITY_ALIAS);
    private static final Table mlTaskTable = Table.aliased("ml_task_type", "mlTask");
    private static final Table structureTable = Table.aliased("model_structure_type", "e_structure");
    private static final Table typeTable = Table.aliased("model_type", "model_type");
    private static final Table familyTypeTable = Table.aliased("model_family_type", "familyType");
    private static final Table ensembleTypeTable = Table.aliased("model_ensemble_type", "ensembleType");
    private static final Table parameterTable = Table.aliased("parameter", "parameter");
    private static final Table parameterTypeDefinitionTable = Table.aliased("parameter_type_definition", "parameterTypeDefinition");
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
        ParameterRowMapper parameterMapper,
        ParameterTypeDefinitionRowMapper parameterTypeDefinitionMapper,
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
        this.parameterMapper = parameterMapper;
        this.parametertypedefinitionMapper = parameterTypeDefinitionMapper;
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

    RowsFetchSpec<Parameter> createParameterQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = new ArrayList<>();
        columns.addAll(ParameterSqlHelper.getColumns(parameterTable, "parameter"));
        SelectFromAndJoinCondition selectFrom = Select
                .builder()
                .select(columns)
                .from(parameterTable)
                .leftOuterJoin(entityTable)
                .on(Column.create("model_id", parameterTable))
                .equals(Column.create("id", entityTable));
        String select = entityManager.createSelect(selectFrom, Parameter.class, pageable, whereClause);

        RowsFetchSpec<Parameter> mappedResults = db.sql(select).map(this::processParameters);
        Flux<Parameter> parameters = mappedResults.all();
        parameters.subscribe(parameter -> {
            System.out.println("Parameter found: ");
            System.out.println(parameter); // Print the ModelDTO when it's available
        });
        return mappedResults;
    }

    RowsFetchSpec<ParameterTypeDefinition> createParameterTypeDefinitionQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = new ArrayList<>();
//        columns.addAll(ParameterSqlHelper.getColumns(parameterTable, "parameter"));
        columns.addAll(ParameterTypeDefinitionSqlHelper.getColumns(parameterTypeDefinitionTable, "parameterTypeDefinition"));
        SelectFromAndJoinCondition selectFrom = Select
                .builder()
                .select(columns)
                .from(parameterTable)
                .leftOuterJoin(parameterTypeDefinitionTable)
                .on(Column.create("parameter_id", parameterTypeDefinitionTable))
                .equals(Column.create("id", parameterTable));
        String select = entityManager.createSelect(selectFrom, ParameterTypeDefinition.class, pageable, whereClause);
        RowsFetchSpec<ParameterTypeDefinition> mappedResults = db.sql(select).map(this::processParameterTypeDefinition);
        Flux<ParameterTypeDefinition> parameterTypeDefinitionFlux = mappedResults.all();
        parameterTypeDefinitionFlux.subscribe(parameter -> {
            System.out.println("Parameter type definition found: ");
            System.out.println(parameter); // Print the ModelDTO when it's available
        });
        return mappedResults;
    }

    @Override
    public Flux<Model> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<Model> findById(UUID id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(StringUtils.wrap(id.toString(), "'")));
        Mono<Model> model = createQuery(null, whereClause).one();
        Mono<List<Parameter>> parameters = createParameterQuery(null, whereClause).all().collectList();

        Map<UUID, Mono<List<ParameterTypeDefinition>>> paramTypeDefMap = new HashMap<>();

        Mono<Model> modelWithParameters = parameters.flatMap(parameterList -> {
            // Fetch ParameterTypeDefinition for each Parameter and then proceed with setting parameters.
            List<Mono<Parameter>> parametersWithDefinitions = parameterList.stream().map(parameter -> {
                Comparison whereClauseForPTD = Conditions.isEqual(parameterTypeDefinitionTable.column("parameter_id"),
                        Conditions.just(StringUtils.wrap(parameter.getId().toString(), "'")));

                return createParameterTypeDefinitionQuery(null, whereClauseForPTD).all().collectList()
                        .doOnNext(parameterTypeDefinitions -> {
                            parameter.setDefinitions(parameterTypeDefinitions); // set the list of parameter type definitions
                        })
                        .thenReturn(parameter);
            }).collect(Collectors.toList());

            parametersWithDefinitions.stream().forEach(
                    parameterMono -> {
                        parameterMono.subscribe(
                                result -> System.out.println("Parameter **************** obtained: " + result),
                                error -> System.out.println("Error: " + error.getMessage()),
                                () -> System.out.println("Completed!")
                        );
                    }
            );

            return Flux.fromIterable(parametersWithDefinitions)
                    .flatMap(mono -> mono)
                    .collectList()
                    .flatMap(updatedParameters -> {
                        // Now it's the time to proceed with setting parameters.
                        return model.doOnNext(m -> {
                            m.setParameters(updatedParameters); // set the list of parameters

                            // Print size of the list.
                            System.out.println("Size: " + updatedParameters.size());

                            // Print all elements of the list.
                            for (Parameter p : updatedParameters) {
                                System.out.println(p.toString()); // Assuming Parameter class has a proper toString() method.
                            }
                        });
                    });
        });

        modelWithParameters.subscribe(
                result -> System.out.println("Model obtained: " + result),
                error -> System.out.println("Error: " + error.getMessage()),
                () -> System.out.println("Completed!")
        );

        return modelWithParameters;
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

    private Parameter processParameters(Row row, RowMetadata metadata) {
        Parameter parameter = parameterMapper.apply(row, "parameter");
        parameter.setModelId(row.get("parameter_model_id", UUID.class));
        parameter.setFixedValue(row.get("parameter_fixed_value", Boolean.class));
        parameter.setOrdering(row.get("parameter_ordering", Integer.class));
        parameter.setEnabled(row.get("parameter_enabled", Boolean.class));
        parameter.setDescription(row.get("parameter_description", String.class));
        parameter.setLabel(row.get("parameter_label", String.class));
        parameter.setName(row.get("parameter_name", String.class));
        parameter.setId(row.get("parameter_id", UUID.class));
        return parameter;
    }

    private ParameterTypeDefinition processParameterTypeDefinition(Row row, RowMetadata metadata) {
        ParameterTypeDefinition parameterTypeDefinition = parametertypedefinitionMapper.apply(row, "parametertypedefinition");
        return parameterTypeDefinition;
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
