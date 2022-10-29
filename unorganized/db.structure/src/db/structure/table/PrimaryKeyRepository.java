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
import java.util.List;
import java.util.stream.Collectors;
import lombok.Value;

public class PrimaryKeyRepository {
    private final Connection connnection;

    public PrimaryKeyRepository(Connection connnection) {
        this.connnection = connnection;
    }
    
    public PrimaryKeyInfo get(String tableName) {
        List<Dto> dtos = new ArrayList<>();
        
        try {
            DatabaseMetaData metaData = connnection.getMetaData();
            ResultSet rs = metaData.getPrimaryKeys(null, null, tableName);
            
            while(rs.next()) {
                dtos.add(new Dto(rs.getString(6), rs.getString(4)));
            }
            
            return new PrimaryKeyInfo(dtos.get(0).name, dtos.stream().map(d -> d.columnName).sorted().collect(Collectors.toList()));
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }
    
    @Value
    private class Dto {
        private String name;
        private String columnName;

        public Dto(String name, String columnName) {
            this.name = name;
            this.columnName = columnName;
        }
    }
    
}
