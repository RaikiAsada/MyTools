/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pk;

import db.connection.DbConnection;
import db.connection.schema.TableName;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Value;

public class PrimaryKeyRepository {
    private final DbConnection connnection;

    public PrimaryKeyRepository(DbConnection connnection) {
        this.connnection = connnection;
    }
    
    public PrimaryKeyInfo get(TableName tableName) {
        List<Dto> dtos = connnection.getPrimaryKeyData(tableName, rs -> {
            return new Dto(rs.getString(6), rs.getString(4));
        });
        
        return new PrimaryKeyInfo(dtos.get(0).name, dtos.stream().map(d -> d.columnName).sorted().collect(Collectors.toList()));
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
