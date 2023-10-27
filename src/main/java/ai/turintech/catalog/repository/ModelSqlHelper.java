package ai.turintech.catalog.repository;

import java.util.ArrayList;
import java.util.List;

import ai.turintech.catalog.service.dto.GenericQueryDTO;
import ai.turintech.catalog.service.dto.TableInfoDTO;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class ModelSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("name", table, columnPrefix + "_name"));
        columns.add(Column.aliased("display_name", table, columnPrefix + "_display_name"));
        columns.add(Column.aliased("description", table, columnPrefix + "_description"));
        columns.add(Column.aliased("advantages", table, columnPrefix + "_advantages"));
        columns.add(Column.aliased("disadvantages", table, columnPrefix + "_disadvantages"));
        columns.add(Column.aliased("enabled", table, columnPrefix + "_enabled"));
        columns.add(Column.aliased("decision_tree", table, columnPrefix + "_decision_tree"));

        columns.add(Column.aliased("ml_task_id", table, columnPrefix + "_ml_task_id"));
        columns.add(Column.aliased("structure_id", table, columnPrefix + "_structure_id"));
        columns.add(Column.aliased("model_type_id", table, columnPrefix + "_model_type_id"));
        columns.add(Column.aliased("family_type_id", table, columnPrefix + "_family_type_id"));
        columns.add(Column.aliased("ensemble_type_id", table, columnPrefix + "_ensemble_type_id"));
        return columns;
    }

    public static List<Expression> getColumnsGeneric(GenericQueryDTO genericQueryDTO) {
        List<Expression> columns = new ArrayList<>();
        for (TableInfoDTO tableInfoDTO : genericQueryDTO.getTables()) {
            for (String column : tableInfoDTO.getColumns()) {
                columns.add(Column.aliased(column, tableInfoDTO.getTable(), tableInfoDTO.getColumnPrefix() + "_" + column));
            }
        }
        return columns;
    }

}
