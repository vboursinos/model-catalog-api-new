package ai.turintech.catalog.service.mapper;

import ai.turintech.catalog.domain.FloatParameter;
import ai.turintech.catalog.domain.ParameterTypeDefinition;
import ai.turintech.catalog.service.dto.FloatParameterDTO;
import ai.turintech.catalog.service.dto.ParameterTypeDefinitionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link FloatParameter} and its DTO {@link FloatParameterDTO}.
 */
@Mapper(componentModel = "spring")
public interface FloatParameterMapper extends EntityMapper<FloatParameterDTO, FloatParameter> {
    @Mapping(target = "parameterTypeDefinition", source = "parameterTypeDefinition", qualifiedByName = "parameterTypeDefinitionId")
    FloatParameterDTO toDto(FloatParameter s);

    @Named("parameterTypeDefinitionId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ParameterTypeDefinitionDTO toDtoParameterTypeDefinitionId(ParameterTypeDefinition parameterTypeDefinition);
}
