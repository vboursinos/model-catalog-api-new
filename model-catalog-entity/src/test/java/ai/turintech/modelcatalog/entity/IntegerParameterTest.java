package ai.turintech.modelcatalog.entity;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

public class IntegerParameterTest {

  private final IntegerParameter integerParameterUnderTest = new IntegerParameter();

  @Test
  public void testDefaultValue() {
    Integer expectedDefaultValue = 10;
    integerParameterUnderTest.defaultValue(expectedDefaultValue);

    Integer actualDefaultValue = integerParameterUnderTest.getDefaultValue();

    assertNotNull(actualDefaultValue, "Expected default value to be not null");
    assertEquals(
        expectedDefaultValue,
        actualDefaultValue,
        "Expected and actual default values should be the same");
  }

  @Test
  public void testSetAndGetIntegerParameterValues() {
    Set<IntegerParameterValue> integerParameterValueSet = new HashSet<>();
    IntegerParameterValue integerValue = new IntegerParameterValue();
    integerParameterValueSet.add(integerValue);

    integerParameterUnderTest.setIntegerParameterValues(integerParameterValueSet);

    Set<IntegerParameterValue> actualValue = integerParameterUnderTest.getIntegerParameterValues();

    assertNotNull(actualValue, "Expected actual value to not be null");
    assertEquals(
        integerParameterValueSet, actualValue, "Expected and actual values should be equal");
  }

  @Test
  public void testAddIntegerParameterValue() {
    IntegerParameterValue integerValue = new IntegerParameterValue();

    integerParameterUnderTest.addIntegerParameterValue(integerValue);

    assertTrue(
        integerParameterUnderTest.getIntegerParameterValues().contains(integerValue),
        "Added value should be present");
  }

  @Test
  public void testRemoveIntegerParameterValue() {
    IntegerParameterValue integerValue = new IntegerParameterValue();
    integerParameterUnderTest.addIntegerParameterValue(integerValue);
    integerParameterUnderTest.removeIntegerParameterValue(integerValue);

    assertFalse(
        integerParameterUnderTest.getIntegerParameterValues().contains(integerValue),
        "Removed value should not be present");
  }

  @Test
  public void testToString() {
    Integer defaultVal = 10;
    integerParameterUnderTest.defaultValue(defaultVal);

    String expectedString =
        "IntegerParameter{" + "defaultValue=" + defaultVal + ", integerParameterValues=[]" + '}';

    assertEquals(
        expectedString,
        integerParameterUnderTest.toString(),
        "Expected and actual toString should be same");
  }
}
