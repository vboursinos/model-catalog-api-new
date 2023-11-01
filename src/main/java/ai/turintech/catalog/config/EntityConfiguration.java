package ai.turintech.catalog.config;

import ai.turintech.catalog.anotatation.Columns;
import ai.turintech.catalog.anotatation.Relationship;
import ai.turintech.catalog.domain.Model;
import ai.turintech.catalog.service.dto.GenericQueryDTO;
import ai.turintech.catalog.service.dto.RelationshipDTO;
import ai.turintech.catalog.service.dto.TableInfoDTO;
import org.reflections.Reflections;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.relational.core.sql.Table;

import java.lang.reflect.Field;
import java.util.*;

@Configuration
public class EntityConfiguration {

    @Bean
    public TableInfoDTO getTableInfo() {
        TableInfoDTO tableInfoDTO = new TableInfoDTO();
        Model model = new Model();
        List<String> columnList = new ArrayList<>();
        List<RelationshipDTO> relationshipDTOList = new ArrayList<>();
        if (model.getClass().isAnnotationPresent(Columns.class)) {
            Columns columns = model.getClass().getAnnotation(Columns.class);
            for (String column : columns.names()) {
                System.out.println(column);
                columnList.add(column);
            }
        }
        tableInfoDTO.setColumns(columnList);
        for (Field field : model.getClass().getDeclaredFields()) {
            System.out.println(field.getName());
            Relationship relationship = field.getAnnotation(Relationship.class);
            if (relationship == null) {
                continue;
            }
            Table tableObj = Table.aliased(relationship.toTable(), relationship.toColumnPrefix());
            RelationshipDTO relationshipDTO = new RelationshipDTO(relationship.type(), relationship.toTable(), tableObj, relationship.fromColumn(), relationship.toColumn(), relationship.toColumnPrefix());
            relationshipDTOList.add(relationshipDTO);
        }
        tableInfoDTO.setRelationships(relationshipDTOList);
        return tableInfoDTO;
    }

    @Bean
    public GenericQueryDTO scanPackageForTableInfo2() {
        GenericQueryDTO genericQueryDTO = new GenericQueryDTO();
        List<TableInfoDTO> tableInfoList = new ArrayList<>();

        Map<String, List<String>> tableColumnMap = new HashMap<>();
        Reflections reflections = new Reflections("ai.turintech.catalog.domain");

        Set<Class<?>> annotated = reflections.getTypesAnnotatedWith(Columns.class);
        for (Class<?> clazz : annotated) {
            if (clazz.isAnnotationPresent(Columns.class)) {
                String tableName = null;
                TableInfoDTO tableInfoDTO = new TableInfoDTO();
                if (clazz.isAnnotationPresent(org.springframework.data.relational.core.mapping.Table.class)) {
                    tableName = clazz.getAnnotation(org.springframework.data.relational.core.mapping.Table.class).value();
                    tableInfoDTO.setTable(Table.aliased(tableName, clazz.getSimpleName()));
                    tableInfoDTO.setColumnPrefix(clazz.getSimpleName());
                }
                Columns columns = clazz.getAnnotation(Columns.class);
                List<String> columnList = new ArrayList<>();
                List<RelationshipDTO> relationshipDTOList = new ArrayList();

                for (String column : columns.names()) {
                    columnList.add(column);
                }

                tableInfoDTO.setColumns(columnList);
                tableColumnMap.put(tableName, columnList);

                for (Field field : clazz.getDeclaredFields()) {
                    Relationship relationship = field.getAnnotation(Relationship.class);
                    if (relationship != null) {
                        Table tableObj = Table.aliased(relationship.toTable(), relationship.toColumnPrefix());
                        RelationshipDTO relationshipDTO = new RelationshipDTO(relationship.type(), relationship.toTable(), tableObj, relationship.fromColumn(), relationship.toColumn(), relationship.toColumnPrefix());
                        relationshipDTOList.add(relationshipDTO);
                    }
                }

                tableInfoDTO.setRelationships(relationshipDTOList);
                tableInfoList.add(tableInfoDTO);
            }
        }
        genericQueryDTO.setTables(tableInfoList);
        genericQueryDTO.setTableColumnsMap(tableColumnMap);
        return genericQueryDTO;
    }
}
