package ai.turintech.catalog.service.mapper;

import ai.turintech.catalog.domain.IntegerParameter;
import ai.turintech.catalog.domain.IntegerParameterValue;
import ai.turintech.catalog.service.dto.IntegerParameterDTO;
import ai.turintech.catalog.service.dto.IntegerParameterValueDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link IntegerParameterValue} and its DTO {@link IntegerParameterValueDTO}.
 */
@Mapper(componentModel = "spring")
public interface IntegerParameterValueMapper extends EntityMapper<IntegerParameterValueDTO, IntegerParameterValue> {

    IntegerParameterValueDTO toDto(IntegerParameterValue s);

    @Named("integerParameterId")
    @BeanMapping(ignoreByDefault = true)
    IntegerParameterDTO toDtoIntegerParameterId(IntegerParameter integerParameter);
}
