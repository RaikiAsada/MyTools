package db.connection.query;

import db.connection.DbConnection;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import nts.arc.time.GeneralDate;


import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * INSERT ... SELECT ... 形式のSQLを実行する
 */
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class InsertSelect {

    public static Builder1 insertInto(String tableName) {
        return new Builder1(tableName);
    }

    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Builder1 {
        private final String tableNameInsert;
        public Builder2 selectFrom(String from) {
            return new Builder2(tableNameInsert, from);
        }
        public Builder2 selectFrom(String tableName, String whereContent) {
            return new Builder2(tableNameInsert, tableName + " where " + whereContent);
        }
    }

    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Builder2 {
        private final String tableNameInsert;
        private final String from;
        private final List<Pair> pairs = new ArrayList<>();

        public Builder2 column(String insertColumn, String selectColumn) {
           return expression(insertColumn, selectColumn);
        }

        public Builder2 literal(String insertColumn, String value) {
            return expression(insertColumn, "'" + value + "'");
        }

        public Builder2 literal(String insertColumn, int value) {
            return expression(insertColumn, String.valueOf(value));
        }

        public Builder2 literal(String insertColumn, GeneralDate value) {
            return literal(insertColumn, value.toString("yyyy/MM/dd"));
        }

        public Builder2 expression(String insertColumn, String expression) {
            pairs.add(new Pair(insertColumn, expression));
            return this;
        }

        public Builder2 expression(String insertColumn, String format, Object... args) {
            return expression(insertColumn, String.format(format, args));
        }

        public void execute(DbConnection connection) {
            connection.execute(buildSql());
        }

        private String buildSql() {

            String insertColumns = pairs.stream().map(p -> p.insertColumn).collect(Collectors.joining(","));
            String selectExpressions = pairs.stream().map(p -> p.selectExpression).collect(Collectors.joining(","));

            String template = "insert into {INS_TABLE} ({INS_COLUMNS})" +
                    " select {SELECTS} from {FROM}";

            return template
                    .replace("{INS_TABLE}", tableNameInsert)
                    .replace("{INS_COLUMNS}", insertColumns)
                    .replace("{SELECTS}", selectExpressions)
                    .replace("{FROM}", from);
        }

    }

    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    private static class Pair {
        private final String insertColumn;
        private final String selectExpression;
    }
}
