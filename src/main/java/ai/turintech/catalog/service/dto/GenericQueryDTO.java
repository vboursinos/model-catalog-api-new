package ai.turintech.catalog.service.dto;

import org.springframework.data.relational.core.sql.Table;

import java.util.ArrayList;
import java.util.List;

public class GenericQueryDTO {
    private List<TableInfoDTO> tables = new ArrayList<>();

    public GenericQueryDTO() {
    }

    public GenericQueryDTO(List<TableInfoDTO> tables) {
        this.tables = tables;
    }

    public List<TableInfoDTO> getTables() {
        return tables;
    }

    public void setTables(List<TableInfoDTO> tables) {
        this.tables = tables;
    }
}
