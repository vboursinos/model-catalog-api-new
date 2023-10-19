package ai.turintech.catalog.service.mapper;

import ai.turintech.catalog.domain.MlTaskType;
import ai.turintech.catalog.service.dto.MlTaskTypeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link MlTaskType} and its DTO {@link MlTaskTypeDTO}.
 */
@Mapper(componentModel = "spring")
public interface MlTaskTypeMapper extends EntityMapper<MlTaskTypeDTO, MlTaskType> {}
