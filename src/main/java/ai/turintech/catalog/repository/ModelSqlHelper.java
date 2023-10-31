package ai.turintech.catalog.repository;

import ai.turintech.catalog.service.dto.GenericQueryDTO;
import ai.turintech.catalog.service.dto.RelationshipDTO;
import ai.turintech.catalog.service.dto.TableInfoDTO;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public static List<Expression> getColumnsGeneric2(GenericQueryDTO genericQueryDTO) {
        List<Expression> columns = new ArrayList<>();
        for (TableInfoDTO tableInfoDTO : genericQueryDTO.getTables()) {
            for (String column : tableInfoDTO.getColumns()) {
                columns.add(Column.aliased(column, tableInfoDTO.getTable(), tableInfoDTO.getColumnPrefix() + "_" + column));
            }
        }
        return columns;
    }

/*    public static Map<String, List<Expression>> getColumnsGeneric(GenericQueryDTO genericQueryDTO) {
        Map<String, List<Expression>> columnsMap = new HashMap<>();
        for (TableInfoDTO tableInfoDTO : genericQueryDTO.getTables()) {
            List<Expression> columns = null;
            if (!tableInfoDTO.getRelationships().isEmpty()) {
                columns = new ArrayList<>();
                for (String column : tableInfoDTO.getColumns()) {
                    columns.add(Column.aliased(column, tableInfoDTO.getTable(), tableInfoDTO.getColumnPrefix() + "_" + column));
                }
                for (RelationshipDTO relationship : tableInfoDTO.getRelationships()) {
                    if (relationship.getType().getValue().equals("many-to-one") || relationship.getType().getValue().equals("many-to-many")) {
                        for (Map.Entry<String, List<String>> table : genericQueryDTO.getTableColumnsMap().entrySet()) {
                            if (table.getKey().equals(relationship.getToTable())) {
                                for (String column : table.getValue()) {
                                    columns.add(Column.aliased(column, relationship.getToTableObject(), relationship.getToColumnPrefix() + "_" + column));
                                }
                            }
                        }
                    }
                    columnsMap.put(tableInfoDTO.getTable().getName().toString(), columns);
                }
            }
        }
        return columnsMap;
    }*/


    public static Map<String, List<Expression>> getColumnsGeneric(GenericQueryDTO genericQueryDTO) {
        Map<String, List<Expression>> columnsMap = new HashMap<>();

        // Looping through tables in the DTO
        for (TableInfoDTO tableInfoDTO : genericQueryDTO.getTables()) {

            // Only work with tables that have relationships
            if (!tableInfoDTO.getRelationships().isEmpty()) {
                List<Expression> columns = generateExpressionList(tableInfoDTO);
                addRelationshipColumnsToMap(tableInfoDTO, genericQueryDTO, columnsMap, columns);
            }
        }

        return columnsMap;
    }

    // Helper method to create a list of Expressions for a given table
    private static List<Expression> generateExpressionList(TableInfoDTO tableInfoDTO) {
        List<Expression> columns = new ArrayList<>();
        for (String column : tableInfoDTO.getColumns()) {
            columns.add(Column.aliased(column, tableInfoDTO.getTable(), tableInfoDTO.getColumnPrefix() + "_" + column));
        }
        return columns;
    }

    // Helper method to update the columnsMap for relationships
    private static void addRelationshipColumnsToMap(TableInfoDTO tableInfoDTO, GenericQueryDTO genericQueryDTO, Map<String, List<Expression>> columnsMap, List<Expression> columns) {
        for (RelationshipDTO relationship : tableInfoDTO.getRelationships()) {
            if (relationship.getType().getValue().equals("many-to-one") || relationship.getType().getValue().equals("many-to-many")) {
                addColumnsForRelationship(genericQueryDTO, relationship, columns);
            }
            columnsMap.put(tableInfoDTO.getTable().getName().toString(), columns);
        }
    }

    // Helper method to update the expressions list for a given relationship
    private static void addColumnsForRelationship(GenericQueryDTO genericQueryDTO, RelationshipDTO relationship, List<Expression> columns) {
        for (Map.Entry<String, List<String>> table : genericQueryDTO.getTableColumnsMap().entrySet()) {
            if (table.getKey().equals(relationship.getToTable())) {
                for (String column : table.getValue()) {
                    columns.add(Column.aliased(column, relationship.getToTableObject(), relationship.getToColumnPrefix() + "_" + column));
                }
            }
        }
    }
}