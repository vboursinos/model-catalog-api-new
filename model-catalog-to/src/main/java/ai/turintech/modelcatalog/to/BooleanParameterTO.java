package ai.turintech.modelcatalog.to;

import java.util.Objects;

@SuppressWarnings("common-java:DuplicatedBlocks")
public class BooleanParameterTO extends ParameterTypeDefinitionTO {

  private static final long serialVersionUID = 1087247945141281117L;
  private Boolean defaultValue;

  public Boolean getDefaultValue() {
    return defaultValue;
  }

  public void setDefaultValue(Boolean defaultValue) {
    this.defaultValue = defaultValue;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof BooleanParameterTO)) {
      return false;
    }

    BooleanParameterTO booleanParameterDTO = (BooleanParameterTO) o;
    if (this.getId() == null) {
      return false;
    }
    return Objects.equals(this.getId(), booleanParameterDTO.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.getId());
  }

  // prettier-ignore
  @Override
  public String toString() {
    return "BooleanParameterDTO{"
        + "parameterTypeDefinitionId="
        + getId()
        + ", defaultValue="
        + defaultValue
        + '}';
  }
}
