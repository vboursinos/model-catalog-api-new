package ai.turintech.catalog.service.dto;

import org.springframework.data.relational.core.sql.Table;

public class RelationshipDTO {
    private RelationshipTypeDTO type;
    private String toTable;
    private Table toTableObject;
    private String toTableAlias;
    private String fromColumn;
    private String toColumn;
    private String toColumnPrefix;

    public RelationshipDTO() {
    }

    public RelationshipDTO(RelationshipTypeDTO type, String toTable, Table toTableObject, String fromColumn, String toColumn, String toColumnPrefix) {
        this.type = type;
        this.toTable = toTable;
        this.toTableObject = toTableObject;
        this.fromColumn = fromColumn;
        this.toColumn = toColumn;
        this.toColumnPrefix = toColumnPrefix;
    }

    public RelationshipDTO(RelationshipTypeDTO type, String toTable, String fromColumn, String toColumn, String toColumnPrefix) {
        this.type = type;
        this.toTable = toTable;
        this.fromColumn = fromColumn;
        this.toColumn = toColumn;
        this.toColumnPrefix = toColumnPrefix;
    }

    public RelationshipTypeDTO getType() {
        return type;
    }

    public void setType(RelationshipTypeDTO type) {
        this.type = type;
    }

    public String getToTable() {
        return toTable;
    }

    public void setToTable(String toTable) {
        this.toTable = toTable;
    }

    public String getFromColumn() {
        return fromColumn;
    }

    public void setFromColumn(String fromColumn) {
        this.fromColumn = fromColumn;
    }

    public String getToColumn() {
        return toColumn;
    }

    public void setToColumn(String toColumn) {
        this.toColumn = toColumn;
    }

    public String getToTableAlias() {
        return toTableAlias;
    }

    public void setToTableAlias(String toTableAlias) {
        this.toTableAlias = toTableAlias;
    }

    public Table getToTableObject() {
        return toTableObject;
    }

    public void setToTableObject(Table toTableObject) {
        this.toTableObject = toTableObject;
    }

    public String getToColumnPrefix() {
        return toColumnPrefix;
    }

    public void setToColumnPrefix(String toColumnPrefix) {
        this.toColumnPrefix = toColumnPrefix;
    }

    @Override
    public String toString() {
        return "RelationshipDTO{" +
                "type='" + type + '\'' +
                ", toTable='" + toTable + '\'' +
                ", toTableAlias='" + toTableAlias + '\'' +
                ", fromColumn='" + fromColumn + '\'' +
                ", toColumn='" + toColumn + '\'' +
                '}';
    }
}
