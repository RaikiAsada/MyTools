/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package column;

import db.connection.DbConnection;
import db.connection.query.UkResultSet;
import db.connection.schema.ColumnName;
import db.connection.schema.TableName;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * カラムリポジトリ
 */
public class ColumnsRepository {
    
    private final DbConnection connnection;

    public ColumnsRepository(DbConnection connnection) {
        this.connnection = connnection;
    }
    
    public List<ColumnInfo> getColumns(String dbName, TableName tableName) {
        return connnection.getColumnData(dbName, tableName, null, rs -> this.toDomain(rs)).stream().
                sorted(Comparator.comparing(c -> c.getName())).collect(Collectors.toList());
    }
    
    private ColumnInfo toDomain(UkResultSet rs) {
        return new ColumnInfo(new ColumnName(rs.getString(4)), rs.getString(6), rs.getInt(7), rs.getInt(9), rs.getString(18));
    }
}
