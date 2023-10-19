package ai.turintech.catalog.service.mapper;

import ai.turintech.catalog.domain.ParameterDistributionType;
import ai.turintech.catalog.service.dto.ParameterDistributionTypeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ParameterDistributionType} and its DTO {@link ParameterDistributionTypeDTO}.
 */
@Mapper(componentModel = "spring")
public interface ParameterDistributionTypeMapper extends EntityMapper<ParameterDistributionTypeDTO, ParameterDistributionType> {}
