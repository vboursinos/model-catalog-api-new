package ai.turintech.catalog.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * A Model.
 */
@Entity
@Table(name = "model")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Model implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    @Column(name = "id")
    private UUID id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "display_name", nullable = false)
    private String displayName;

    @Column(name = "description")
    private String description;

    @Column(name = "advantages")
    private String advantages;

    @Column(name = "disadvantages")
    private String disadvantages;

    @NotNull
    @Column(name = "enabled", nullable = false)
    private Boolean enabled;

    @NotNull
    @Column(name = "decision_tree", nullable = false)
    private Boolean decisionTree;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "model")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "definitions", "model" }, allowSetters = true)
    private Set<Parameter> parameters = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_model__groups",
        joinColumns = @JoinColumn(name = "model_id"),
        inverseJoinColumns = @JoinColumn(name = "groups_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "models" }, allowSetters = true)
    private Set<ModelGroupType> groups = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_model__incompatible_metrics",
        joinColumns = @JoinColumn(name = "model_id"),
        inverseJoinColumns = @JoinColumn(name = "incompatible_metrics_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "models" }, allowSetters = true)
    private Set<Metric> incompatibleMetrics = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "models" }, allowSetters = true)
    private MlTaskType mlTask;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "models" }, allowSetters = true)
    private ModelStructureType structure;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "models" }, allowSetters = true)
    private ModelType type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "models" }, allowSetters = true)
    private ModelFamilyType familyType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "models" }, allowSetters = true)
    private ModelEnsembleType ensembleType;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public UUID getId() {
        return this.id;
    }

    public Model id(UUID id) {
        this.setId(id);
        return this;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Model name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public Model displayName(String displayName) {
        this.setDisplayName(displayName);
        return this;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getDescription() {
        return this.description;
    }

    public Model description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAdvantages() {
        return this.advantages;
    }

    public Model advantages(String advantages) {
        this.setAdvantages(advantages);
        return this;
    }

    public void setAdvantages(String advantages) {
        this.advantages = advantages;
    }

    public String getDisadvantages() {
        return this.disadvantages;
    }

    public Model disadvantages(String disadvantages) {
        this.setDisadvantages(disadvantages);
        return this;
    }

    public void setDisadvantages(String disadvantages) {
        this.disadvantages = disadvantages;
    }

    public Boolean getEnabled() {
        return this.enabled;
    }

    public Model enabled(Boolean enabled) {
        this.setEnabled(enabled);
        return this;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Boolean getDecisionTree() {
        return this.decisionTree;
    }

    public Model decisionTree(Boolean decisionTree) {
        this.setDecisionTree(decisionTree);
        return this;
    }

    public void setDecisionTree(Boolean decisionTree) {
        this.decisionTree = decisionTree;
    }

    public Set<Parameter> getParameters() {
        return this.parameters;
    }

    public void setParameters(Set<Parameter> parameters) {
        if (this.parameters != null) {
            this.parameters.forEach(i -> i.setModel(null));
        }
        if (parameters != null) {
            parameters.forEach(i -> i.setModel(this));
        }
        this.parameters = parameters;
    }

    public Model parameters(Set<Parameter> parameters) {
        this.setParameters(parameters);
        return this;
    }

    public Model addParameters(Parameter parameter) {
        this.parameters.add(parameter);
        parameter.setModel(this);
        return this;
    }

    public Model removeParameters(Parameter parameter) {
        this.parameters.remove(parameter);
        parameter.setModel(null);
        return this;
    }

    public Set<ModelGroupType> getGroups() {
        return this.groups;
    }

    public void setGroups(Set<ModelGroupType> modelGroupTypes) {
        this.groups = modelGroupTypes;
    }

    public Model groups(Set<ModelGroupType> modelGroupTypes) {
        this.setGroups(modelGroupTypes);
        return this;
    }

    public Model addGroups(ModelGroupType modelGroupType) {
        this.groups.add(modelGroupType);
        return this;
    }

    public Model removeGroups(ModelGroupType modelGroupType) {
        this.groups.remove(modelGroupType);
        return this;
    }

    public Set<Metric> getIncompatibleMetrics() {
        return this.incompatibleMetrics;
    }

    public void setIncompatibleMetrics(Set<Metric> metrics) {
        this.incompatibleMetrics = metrics;
    }

    public Model incompatibleMetrics(Set<Metric> metrics) {
        this.setIncompatibleMetrics(metrics);
        return this;
    }

    public Model addIncompatibleMetrics(Metric metric) {
        this.incompatibleMetrics.add(metric);
        return this;
    }

    public Model removeIncompatibleMetrics(Metric metric) {
        this.incompatibleMetrics.remove(metric);
        return this;
    }

    public MlTaskType getMlTask() {
        return this.mlTask;
    }

    public void setMlTask(MlTaskType mlTaskType) {
        this.mlTask = mlTaskType;
    }

    public Model mlTask(MlTaskType mlTaskType) {
        this.setMlTask(mlTaskType);
        return this;
    }

    public ModelStructureType getStructure() {
        return this.structure;
    }

    public void setStructure(ModelStructureType modelStructureType) {
        this.structure = modelStructureType;
    }

    public Model structure(ModelStructureType modelStructureType) {
        this.setStructure(modelStructureType);
        return this;
    }

    public ModelType getType() {
        return this.type;
    }

    public void setType(ModelType modelType) {
        this.type = modelType;
    }

    public Model type(ModelType modelType) {
        this.setType(modelType);
        return this;
    }

    public ModelFamilyType getFamilyType() {
        return this.familyType;
    }

    public void setFamilyType(ModelFamilyType modelFamilyType) {
        this.familyType = modelFamilyType;
    }

    public Model familyType(ModelFamilyType modelFamilyType) {
        this.setFamilyType(modelFamilyType);
        return this;
    }

    public ModelEnsembleType getEnsembleType() {
        return this.ensembleType;
    }

    public void setEnsembleType(ModelEnsembleType modelEnsembleType) {
        this.ensembleType = modelEnsembleType;
    }

    public Model ensembleType(ModelEnsembleType modelEnsembleType) {
        this.setEnsembleType(modelEnsembleType);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Model)) {
            return false;
        }
        return getId() != null && getId().equals(((Model) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Model{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", displayName='" + getDisplayName() + "'" +
            ", description='" + getDescription() + "'" +
            ", advantages='" + getAdvantages() + "'" +
            ", disadvantages='" + getDisadvantages() + "'" +
            ", enabled='" + getEnabled() + "'" +
            ", decisionTree='" + getDecisionTree() + "'" +
            "}";
    }
}
