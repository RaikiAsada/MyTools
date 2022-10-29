/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ukmoving.dom.table;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 引越し対象テーブルを読み込む
 */
public class TableLoader {
        
    public static List<TableInfo> load(Require require, String dbName) {
        Connection connnection = require.getFromConnection();
        List<Dto> dtos = new ArrayList<>();
        
        try {
            DatabaseMetaData metaData = connnection.getMetaData();
            ResultSet rs = metaData.getColumns(dbName, null, null, null);
            
            while(rs.next()) {
                Dto dto = new Dto(rs.getString(3), rs.getString(4));
                dtos.add(dto);
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        
        return dtos.stream().collect(Collectors.groupingBy(dto -> dto.tableName))
                .entrySet().stream().map(entry -> new TableInfo(entry.getKey(), entry.getValue().stream().map(dto -> dto.columnName).collect(Collectors.toList())))
                .sorted(Comparator.comparing(c -> c.getTable()))
                .filter(table -> table.getTable().matches("[0-9a-zA-Z\\_]+"))
                .filter(table -> table.containsColumn("contract_cd"))
                .collect(Collectors.toList());
    }
    
    public interface Require {
        Connection getFromConnection();
    }
    
    private static class Dto {
        public String tableName;
        public String columnName;

        public Dto(String tableName, String columnName) {
            this.tableName = tableName;
            this.columnName = columnName;
        }
    }    
}
