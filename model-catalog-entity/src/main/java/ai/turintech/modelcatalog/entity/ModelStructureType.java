package ai.turintech.modelcatalog.entity;

import ai.turintech.components.data.common.entity.AbstractEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/** A ModelStructureType. */
@Entity
@Table(name = "model_structure_type")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ModelStructureType extends AbstractEntity implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue
  @Column(name = "id")
  private UUID id;

  @NotNull
  @Column(name = "name", nullable = false)
  private String name;

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "structure")
  @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
  private Set<Model> models = new HashSet<>();

  // jhipster-needle-entity-add-field - JHipster will add fields here

  public UUID getId() {
    return this.id;
  }

  public ModelStructureType id(UUID id) {
    this.setId(id);
    return this;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public String getName() {
    return this.name;
  }

  public ModelStructureType name(String name) {
    this.setName(name);
    return this;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Set<Model> getModels() {
    return this.models;
  }

  public void setModels(Set<Model> models) {
    if (this.models != null) {
      this.models.forEach(i -> i.setStructure(null));
    }
    if (models != null) {
      models.forEach(i -> i.setStructure(this));
    }
    this.models = models;
  }

  public ModelStructureType models(Set<Model> models) {
    this.setModels(models);
    return this;
  }

  public ModelStructureType addModels(Model model) {
    this.models.add(model);
    model.setStructure(this);
    return this;
  }

  public ModelStructureType removeModels(Model model) {
    this.models.remove(model);
    model.setStructure(null);
    return this;
  }

  // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof ModelStructureType)) {
      return false;
    }
    return getId() != null && getId().equals(((ModelStructureType) o).getId());
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
    return "ModelStructureType{" + "id=" + getId() + ", name='" + getName() + "'" + "}";
  }
}
