package ai.turintech.modelcatalog.todtomapper;

import ai.turintech.modelcatalog.to.ParameterTO;
import ai.turintech.modelcatalog.to.ParameterDistributionTypeTO;
import ai.turintech.modelcatalog.dto.ParameterDTO;
import ai.turintech.modelcatalog.dto.ParameterDistributionTypeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ParameterDistributionType} and its DTO {@link ParameterDistributionTypeDTO}.
 */
@Mapper(componentModel = "spring")
public interface ParameterDistributionTypeMapper extends EntityMapper<ParameterDistributionTypeTO, ParameterDistributionTypeDTO> {
    @Mapping(target = "parameter", source = "parameter", qualifiedByName = "parameterId")
    ParameterDistributionTypeDTO toDto(ParameterDistributionTypeTO s);

    @Named("parameterId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ParameterDTO toDtoParameterId(ParameterTO parameter);
}