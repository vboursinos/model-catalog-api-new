package ai.turintech.catalog.service.dto;

public class SearchDTO {
    private String mlTask;
    private String modelType;

    private String structure;
    private boolean enabled = true;

    public SearchDTO() {
    }

    public SearchDTO(String mlTask, String modelType, String structure, boolean enabled) {
        this.mlTask = mlTask;
        this.modelType = modelType;
        this.enabled = enabled;
        this.structure = structure;
    }

    public String getMlTask() {
        return mlTask;
    }

    public void setMlTask(String mlTask) {
        this.mlTask = mlTask;
    }

    public String getModelType() {
        return modelType;
    }

    public void setModelType(String modelType) {
        this.modelType = modelType;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getStructure() {
        return structure;
    }

    public void setStructure(String structure) {
        this.structure = structure;
    }

    @Override
    public String toString() {
        return "SearchDTO{" +
                "mlTask='" + mlTask + '\'' +
                ", modelType='" + modelType + '\'' +
                ", structure='" + structure + '\'' +
                ", enabled=" + enabled +
                '}';
    }
}
