package ai.turintech.modelcatalog.entity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ParameterTest {

  private final Parameter parameterUnderTest = new Parameter();

  @BeforeEach
  public void setUp() {

    UUID id = UUID.randomUUID();
    parameterUnderTest.setId(id);
    parameterUnderTest.setName("Test");
    parameterUnderTest.setLabel("Test Label");
    parameterUnderTest.setDescription("This is a test parameter");
    parameterUnderTest.setEnabled(true);
    parameterUnderTest.setFixedValue(true);
    parameterUnderTest.setOrdering(1);
  }

  @Test
  public void testGetSetMethods() {
    String resultName = parameterUnderTest.getName();
    assertThat(resultName).isEqualTo("Test");

    String resultLabel = parameterUnderTest.getLabel();
    assertThat(resultLabel).isEqualTo("Test Label");

    String resultDescription = parameterUnderTest.getDescription();
    assertThat(resultDescription).isEqualTo("This is a test parameter");

    Boolean resultEnabled = parameterUnderTest.getEnabled();
    assertThat(resultEnabled).isEqualTo(true);

    Boolean resultFixedValue = parameterUnderTest.getFixedValue();
    assertThat(resultFixedValue).isEqualTo(true);

    Integer resultOrdering = parameterUnderTest.getOrdering();
    assertThat(resultOrdering).isEqualTo(1);
  }

  @Test
  public void testGetSetBooleanParameters() {
    Set<BooleanParameter> booleanParameters = new HashSet<>();
    BooleanParameter booleanParameter = new BooleanParameter();
    booleanParameters.add(booleanParameter);
    parameterUnderTest.setBooleanParameters(booleanParameters);
    assertEquals(booleanParameters, parameterUnderTest.getBooleanParameters());
  }

  @Test
  public void testGetSetCategoricalParameters() {
    Set<CategoricalParameter> categoricalParameters = new HashSet<>();
    CategoricalParameter categoricalParameter = new CategoricalParameter();
    categoricalParameters.add(categoricalParameter);
    parameterUnderTest.setCategoricalParameters(categoricalParameters);
    assertEquals(categoricalParameters, parameterUnderTest.getCategoricalParameters());
  }

  @Test
  public void testGetSetFloatParameters() {
    Set<FloatParameter> floatParameters = new HashSet<>();
    FloatParameter floatParameter = new FloatParameter();
    floatParameters.add(floatParameter);
    parameterUnderTest.setFloatParameters(floatParameters);
    assertEquals(floatParameters, parameterUnderTest.getFloatParameters());
  }

  @Test
  public void testGetSetIntegerParameters() {
    Set<IntegerParameter> integerParameters = new HashSet<>();
    IntegerParameter integerParameter = new IntegerParameter();
    integerParameters.add(integerParameter);
    parameterUnderTest.setIntegerParameters(integerParameters);
    assertEquals(integerParameters, parameterUnderTest.getIntegerParameters());
  }

  @Test
  public void testEquals() {
    Parameter entityToCompare = new Parameter();
    entityToCompare.setId(parameterUnderTest.getId());
    entityToCompare.setName(parameterUnderTest.getName());

    assertThat(parameterUnderTest).isEqualTo(entityToCompare);
  }

  @Test
  public void testHashCode() {
    int result = parameterUnderTest.hashCode();
    assertThat(result).isEqualTo(Parameter.class.hashCode());
  }

  @Test
  public void testToString() {
    String result = parameterUnderTest.toString();
    assertThat(result)
        .isEqualTo(
            "Parameter{name='"
                + parameterUnderTest.getName()
                + "', label='"
                + parameterUnderTest.getLabel()
                + "', description='"
                + parameterUnderTest.getDescription()
                + "', enabled="
                + parameterUnderTest.getEnabled()
                + ", fixedValue="
                + parameterUnderTest.getFixedValue()
                + ", ordering="
                + parameterUnderTest.getOrdering()
                + ", booleanParameters="
                + parameterUnderTest.getBooleanParameters()
                + ", categoricalParameters="
                + parameterUnderTest.getCategoricalParameters()
                + ", floatParameters="
                + parameterUnderTest.getFloatParameters()
                + ", integerParameters="
                + parameterUnderTest.getIntegerParameters()
                + ", model="
                + parameterUnderTest.getModel()
                + "}");
  }
}
