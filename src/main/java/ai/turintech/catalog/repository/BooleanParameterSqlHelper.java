package ai.turintech.catalog.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class BooleanParameterSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("default_value", table, columnPrefix + "_default_value"));

        columns.add(Column.aliased("parameter_type_definition_id", table, columnPrefix + "_parameter_type_definition_id"));
        return columns;
    }
}
