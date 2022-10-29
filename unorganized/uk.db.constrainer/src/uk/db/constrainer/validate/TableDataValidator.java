/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.db.constrainer.validate;

import db.connection.query.UkResultSet;
import db.connection.schema.ColumnName;
import lombok.Value;
import uk.db.constrainer.rules.ConstrainRules;

/**
 * 対象のカラムを対象のルールで検証する
 */
@Value
public class TableDataValidator {
    private ColumnName columnName;
    private ConstrainRules rules;

    public boolean check(UkResultSet source) {
        String targetData = source.getString(columnName.v());
        
        if(targetData == null) {
            return false;
        }
        
        return !rules.getCheckPattern().test(targetData);
    }
}
