package ai.turintech.catalog.config;

import ai.turintech.catalog.anotatation.Relationship;
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
    public GenericQueryDTO scanPackageForTableInfo() {
        GenericQueryDTO genericQueryDTO = new GenericQueryDTO();
        List<TableInfoDTO> tableInfoList = new ArrayList<>();

        Map<String, List<String>> tableColumnMap = new HashMap<>();
        Reflections reflections = new Reflections("ai.turintech.catalog.domain");

        Set<Class<?>> annotated = reflections.getTypesAnnotatedWith(org.springframework.data.relational.core.mapping.Table.class);
        for (Class<?> clazz : annotated) {
            if (clazz.isAnnotationPresent(org.springframework.data.relational.core.mapping.Table.class)) {
                String tableName = null;
                TableInfoDTO tableInfoDTO = new TableInfoDTO();
                if (clazz.isAnnotationPresent(org.springframework.data.relational.core.mapping.Table.class)) {
                    tableName = clazz.getAnnotation(org.springframework.data.relational.core.mapping.Table.class).value();
                    tableInfoDTO.setTable(Table.aliased(tableName, clazz.getSimpleName()));
                    tableInfoDTO.setColumnPrefix(clazz.getSimpleName());
                }
                List<String> columnList = new ArrayList<>();
                List<RelationshipDTO> relationshipDTOList = new ArrayList();

                for (Field field : clazz.getDeclaredFields()) {
                    org.springframework.data.relational.core.mapping.Column column = field.getAnnotation(org.springframework.data.relational.core.mapping.Column.class);
                    if (column != null) {
                        columnList.add(column.value());
                    }
                    Relationship relationship = field.getAnnotation(Relationship.class);
                    if (relationship != null) {
                        Table tableObj = Table.aliased(relationship.toTable(), relationship.toColumnPrefix());
                        RelationshipDTO relationshipDTO = new RelationshipDTO(relationship.type(), relationship.toTable(), tableObj, relationship.fromColumn(), relationship.toColumn(), relationship.toColumnPrefix());
                        relationshipDTOList.add(relationshipDTO);
                    }
                }
                tableInfoDTO.setColumns(columnList);
                tableColumnMap.put(tableName, columnList);

                tableInfoDTO.setRelationships(relationshipDTOList);
                tableInfoList.add(tableInfoDTO);
            }
        }
        genericQueryDTO.setTables(tableInfoList);
        genericQueryDTO.setTableColumnsMap(tableColumnMap);
        return genericQueryDTO;
    }
}
