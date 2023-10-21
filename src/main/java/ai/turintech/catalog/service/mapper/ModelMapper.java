package ai.turintech.catalog.service.mapper;

import ai.turintech.catalog.domain.Metric;
import ai.turintech.catalog.domain.MlTaskType;
import ai.turintech.catalog.domain.Model;
import ai.turintech.catalog.domain.ModelEnsembleType;
import ai.turintech.catalog.domain.ModelFamilyType;
import ai.turintech.catalog.domain.ModelGroupType;
import ai.turintech.catalog.domain.ModelStructureType;
import ai.turintech.catalog.domain.ModelType;
import ai.turintech.catalog.service.dto.MetricDTO;
import ai.turintech.catalog.service.dto.MlTaskTypeDTO;
import ai.turintech.catalog.service.dto.ModelDTO;
import ai.turintech.catalog.service.dto.ModelEnsembleTypeDTO;
import ai.turintech.catalog.service.dto.ModelFamilyTypeDTO;
import ai.turintech.catalog.service.dto.ModelGroupTypeDTO;
import ai.turintech.catalog.service.dto.ModelStructureTypeDTO;
import ai.turintech.catalog.service.dto.ModelTypeDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Model} and its DTO {@link ModelDTO}.
 */
@Mapper(componentModel = "spring")
public interface ModelMapper extends EntityMapper<ModelDTO, Model> {
    @Mapping(target = "groups", source = "groups")
    @Mapping(target = "incompatibleMetrics", source = "incompatibleMetrics")
    @Mapping(target = "mlTask", source = "mlTask")
    @Mapping(target = "structure", source = "structure")
    @Mapping(target = "type", source = "type")
    @Mapping(target = "familyType", source = "familyType")
    @Mapping(target = "ensembleType", source = "ensembleType")
    @Mapping(target = "parameters", source = "parameters")
    ModelDTO toDto(Model s);

    @Mapping(target = "removeGroups", ignore = true)
    @Mapping(target = "removeIncompatibleMetrics", ignore = true)
    Model toEntity(ModelDTO modelDTO);

    @Named("modelGroupTypeId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ModelGroupTypeDTO toDtoModelGroupTypeId(ModelGroupType modelGroupType);

    @Named("modelGroupTypeIdSet")
    default Set<ModelGroupTypeDTO> toDtoModelGroupTypeIdSet(Set<ModelGroupType> modelGroupType) {
        return modelGroupType.stream().map(this::toDtoModelGroupTypeId).collect(Collectors.toSet());
    }

    @Named("metricId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MetricDTO toDtoMetricId(Metric metric);

    @Named("metricIdSet")
    default Set<MetricDTO> toDtoMetricIdSet(Set<Metric> metric) {
        return metric.stream().map(this::toDtoMetricId).collect(Collectors.toSet());
    }

    @Named("mlTaskTypeId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MlTaskTypeDTO toDtoMlTaskTypeId(MlTaskType mlTaskType);

    @Named("modelStructureTypeId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ModelStructureTypeDTO toDtoModelStructureTypeId(ModelStructureType modelStructureType);

    @Named("modelTypeId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ModelTypeDTO toDtoModelTypeId(ModelType modelType);

    @Named("modelFamilyTypeId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ModelFamilyTypeDTO toDtoModelFamilyTypeId(ModelFamilyType modelFamilyType);

    @Named("modelEnsembleTypeId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ModelEnsembleTypeDTO toDtoModelEnsembleTypeId(ModelEnsembleType modelEnsembleType);
}
