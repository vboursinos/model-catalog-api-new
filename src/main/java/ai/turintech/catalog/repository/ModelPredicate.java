package ai.turintech.catalog.repository;

import ai.turintech.catalog.service.dto.SearchDTO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.relational.core.sql.*;

public class ModelPredicate {
    private SearchDTO searchDTO;
    private Table entityTable;

    public ModelPredicate(final SearchDTO searchDTO, Table entityTable) {
        this.searchDTO = searchDTO;
        this.entityTable = entityTable;
    }

    public Object getPredicate() {
        Object whereClause = null;


        if (isNumeric(searchDTO.getValue().toString())) {
            final int value = Integer.parseInt(searchDTO.getValue().toString());
            switch (searchDTO.getOperation()) {
                case ":":
                    whereClause = Conditions.isEqual(entityTable.column(searchDTO.getKey()), Conditions.just(String.valueOf(value)));
                    return whereClause;
                case ">":
                    whereClause = Conditions.isGreater(entityTable.column(searchDTO.getKey()), Conditions.just(String.valueOf(value)));
                    return whereClause;
                case "<":
                    whereClause = Conditions.isLess(entityTable.column(searchDTO.getKey()), Conditions.just(String.valueOf(value)));
                    return whereClause;
            }
        } else {
            if (searchDTO.getOperation().equalsIgnoreCase(":")) {
                Like whereLikeClause = Conditions.like(entityTable.column(searchDTO.getKey()), Conditions.just(StringUtils.wrap("%" + searchDTO.getValue() + "%", "'")));
                return whereLikeClause;
            }
        }
        return null;
    }

    public static boolean isNumeric(String value) {
        try {
            Integer.parseInt(value);
        } catch (final NumberFormatException e) {
            return false;
        }
        return true;
    }
}
