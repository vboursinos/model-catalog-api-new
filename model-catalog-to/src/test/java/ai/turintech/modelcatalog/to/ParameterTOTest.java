package ai.turintech.modelcatalog.to;

import static org.junit.jupiter.api.Assertions.*;

import java.util.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ParameterTOTest {

  private ParameterTO parameterTO;

  @BeforeEach
  public void setUp() {
    parameterTO = new ParameterTO();
    parameterTO.setId(UUID.randomUUID());
    parameterTO.setName("TestParameter");
    parameterTO.setLabel("Test Label");
    parameterTO.setDescription("Test Description");
    parameterTO.setEnabled(true);
    parameterTO.setFixedValue(true);
    parameterTO.setOrdering(1);
    parameterTO.setModelId(UUID.randomUUID());
    parameterTO.setBooleanParameters(Collections.singletonList(new BooleanParameterTO()));
    parameterTO.setCategoricalParameters(Collections.singletonList(new CategoricalParameterTO()));
    parameterTO.setFloatParameters(Collections.singletonList(new FloatParameterTO()));
    parameterTO.setIntegerParameters(Collections.singletonList(new IntegerParameterTO()));
  }

  @Test
  public void testEquals() {
    ParameterTO sameIdParameterTO = new ParameterTO();
    sameIdParameterTO.setId(parameterTO.getId());
    sameIdParameterTO.setName("TestParameter");
    sameIdParameterTO.setLabel("Test Label");
    sameIdParameterTO.setDescription("Test Description");
    sameIdParameterTO.setEnabled(true);
    sameIdParameterTO.setFixedValue(true);
    sameIdParameterTO.setOrdering(1);
    sameIdParameterTO.setModelId(UUID.randomUUID());
    sameIdParameterTO.setBooleanParameters(Collections.singletonList(new BooleanParameterTO()));
    sameIdParameterTO.setCategoricalParameters(
        Collections.singletonList(new CategoricalParameterTO()));
    sameIdParameterTO.setFloatParameters(Collections.singletonList(new FloatParameterTO()));
    sameIdParameterTO.setIntegerParameters(Collections.singletonList(new IntegerParameterTO()));

    ParameterTO differentIdParameterTO = new ParameterTO();
    differentIdParameterTO.setId(UUID.randomUUID());
    differentIdParameterTO.setName("DifferentTestParameter");
    differentIdParameterTO.setLabel("Different Test Label");
    differentIdParameterTO.setDescription("Different Test Description");
    differentIdParameterTO.setEnabled(false);
    differentIdParameterTO.setFixedValue(false);
    differentIdParameterTO.setOrdering(2);
    differentIdParameterTO.setModelId(UUID.randomUUID());
    differentIdParameterTO.setBooleanParameters(
        Collections.singletonList(new BooleanParameterTO()));
    differentIdParameterTO.setCategoricalParameters(
        Collections.singletonList(new CategoricalParameterTO()));
    differentIdParameterTO.setFloatParameters(Collections.singletonList(new FloatParameterTO()));
    differentIdParameterTO.setIntegerParameters(
        Collections.singletonList(new IntegerParameterTO()));
    assertEquals(parameterTO, sameIdParameterTO);
    assertNotEquals(parameterTO, differentIdParameterTO);
    assertNotEquals(parameterTO, null);
  }

  @Test
  public void testHashCode() {
    ParameterTO sameIdParameterTO = new ParameterTO();
    sameIdParameterTO.setId(parameterTO.getId());
    sameIdParameterTO.setName("TestParameter");
    sameIdParameterTO.setLabel("Test Label");
    sameIdParameterTO.setDescription("Test Description");
    sameIdParameterTO.setEnabled(true);
    sameIdParameterTO.setFixedValue(true);
    sameIdParameterTO.setOrdering(1);
    sameIdParameterTO.setModelId(UUID.randomUUID());
    sameIdParameterTO.setBooleanParameters(Collections.singletonList(new BooleanParameterTO()));
    sameIdParameterTO.setCategoricalParameters(
        Collections.singletonList(new CategoricalParameterTO()));
    sameIdParameterTO.setFloatParameters(Collections.singletonList(new FloatParameterTO()));
    sameIdParameterTO.setIntegerParameters(Collections.singletonList(new IntegerParameterTO()));

    ParameterTO differentIdParameterTO = new ParameterTO();
    differentIdParameterTO.setId(UUID.randomUUID());
    differentIdParameterTO.setName("DifferentTestParameter");
    differentIdParameterTO.setLabel("Different Test Label");
    differentIdParameterTO.setDescription("Different Test Description");
    differentIdParameterTO.setEnabled(false);
    differentIdParameterTO.setFixedValue(false);
    differentIdParameterTO.setOrdering(2);
    differentIdParameterTO.setModelId(UUID.randomUUID());
    differentIdParameterTO.setBooleanParameters(
        Collections.singletonList(new BooleanParameterTO()));
    differentIdParameterTO.setCategoricalParameters(
        Collections.singletonList(new CategoricalParameterTO()));
    differentIdParameterTO.setFloatParameters(Collections.singletonList(new FloatParameterTO()));
    differentIdParameterTO.setIntegerParameters(
        Collections.singletonList(new IntegerParameterTO()));

    assertEquals(parameterTO.hashCode(), sameIdParameterTO.hashCode());
    assertNotEquals(parameterTO.hashCode(), differentIdParameterTO.hashCode());
  }

  @Test
  public void testToString() {
    String expectedToString =
        "ParameterTO{name='TestParameter'"
            + ", label='Test Label'"
            + ", description='Test Description'"
            + ", enabled=true"
            + ", fixedValue=true"
            + ", ordering=1"
            + ", modelId="
            + parameterTO.getModelId()
            + ", booleanParameters="
            + parameterTO.getBooleanParameters()
            + ", categoricalParameters="
            + parameterTO.getCategoricalParameters()
            + ", floatParameters="
            + parameterTO.getFloatParameters()
            + ", integerParameters="
            + parameterTO.getIntegerParameters()
            + '}';
    assertEquals(expectedToString, parameterTO.toString());
  }

  @Test
  public void testNullId() {
    ParameterTO nullIdParameterTO = new ParameterTO();
    nullIdParameterTO.setId(null);
    assertNotEquals(parameterTO, nullIdParameterTO);
  }
}
