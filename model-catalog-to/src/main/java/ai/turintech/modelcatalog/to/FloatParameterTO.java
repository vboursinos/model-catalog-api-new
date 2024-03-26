package ai.turintech.modelcatalog.to;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@SuppressWarnings("common-java:DuplicatedBlocks")
public class FloatParameterTO extends ParameterTypeDefinitionTO {

  private static final long serialVersionUID = -6709359691936403897L;
  private Double defaultValue;

  private List<FloatParameterRangeTO> floatParameterRanges = new ArrayList<>();

  public Double getDefaultValue() {
    return defaultValue;
  }

  public void setDefaultValue(Double defaultValue) {
    this.defaultValue = defaultValue;
  }

  public List<FloatParameterRangeTO> getFloatParameterRanges() {
    return floatParameterRanges;
  }

  public void setFloatParameterRanges(List<FloatParameterRangeTO> floatParameterRanges) {
    this.floatParameterRanges = floatParameterRanges;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof FloatParameterTO)) {
      return false;
    }

    FloatParameterTO floatParameterDTO = (FloatParameterTO) o;
    if (this.getId() == null) {
      return false;
    }
    return Objects.equals(this.getId(), floatParameterDTO.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.getId());
  }

  // prettier-ignore
  @Override
  public String toString() {
    return "FloatParameterDTO{"
        + "parameterTypeDefinitionId="
        + getId()
        + ", defaultValue="
        + getDefaultValue()
        + "}";
  }
}
