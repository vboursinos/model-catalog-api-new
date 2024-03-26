package ai.turintech.modelcatalog.to;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@SuppressWarnings("common-java:DuplicatedBlocks")
public class CategoricalParameterTO extends ParameterTypeDefinitionTO {

  private static final long serialVersionUID = 6418488670699594296L;

  private String defaultValue;

  private Set<CategoricalParameterValueTO> categoricalParameterValues = new HashSet<>();;

  public String getDefaultValue() {
    return defaultValue;
  }

  public void setDefaultValue(String defaultValue) {
    this.defaultValue = defaultValue;
  }

  public Set<CategoricalParameterValueTO> getCategoricalParameterValues() {
    return categoricalParameterValues;
  }

  public void setCategoricalParameterValues(
      Set<CategoricalParameterValueTO> categoricalParameterValues) {
    this.categoricalParameterValues = categoricalParameterValues;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof CategoricalParameterTO)) {
      return false;
    }

    CategoricalParameterTO categoricalParameterDTO = (CategoricalParameterTO) o;
    if (this.getId() == null) {
      return false;
    }
    return Objects.equals(this.getId(), categoricalParameterDTO.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.getId());
  }

  @Override
  public String toString() {
    return "CategoricalParameterDTO{"
        + "parameterTypeDefinitionId="
        + getId()
        + ", defaultValue='"
        + defaultValue
        + '\''
        + '}';
  }
}
