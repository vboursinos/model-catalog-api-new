package ai.turintech.modelcatalog.dtoentitymapper.utils;

import ai.turintech.modelcatalog.dto.ParameterTypeDefinitionDTO;
import ai.turintech.modelcatalog.entity.Parameter;
import ai.turintech.modelcatalog.entity.ParameterTypeDefinition;
import org.mapstruct.MappingTarget;

public class ParameterTypeDefinitionMapperUtils<
    ENTITY extends ParameterTypeDefinition, DTO extends ParameterTypeDefinitionDTO> {
  public void addParameter(@MappingTarget ENTITY target, DTO source) {
    Parameter parameter = new Parameter();
    parameter.setId(source.getParameterId());
    target.setParameter(parameter);
  }
}
