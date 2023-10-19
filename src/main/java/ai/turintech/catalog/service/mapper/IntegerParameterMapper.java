package ai.turintech.catalog.service.mapper;

import ai.turintech.catalog.domain.IntegerParameter;
import ai.turintech.catalog.domain.ParameterTypeDefinition;
import ai.turintech.catalog.service.dto.IntegerParameterDTO;
import ai.turintech.catalog.service.dto.ParameterTypeDefinitionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link IntegerParameter} and its DTO {@link IntegerParameterDTO}.
 */
@Mapper(componentModel = "spring")
public interface IntegerParameterMapper extends EntityMapper<IntegerParameterDTO, IntegerParameter> {
    @Mapping(target = "parameterTypeDefinition", source = "parameterTypeDefinition", qualifiedByName = "parameterTypeDefinitionId")
    IntegerParameterDTO toDto(IntegerParameter s);

    @Named("parameterTypeDefinitionId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ParameterTypeDefinitionDTO toDtoParameterTypeDefinitionId(ParameterTypeDefinition parameterTypeDefinition);
}
