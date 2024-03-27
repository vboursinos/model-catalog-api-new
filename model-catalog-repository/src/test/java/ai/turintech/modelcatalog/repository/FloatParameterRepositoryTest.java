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
public class FloatParameterRepositoryTest {
  @Autowired private FloatParameterRepository floatParameterRepository;

  private static final String FLOAT_PARAMETER_ID = "323e4567-e89b-12d3-a456-426614174007";

  @Test
  void testFindAllFloatParameterRepository() {
    List<FloatParameter> floatParameters = floatParameterRepository.findAll();
    Assertions.assertEquals(2, floatParameters.size());
  }

  @Test
  void testFindByIdFloatParameterRepository() {
    FloatParameter floatParameter =
        floatParameterRepository
            .findById(UUID.fromString(FLOAT_PARAMETER_ID))
            .orElseThrow(() -> new NoSuchElementException("Float parameter not found"));
    Assertions.assertEquals(
        10.1,
        floatParameter.getDefaultValue(),
        0.001); // Provide delta for floating-point comparison
  }
}
