package ai.turintech.modelcatalog.entity;

import ai.turintech.components.data.common.entity.AbstractUUIDIdentityEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/** A Parameter. */
@Entity
@Table(name = "parameter")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Parameter extends AbstractUUIDIdentityEntity {

  private static final long serialVersionUID = 1L;

  @NotNull
  @Column(name = "name", nullable = false)
  private String name;

  @NotNull
  @Column(name = "label", nullable = false)
  private String label;

  @Column(name = "description")
  private String description;

  @NotNull
  @Column(name = "enabled", nullable = false)
  private Boolean enabled;

  @NotNull
  @Column(name = "fixed_value", nullable = false)
  private Boolean fixedValue;

  @NotNull
  @Column(name = "ordering", nullable = false)
  private Integer ordering;

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "parameter")
  @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
  private Set<BooleanParameter> booleanParameters = new HashSet<>();

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "parameter")
  @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
  private Set<CategoricalParameter> categoricalParameters = new HashSet<>();

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "parameter")
  @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
  private Set<FloatParameter> floatParameters = new HashSet<>();

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "parameter")
  @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
  private Set<IntegerParameter> integerParameters = new HashSet<>();

  @ManyToOne(fetch = FetchType.LAZY)
  private Model model;

  // jhipster-needle-entity-add-field - JHipster will add fields here

  public String getName() {
    return this.name;
  }

  public Parameter name(String name) {
    this.setName(name);
    return this;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getLabel() {
    return this.label;
  }

  public Parameter label(String label) {
    this.setLabel(label);
    return this;
  }

  public void setLabel(String label) {
    this.label = label;
  }

  public String getDescription() {
    return this.description;
  }

  public Parameter description(String description) {
    this.setDescription(description);
    return this;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Boolean getEnabled() {
    return this.enabled;
  }

  public Parameter enabled(Boolean enabled) {
    this.setEnabled(enabled);
    return this;
  }

  public void setEnabled(Boolean enabled) {
    this.enabled = enabled;
  }

  public Boolean getFixedValue() {
    return this.fixedValue;
  }

  public Parameter fixedValue(Boolean fixedValue) {
    this.setFixedValue(fixedValue);
    return this;
  }

  public void setFixedValue(Boolean fixedValue) {
    this.fixedValue = fixedValue;
  }

  public Integer getOrdering() {
    return this.ordering;
  }

  public Parameter ordering(Integer ordering) {
    this.setOrdering(ordering);
    return this;
  }

  public void setOrdering(Integer ordering) {
    this.ordering = ordering;
  }

  public Set<BooleanParameter> getBooleanParameters() {
    return booleanParameters;
  }

  public void setBooleanParameters(Set<BooleanParameter> booleanParameters) {
    this.booleanParameters = booleanParameters;
  }

  public Set<FloatParameter> getFloatParameters() {
    return floatParameters;
  }

  public void setFloatParameters(Set<FloatParameter> floatParameters) {
    this.floatParameters = floatParameters;
  }

  public Set<IntegerParameter> getIntegerParameters() {
    return integerParameters;
  }

  public void setIntegerParameters(Set<IntegerParameter> integerParameters) {
    this.integerParameters = integerParameters;
  }

  public Model getModel() {
    return this.model;
  }

  public void setModel(Model model) {
    this.model = model;
  }

  public Parameter model(Model model) {
    this.setModel(model);
    return this;
  }

  public Set<CategoricalParameter> getCategoricalParameters() {
    return this.categoricalParameters;
  }

  public void setCategoricalParameters(Set<CategoricalParameter> categoricalParameters) {
    if (this.categoricalParameters != null) {
      this.categoricalParameters.forEach(i -> i.setParameter(null));
    }
    if (categoricalParameters != null) {
      categoricalParameters.forEach(i -> i.setParameter(this));
    }
    this.categoricalParameters = categoricalParameters;
  }
  // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Parameter)) {
      return false;
    }
    return getId() != null && getId().equals(((Parameter) o).getId());
  }

  @Override
  public int hashCode() {
    // see
    // https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
    return getClass().hashCode();
  }

  // prettier-ignore

  @Override
  public String toString() {
    return "Parameter{"
        + "id='"
        + super.getId()
        + "', name='"
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
        + ", booleanParameters="
        + booleanParameters
        + ", categoricalParameters="
        + categoricalParameters
        + ", floatParameters="
        + floatParameters
        + ", integerParameters="
        + integerParameters
        + ", model="
        + model
        + '}';
  }
}
