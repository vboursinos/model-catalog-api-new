package ai.turintech.catalog.service.dto;

import org.springframework.data.relational.core.sql.Table;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GenericQueryDTO {
    private List<TableInfoDTO> tables = new ArrayList<>();
    private Map<String,List<String>> tableColumnsMap = new HashMap<>();

    public GenericQueryDTO() {
    }

    public GenericQueryDTO(List<TableInfoDTO> tables, Map<String, List<String>> tableColumnsMap) {
        this.tables = tables;
        this.tableColumnsMap = tableColumnsMap;
    }
    public List<TableInfoDTO> getTables() {
        return tables;
    }

    public void setTables(List<TableInfoDTO> tables) {
        this.tables = tables;
    }

    public Map<String, List<String>> getTableColumnsMap() {
        return tableColumnsMap;
    }

    public void setTableColumnsMap(Map<String, List<String>> tableColumnsMap) {
        this.tableColumnsMap = tableColumnsMap;
    }
}
