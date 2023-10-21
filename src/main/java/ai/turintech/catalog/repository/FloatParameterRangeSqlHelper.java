package ai.turintech.catalog.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class FloatParameterRangeSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("is_left_open", table, columnPrefix + "_is_left_open"));
        columns.add(Column.aliased("is_right_open", table, columnPrefix + "_is_right_open"));
        columns.add(Column.aliased("lower", table, columnPrefix + "_lower"));
        columns.add(Column.aliased("upper", table, columnPrefix + "_upper"));

        columns.add(Column.aliased("parameter_type_definition_id", table, columnPrefix + "_parameter_type_definition_id"));
        return columns;
    }
}
