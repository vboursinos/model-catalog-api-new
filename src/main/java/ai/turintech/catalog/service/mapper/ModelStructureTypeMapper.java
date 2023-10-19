package ai.turintech.catalog.service.mapper;

import ai.turintech.catalog.domain.ModelStructureType;
import ai.turintech.catalog.service.dto.ModelStructureTypeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ModelStructureType} and its DTO {@link ModelStructureTypeDTO}.
 */
@Mapper(componentModel = "spring")
public interface ModelStructureTypeMapper extends EntityMapper<ModelStructureTypeDTO, ModelStructureType> {}
