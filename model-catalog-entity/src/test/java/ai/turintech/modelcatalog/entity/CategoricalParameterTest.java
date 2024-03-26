package ai.turintech.modelcatalog.entity;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CategoricalParameterTest {

  private final CategoricalParameter categoricalParameter = new CategoricalParameter();

  @Test
  public void testDefaultValue() {
    String defaultValue = "value";
    categoricalParameter.setDefaultValue(defaultValue);
    Assertions.assertEquals(defaultValue, categoricalParameter.getDefaultValue());
  }

  @Test
  public void testCategoricalParameterValues() {
    Set<CategoricalParameterValue> categoricalParameterValues = new HashSet<>();
    CategoricalParameterValue categoricalParameterValue = new CategoricalParameterValue();
    categoricalParameterValues.add(categoricalParameterValue);

    categoricalParameter.setCategoricalParameterValues(categoricalParameterValues);
    Assertions.assertEquals(
        categoricalParameterValues, categoricalParameter.getCategoricalParameterValues());
  }

  @Test
  public void testToString() {
    String expectedStringValue =
        "CategoricalParameter{defaultValue='null', categoricalParameterValues=[]}";
    CategoricalParameter categoricalParameter = new CategoricalParameter();
    Assertions.assertEquals(expectedStringValue, categoricalParameter.toString());
  }
}
