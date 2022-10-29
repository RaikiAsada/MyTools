/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package table;

import column.ColumnInfo;
import db.connection.schema.ColumnName;
import db.connection.schema.TableName;
import java.util.List;


public class BuildSql {
    
    public static String allData(Require require, TableName tableName) {
        return String.format("SELECT %s FROM %s", getSelectColumns(require, tableName), tableName.v());
    }
    
    public static String otherThanInstallData(Require require, TableName tableName) {
        String sqlAllData = allData(require, tableName);
        return sqlAllData.concat(" WHERE contract_cd != '000000000000'");
    }
    
    public static String convertData(Require require, TableName tableName) {
        String sqlAllData = allData(require, tableName);
        return sqlAllData.concat(" WHERE ins_pg = 'CNV001        '");
    }
     
    public static String haveContractCd(Require require, TableName tableName, String contractCd) {
        String sqlAllData = allData(require, tableName);
        return sqlAllData.concat(String.format(" WHERE contract_cd = '%s'", contractCd));
    }
    
    private static String getSelectColumns(Require require, TableName tableName) {
        return require.getColumnInfo(tableName).stream()
                .map(ColumnInfo::getName)
                .map(ColumnName::v)
                .reduce((accum, value) -> accum + "," + value).get();
    }
    
    public interface Require {
         List<ColumnInfo> getColumnInfo(TableName tableName);
    }
}
