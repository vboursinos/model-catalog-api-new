package ai.turintech.catalog.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class ParameterSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("name", table, columnPrefix + "_name"));
        columns.add(Column.aliased("label", table, columnPrefix + "_label"));
        columns.add(Column.aliased("description", table, columnPrefix + "_description"));
        columns.add(Column.aliased("enabled", table, columnPrefix + "_enabled"));
        columns.add(Column.aliased("fixed_value", table, columnPrefix + "_fixed_value"));
        columns.add(Column.aliased("ordering", table, columnPrefix + "_ordering"));

        columns.add(Column.aliased("model_id", table, columnPrefix + "_model_id"));
        return columns;
    }
}
