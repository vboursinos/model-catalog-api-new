package ai.turintech.modelcatalog.dtoentitymapper;

import ai.turintech.modelcatalog.dto.BooleanParameterDTO;
import ai.turintech.modelcatalog.dto.ParameterTypeDefinitionDTO;
import ai.turintech.modelcatalog.entity.BooleanParameter;
import ai.turintech.modelcatalog.entity.ParameterTypeDefinition;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link BooleanParameter} and its DTO {@link BooleanParameterDTO}.
 */
@Mapper(componentModel = "spring")
public interface BooleanParameterMapper extends EntityMapper<BooleanParameterDTO, BooleanParameter> {

    BooleanParameterDTO toDto(BooleanParameter s);

    @Named("parameterTypeDefinitionId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ParameterTypeDefinitionDTO toDtoParameterTypeDefinitionId(ParameterTypeDefinition parameterTypeDefinition);
}