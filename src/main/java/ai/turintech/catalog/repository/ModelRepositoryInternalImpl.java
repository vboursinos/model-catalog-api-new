package ai.turintech.catalog.repository;

import ai.turintech.catalog.anotatation.Columns;
import ai.turintech.catalog.config.EntityConfiguration;
import ai.turintech.catalog.domain.*;
import ai.turintech.catalog.repository.rowmapper.*;
import ai.turintech.catalog.service.dto.*;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple3;
import reactor.util.function.Tuple4;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

/**
 * Spring Data R2DBC custom repository implementation for the Model entity.
 */
@SuppressWarnings("unused")
@Repository
class ModelRepositoryInternalImpl extends SimpleR2dbcRepository<Model, UUID> implements ModelRepositoryInternal {

    @Autowired
    private TableInfoDTO tableInfoDTONew;
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
    public Flux<Model> findAllBy(Pageable pageable, FilterDTO filterDTO, List<SearchDTO> searchParams) {
        Condition conditions = createConditions(filterDTO, searchParams);
        return createModelJoinQuery(pageable, conditions)
                .all()
                .flatMap(model -> {
                    findByIdWithoutParameters(model.getId()).subscribe();
                    Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(StringUtils.wrap(model.getId().toString(), "'")));
                    return Mono.zip(
                            Mono.just(model),
                            fetchModelGroupJoinQuery(whereClause),
                            fetchModelMetricJoinQuery(whereClause)
                    ).flatMap(this::createModelWithGroupsAndMetrics);
                });
    }

    @Override
    public Flux<Model> findAllBy(Pageable pageable) {
        return findAllBy(pageable);
    }

    public void init(){
        System.out.println(this.tableInfoDTONew.getRelationships());
    }

    public static GenericQueryDTO initModel() {
        String[] modelColumnsArray = {"id", "name", "description", "display_name", "advantages", "enabled", "decision_tree", "disadvantages", "model_type_id", "structure_id", "family_type_id", "ensemble_type_id", "ml_task_id"};
        List<String> modelColumns = new ArrayList<>(Arrays.asList(modelColumnsArray));
        RelationshipDTO modelMltaskRelationshipDTO = new RelationshipDTO(RelationshipTypeDTO.MANY_TO_ONE, "ml_task_type", Table.aliased("ml_task_type", "mlTask"), "ml_task_id", "id", "mlTask");
        RelationshipDTO modelStructureRelationshipDTO = new RelationshipDTO(RelationshipTypeDTO.MANY_TO_ONE, "model_structure_type", Table.aliased("model_structure_type", "e_structure"), "structure_id", "id", "structure");
        RelationshipDTO modelTypeRelationshipDTO = new RelationshipDTO(RelationshipTypeDTO.MANY_TO_ONE, "model_type", Table.aliased("model_type", "model_type"), "model_type_id", "id", "modelType");
        RelationshipDTO modelFamilyTypeRelationshipDTO = new RelationshipDTO(RelationshipTypeDTO.MANY_TO_ONE, "model_family_type", Table.aliased("model_family_type", "familyType"), "family_type_id", "id", "familyType");
        RelationshipDTO modelEnsembleTypeRelationshipDTO = new RelationshipDTO(RelationshipTypeDTO.MANY_TO_ONE, "model_ensemble_type", Table.aliased("model_ensemble_type", "ensembleType"), "ensemble_type_id", "id", "ensembleType");
        List<RelationshipDTO> modelRelationships = new ArrayList<>(Arrays.asList(modelMltaskRelationshipDTO, modelStructureRelationshipDTO, modelTypeRelationshipDTO, modelFamilyTypeRelationshipDTO, modelEnsembleTypeRelationshipDTO));
        TableInfoDTO modelTableInfoDTO = new TableInfoDTO(Table.aliased("model", EntityManager.ENTITY_ALIAS), "e", modelColumns, modelRelationships);

        String[] modelTypeColumnsArray = {"id", "name"};
        List<String> modelTypeColumns = new ArrayList<>(Arrays.asList(modelTypeColumnsArray));
        TableInfoDTO modelTypeTableInfoDTO = new TableInfoDTO(Table.aliased("model_type", "model_type"), "modelType", modelTypeColumns, Collections.emptyList());

        String[] structureColumnsArray = {"id", "name"};
        List<String> structureColumns = new ArrayList<>(Arrays.asList(structureColumnsArray));
        TableInfoDTO structureTableInfoDTO = new TableInfoDTO(Table.aliased("model_structure_type", "e_structure"), "structure", structureColumns, Collections.emptyList());

        String[] familyTypeColumnsArray = {"id", "name"};
        List<String> familyTypeColumns = new ArrayList<>(Arrays.asList(familyTypeColumnsArray));
        TableInfoDTO familyTypeTableInfoDTO = new TableInfoDTO(Table.aliased("model_family_type", "familyType"), "familyType", familyTypeColumns, Collections.emptyList());

        String[] ensembleTypeColumnsArray = {"id", "name"};
        List<String> ensembleTypeColumns = new ArrayList<>(Arrays.asList(ensembleTypeColumnsArray));
        TableInfoDTO ensembleTypeTableInfoDTO = new TableInfoDTO(Table.aliased("model_ensemble_type", "ensembleType"), "ensembleType", ensembleTypeColumns, Collections.emptyList());

        String[] mlTaskColumnsArray = {"id", "name"};
        List<String> mlTaskColumns = new ArrayList<>(Arrays.asList(mlTaskColumnsArray));
        TableInfoDTO mlTaskTableInfoDTO = new TableInfoDTO(Table.aliased("ml_task_type", "mlTask"), "mlTask", mlTaskColumns, Collections.emptyList());

        List<TableInfoDTO> tables = new ArrayList<>();
        tables.add(modelTableInfoDTO);
        tables.add(modelTypeTableInfoDTO);
        tables.add(structureTableInfoDTO);
        tables.add(familyTypeTableInfoDTO);
        tables.add(ensembleTypeTableInfoDTO);
        tables.add(mlTaskTableInfoDTO);
        Map<String, List<String>> tableColumnsMap = new HashMap<>();
        tableColumnsMap.put("model", modelColumns);
        tableColumnsMap.put("model_type", modelTypeColumns);
        tableColumnsMap.put("model_structure_type", structureColumns);
        tableColumnsMap.put("model_family_type", familyTypeColumns);
        tableColumnsMap.put("model_ensemble_type", ensembleTypeColumns);
        tableColumnsMap.put("ml_task_type", mlTaskColumns);
        return new GenericQueryDTO(tables, tableColumnsMap);
    }

    public static GenericQueryDTO initGroupTable() {

        String[] modelGroupColumnsArray = {"model_id", "group_id"};
        List<String> modelGroupColumns = new ArrayList<>(Arrays.asList(modelGroupColumnsArray));
        RelationshipDTO relationshipDTO1 = new RelationshipDTO(RelationshipTypeDTO.MANY_TO_MANY, "model", Table.aliased("model", "e"), "model_id", "id", "e");
        RelationshipDTO relationshipDTO2 = new RelationshipDTO(RelationshipTypeDTO.MANY_TO_MANY, "model_group_type", Table.aliased("model_group_type", "modelGroup"), "group_id", "id", "modelGroup");
        List<RelationshipDTO> modelRelationships = new ArrayList<>(Arrays.asList(relationshipDTO1, relationshipDTO2));
        TableInfoDTO modelGroupTableInfoDTO = new TableInfoDTO(Table.aliased("rel_model__groups", "rel_model__groups"), "rel_model__groups", modelGroupColumns, modelRelationships);


        String[] groupColumnsArray = {"id", "name"};
        List<String> groupColumns = new ArrayList<>(Arrays.asList(groupColumnsArray));
        TableInfoDTO groupTableInfoDTO = new TableInfoDTO(Table.aliased("model_group_type", "modelGroup"), "modelGroup", groupColumns, Collections.emptyList());

        String[] modelColumnsArray = {"id", "name", "description", "display_name", "advantages", "enabled", "decision_tree", "disadvantages", "model_type_id", "structure_id", "family_type_id", "ensemble_type_id", "ml_task_id"};
        List<String> modelColumns = new ArrayList<>(Arrays.asList(modelColumnsArray));
        TableInfoDTO modelTableInfoDTO2 = new TableInfoDTO(Table.aliased("model", "e"), "e", modelColumns, Collections.emptyList());

        List<TableInfoDTO> tables = new ArrayList<>();
        tables.add(modelGroupTableInfoDTO);
        tables.add(groupTableInfoDTO);
        tables.add(modelTableInfoDTO2);
        Map<String, List<String>> tableColumnsMap = new HashMap<>();
        tableColumnsMap.put("rel_model__groups", modelGroupColumns);
        tableColumnsMap.put("model_group_type", groupColumns);
        tableColumnsMap.put("model", modelColumns);
        return new GenericQueryDTO(tables, tableColumnsMap);
    }

    RowsFetchSpec<Model> createModelJoinQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = new ArrayList<>();
        GenericQueryDTO genericQueryDTO = initModel();
        TableInfoDTO table = genericQueryDTO.getTables().get(0);
        Map<String,List<Expression>> tableColumnsMap = ModelSqlHelper.getColumnsGeneric(genericQueryDTO);
        columns.addAll(tableColumnsMap.get(table.getTable().getName().toString()));
        SelectBuilder.SelectFromAndJoinCondition selectFrom = null;
        boolean firstJoin = true;
        if (!table.getRelationships().isEmpty()) {
            for (RelationshipDTO relationship : table.getRelationships()) {
                String foreignKey = relationship.getFromColumn();
                Table tableName = relationship.getToTableObject();
                String pkJoinTable = relationship.getToColumn();
                if ("many-to-one".equals(relationship.getType().getValue())) {
                    if (firstJoin) {
                        selectFrom = Select.builder().select(columns).from(table.getTable()).join(tableName).on(Column.create(foreignKey, table.getTable())).equals(Column.create(pkJoinTable, tableName));
                        firstJoin = false;
                    } else {
                        selectFrom = selectFrom.leftOuterJoin(tableName).on(Column.create(foreignKey, table.getTable())).equals(Column.create(pkJoinTable, tableName));
                    }
                }
            }
        }
        String select = entityManager.createSelect(selectFrom, Object.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    RowsFetchSpec<ModelGroupType> createModelGroupJoinQuery(Pageable pageable, Condition whereClause) {
        init();
        List<Expression> columns = new ArrayList<>();
        GenericQueryDTO genericQueryDTO = initGroupTable();
        TableInfoDTO table = genericQueryDTO.getTables().get(0);
        Map<String,List<Expression>> tableColumnsMap = ModelSqlHelper.getColumnsGeneric(genericQueryDTO);
        columns.addAll(tableColumnsMap.get(table.getTable().getName().toString()));
        SelectBuilder.SelectFromAndJoinCondition selectFrom = null;
        boolean firstJoin = true;
        if (!table.getRelationships().isEmpty()) {
            for (RelationshipDTO relationship : table.getRelationships()) {
                String foreignKey = relationship.getFromColumn();
                Table tableName = relationship.getToTableObject();
                String pkJoinTable = relationship.getToColumn();
                if ("many-to-many".equals(relationship.getType().getValue())) {
                    if (firstJoin) {
                        selectFrom = Select.builder().select(columns).from(table.getTable()).leftOuterJoin(tableName).on(Column.create(foreignKey, table.getTable())).equals(Column.create(pkJoinTable, tableName));
                        firstJoin = false;
                    } else {
                        selectFrom = selectFrom.leftOuterJoin(tableName).on(Column.create(foreignKey, table.getTable())).equals(Column.create(pkJoinTable, tableName));
                    }
                }
            }
        }

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
                .orderBy(Column.create("ordering", parameterTable));
        String select = entityManager.createSelect((SelectBuilder.SelectFromAndJoin) selectFrom, Parameter.class, pageable, whereClause);
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

    public Mono<Model> findByIdWithoutParameters(UUID id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(StringUtils.wrap(id.toString(), "'")));
        return Mono.zip(
                        fetchModel(whereClause),
                        fetchParameters(null),
                        fetchModelGroupJoinQuery(whereClause),
                        fetchModelMetricJoinQuery(whereClause)
                )
                .flatMap(this::createModelWithParametersAndMetrics);
    }


    private Mono<Model> fetchModel(Comparison whereClause) {
        return createModelJoinQuery(null, whereClause).one();
    }

    private Mono<List<Parameter>> fetchParameters(Comparison whereClause) {
        return createParameterQuery(null, whereClause).all().collectList()
                .flatMapMany(Flux::fromIterable)
                .flatMap(this::populateParameterWithDefinitions)
//                .sort(Comparator.comparing(Parameter::getOrdering))
                .collectList();
    }

    private Mono<Parameter> populateParameterWithDefinitions(Parameter parameter) {
        Comparison whereClauseForPTD = Conditions.isEqual(parameterTypeDefinitionTable.column("parameter_id"),
                Conditions.just(StringUtils.wrap(parameter.getId().toString(), "'")));

        return createParameterTypeDefinitionQuery(null, whereClauseForPTD).all().collectList()
                .flatMapMany(Flux::fromIterable)
                .flatMap(this::populateParameterDefinitionsWithType)
                .collectList()
                .doOnNext(parameter::setDefinitions)
                .thenReturn(parameter);
    }

    private Mono<ParameterTypeDefinition> populateParameterDefinitionsWithType(ParameterTypeDefinition definition) {
        Comparison whereClauseForDist = Conditions.isEqual(parameterTypeDefinitionTable.column("id"),
                Conditions.just(StringUtils.wrap(definition.getId().toString(), "'")));

        return createParameterDistributionTypeJoinQuery(null, whereClauseForDist).one();
    }

    private Mono<List<ModelGroupType>> fetchModelGroupJoinQuery(Comparison whereClause) {
        return createModelGroupJoinQuery(null, whereClause).all().collectList();
    }

    private Mono<List<Metric>> fetchModelMetricJoinQuery(Comparison whereClause) {
        return createModelMetricJoinQuery(null, whereClause).all().collectList();
    }

    private Mono<Model> createModelWithGroupsAndMetrics(Tuple3<Model, List<ModelGroupType>, List<Metric>> tuple) {
        Model mod = tuple.getT1();
        List<ModelGroupType> modelGroupTypes = tuple.getT2();
        List<Metric> modelMetrics = tuple.getT3();

        mod.setGroups(modelGroupTypes);
        mod.setIncompatibleMetrics(modelMetrics);
        System.out.println(String.format("internal mod: %s", mod));
        return Flux.fromIterable(modelMetrics)
                .collectList()
                .thenReturn(mod);
    }

    private Mono<Model> createModelWithParametersAndMetrics(Tuple4<Model, List<Parameter>, List<ModelGroupType>, List<Metric>> tuple) {
        Model mod = tuple.getT1();
        List<Parameter> parameters = tuple.getT2();
        List<ModelGroupType> modelGroupTypes = tuple.getT3();
        List<Metric> modelMetrics = tuple.getT4();

        mod.setParameters(parameters);
        mod.setGroups(modelGroupTypes);
        mod.setIncompatibleMetrics(modelMetrics);
        System.out.println(String.format("internal mod: %s", mod));
        return Flux.fromIterable(parameters)
                .flatMap(this::populateParameterWithValues)
                .collectList()
                .doOnNext(mod::setParameters)
                .thenReturn(mod);
    }

    private Mono<Parameter> populateParameterWithValues(Parameter parameter) {
        return Flux.fromIterable(parameter.getDefinitions()).flatMap(def -> {
                    if (def.getCategoricalParameter() != null && def.getCategoricalParameter().getParameterTypeDefinitionId() != null) {
                        return fetchCategoricalValues(def);
                    } else if (def.getIntegerParameter() != null && def.getIntegerParameter().getParameterTypeDefinitionId() != null) {
                        return fetchIntegerValues(def);
                    } else if (def.getFloatParameter() != null && def.getFloatParameter().getParameterTypeDefinitionId() != null) {
                        return fetchFloatValues(def);
                    } else {
                        return Flux.just(def);
                    }
                }).collectList()
                .doOnNext(updatedDefs -> {
                    parameter.setDefinitions(updatedDefs);
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
//                    System.out.println("Categorical Parameter Values fetched: " + categoricalParameterValues);
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
//                    System.out.println("Integer Parameter Values fetched: " + integerParameterValues);
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
//                    System.out.println("Float Parameter Values fetched: " + floatParameterRanges);
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

    @Override
    public Mono<Long> count(FilterDTO filterDTO, List<SearchDTO> searchParams) {

        String baseQuery = "SELECT COUNT(*) FROM " + entityTable
                + " LEFT OUTER JOIN " + mlTaskTable
                + " ON " + entityTable.column("ml_task_id")
                + " = " + mlTaskTable.column("id")
                + " LEFT OUTER JOIN " + typeTable
                + " ON " + entityTable.column("model_type_id")
                + " = " + typeTable.column("id")
                + " LEFT OUTER JOIN " + structureTable
                + " ON " + entityTable.column("structure_id")
                + " = " + structureTable.column("id");

        Map<Column, Object> criteria = new HashMap<>();
        if (filterDTO.getMlTask() != null) {
            criteria.put(mlTaskTable.column("name"), filterDTO.getMlTask());
        }
        if (filterDTO.getModelType() != null) {
            criteria.put(typeTable.column("name"), filterDTO.getModelType());
        }
        if (filterDTO.getStructure() != null) {
            criteria.put(structureTable.column("name"), filterDTO.getStructure());
        }
        if (filterDTO.isEnabled() != null) {
            criteria.put(entityTable.column("enabled"), filterDTO.isEnabled());
        }
        for (SearchDTO searchDTO : searchParams) {
            if (searchDTO.getKey() != null) {
                criteria.put(entityTable.column(searchDTO.getKey()), "%" + searchDTO.getValue() + "%");
            }
        }
        String query = buildQueryWithConditions(baseQuery, criteria);
        DatabaseClient.GenericExecuteSpec genericExecuteSpec = r2dbcEntityTemplate.getDatabaseClient().sql(query);

        for (Map.Entry<Column, Object> entry : criteria.entrySet()) {
            genericExecuteSpec = genericExecuteSpec.bind(entry.getKey().toString(), entry.getValue());
        }

        return genericExecuteSpec
                .map((row, metadata) -> row.get(0, Long.class))
                .one();
    }

    private String buildQueryWithConditions(String baseQuery, Map<Column, Object> criteria) {
        if (criteria.isEmpty()) {
            return baseQuery;
        }
        String conditions = criteria.entrySet().stream()
                .map(entry -> {
                    String key = entry.getKey().toString();
                    String value = (String) entry.getValue();
                    return " " + key + ((value.contains("%")) ? " LIKE " : " = ") + ":" + key;
                })
                .collect(Collectors.joining(" AND "));
        return baseQuery + " WHERE" + conditions;
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


    public <T> T process(Row row, RowMetadata metadata, BiFunction<Row, String, T> rowMapper, Map<String, BiFunction<Row, String, ?>> propertySetters) {
        T entity = rowMapper.apply(row, "e");

        for (Map.Entry<String, BiFunction<Row, String, ?>> entry : propertySetters.entrySet()) {
            // assuming a method that sets this
            setProperty(entity, entry.getKey(), entry.getValue().apply(row, entry.getKey()));
        }
        return entity;
    }
    private <T, U> void setProperty(T entity, String propertyName, U value) {
        try {
            // Convert the property name to upper camel case to match method naming convention
            String methodName = "set" + Character.toUpperCase(propertyName.charAt(0)) + propertyName.substring(1);

            Method method = entity.getClass().getMethod(methodName, value.getClass());

            if (method != null) {
                method.invoke(entity, value);
            }
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    private ModelGroupType modelGroupProcess(Row row, RowMetadata metadata) {
        return modelGroupTypeMapper.apply(row, "modelGroup");
    }

    private Metric metricProcess(Row row, RowMetadata metadata) {
        return metricMapper.apply(row, "metric");
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

    private Condition createConditions(FilterDTO filterDTO, List<SearchDTO> searchParams) {

        ModelPredicatesCreator modelPredicatesCreator = new ModelPredicatesCreator(searchParams, entityTable);
        Condition combinedConditions = modelPredicatesCreator.create();

        if (filterDTO.getMlTask() != null) {
            Comparison whereMlTaskClause = Conditions.isEqual(mlTaskTable.column("name"), Conditions.just(StringUtils.wrap(filterDTO.getMlTask(), "'")));
            combinedConditions = combinedConditions.and(whereMlTaskClause);
        }

        if (filterDTO.getModelType() != null) {
            Comparison whereTypeClause = Conditions.isEqual(typeTable.column("name"), Conditions.just(StringUtils.wrap(filterDTO.getModelType(), "'")));
            combinedConditions = combinedConditions.and(whereTypeClause);
        }

        if (filterDTO.getStructure() != null) {
            Comparison whereStructureClause = Conditions.isEqual(structureTable.column("name"), Conditions.just(StringUtils.wrap(filterDTO.getStructure(), "'")));
            combinedConditions = combinedConditions.and(whereStructureClause);
        }

        if (filterDTO.isEnabled() != null) {
            Comparison whereEnabledClause = Conditions.isEqual(entityTable.column("enabled"), Conditions.just(String.valueOf(filterDTO.isEnabled())));
            combinedConditions = combinedConditions.and(whereEnabledClause);
        }
        return combinedConditions;
    }
}
