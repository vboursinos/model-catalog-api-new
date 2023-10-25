package ai.turintech.catalog.service.dto;

public class SearchDTO {
    private String key;
    private String operation;
    private Object value;

    public SearchDTO() {
    }

    public SearchDTO(String key, String operation, Object value) {
        this.key = key;
        this.operation = operation;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "SearchDTO{" +
                "key='" + key + '\'' +
                ", operation='" + operation + '\'' +
                ", value=" + value +
                '}';
    }
}
