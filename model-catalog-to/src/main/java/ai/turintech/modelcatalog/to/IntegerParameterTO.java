package ai.turintech.modelcatalog.to;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@SuppressWarnings("common-java:DuplicatedBlocks")
public class IntegerParameterTO extends ParameterTypeDefinitionTO {

  private static final long serialVersionUID = -1708744270555813607L;
  private Integer defaultValue;

  private List<IntegerParameterValueTO> integerParameterValues = new ArrayList<>();

  public Integer getDefaultValue() {
    return defaultValue;
  }

  public void setDefaultValue(Integer defaultValue) {
    this.defaultValue = defaultValue;
  }

  public List<IntegerParameterValueTO> getIntegerParameterValues() {
    return integerParameterValues;
  }

  public void setIntegerParameterValues(List<IntegerParameterValueTO> integerParameterValues) {
    this.integerParameterValues = integerParameterValues;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof IntegerParameterTO)) {
      return false;
    }

    IntegerParameterTO integerParameterDTO = (IntegerParameterTO) o;
    if (this.getId() == null) {
      return false;
    }
    return Objects.equals(this.getId(), integerParameterDTO.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.getId());
  }

  // prettier-ignore
  @Override
  public String toString() {
    return "IntegerParameterDTO{"
        + "parameterTypeDefinitionId="
        + getId()
        + ", defaultValue="
        + getDefaultValue()
        + "}";
  }
}
