package ai.turintech.catalog.service.mapper;

import ai.turintech.catalog.domain.ModelGroupType;
import ai.turintech.catalog.service.dto.ModelGroupTypeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ModelGroupType} and its DTO {@link ModelGroupTypeDTO}.
 */
@Mapper(componentModel = "spring")
public interface ModelGroupTypeMapper extends EntityMapper<ModelGroupTypeDTO, ModelGroupType> {}
