/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package db.structure.table;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * カラムリポジトリ
 */
public class ColumnsRepository {
    
    private final Connection connnection;

    public ColumnsRepository(Connection connnection) {
        this.connnection = connnection;
    }
    
    public List<ColumnInfo> getColumns(String dbName, String tableName) {
        
        List<ColumnInfo> result = new ArrayList<>();
        
        try {
            DatabaseMetaData metaData = connnection.getMetaData();
            ResultSet rs = metaData.getColumns(dbName, null, tableName, null);
            
            while(rs.next()) {
                ColumnInfo columnInfo = this.toDomain(rs);
                result.add(columnInfo);
            }
            
            return result.stream().sorted(Comparator.comparing(c -> c.getName())).collect(Collectors.toList());
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }
    
    private ColumnInfo toDomain(ResultSet rs) throws SQLException {
        return new ColumnInfo(rs.getString(4), rs.getString(6), rs.getInt(7), rs.getInt(9), rs.getString(18));
    }
}
