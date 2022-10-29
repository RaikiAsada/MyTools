/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.db.constrainer.rules;

import column.ColumnInfo;
import db.connection.schema.ColumnName;
import db.connection.schema.DataTypeDiff;
import java.util.function.BiPredicate;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import lombok.Getter;

/**
 * 制約の種類
 */
public enum ConstrainRules {
    UUID("UUID検証", "char/nchar(36)のカラムにUUID形式でない値が入っています", (require, column) -> {
        boolean equalDataType = column.getDataType().equals(require.getDataTypeDiff().charType())
                || column.getDataType().equals(require.getDataTypeDiff().ncharType());
        
        return equalDataType && column.getColumnSize() == 36;
    }, "([0-9a-f]{8})-([0-9a-f]{4})-([0-9a-f]{4})-([0-9a-f]{4})-([0-9a-f]{12})"),
    
    CID("CID検証", "CID形式でない値が入っています", (require, column) -> {
        return column.getName().toString().equals(new ColumnName("CID").toString());
    }, "([0-9]{12})-([0-9]{4})");
    
    private final String name;
    @Getter
    private final String message;
    @Getter
    private final BiPredicate<Require, ColumnInfo> columnChecker;
    private final String regex;

    private ConstrainRules(String name, String message, BiPredicate<Require, ColumnInfo> columnChecker, String regex) {
        this.name = name;
        this.message = message;
        this.columnChecker = columnChecker;
        this.regex = regex;
    }
    
    public Predicate<String> getCheckPattern() {
        Pattern pattern = this.getPattern();
        
        return (value) -> {
            //Char型の場合、スペースが入るので
            String targetData = value.trim();
            return pattern .matcher(targetData).matches();
        };
    }
    
    public Pattern getPattern() {
        return Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
    }
    
    public interface Require {
        DataTypeDiff getDataTypeDiff();
    }
}
