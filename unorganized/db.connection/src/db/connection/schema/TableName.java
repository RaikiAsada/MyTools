/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package db.connection.schema;

import nts.arc.primitive.StringPrimitiveValue;

/**
 * テーブル名のラッパークラス
 */
public class TableName extends StringPrimitiveValue<TableName>{
    
    public TableName(String rawValue) {
        super(rawValue);
    }
    
    @Override
    public String toString() {
        //すべて大文字に変換する
        return this.v().toUpperCase();
    }
}
