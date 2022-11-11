/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.db.moving.dom.table;

import java.util.List;

/**
 * 引越しするテーブルとそのカラム
 */
public class TableInfo {    
    private final String table;
    private final List<String> colums;

    public TableInfo(String table, List<String> colums) {
        this.table = table;
        this.colums = colums;
    }

    public String getTable() {
        return table;
    }

    public boolean containsColumn(String columnName) {
        return this.getColums().contains(columnName);
    }
    
    public List<String> getColums() {
        return colums;
    }
    
    public String getSelectColumns() {
        return this.colums.stream().reduce((accum, value) -> accum + "," + value).get();
    }
    
    public String createSQL(String contractCode) {
        return String.format("SELECT %s FROM %s WHERE contract_cd = '%s'", getSelectColumns(), table, contractCode);
    }
    
    public String deleteSQL(String contractCode) {
        return String.format("delete FROM %s WHERE contract_cd = '%s'",  table, contractCode);
    }
}
