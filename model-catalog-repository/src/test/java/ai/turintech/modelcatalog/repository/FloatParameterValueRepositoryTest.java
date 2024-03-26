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
public class FloatParameterValueRepositoryTest {
  @Autowired private FloatParameterRangeRepository floatParameterRangeRepository;

  private static final String FLOAT_PARAMETER_RANGE_ID = "423e4567-e89b-12d3-a456-426614174004";
  private static final String NEW_FLOAT_PARAMETER_RANGE_ID = "423e4567-e89b-12d3-a456-426614174003";
  private static final String FLOAT_PARAMETER_ID = "323e4567-e89b-12d3-a456-426614174002";
  private static final String PARAMETER_TYPE_DEFINITION_ID = "323e4567-e89b-12d3-a456-426614174001";

  private FloatParameterRange getFloatParameterRange() {
    ParameterTypeDefinition parameterTypeDefinition = new ParameterTypeDefinition();
    parameterTypeDefinition.setId(UUID.fromString(PARAMETER_TYPE_DEFINITION_ID));
    parameterTypeDefinition.setOrdering(10);

    FloatParameter floatParameter = new FloatParameter();
    floatParameter.setId(UUID.fromString(FLOAT_PARAMETER_ID));
    floatParameter.setDefaultValue(1.0);

    FloatParameterRange floatParameterRange = new FloatParameterRange();
    floatParameterRange.setFloatParameter(floatParameter);
    floatParameterRange.setLower(1.0);
    floatParameterRange.setUpper(10.0);
    floatParameterRange.setIsLeftOpen(false);
    floatParameterRange.setIsRightOpen(false);
    return floatParameterRange;
  }

  private FloatParameterRange getUpdatedFloatParameterRange() {
    ParameterTypeDefinition parameterTypeDefinition = new ParameterTypeDefinition();
    parameterTypeDefinition.setId(UUID.fromString(PARAMETER_TYPE_DEFINITION_ID));
    parameterTypeDefinition.setOrdering(10);

    FloatParameter floatParameter = new FloatParameter();
    floatParameter.setId(UUID.fromString(FLOAT_PARAMETER_ID));
    floatParameter.setDefaultValue(1.0);

    FloatParameterRange floatParameterRange = new FloatParameterRange();
    floatParameterRange.setId(UUID.fromString(NEW_FLOAT_PARAMETER_RANGE_ID));
    floatParameterRange.setFloatParameter(floatParameter);
    floatParameterRange.setLower(1.0);
    floatParameterRange.setUpper(10.0);
    floatParameterRange.setIsLeftOpen(false);
    floatParameterRange.setIsRightOpen(false);
    return floatParameterRange;
  }

  @Test
  void testFindAllFloatParameterRangeRepository() {
    List<FloatParameterRange> floatParameterRanges = floatParameterRangeRepository.findAll();
    Assertions.assertEquals(4, floatParameterRanges.size());
  }

  @Test
  void testFindByIdFloatParameterRangeRepository() {
    FloatParameterRange floatParameterRange =
        floatParameterRangeRepository
            .findById(UUID.fromString(FLOAT_PARAMETER_RANGE_ID))
            .orElseThrow(() -> new NoSuchElementException("Float parameter range not found"));
    Assertions.assertEquals(25.3, floatParameterRange.getLower());
  }

  @Test
  void testSaveFloatParameterValueRepository() {
    FloatParameterRange savedFloatParameterRange =
        floatParameterRangeRepository.save(getFloatParameterRange());
    Assertions.assertEquals(
        getFloatParameterRange().getLower(), savedFloatParameterRange.getLower());
    floatParameterRangeRepository.delete(savedFloatParameterRange);
  }

  @Test
  void testUpdateFloatParameterValueRepository() {
    FloatParameterRange updatedFloatParameterRange =
        floatParameterRangeRepository.save(getUpdatedFloatParameterRange());
    Assertions.assertEquals(
        getUpdatedFloatParameterRange().getLower(), updatedFloatParameterRange.getLower());
  }
}
