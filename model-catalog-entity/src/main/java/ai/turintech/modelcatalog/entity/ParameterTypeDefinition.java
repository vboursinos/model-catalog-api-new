package ai.turintech.modelcatalog.entity;

import ai.turintech.components.data.common.entity.AbstractUUIDIdentityEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/** A ParameterTypeDefinition. */
@Entity
@Table(name = "parameter_type_definition")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Inheritance(strategy = InheritanceType.JOINED)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ParameterTypeDefinition extends AbstractUUIDIdentityEntity {

  private static final long serialVersionUID = 1L;

  @NotNull
  @Column(name = "ordering", nullable = false)
  private Integer ordering;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "parameter_distribution_type_id", referencedColumnName = "id")
  private ParameterDistributionType distribution;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "parameter_id", referencedColumnName = "id")
  private Parameter parameter;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "parameter_type_id", referencedColumnName = "id")
  private ParameterType type;

  // jhipster-needle-entity-add-field - JHipster will add fields here

  public Integer getOrdering() {
    return this.ordering;
  }

  public ParameterTypeDefinition ordering(Integer ordering) {
    this.setOrdering(ordering);
    return this;
  }

  public void setOrdering(Integer ordering) {
    this.ordering = ordering;
  }

  public ParameterDistributionType getDistribution() {
    return this.distribution;
  }

  public void setDistribution(ParameterDistributionType parameterDistributionType) {
    this.distribution = parameterDistributionType;
  }

  public ParameterTypeDefinition distribution(ParameterDistributionType parameterDistributionType) {
    this.setDistribution(parameterDistributionType);
    return this;
  }

  public Parameter getParameter() {
    return this.parameter;
  }

  public void setParameter(Parameter parameter) {
    this.parameter = parameter;
  }

  public ParameterTypeDefinition parameter(Parameter parameter) {
    this.setParameter(parameter);
    return this;
  }

  public ParameterType getType() {
    return this.type;
  }

  public void setType(ParameterType parameterType) {
    this.type = parameterType;
  }

  public ParameterTypeDefinition type(ParameterType parameterType) {
    this.setType(parameterType);
    return this;
  }

  // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof ParameterTypeDefinition)) {
      return false;
    }
    return getId() != null && getId().equals(((ParameterTypeDefinition) o).getId());
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
    return "ParameterTypeDefinition{" + "id=" + getId() + ", ordering=" + getOrdering() + "}";
  }
}
