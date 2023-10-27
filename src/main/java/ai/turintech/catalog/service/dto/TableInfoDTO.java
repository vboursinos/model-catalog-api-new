package ai.turintech.catalog.service.dto;

import org.springframework.data.relational.core.sql.Table;

import java.util.List;

public class TableInfoDTO {
    Table table;
    String columnPrefix;
    List<String> columns;
    List<RelationshipDTO> relationships;

    public TableInfoDTO() {
    }

    public TableInfoDTO(Table table, String columnPrefix, List<String> columns, List<RelationshipDTO> relationships) {
        this.table = table;
        this.columnPrefix = columnPrefix;
        this.columns = columns;
        this.relationships = relationships;
    }

    public Table getTable() {
        return table;
    }

    public void setTable(Table table) {
        this.table = table;
    }

    public String getColumnPrefix() {
        return columnPrefix;
    }

    public void setColumnPrefix(String columnPrefix) {
        this.columnPrefix = columnPrefix;
    }

    public List<String> getColumns() {
        return columns;
    }

    public void setColumns(List<String> columns) {
        this.columns = columns;
    }

    public List<RelationshipDTO> getRelationships() {
        return relationships;
    }

    public void setRelationships(List<RelationshipDTO> relationships) {
        this.relationships = relationships;
    }

    @Override
    public String toString() {
        return "TableInfoDTO{" +
                "table=" + table +
                ", columnPrefix='" + columnPrefix + '\'' +
                ", columns=" + columns +
                ", relationships=" + relationships +
                '}';
    }
}
