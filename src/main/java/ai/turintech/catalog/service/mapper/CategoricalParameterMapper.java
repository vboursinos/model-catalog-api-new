package ai.turintech.catalog.service.mapper;

import ai.turintech.catalog.domain.CategoricalParameter;
import ai.turintech.catalog.domain.ParameterTypeDefinition;
import ai.turintech.catalog.service.dto.CategoricalParameterDTO;
import ai.turintech.catalog.service.dto.ParameterTypeDefinitionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link CategoricalParameter} and its DTO {@link CategoricalParameterDTO}.
 */
@Mapper(componentModel = "spring")
public interface CategoricalParameterMapper extends EntityMapper<CategoricalParameterDTO, CategoricalParameter> {
    @Mapping(target = "parameterTypeDefinition", source = "parameterTypeDefinition", qualifiedByName = "parameterTypeDefinitionId")
    CategoricalParameterDTO toDto(CategoricalParameter s);

    @Named("parameterTypeDefinitionId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ParameterTypeDefinitionDTO toDtoParameterTypeDefinitionId(ParameterTypeDefinition parameterTypeDefinition);
}
