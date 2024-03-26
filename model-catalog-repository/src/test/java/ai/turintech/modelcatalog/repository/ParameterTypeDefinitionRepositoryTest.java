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
public class ParameterTypeDefinitionRepositoryTest {
  @Autowired private ParameterTypeDefinitionRepository parameterTypeDefinitionRepository;

  private static final String PARAMETER_TYPE_DEFINITION_ID = "323e4567-e89b-12d3-a456-426614174001";
  private static final String PARAMETER_ID = "523e4567-e89b-12d3-a456-426614174001";
  private static final String PARAMETER_DISTRIBUTION_TYPE_ID =
      "1b6f7a9a-4a2d-4e9a-8f2a-6d6bb9c66d27";
  private static final String PARAMETER_TYPE_ID = "1b6f7a9a-4a2d-4e9a-8f2a-6d6bb9c66d27";

  private ParameterTypeDefinition getParameterTypeDefinition() {
    Parameter parameter = new Parameter();
    parameter.setId(UUID.fromString(PARAMETER_ID));
    parameter.setName("test_parameter");
    parameter.setDescription("test_description");
    parameter.setEnabled(true);
    parameter.setFixedValue(true);
    parameter.setLabel("test_label");
    parameter.setOrdering(1);

    ParameterDistributionType parameterDistributionType = new ParameterDistributionType();
    parameterDistributionType.setId(UUID.fromString(PARAMETER_DISTRIBUTION_TYPE_ID));
    parameterDistributionType.setName("parameterdistributiontype1");

    ParameterType parameterType = new ParameterType();
    parameterType.setId(UUID.fromString(PARAMETER_TYPE_ID));
    parameterType.setName("parametertype1");

    ParameterTypeDefinition parameterTypeDefinition = new ParameterTypeDefinition();
    parameterTypeDefinition.setParameter(parameter);
    parameterTypeDefinition.setDistribution(parameterDistributionType);
    parameterTypeDefinition.setType(parameterType);
    parameterTypeDefinition.setOrdering(10);

    return parameterTypeDefinition;
  }

  private ParameterTypeDefinition getUpdatedParameterTypeDefinition() {
    Parameter parameter = new Parameter();
    parameter.setId(UUID.fromString(PARAMETER_ID));
    parameter.setName("test_parameter");
    parameter.setDescription("test_description");
    parameter.setEnabled(true);
    parameter.setFixedValue(true);
    parameter.setLabel("test_label");
    parameter.setOrdering(1);

    ParameterDistributionType parameterDistributionType = new ParameterDistributionType();
    parameterDistributionType.setId(UUID.fromString(PARAMETER_DISTRIBUTION_TYPE_ID));
    parameterDistributionType.setName("parameterdistributiontype1");

    ParameterType parameterType = new ParameterType();
    parameterType.setId(UUID.fromString(PARAMETER_TYPE_ID));
    parameterType.setName("parametertype1");

    ParameterTypeDefinition parameterTypeDefinition = new ParameterTypeDefinition();
    parameterTypeDefinition.setId(UUID.fromString(PARAMETER_TYPE_DEFINITION_ID));
    parameterTypeDefinition.setParameter(parameter);
    parameterTypeDefinition.setDistribution(parameterDistributionType);
    parameterTypeDefinition.setType(parameterType);
    parameterTypeDefinition.setOrdering(12);

    return parameterTypeDefinition;
  }

  @Test
  void testFindAllParameterTypeDefinitionRepository() {
    List<ParameterTypeDefinition> parameterTypeDefinitions =
        parameterTypeDefinitionRepository.findAll();
    Assertions.assertEquals(4, parameterTypeDefinitions.size());
  }

  @Test
  void testFindByIdParameterTypeDefinitionRepository() {
    ParameterTypeDefinition parameterTypeDefinition =
        parameterTypeDefinitionRepository
            .findById(UUID.fromString(PARAMETER_TYPE_DEFINITION_ID))
            .orElseThrow(() -> new NoSuchElementException("Parameter type definition not found"));
    Assertions.assertEquals(1, parameterTypeDefinition.getOrdering());
  }

  @Test
  void testSaveParameterTypeDefinitionRepository() {
    ParameterTypeDefinition savedParameterTypeDefinition =
        parameterTypeDefinitionRepository.save(getParameterTypeDefinition());
    Assertions.assertEquals(
        getParameterTypeDefinition().getOrdering(), savedParameterTypeDefinition.getOrdering());
    parameterTypeDefinitionRepository.delete(savedParameterTypeDefinition);
  }

  @Test
  void testUpdateParameterTypeDefinitionRepository() {
    ParameterTypeDefinition parameterTypeDefinition = getParameterTypeDefinition();
    ParameterTypeDefinition savedParameterTypeDefinition =
        parameterTypeDefinitionRepository.save(parameterTypeDefinition);
    savedParameterTypeDefinition.setOrdering(80);
    ParameterTypeDefinition updateParameterTypeDefinition =
        parameterTypeDefinitionRepository.save(savedParameterTypeDefinition);
    Assertions.assertEquals(updateParameterTypeDefinition.getOrdering(), 80);
  }
}
