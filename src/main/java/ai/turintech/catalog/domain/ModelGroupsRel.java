package ai.turintech.catalog.domain;

import ai.turintech.catalog.anotatation.Columns;
import ai.turintech.catalog.anotatation.Relationship;
import ai.turintech.catalog.service.dto.RelationshipTypeDTO;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Columns(names = {"model_id", "group_id"})
@Table("rel_model__groups")
public class ModelGroupsRel {

    @Column("model_id")
    @Relationship(type = RelationshipTypeDTO.MANY_TO_MANY, fromColumn = "model_id", toTable = "model", toColumn = "id", toColumnPrefix = "Model")
    private Model model;

    @Column("group_id")
    @Relationship(type = RelationshipTypeDTO.MANY_TO_MANY, fromColumn = "group_id", toTable = "model_group_type", toColumn = "id", toColumnPrefix = "modelGroup")
    private ModelGroupType groupType;

    public ModelGroupsRel() {
    }

    public ModelGroupsRel(Model model, ModelGroupType groupType) {
        this.model = model;
        this.groupType = groupType;
    }


    public Model getModel() {
        return model;
    }

    public void setModel(Model model) {
        this.model = model;
    }

    public ModelGroupType getGroupType() {
        return groupType;
    }

    public void setGroupType(ModelGroupType groupType) {
        this.groupType = groupType;
    }

    @Override
    public String toString() {
        return "ModelGroupsRel{" +
            "model=" + model +
            ", groupType=" + groupType +
            '}';
    }

}
