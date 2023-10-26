package ai.turintech.catalog.repository;

import ai.turintech.catalog.service.dto.SearchDTO;
import org.springframework.data.relational.core.sql.Condition;
import org.springframework.data.relational.core.sql.Conditions;
import org.springframework.data.relational.core.sql.Table;

import java.util.List;
import java.util.stream.Collectors;

public final class ModelPredicatesCreator {
    private final List<SearchDTO> params;
    private Table entityTable;

    public ModelPredicatesCreator(List<SearchDTO> params, Table entityTable) {
        this.params = params;
        this.entityTable = entityTable;
    }

    public Condition create() {
        Condition combinedConditions = Conditions.just(String.valueOf(true));
        if (params.size() > 0) {
            Condition finalCombinedConditions = combinedConditions;
            final List<Object> predicates = params.stream().map(param -> {
                ModelPredicate predicate = new ModelPredicate(param, entityTable);
                return finalCombinedConditions.and((Condition) predicate.getPredicate());
            }).collect(Collectors.toList());
            for (Object predicate : predicates) {
                combinedConditions = combinedConditions.and((Condition) predicate);
            }
        }
        return combinedConditions;
    }
}
