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
public class CategoricalParameterRepositoryTest {
  @Autowired private CategoricalParameterRepository categoricalParameterRepository;

  private static final String CATEGORICAL_PARAMETER_ID = "323e4567-e89b-12d3-a456-426614174003";

  @Test
  void testFindAllCategoricalParameterRepository() {
    List<CategoricalParameter> categoricalParameters = categoricalParameterRepository.findAll();
    Assertions.assertEquals(2, categoricalParameters.size());
  }

  @Test
  void testFindByIdCategoricalParameterRepository() {
    CategoricalParameter categoricalParameter =
        categoricalParameterRepository
            .findById(UUID.fromString(CATEGORICAL_PARAMETER_ID))
            .orElseThrow(() -> new NoSuchElementException("Categorical parameter not found"));
    Assertions.assertEquals("value1", categoricalParameter.getDefaultValue());
  }
}
