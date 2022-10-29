/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package db.connection.schema;

import nts.arc.primitive.StringPrimitiveValue;

/**
 *  カラム名のラッパークラス
 */
public class ColumnName extends StringPrimitiveValue<ColumnName> {

    public ColumnName(String rawValue) {
        super(rawValue);
    }
    
    @Override
    public String toString() {
        //すべて大文字に変換する
        return this.v().toUpperCase();
    }
}
