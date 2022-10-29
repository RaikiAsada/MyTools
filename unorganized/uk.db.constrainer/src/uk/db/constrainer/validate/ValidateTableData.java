/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.db.constrainer.validate;

import db.connection.query.UkResultSet;
import db.connection.schema.TableName;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Value;
import table.BuildSql;
import uk.db.constrainer.rules.ConstrainRules;


public class ValidateTableData {

    public static List<Result> validate(Require require, TableName tableName, List<ConstrainRules> rules) {
        List<TableDataValidator> validators = rules.stream().flatMap(rule -> {
            return require.getColumnInfo(tableName).stream()
                    .filter(column -> rule.getColumnChecker().test(require, column))
                    .map(column -> new TableDataValidator(column.getName(), rule));
        }).collect(Collectors.toList());

        //コンバートデータだけを対象とする
        String SQL = BuildSql.convertData(require, tableName);
        
        UkResultSet resultSet = require.getUkResultSet(SQL);
        
        while(resultSet.next()) {
            List<Result> result = validators.stream()
                .filter(validator -> validator.check(resultSet))
                .map(validator -> {
                    String sampleValue = resultSet.getString(validator.getColumnName().v());
                    return new Result(tableName.toString(), validator.getColumnName().toString(), validator.getRules(), sampleValue);
                }).collect(Collectors.toList());
            
            if(!result.isEmpty()) {
                return result;
            }
        }
        
        return Collections.emptyList();
    }

    public interface Require extends BuildSql.Require, ConstrainRules.Require {
        UkResultSet getUkResultSet(String SQL);
    }

    @Value
    public static class Result {
        private final String tableName;
        private final String columnName;
        private final ConstrainRules rules;
        private final String sampleValue;

        public Result(String tableName, String columnName, ConstrainRules rules, String sampleValue) {
            this.tableName = tableName;
            this.columnName = columnName;
            this.rules = rules;
            this.sampleValue = sampleValue;
        }
    }
}
