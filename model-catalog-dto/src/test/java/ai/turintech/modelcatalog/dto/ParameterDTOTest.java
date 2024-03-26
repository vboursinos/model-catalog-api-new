package ai.turintech.modelcatalog.dto;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ParameterDTOTest {

  private ParameterDTO parameterDTO;

  @BeforeAll
  public void setUp() {
    parameterDTO = new ParameterDTO();
    parameterDTO.setId(UUID.randomUUID());
    parameterDTO.setName("TestParameter");
    parameterDTO.setLabel("TestLabel");
    parameterDTO.setDescription("TestDescription");
    parameterDTO.setEnabled(true);
    parameterDTO.setFixedValue(true);
    parameterDTO.setOrdering(1);
    parameterDTO.setModelId(UUID.randomUUID());
  }

  @Test
  public void testEquals() {
    ParameterDTO sameIdParameterDTO = new ParameterDTO();
    sameIdParameterDTO.setId(parameterDTO.getId());

    ParameterDTO differentIdParameterDTO = new ParameterDTO();
    differentIdParameterDTO.setId(UUID.randomUUID());

    assertEquals(parameterDTO, sameIdParameterDTO);
    assertNotEquals(parameterDTO, differentIdParameterDTO);
    assertNotEquals(parameterDTO, null);
  }

  @Test
  public void testHashCode() {
    ParameterDTO sameIdParameterDTO = new ParameterDTO();
    sameIdParameterDTO.setId(parameterDTO.getId());

    ParameterDTO differentIdParameterDTO = new ParameterDTO();
    differentIdParameterDTO.setId(UUID.randomUUID());

    assertEquals(parameterDTO.hashCode(), sameIdParameterDTO.hashCode());
    assertNotEquals(parameterDTO.hashCode(), differentIdParameterDTO.hashCode());
  }

  @Test
  public void testToString() {
    String expectedToString =
        "ParameterDTO{id='"+parameterDTO.getId()
            + "', name='TestParameter', label='TestLabel', description='TestDescription', "
            + "enabled=true, fixedValue=true, ordering=1, modelId="
            + parameterDTO.getModelId()
            + ", booleanParameters=[]"
            + ", categoricalParameters=[]"
            + ", floatParameters=[]"
            + ", integerParameters=[]"
  			+"}";
    assertEquals(expectedToString, parameterDTO.toString());
  }

  @Test
  public void testGetSetMethods() {
    assertEquals("TestParameter", parameterDTO.getName());
    assertEquals("TestLabel", parameterDTO.getLabel());
    assertEquals("TestDescription", parameterDTO.getDescription());
    assertTrue(parameterDTO.getEnabled());
    assertTrue(parameterDTO.getFixedValue());
    assertEquals(1, parameterDTO.getOrdering());
    assertEquals(UUID.class, parameterDTO.getModelId().getClass());

    parameterDTO.setName("NewTestParameter");
    parameterDTO.setLabel("NewTestLabel");
    parameterDTO.setDescription("NewTestDescription");
    parameterDTO.setEnabled(false);
    parameterDTO.setFixedValue(false);
    parameterDTO.setOrdering(2);
    UUID newModelId = UUID.randomUUID();
    parameterDTO.setModelId(newModelId);

    assertEquals("NewTestParameter", parameterDTO.getName());
    assertEquals("NewTestLabel", parameterDTO.getLabel());
    assertEquals("NewTestDescription", parameterDTO.getDescription());
    assertFalse(parameterDTO.getEnabled());
    assertFalse(parameterDTO.getFixedValue());
    assertEquals(2, parameterDTO.getOrdering());
    assertEquals(newModelId, parameterDTO.getModelId());
  }

  @Test
  public void testEqualsDifferentClass() {
    Object differentObject = new Object();

    assertFalse(parameterDTO.equals(differentObject));
  }
}
