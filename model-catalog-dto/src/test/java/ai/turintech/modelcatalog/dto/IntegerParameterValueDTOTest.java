package ai.turintech.modelcatalog.dto;

import static org.junit.jupiter.api.Assertions.*;

import java.util.UUID;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class IntegerParameterValueDTOTest {

  private IntegerParameterValueDTO integerParameterValueDTO;

  @BeforeAll
  public void setUp() {
    integerParameterValueDTO = new IntegerParameterValueDTO();
    integerParameterValueDTO.setId(UUID.randomUUID());
    integerParameterValueDTO.setLower(10);
    integerParameterValueDTO.setUpper(20);
  }

  @Test
  public void testEquals() {
    IntegerParameterValueDTO sameIdIntegerParameterValueDTO = new IntegerParameterValueDTO();
    sameIdIntegerParameterValueDTO.setId(integerParameterValueDTO.getId());

    IntegerParameterValueDTO differentIdIntegerParameterValueDTO = new IntegerParameterValueDTO();
    differentIdIntegerParameterValueDTO.setId(UUID.randomUUID());

    assertEquals(integerParameterValueDTO, sameIdIntegerParameterValueDTO);
    assertNotEquals(integerParameterValueDTO, differentIdIntegerParameterValueDTO);
    assertNotEquals(integerParameterValueDTO, null);
  }

  @Test
  public void testHashCode() {
    IntegerParameterValueDTO sameIdIntegerParameterValueDTO = new IntegerParameterValueDTO();
    sameIdIntegerParameterValueDTO.setId(integerParameterValueDTO.getId());

    IntegerParameterValueDTO differentIdIntegerParameterValueDTO = new IntegerParameterValueDTO();
    differentIdIntegerParameterValueDTO.setId(UUID.randomUUID());

    assertEquals(integerParameterValueDTO.hashCode(), sameIdIntegerParameterValueDTO.hashCode());
    assertNotEquals(
        integerParameterValueDTO.hashCode(), differentIdIntegerParameterValueDTO.hashCode());
  }

  @Test
  public void testToString() {
    String expectedToString =
        "IntegerParameterValueDTO{"
            + "id="
            + integerParameterValueDTO.getId()
            + ", lower="
            + integerParameterValueDTO.getLower()
            + ", upper="
            + integerParameterValueDTO.getUpper()
            + "}";
    assertEquals(expectedToString, integerParameterValueDTO.toString());
  }

  @Test
  public void testGetSetMethods() {
    assertEquals(10, integerParameterValueDTO.getLower());
    assertEquals(20, integerParameterValueDTO.getUpper());

    integerParameterValueDTO.setLower(30);
    integerParameterValueDTO.setUpper(40);

    assertEquals(30, integerParameterValueDTO.getLower());
    assertEquals(40, integerParameterValueDTO.getUpper());
  }

  @Test
  public void testEqualsDifferentClass() {
    Object differentObject = new Object();

    assertFalse(integerParameterValueDTO.equals(differentObject));
  }
}
