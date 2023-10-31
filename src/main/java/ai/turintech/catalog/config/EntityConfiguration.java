package ai.turintech.catalog.config;

import ai.turintech.catalog.anotatation.Columns;
import ai.turintech.catalog.anotatation.Relationship;
import ai.turintech.catalog.domain.*;
import ai.turintech.catalog.service.dto.GenericQueryDTO;
import ai.turintech.catalog.service.dto.RelationshipDTO;
import ai.turintech.catalog.service.dto.TableInfoDTO;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.relational.core.sql.Table;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public GenericQueryDTO scanPackageForTableInfo() {
        GenericQueryDTO genericQueryDTO = new GenericQueryDTO();
        List<TableInfoDTO> tableInfoList = new ArrayList<>();
        Map<String, List<String>> tableColumnMap = new HashMap<>();
        for (Object instance : getInstances()) {
            TableInfoDTO tableInfoDTO = new TableInfoDTO();
            List<String> columnList = new ArrayList<>();
            List<RelationshipDTO> relationshipDTOList = new ArrayList<>();
            String tableName = null;
            if (instance.getClass().isAnnotationPresent(org.springframework.data.relational.core.mapping.Table.class)) {
                tableName = instance.getClass().getAnnotation(org.springframework.data.relational.core.mapping.Table.class).value();
                tableInfoDTO.setTable(Table.aliased(tableName, instance.getClass().getSimpleName()));
                tableInfoDTO.setColumnPrefix(instance.getClass().getSimpleName());
            }
            if (instance.getClass().isAnnotationPresent(Columns.class)) {
                Columns columns = instance.getClass().getAnnotation(Columns.class);
                for (String column : columns.names()) {
                    System.out.println(column);
                    columnList.add(column);
                }
                tableInfoDTO.setColumns(columnList);
                tableColumnMap.put(tableName, columnList);
            }
            for (Field field : instance.getClass().getDeclaredFields()) {
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
            tableInfoList.add(tableInfoDTO);
        }
        genericQueryDTO.setTables(tableInfoList);
        genericQueryDTO.setTableColumnsMap(tableColumnMap);
        return genericQueryDTO;
    }


    private List<Object> getInstances() {
        Model model = new Model();
        MlTaskType mlTaskType = new MlTaskType();
        ModelType modelType = new ModelType();
        ModelEnsembleType modelEnsembleType = new ModelEnsembleType();
        ModelFamilyType modelFamilyType = new ModelFamilyType();
        ModelStructureType modelStructureType = new ModelStructureType();
        ModelGroupType modelGroupType = new ModelGroupType();
        ModelGroupsRel modelGroupsRel = new ModelGroupsRel();
        List<Object> instances = new ArrayList<>();
        instances.add(model);
        instances.add(mlTaskType);
        instances.add(modelType);
        instances.add(modelEnsembleType);
        instances.add(modelFamilyType);
        instances.add(modelStructureType);
        instances.add(modelGroupType);
        instances.add(modelGroupsRel);
        return instances;
    }

}
