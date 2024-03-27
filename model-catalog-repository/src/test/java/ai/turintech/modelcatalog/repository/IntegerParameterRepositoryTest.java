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
public class IntegerParameterRepositoryTest {
  @Autowired private IntegerParameterRepository integerParameterRepository;

  private static final String INTEGER_PARAMETER_ID = "323e4567-e89b-12d3-a456-426614174005";

  @Test
  void testFindAllIntegerParameterRepository() {
    List<IntegerParameter> integerParameters = integerParameterRepository.findAll();
    Assertions.assertEquals(2, integerParameters.size());
  }

  @Test
  void testFindByIdIntegerParameterRepository() {
    IntegerParameter integerParameter =
        integerParameterRepository
            .findById(UUID.fromString(INTEGER_PARAMETER_ID))
            .orElseThrow(() -> new NoSuchElementException("Integer parameter not found"));
    Assertions.assertEquals(10, integerParameter.getDefaultValue());
  }
}
