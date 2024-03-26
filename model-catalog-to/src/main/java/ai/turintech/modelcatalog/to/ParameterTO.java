package ai.turintech.modelcatalog.to;

import ai.turintech.components.data.common.to.AbstractUUIDIdentityTO;
import jakarta.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@SuppressWarnings("common-java:DuplicatedBlocks")
public class ParameterTO extends AbstractUUIDIdentityTO {

  private static final long serialVersionUID = -3037790423144785590L;

  @NotNull(message = "must not be null")
  private String name;

  @NotNull(message = "must not be null")
  private String label;

  private String description;

  @NotNull(message = "must not be null")
  private Boolean enabled;

  @NotNull(message = "must not be null")
  private Boolean fixedValue;

  @NotNull(message = "must not be null")
  private Integer ordering;

  @NotNull private UUID modelId;

  @NotNull private List<BooleanParameterTO> booleanParameters = new ArrayList<>();
  @NotNull private List<CategoricalParameterTO> categoricalParameters = new ArrayList<>();

  @NotNull private List<FloatParameterTO> floatParameters = new ArrayList<>();

  @NotNull private List<IntegerParameterTO> integerParameters = new ArrayList<>();

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getLabel() {
    return label;
  }

  public void setLabel(String label) {
    this.label = label;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Boolean getEnabled() {
    return enabled;
  }

  public void setEnabled(Boolean enabled) {
    this.enabled = enabled;
  }

  public Boolean getFixedValue() {
    return fixedValue;
  }

  public void setFixedValue(Boolean fixedValue) {
    this.fixedValue = fixedValue;
  }

  public Integer getOrdering() {
    return ordering;
  }

  public void setOrdering(Integer ordering) {
    this.ordering = ordering;
  }

  public UUID getModelId() {
    return modelId;
  }

  public void setModelId(UUID modelId) {
    this.modelId = modelId;
  }

  public List<BooleanParameterTO> getBooleanParameters() {
    return booleanParameters;
  }

  public void setBooleanParameters(List<BooleanParameterTO> booleanParameters) {
    this.booleanParameters = booleanParameters;
  }

  public List<FloatParameterTO> getFloatParameters() {
    return floatParameters;
  }

  public void setFloatParameters(List<FloatParameterTO> floatParameters) {
    this.floatParameters = floatParameters;
  }

  public List<IntegerParameterTO> getIntegerParameters() {
    return integerParameters;
  }

  public void setIntegerParameters(List<IntegerParameterTO> integerParameters) {
    this.integerParameters = integerParameters;
  }

  public List<CategoricalParameterTO> getCategoricalParameters() {
    return categoricalParameters;
  }

  public void setCategoricalParameters(List<CategoricalParameterTO> categoricalParameters) {
    this.categoricalParameters = categoricalParameters;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof ParameterTO)) {
      return false;
    }

    ParameterTO parameterDTO = (ParameterTO) o;
    if (this.getId() == null) {
      return false;
    }
    return Objects.equals(this.getId(), parameterDTO.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.getId());
  }

  // prettier-ignore

  @Override
  public String toString() {
    return "ParameterTO{"
    	+ "id="+super.getId()
        + ", name='"
        + name
        + '\''
        + ", label='"
        + label
        + '\''
        + ", description='"
        + description
        + '\''
        + ", enabled="
        + enabled
        + ", fixedValue="
        + fixedValue
        + ", ordering="
        + ordering
        + ", modelId="
        + modelId
        + ", booleanParameters="
        + booleanParameters
        + ", categoricalParameters="
        + categoricalParameters
        + ", floatParameters="
        + floatParameters
        + ", integerParameters="
        + integerParameters
        + '}';
  }
}
