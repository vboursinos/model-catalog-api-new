package ai.turintech.catalog.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class ParameterTypeDefinitionSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("ordering", table, columnPrefix + "_ordering"));

        columns.add(Column.aliased("parameter_distribution_type_id", table, columnPrefix + "_parameter_distribution_type_id"));
        columns.add(Column.aliased("parameter_id", table, columnPrefix + "_parameter_id"));
        columns.add(Column.aliased("parameter_type_id", table, columnPrefix + "_parameter_type_id"));
        return columns;
    }
}
