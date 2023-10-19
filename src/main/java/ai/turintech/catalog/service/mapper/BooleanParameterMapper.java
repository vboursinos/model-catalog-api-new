package ai.turintech.catalog.service.mapper;

import ai.turintech.catalog.domain.BooleanParameter;
import ai.turintech.catalog.domain.ParameterTypeDefinition;
import ai.turintech.catalog.service.dto.BooleanParameterDTO;
import ai.turintech.catalog.service.dto.ParameterTypeDefinitionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link BooleanParameter} and its DTO {@link BooleanParameterDTO}.
 */
@Mapper(componentModel = "spring")
public interface BooleanParameterMapper extends EntityMapper<BooleanParameterDTO, BooleanParameter> {
    @Mapping(target = "parameterTypeDefinition", source = "parameterTypeDefinition", qualifiedByName = "parameterTypeDefinitionId")
    BooleanParameterDTO toDto(BooleanParameter s);

    @Named("parameterTypeDefinitionId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ParameterTypeDefinitionDTO toDtoParameterTypeDefinitionId(ParameterTypeDefinition parameterTypeDefinition);
}
