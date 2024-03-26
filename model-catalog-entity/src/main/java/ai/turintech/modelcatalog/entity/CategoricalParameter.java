package ai.turintech.modelcatalog.entity;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/** A CategoricalParameter. */
@Entity
@Table(name = "categorical_parameter")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CategoricalParameter extends ParameterTypeDefinition {

  private static final long serialVersionUID = 1L;

  @Column(name = "default_value")
  private String defaultValue;

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "categoricalParameter")
  @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
  private Set<CategoricalParameterValue> categoricalParameterValues = new HashSet<>();

  public CategoricalParameter() {}

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

  @Override
  public String toString() {
    return "CategoricalParameter{"
        + "defaultValue='"
        + defaultValue
        + '\''
        + ", categoricalParameterValues="
        + categoricalParameterValues
        + '}';
  }
}
