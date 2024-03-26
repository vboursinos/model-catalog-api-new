package ai.turintech.modelcatalog.dtoentitymapper;

import ai.turintech.components.mapper.api.MapperInterface;
import ai.turintech.modelcatalog.dto.CategoricalParameterDTO;
import ai.turintech.modelcatalog.dtoentitymapper.utils.ParameterTypeDefinitionMapperUtils;
import ai.turintech.modelcatalog.entity.CategoricalParameter;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

/**
 * Mapper for the entity {@link CategoricalParameter} and its DTO {@link CategoricalParameterDTO}.
 */
@Mapper(componentModel = "spring")
public interface CategoricalParameterMapper
    extends MapperInterface<CategoricalParameterDTO, CategoricalParameter> {

  @AfterMapping
  default void addParameter(
      @MappingTarget CategoricalParameter target, CategoricalParameterDTO source) {
    ParameterTypeDefinitionMapperUtils<CategoricalParameter, CategoricalParameterDTO> mapperUtils =
        new ParameterTypeDefinitionMapperUtils<CategoricalParameter, CategoricalParameterDTO>();
    mapperUtils.addParameter(target, source);
  }
}
