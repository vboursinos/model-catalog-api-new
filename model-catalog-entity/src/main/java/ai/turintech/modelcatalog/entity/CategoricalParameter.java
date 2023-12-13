package ai.turintech.modelcatalog.entity;

import ai.turintech.components.data.common.entity.AbstractEntity;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/** A CategoricalParameter. */
@Entity
@Table(name = "categorical_parameter")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CategoricalParameter extends AbstractEntity implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @Column(name = "parameter_type_definition_id", insertable = false, updatable = false)
  private UUID parameterTypeDefinitionId;

  @Column(name = "default_value")
  private String defaultValue;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "parameter_type_definition_id", unique = true)
  private ParameterTypeDefinition parameterTypeDefinition;

  @OneToMany(fetch = FetchType.EAGER, mappedBy = "categoricalParameter")
  @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
  private Set<CategoricalParameterValue> categoricalParameterValues = new HashSet<>();

  public CategoricalParameter() {}

  public CategoricalParameter(ParameterTypeDefinition parameterTypeDefinition) {
    this.parameterTypeDefinitionId = parameterTypeDefinition.getId();
    this.parameterTypeDefinition = parameterTypeDefinition;
  }

  public UUID getParameterTypeDefinitionId() {
    return parameterTypeDefinitionId;
  }

  public void setParameterTypeDefinitionId(UUID parameterTypeDefinitionId) {
    this.parameterTypeDefinitionId = parameterTypeDefinitionId;
  }

  public String getDefaultValue() {
    return this.defaultValue;
  }

  public CategoricalParameter defaultValue(String defaultValue) {
    this.setDefaultValue(defaultValue);
    return this;
  }

  public void setDefaultValue(String defaultValue) {
    this.defaultValue = defaultValue;
  }

  public ParameterTypeDefinition getParameterTypeDefinition() {
    return this.parameterTypeDefinition;
  }

  public void setParameterTypeDefinition(ParameterTypeDefinition parameterTypeDefinition) {
    this.parameterTypeDefinition = parameterTypeDefinition;
  }

  public CategoricalParameter parameterTypeDefinition(
      ParameterTypeDefinition parameterTypeDefinition) {
    this.setParameterTypeDefinition(parameterTypeDefinition);
    return this;
  }

  public Set<CategoricalParameterValue> getCategoricalParameterValues() {
    return this.categoricalParameterValues;
  }

  public void setCategoricalParameterValues(
      Set<CategoricalParameterValue> categoricalParameterValues) {
    if (this.categoricalParameterValues != null) {
      this.categoricalParameterValues.forEach(i -> i.setCategoricalParameter(null));
    }
    if (categoricalParameterValues != null) {
      categoricalParameterValues.forEach(i -> i.setCategoricalParameter(this));
    }
    this.categoricalParameterValues = categoricalParameterValues;
  }

  public CategoricalParameter categoricalParameterValues(
      Set<CategoricalParameterValue> categoricalParameterValues) {
    this.setCategoricalParameterValues(categoricalParameterValues);
    return this;
  }

  public CategoricalParameter addCategoricalParameterValue(
      CategoricalParameterValue categoricalParameterValue) {
    this.categoricalParameterValues.add(categoricalParameterValue);
    categoricalParameterValue.setCategoricalParameter(this);
    return this;
  }

  public CategoricalParameter removeCategoricalParameterValue(
      CategoricalParameterValue categoricalParameterValue) {
    this.categoricalParameterValues.remove(categoricalParameterValue);
    categoricalParameterValue.setCategoricalParameter(null);
    return this;
  }

  // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

  @Override
  public String toString() {
    return "CategoricalParameter{" + "defaultValue='" + getDefaultValue() + "'" + "}";
  }
}
