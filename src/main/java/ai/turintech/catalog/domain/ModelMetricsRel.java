package ai.turintech.catalog.domain;

import ai.turintech.catalog.anotatation.Relationship;
import ai.turintech.catalog.service.dto.RelationshipTypeDTO;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("rel_model__incompatible_metrics")
public class ModelMetricsRel {

    @Column("model=_id")
    @Relationship(type = RelationshipTypeDTO.MANY_TO_MANY, fromColumn = "model_id", toTable = "model", toColumn = "id", toColumnPrefix = "Model")
    private Model model;

    @Column("metric_id")
    @Relationship(type = RelationshipTypeDTO.MANY_TO_MANY, fromColumn = "metric_id", toTable = "metric", toColumn = "id", toColumnPrefix = "metric")
    private Metric metric;

    public ModelMetricsRel() {
    }

    public ModelMetricsRel(Model model, Metric metric) {
        this.model = model;
        this.metric = metric;
    }

    public Model getModel() {
        return model;
    }

    public void setModel(Model model) {
        this.model = model;
    }

    public Metric getMetric() {
        return metric;
    }

    public void setMetric(Metric metric) {
        this.metric = metric;
    }

    @Override
    public String toString() {
        return "ModelMetricsRel{" +
            "model=" + model +
            ", metric=" + metric +
            '}';
    }

}
