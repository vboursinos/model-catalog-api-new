package ai.turintech.modelcatalog.dtoentitymapper;

import ai.turintech.components.mapper.api.MapperInterface;
import ai.turintech.modelcatalog.dto.FloatParameterDTO;
import ai.turintech.modelcatalog.dtoentitymapper.utils.ParameterTypeDefinitionMapperUtils;
import ai.turintech.modelcatalog.entity.FloatParameter;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

/** Mapper for the entity {@link FloatParameter} and its DTO {@link FloatParameterDTO}. */
@Mapper(componentModel = "spring")
public interface FloatParameterMapper extends MapperInterface<FloatParameterDTO, FloatParameter> {

  @AfterMapping
  default void addParameter(@MappingTarget FloatParameter target, FloatParameterDTO source) {
    ParameterTypeDefinitionMapperUtils<FloatParameter, FloatParameterDTO> mapperUtils =
        new ParameterTypeDefinitionMapperUtils<FloatParameter, FloatParameterDTO>();
    mapperUtils.addParameter(target, source);
  }
}
