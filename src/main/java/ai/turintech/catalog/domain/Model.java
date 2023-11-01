package ai.turintech.catalog.domain;

import ai.turintech.catalog.anotatation.Relationship;
import ai.turintech.catalog.service.dto.RelationshipTypeDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.*;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A Model.
 */
@Table("model")
@JsonIgnoreProperties(value = { "new" })
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Model implements Serializable, Persistable<UUID> {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private UUID id;

    @NotNull(message = "must not be null")
    @Column("name")
    private String name;

    @NotNull(message = "must not be null")
    @Column("display_name")
    private String displayName;

    @Column("description")
    private String description;

    @Column("advantages")
    private String advantages;

    @Column("disadvantages")
    private String disadvantages;

    @NotNull(message = "must not be null")
    @Column("enabled")
    private Boolean enabled;

    @NotNull(message = "must not be null")
    @Column("decision_tree")
    private Boolean decisionTree;

    @Transient
    private boolean isPersisted;

    @Transient
    @JsonIgnoreProperties(value = { "definitions", "model" }, allowSetters = true)
    private List<Parameter> parameters = new ArrayList<>();

    @Transient
    @JsonIgnoreProperties(value = { "models" }, allowSetters = true)
    private List<ModelGroupType> groups = new ArrayList<>();

    @Transient
    @JsonIgnoreProperties(value = { "models" }, allowSetters = true)
    private List<Metric> incompatibleMetrics = new ArrayList<>();

    @Relationship(type = RelationshipTypeDTO.MANY_TO_ONE, toTable = "ml_task_type", fromColumn = "ml_task_id", toColumn = "id", toColumnPrefix = "mlTask")
    @Transient
    @JsonIgnoreProperties(value = { "models" }, allowSetters = true)
    private MlTaskType mlTask;

    @Relationship(type = RelationshipTypeDTO.MANY_TO_ONE, toTable = "model_structure_type", fromColumn = "structure_id", toColumn = "id", toColumnPrefix = "structure")
    @Transient
    @JsonIgnoreProperties(value = { "models" }, allowSetters = true)
    private ModelStructureType structure;

    @Relationship(type = RelationshipTypeDTO.MANY_TO_ONE, toTable = "model_type", fromColumn = "model_type_id", toColumn = "id", toColumnPrefix = "modelType")
    @Transient
    @JsonIgnoreProperties(value = { "models" }, allowSetters = true)
    private ModelType type;

    @Relationship(type = RelationshipTypeDTO.MANY_TO_ONE, toTable = "model_family_type", fromColumn = "family_type_id", toColumn = "id", toColumnPrefix = "familyType")
    @Transient
    @JsonIgnoreProperties(value = { "models" }, allowSetters = true)
    private ModelFamilyType familyType;

    @Relationship(type = RelationshipTypeDTO.MANY_TO_ONE, toTable = "model_ensemble_type", fromColumn = "ensemble_type_id", toColumn = "id", toColumnPrefix = "ensembleType")
    @Transient
    @JsonIgnoreProperties(value = { "models" }, allowSetters = true)
    private ModelEnsembleType ensembleType;

    @Column("ml_task_id")
    private UUID mlTaskId;

    @Column("structure_id")
    private UUID structureId;

    @Column("model_type_id")
    private UUID modelTypeId;

    @Column("family_type_id")
    private UUID familyTypeId;

    @Column("ensemble_type_id")
    private UUID ensembleTypeId;

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

    @Transient
    @Override
    public boolean isNew() {
        return !this.isPersisted;
    }

    public Model setIsPersisted() {
        this.isPersisted = true;
        return this;
    }

    public List<Parameter> getParameters() {
        return this.parameters;
    }

    public void setParameters(List<Parameter> parameters) {
        this.parameters = parameters;
    }

    public Model parameters(List<Parameter> parameters) {
        this.setParameters(parameters);
        return this;
    }

    public List<ModelGroupType> getGroups() {
        return groups;
    }

    public void setGroups(List<ModelGroupType> groups) {
        this.groups = groups;
    }

    public void setIncompatibleMetrics(List<Metric> incompatibleMetrics) {
        this.incompatibleMetrics = incompatibleMetrics;
    }

    public List<Metric> getIncompatibleMetrics() {
        return incompatibleMetrics;
    }

    public Model groups(List<ModelGroupType> modelGroupTypes) {
        this.setGroups(modelGroupTypes);
        return this;
    }

    public Model addGroups(ModelGroupType modelGroupType) {
        this.groups.add(modelGroupType);
        modelGroupType.getModels().add(this);
        return this;
    }

    public Model removeGroups(ModelGroupType modelGroupType) {
        this.groups.remove(modelGroupType);
        modelGroupType.getModels().remove(this);
        return this;
    }

    public Model incompatibleMetrics(List<Metric> metrics) {
        this.setIncompatibleMetrics(metrics);
        return this;
    }

    public Model addIncompatibleMetrics(Metric metric) {
        this.incompatibleMetrics.add(metric);
        metric.getModels().add(this);
        return this;
    }

    public Model removeIncompatibleMetrics(Metric metric) {
        this.incompatibleMetrics.remove(metric);
        metric.getModels().remove(this);
        return this;
    }

    public MlTaskType getMlTask() {
        return this.mlTask;
    }

    public void setMlTask(MlTaskType mlTaskType) {
        this.mlTask = mlTaskType;
        this.mlTaskId = mlTaskType != null ? mlTaskType.getId() : null;
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
        this.structureId = modelStructureType != null ? modelStructureType.getId() : null;
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
        this.modelTypeId = modelType != null ? modelType.getId() : null;
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
        this.familyTypeId = modelFamilyType != null ? modelFamilyType.getId() : null;
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
        this.ensembleTypeId = modelEnsembleType != null ? modelEnsembleType.getId() : null;
    }

    public Model ensembleType(ModelEnsembleType modelEnsembleType) {
        this.setEnsembleType(modelEnsembleType);
        return this;
    }

    public UUID getMlTaskId() {
        return this.mlTaskId;
    }

    public void setMlTaskId(UUID mlTaskType) {
        this.mlTaskId = mlTaskType;
    }

    public UUID getStructureId() {
        return this.structureId;
    }

    public void setStructureId(UUID modelStructureType) {
        this.structureId = modelStructureType;
    }

    public UUID getModelTypeId() {
        return modelTypeId;
    }

    public void setModelTypeId(UUID modelTypeId) {
        this.modelTypeId = modelTypeId;
    }

    public UUID getFamilyTypeId() {
        return this.familyTypeId;
    }

    public void setFamilyTypeId(UUID modelFamilyType) {
        this.familyTypeId = modelFamilyType;
    }

    public UUID getEnsembleTypeId() {
        return this.ensembleTypeId;
    }

    public void setEnsembleTypeId(UUID modelEnsembleType) {
        this.ensembleTypeId = modelEnsembleType;
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
                "id=" + id +
                ", name='" + name + '\'' +
                ", displayName='" + displayName + '\'' +
                ", description='" + description + '\'' +
                ", advantages='" + advantages + '\'' +
                ", disadvantages='" + disadvantages + '\'' +
                ", enabled=" + enabled +
                ", decisionTree=" + decisionTree +
                ", parameters=" + parameters +
                ", groups=" + groups +
                ", incompatibleMetrics=" + incompatibleMetrics +
                ", mlTask=" + mlTask +
                ", structure=" + structure +
                ", type=" + type +
                ", familyType=" + familyType +
                ", ensembleType=" + ensembleType +
                ", mlTaskId=" + mlTaskId +
                ", structureId=" + structureId +
                ", modelTypeId=" + modelTypeId +
                ", familyTypeId=" + familyTypeId +
                ", ensembleTypeId=" + ensembleTypeId +
                '}';
    }
}
