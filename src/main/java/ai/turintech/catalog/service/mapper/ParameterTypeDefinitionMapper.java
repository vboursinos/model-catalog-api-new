package ai.turintech.catalog.service.mapper;

import ai.turintech.catalog.domain.Parameter;
import ai.turintech.catalog.domain.ParameterDistributionType;
import ai.turintech.catalog.domain.ParameterType;
import ai.turintech.catalog.domain.ParameterTypeDefinition;
import ai.turintech.catalog.service.dto.ParameterDTO;
import ai.turintech.catalog.service.dto.ParameterDistributionTypeDTO;
import ai.turintech.catalog.service.dto.ParameterTypeDTO;
import ai.turintech.catalog.service.dto.ParameterTypeDefinitionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ParameterTypeDefinition} and its DTO {@link ParameterTypeDefinitionDTO}.
 */
@Mapper(componentModel = "spring")
public interface ParameterTypeDefinitionMapper extends EntityMapper<ParameterTypeDefinitionDTO, ParameterTypeDefinition> {
    @Mapping(target = "distributionId", source = "distributionId")
    @Mapping(target = "parameterId", source = "parameterId")
    @Mapping(target = "distribution", source = "distribution")
    @Mapping(target = "type", source = "type")
    @Mapping(target = "categoricalParameter", source = "categoricalParameter")
    @Mapping(target = "booleanParameter", source = "booleanParameter")
    @Mapping(target = "floatParameter", source = "floatParameter")
    @Mapping(target = "integerParameter", source = "integerParameter")
    ParameterTypeDefinitionDTO toDto(ParameterTypeDefinition s);

    @Named("parameterDistributionTypeId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ParameterDistributionTypeDTO toDtoParameterDistributionTypeId(ParameterDistributionType parameterDistributionType);

    @Named("parameterId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ParameterDTO toDtoParameterId(Parameter parameter);

    @Named("parameterTypeId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ParameterTypeDTO toDtoParameterTypeId(ParameterType parameterType);
}
