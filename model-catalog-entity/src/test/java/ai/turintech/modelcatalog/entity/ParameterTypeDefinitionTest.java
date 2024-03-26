package ai.turintech.modelcatalog.entity;

import java.util.UUID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ParameterTypeDefinitionTest {

  private final ParameterTypeDefinition parameterTypeDefinition = new ParameterTypeDefinition();

  @Test
  public void testOrdering() {
    Integer ordering = 1;
    parameterTypeDefinition.setOrdering(ordering);
    Assertions.assertEquals(ordering, parameterTypeDefinition.getOrdering());
  }

  @Test
  public void testDistribution() {
    ParameterDistributionType distribution = new ParameterDistributionType();
    parameterTypeDefinition.setDistribution(distribution);
    Assertions.assertEquals(distribution, parameterTypeDefinition.getDistribution());
  }

  @Test
  public void testParameter() {
    Parameter parameter = new Parameter();
    parameterTypeDefinition.setParameter(parameter);
    Assertions.assertEquals(parameter, parameterTypeDefinition.getParameter());
  }

  @Test
  public void testType() {
    ParameterType type = new ParameterType();
    parameterTypeDefinition.setType(type);
    Assertions.assertEquals(type, parameterTypeDefinition.getType());
  }

  @Test
  public void testEqualsAndHashcode() {
    Assertions.assertDoesNotThrow(
        () -> {
          ParameterTypeDefinition parameterTypeDefinition1 = new ParameterTypeDefinition();
          ParameterTypeDefinition parameterTypeDefinition2 = new ParameterTypeDefinition();
          UUID id = UUID.randomUUID();
          parameterTypeDefinition1.setId(id);
          parameterTypeDefinition2.setId(id);

          Assertions.assertEquals(parameterTypeDefinition1, parameterTypeDefinition2);
          Assertions.assertEquals(
              parameterTypeDefinition1.hashCode(), parameterTypeDefinition2.hashCode());
        });
  }
}
