package ai.turintech.catalog.repository;

import ai.turintech.catalog.domain.*;
import ai.turintech.catalog.repository.rowmapper.*;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.convert.R2dbcConverter;
import org.springframework.data.r2dbc.core.R2dbcEntityOperations;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.r2dbc.repository.support.SimpleR2dbcRepository;
import org.springframework.data.relational.core.sql.*;
import org.springframework.data.relational.core.sql.SelectBuilder.SelectFromAndJoinCondition;
import org.springframework.data.relational.repository.support.MappingRelationalEntityInformation;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.r2dbc.core.RowsFetchSpec;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple4;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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
    private final ParameterDistributionTypeRowMapper parameterdistributiontypeMapper;

    private final ModelGroupTypeRowMapper modelGroupTypeMapper;
    private final MetricRowMapper metricMapper;
    private final CategoricalParameterRowMapper categoricalParameterMapper;
    private final BooleanParameterRowMapper booleanParameterMapper;
    private final IntegerParameterRowMapper integerParameterMapper;
    private final FloatParameterRowMapper floatParameterMapper;
    private final ParameterTypeRowMapper parametertypeMapper;

    private final CategoricalParameterValueRowMapper categoricalparametervalueMapper;
    private final IntegerParameterValueRowMapper integerparametervalueMapper;
    private final FloatParameterRangeRowMapper floatparameterrangeMapper;
    private static final Table entityTable = Table.aliased("model", EntityManager.ENTITY_ALIAS);
    private static final Table mlTaskTable = Table.aliased("ml_task_type", "mlTask");
    private static final Table structureTable = Table.aliased("model_structure_type", "e_structure");
    private static final Table typeTable = Table.aliased("model_type", "model_type");
    private static final Table familyTypeTable = Table.aliased("model_family_type", "familyType");
    private static final Table ensembleTypeTable = Table.aliased("model_ensemble_type", "ensembleType");
    private static final Table parameterTable = Table.aliased("parameter", "parameter");
    private static final Table parameterTypeDefinitionTable = Table.aliased("parameter_type_definition", "parameterTypeDefinition");
    private static final Table parameterDistributionTypeTable = Table.aliased("parameter_distribution_type", "parameterDistributionType");
    private static final Table parameterTypeTable = Table.aliased("parameter_type", "parameterType");
    private static final Table categoricalParameterTable = Table.aliased("categorical_parameter", "categoricalParameter");
    private static final Table categoricalParameterValueTable = Table.aliased("categorical_parameter_value", "categoricalParameterValue");
    private static final Table booleanParameterTable = Table.aliased("boolean_parameter", "booleanParameter");
    private static final Table integerParameterTable = Table.aliased("integer_parameter", "integerParameter");
    private static final Table integerParameterValueTable = Table.aliased("integer_parameter_value", "integerParameterValue");
    private static final Table floatParameterTable = Table.aliased("float_parameter", "floatParameter");
    private static final Table floatParameterRangeTable = Table.aliased("float_parameter_range", "floatParameterRange");
    private static final Table groupTable = Table.aliased("model_group_type", "modelGroup");
    private static final Table modelGroupTable = Table.aliased("rel_model__groups", "rel_model__groups");
    private static final Table metricTable = Table.aliased("metric", "metric");
    private static final Table modelIncompatibleMetricsTable = Table.aliased("rel_model__incompatible_metrics", "rel_model__incompatible_metrics");

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
        ParameterDistributionTypeRowMapper parameterDistributionTypeMapper,
        ParameterTypeRowMapper parameterTypeMapper,
        CategoricalParameterRowMapper categoricalParameterMapper,
        CategoricalParameterValueRowMapper categoricalParameterValueMapper,
        BooleanParameterRowMapper booleanParameterMapper,
        IntegerParameterRowMapper integerParameterMapper,
        IntegerParameterValueRowMapper integerParameterValueMapper,
        FloatParameterRowMapper floatParameterMapper,
        FloatParameterRangeRowMapper floatParameterRangeMapper,
        ModelGroupTypeRowMapper modelGroupTypeMapper,
        MetricRowMapper metricMapper,
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
        this.parameterdistributiontypeMapper = parameterDistributionTypeMapper;
        this.parametertypeMapper = parameterTypeMapper;
        this.categoricalParameterMapper = categoricalParameterMapper;
        this.categoricalparametervalueMapper = categoricalParameterValueMapper;
        this.booleanParameterMapper = booleanParameterMapper;
        this.integerParameterMapper = integerParameterMapper;
        this.integerparametervalueMapper = integerParameterValueMapper;
        this.floatParameterMapper = floatParameterMapper;
        this.floatparameterrangeMapper = floatParameterRangeMapper;
        this.modelGroupTypeMapper = modelGroupTypeMapper;
        this.metricMapper = metricMapper;
    }

    @Override
    public Flux<Model> findAllBy(Pageable pageable) {
        return createModelJoinQuery(pageable, null).all();
    }

    RowsFetchSpec<Model> createModelJoinQuery(Pageable pageable, Condition whereClause) {
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

    RowsFetchSpec<ModelGroupType> createModelGroupJoinQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = ModelSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(ModelGroupTypeSqlHelper.getColumns(groupTable, "modelGroup"));
        SelectFromAndJoinCondition selectFrom = Select
                .builder()
                .select(columns)
                .from(modelGroupTable)
                .leftOuterJoin(entityTable)
                .on(Column.create("id", entityTable))
                .equals(Column.create("model_id", modelGroupTable))
                .leftOuterJoin(groupTable)
                .on(Column.create("id", groupTable))
                .equals(Column.create("group_id", modelGroupTable));
        String select = entityManager.createSelect(selectFrom, Model.class, pageable, whereClause);
        RowsFetchSpec<ModelGroupType> mappedResults = db.sql(select).map(this::modelGroupProcess);
        return mappedResults;
    }

    RowsFetchSpec<Metric> createModelMetricJoinQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = ModelSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(ModelGroupTypeSqlHelper.getColumns(metricTable, "metric"));
        SelectFromAndJoinCondition selectFrom = Select
                .builder()
                .select(columns)
                .from(modelIncompatibleMetricsTable)
                .leftOuterJoin(entityTable)
                .on(Column.create("id", entityTable))
                .equals(Column.create("model_id", modelIncompatibleMetricsTable))
                .leftOuterJoin(metricTable)
                .on(Column.create("id", metricTable))
                .equals(Column.create("metric_id", modelIncompatibleMetricsTable));
        String select = entityManager.createSelect(selectFrom, Model.class, pageable, whereClause);
        RowsFetchSpec<Metric> mappedResults = db.sql(select).map(this::metricProcess);
        return mappedResults;
    }
    RowsFetchSpec<Parameter> createParameterQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = new ArrayList<>();
        columns.addAll(ParameterSqlHelper.getColumns(parameterTable, "parameter"));
        SelectBuilder.SelectOrdered selectFrom = Select
                .builder()
                .select(columns)
                .from(parameterTable)
                .leftOuterJoin(entityTable)
                .on(Column.create("model_id", parameterTable))
                .equals(Column.create("id", entityTable))
                .orderBy(Column.create("ordering", parameterTable)); // added orderBy method with entityTable's ordering column in ascending order

        String select = entityManager.createSelect((SelectBuilder.SelectFromAndJoin) selectFrom, Parameter.class, pageable, whereClause);
        RowsFetchSpec<Parameter> mappedResults = db.sql(select).map(this::processParameters);
        return mappedResults;
    }

    RowsFetchSpec<ParameterTypeDefinition> createParameterTypeDefinitionQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = new ArrayList<>();
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
        return mappedResults;
    }

    RowsFetchSpec<ParameterTypeDefinition> createParameterDistributionTypeJoinQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = new ArrayList<>();
        columns.addAll(ParameterTypeDefinitionSqlHelper.getColumns(parameterTypeDefinitionTable, "parameterTypeDefinition"));
        columns.addAll(ParameterTypeSqlHelper.getColumns(parameterTypeTable, "parameterType"));
        columns.addAll(ParameterDistributionTypeSqlHelper.getColumns(parameterDistributionTypeTable, "parameterDistributionType"));
        columns.addAll(CategoricalParameterSqlHelper.getColumns(categoricalParameterTable, "categoricalParameter"));
        columns.addAll(BooleanParameterSqlHelper.getColumns(booleanParameterTable, "booleanParameter"));
        columns.addAll(IntegerParameterSqlHelper.getColumns(integerParameterTable, "integerParameter"));
        columns.addAll(FloatParameterSqlHelper.getColumns(floatParameterTable, "floatParameter"));
        SelectFromAndJoinCondition selectFrom = Select
                .builder()
                .select(columns)
                .from(parameterTypeDefinitionTable)
                .leftOuterJoin(parameterTypeTable)
                .on(Column.create("parameter_type_id", parameterTypeDefinitionTable))
                .equals(Column.create("id", parameterTypeTable))
                .leftOuterJoin(parameterDistributionTypeTable)
                .on(Column.create("parameter_distribution_type_id", parameterTypeDefinitionTable))
                .equals(Column.create("id", parameterDistributionTypeTable))
                .leftOuterJoin(categoricalParameterTable)
                .on(Column.create("id", parameterTypeDefinitionTable))
                .equals(Column.create("parameter_type_definition_id", categoricalParameterTable))
                .leftOuterJoin(booleanParameterTable)
                .on(Column.create("id", parameterTypeDefinitionTable))
                .equals(Column.create("parameter_type_definition_id", booleanParameterTable))
                .leftOuterJoin(integerParameterTable)
                .on(Column.create("id", parameterTypeDefinitionTable))
                .equals(Column.create("parameter_type_definition_id", integerParameterTable))
                .leftOuterJoin(floatParameterTable)
                .on(Column.create("id", parameterTypeDefinitionTable))
                .equals(Column.create("parameter_type_definition_id", floatParameterTable));
        String select = entityManager.createSelect(selectFrom, ParameterTypeDefinition.class, pageable, whereClause);
        RowsFetchSpec<ParameterTypeDefinition> mappedResults = db.sql(select).map(this::processAddParameterProperties);
        return mappedResults;
    }

    RowsFetchSpec<CategoricalParameterValue> createCategoricalValuesJoinQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = new ArrayList<>();
        columns.addAll(CategoricalParameterValueSqlHelper.getColumns(categoricalParameterValueTable, "categoricalparametervalue"));
        SelectFromAndJoinCondition selectFrom = Select
                .builder()
                .select(columns)
                .from(categoricalParameterTable)
                .leftOuterJoin(categoricalParameterValueTable)
                .on(Column.create("parameter_type_definition_id", categoricalParameterTable))
                .equals(Column.create("parameter_type_definition_id", categoricalParameterValueTable));
        String select = entityManager.createSelect(selectFrom, Model.class, pageable, whereClause);
        RowsFetchSpec<CategoricalParameterValue> mappedResults = db.sql(select).map(this::processCategoricalParameterValue);
        return mappedResults;
    }

    RowsFetchSpec<IntegerParameterValue> createIntegerValuesJoinQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = new ArrayList<>();
        columns.addAll(IntegerParameterValueSqlHelper.getColumns(integerParameterValueTable, "integerparametervalue"));
        SelectFromAndJoinCondition selectFrom = Select
                .builder()
                .select(columns)
                .from(integerParameterTable)
                .leftOuterJoin(integerParameterValueTable)
                .on(Column.create("parameter_type_definition_id", integerParameterTable))
                .equals(Column.create("parameter_type_definition_id", integerParameterValueTable));
        String select = entityManager.createSelect(selectFrom, Model.class, pageable, whereClause);
        RowsFetchSpec<IntegerParameterValue> mappedResults = db.sql(select).map(this::processIntegerParameterValue);
        return mappedResults;
    }

    RowsFetchSpec<FloatParameterRange> createFloatValuesJoinQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = new ArrayList<>();
        columns.addAll(FloatParameterRangeSqlHelper.getColumns(floatParameterRangeTable, "floatparameterrange"));
        SelectFromAndJoinCondition selectFrom = Select
                .builder()
                .select(columns)
                .from(floatParameterTable)
                .leftOuterJoin(floatParameterRangeTable)
                .on(Column.create("parameter_type_definition_id", floatParameterTable))
                .equals(Column.create("parameter_type_definition_id", floatParameterRangeTable));
        String select = entityManager.createSelect(selectFrom, Model.class, pageable, whereClause);
        RowsFetchSpec<FloatParameterRange> mappedResults = db.sql(select).map(this::processFloatParameterValue);
        return mappedResults;
    }

    @Override
    public Flux<Model> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<Model> findById(UUID id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(StringUtils.wrap(id.toString(), "'")));

        return Mono.zip(
                        fetchModel(whereClause),
                        fetchParameters(whereClause),
                        fetchModelGroupJoinQuery(whereClause),
                        fetchModelMetricJoinQuery(whereClause)
                )
                .flatMap(this::createModelWithParametersAndMetrics);
    }

    private Mono<Model> fetchModel(Comparison whereClause){
        return createModelJoinQuery(null, whereClause).one();
    }

    private Mono<List<Parameter>> fetchParameters(Comparison whereClause){
        return createParameterQuery(null, whereClause).all().collectList()
                .flatMapMany(Flux::fromIterable)
                .flatMap(this::populateParameterWithDefinitions)
                .collectList();
    }

    private Mono<Parameter> populateParameterWithDefinitions(Parameter parameter){
        Comparison whereClauseForPTD = Conditions.isEqual(parameterTypeDefinitionTable.column("parameter_id"),
                Conditions.just(StringUtils.wrap(parameter.getId().toString(), "'")));

        return createParameterTypeDefinitionQuery(null, whereClauseForPTD).all().collectList()
                .flatMapMany(Flux::fromIterable)
                .flatMap(this::populateParameterDefinitionsWithType)
                .collectList()
                .doOnNext(parameter::setDefinitions)
                .thenReturn(parameter);
    }

    private Mono<ParameterTypeDefinition> populateParameterDefinitionsWithType(ParameterTypeDefinition definition){
        Comparison whereClauseForDist = Conditions.isEqual(parameterTypeDefinitionTable.column("id"),
                Conditions.just(StringUtils.wrap(definition.getId().toString(), "'")));

        return createParameterDistributionTypeJoinQuery(null, whereClauseForDist).one();
    }

    private Mono<List<ModelGroupType>> fetchModelGroupJoinQuery(Comparison whereClause){
        return createModelGroupJoinQuery(null, whereClause).all().collectList();
    }

    private Mono<List<Metric>> fetchModelMetricJoinQuery(Comparison whereClause){
        return createModelMetricJoinQuery(null, whereClause).all().collectList();
    }

    private Mono<Model> createModelWithParametersAndMetrics(Tuple4<Model, List<Parameter>, List<ModelGroupType>, List<Metric>> tuple){
        Model mod = tuple.getT1();
        List<Parameter> parameters = tuple.getT2();
        List<ModelGroupType> modelGroupTypes = tuple.getT3();
        List<Metric> modelMetrics = tuple.getT4();

        mod.setParameters(parameters);
        mod.setGroups(modelGroupTypes);
        mod.setIncompatibleMetrics(modelMetrics);

        return Flux.fromIterable(parameters)
                .flatMap(this::populateParameterWithValues)
                .collectList()
                .doOnNext(mod::setParameters)
                .thenReturn(mod);
    }

    private Mono<Parameter> populateParameterWithValues(Parameter parameter){
        return Flux.fromIterable(parameter.getDefinitions()).flatMap(def -> {
                    if (def.getCategoricalParameter() != null && def.getCategoricalParameter().getParameterTypeDefinitionId() != null) {
                        return fetchCategoricalValues(def);
                    } else if (def.getIntegerParameter() != null && def.getIntegerParameter().getParameterTypeDefinitionId() != null){
                        return fetchIntegerValues(def);
                    } else if (def.getFloatParameter() != null && def.getFloatParameter().getParameterTypeDefinitionId() != null){
                        return fetchFloatValues(def);
                    } else {
                        return Flux.just(def);
                    }
                }).collectList()
                .doOnNext(updatedDefs -> {
                    parameter.setDefinitions(updatedDefs);
                    updatedDefs.forEach(def -> {
                        if (def.getCategoricalParameter() != null) {
                            System.out.println("After set: " + def.getCategoricalParameter()); // Print after set
                        }
                    });
                })
                .thenReturn(parameter);
    }

    private Mono<ParameterTypeDefinition> fetchCategoricalValues(ParameterTypeDefinition def) {
        Comparison whereClauseForValues = Conditions.isEqual(categoricalParameterTable.column("parameter_type_definition_id"),
                Conditions.just(StringUtils.wrap(def.getId().toString(), "'")));

        return createCategoricalValuesJoinQuery(null, whereClauseForValues).all()
                .collectList()
                .doOnNext(categoricalParameterValues -> {
                    def.getCategoricalParameter().setCategoricalParameterValues(categoricalParameterValues);
                    System.out.println("Categorical Parameter Values fetched: " + categoricalParameterValues);  // Added print
                })
                .thenReturn(def);
    }

    private Mono<ParameterTypeDefinition> fetchIntegerValues(ParameterTypeDefinition def) {
        Comparison whereClauseForValues = Conditions.isEqual(integerParameterTable.column("parameter_type_definition_id"),
                Conditions.just(StringUtils.wrap(def.getId().toString(), "'")));

        return createIntegerValuesJoinQuery(null, whereClauseForValues).all()
                .collectList()
                .doOnNext(integerParameterValues -> {
                    def.getIntegerParameter().setIntegerParameterValues(integerParameterValues);
                    System.out.println("Integer Parameter Values fetched: " + integerParameterValues);  // Added print
                })
                .thenReturn(def);
    }

    private Mono<ParameterTypeDefinition> fetchFloatValues(ParameterTypeDefinition def) {
        Comparison whereClauseForValues = Conditions.isEqual(floatParameterTable.column("parameter_type_definition_id"),
                Conditions.just(StringUtils.wrap(def.getId().toString(), "'")));

        return createFloatValuesJoinQuery(null, whereClauseForValues).all()
                .collectList()
                .doOnNext(floatParameterRanges -> {
                    def.getFloatParameter().setFloatParameterRanges(floatParameterRanges);
                    System.out.println("Float Parameter Values fetched: " + floatParameterRanges);  // Added print
                })
                .thenReturn(def);
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

    private ModelGroupType modelGroupProcess(Row row, RowMetadata metadata) {
        return modelGroupTypeMapper.apply(row, "modelGroup");
    }

    private Metric metricProcess(Row row, RowMetadata metadata) {
        return  metricMapper.apply(row, "metric");
    }

    private Parameter processParameters(Row row, RowMetadata metadata) {
        return parameterMapper.apply(row, "parameter");
    }

    private ParameterTypeDefinition processParameterTypeDefinition(Row row, RowMetadata metadata) {
        return parametertypedefinitionMapper.apply(row, "parametertypedefinition");
    }

    private ParameterTypeDefinition processAddParameterProperties(Row row, RowMetadata metadata) {
        ParameterTypeDefinition parameterTypeDefinition = parametertypedefinitionMapper.apply(row, "parametertypedefinition");
        parameterTypeDefinition.setType(parametertypeMapper.apply(row, "parametertype"));
        parameterTypeDefinition.setDistribution(parameterdistributiontypeMapper.apply(row, "parameterdistributiontype"));
        parameterTypeDefinition.setCategoricalParameter(categoricalParameterMapper.apply(row, "categoricalparameter"));
        parameterTypeDefinition.setBooleanParameter(booleanParameterMapper.apply(row, "booleanparameter"));
        parameterTypeDefinition.setIntegerParameter(integerParameterMapper.apply(row, "integerparameter"));
        parameterTypeDefinition.setFloatParameter(floatParameterMapper.apply(row, "floatparameter"));
        return parameterTypeDefinition;
    }

    private CategoricalParameterValue processCategoricalParameterValue(Row row, RowMetadata metadata) {
        CategoricalParameterValue categoricalParameterValue = categoricalparametervalueMapper.apply(row, "categoricalparametervalue");
        return categoricalParameterValue;
    }

    private IntegerParameterValue processIntegerParameterValue(Row row, RowMetadata metadata) {
        IntegerParameterValue integerParameterValue = integerparametervalueMapper.apply(row, "integerparametervalue");
        return integerParameterValue;
    }

    private FloatParameterRange processFloatParameterValue(Row row, RowMetadata metadata) {
        FloatParameterRange floatParameterRange = floatparameterrangeMapper.apply(row, "floatparameterrange");
        return floatParameterRange;
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
