package ai.turintech.catalog.service.mapper;

import ai.turintech.catalog.domain.ParameterType;
import ai.turintech.catalog.service.dto.ParameterTypeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ParameterType} and its DTO {@link ParameterTypeDTO}.
 */
@Mapper(componentModel = "spring")
public interface ParameterTypeMapper extends EntityMapper<ParameterTypeDTO, ParameterType> {}
