package ai.turintech.modelcatalog.repository;

import ai.turintech.modelcatalog.entity.*;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith({SpringExtension.class, MockitoExtension.class})
@ContextConfiguration(classes = TestRepositoryConfig.class)
public class IntegerParameterValueRepositoryTest {
  @Autowired private IntegerParameterValueRepository integerParameterValueRepository;

  private static final String INTEGER_PARAMETER_VALUE_ID = "423e4567-e89b-12d3-a456-426614174004";
  private static final String NEW_INTEGER_PARAMETER_VALUE_ID =
      "423e4567-e89b-12d3-a456-426614174003";
  private static final String INTEGER_PARAMETER_ID = "323e4567-e89b-12d3-a456-426614174006";
  private static final String PARAMETER_TYPE_DEFINITION_ID = "323e4567-e89b-12d3-a456-426614174006";

  private IntegerParameterValue getIntegerParameterValue() {
    ParameterTypeDefinition parameterTypeDefinition = new ParameterTypeDefinition();
    parameterTypeDefinition.setId(UUID.fromString(PARAMETER_TYPE_DEFINITION_ID));
    parameterTypeDefinition.setOrdering(10);

    IntegerParameter integerParameter = new IntegerParameter();
    integerParameter.setId(UUID.fromString(INTEGER_PARAMETER_ID));
    integerParameter.setDefaultValue(1);

    IntegerParameterValue integerParameterValue = new IntegerParameterValue();
    integerParameterValue.setIntegerParameter(integerParameter);
    integerParameterValue.setLower(1);
    integerParameterValue.setUpper(10);
    return integerParameterValue;
  }

  private IntegerParameterValue getUpdatedIntegerParameterValue() {
    ParameterTypeDefinition parameterTypeDefinition = new ParameterTypeDefinition();
    parameterTypeDefinition.setId(UUID.fromString(PARAMETER_TYPE_DEFINITION_ID));
    parameterTypeDefinition.setOrdering(10);

    IntegerParameter integerParameter = new IntegerParameter();
    integerParameter.setId(UUID.fromString(INTEGER_PARAMETER_ID));
    integerParameter.setDefaultValue(1);

    IntegerParameterValue integerParameterValue = new IntegerParameterValue();
    integerParameterValue.setId(UUID.fromString(INTEGER_PARAMETER_VALUE_ID));
    integerParameterValue.setIntegerParameter(integerParameter);
    integerParameterValue.setLower(1);
    integerParameterValue.setUpper(10);
    return integerParameterValue;
  }

  @Test
  void testFindAllIntegerParameterValueRepository() {
    List<IntegerParameterValue> integerParameterValues = integerParameterValueRepository.findAll();
    Assertions.assertEquals(4, integerParameterValues.size());
  }

  @Test
  void testFindByIdIntegerParameterValueRepository() {
    IntegerParameterValue integerParameterValue =
        integerParameterValueRepository
            .findById(UUID.fromString(INTEGER_PARAMETER_VALUE_ID))
            .orElseThrow(() -> new NoSuchElementException("Integer parameter value not found"));
    Assertions.assertEquals(25, integerParameterValue.getLower());
  }

  @Test
  void testSaveIntegerParameterValueRepository() {
    IntegerParameterValue savedIntegerParameterValue =
        integerParameterValueRepository.save(getIntegerParameterValue());
    Assertions.assertEquals(
        getIntegerParameterValue().getLower(), savedIntegerParameterValue.getLower());
    integerParameterValueRepository.delete(savedIntegerParameterValue);
  }

  @Test
  void testUpdateIntegerParameterValueRepository() {
    IntegerParameterValue updatedIntegerParameterValue =
        integerParameterValueRepository.save(getUpdatedIntegerParameterValue());
    Assertions.assertEquals(
        getIntegerParameterValue().getLower(), updatedIntegerParameterValue.getLower());
  }
}
