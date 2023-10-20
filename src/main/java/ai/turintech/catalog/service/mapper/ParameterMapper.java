package ai.turintech.catalog.service.mapper;

import ai.turintech.catalog.domain.Model;
import ai.turintech.catalog.domain.Parameter;
import ai.turintech.catalog.service.dto.ModelDTO;
import ai.turintech.catalog.service.dto.ParameterDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Parameter} and its DTO {@link ParameterDTO}.
 */
@Mapper(componentModel = "spring")
public interface ParameterMapper extends EntityMapper<ParameterDTO, Parameter> {
    @Mapping(target = "modelId", source = "modelId")
    ParameterDTO toDto(Parameter s);

    @Named("modelId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ModelDTO toDtoModelId(Model model);
}
