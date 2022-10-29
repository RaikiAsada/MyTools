/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package table;

import db.connection.DbConnection;
import db.connection.schema.ColumnName;
import db.connection.schema.TableName;
import java.util.List;
import java.util.stream.Collectors;

public class TableRepository {

    private final DbConnection connnection;

    public TableRepository(DbConnection connnection) {
        this.connnection = connnection;
    }

    /**
     * 契約コードカラムを持つテーブルを取得する
     * @param dbName　メタデータを取得するDB名
     * @return 
     */
    public List<TableName> getTablesHaveContractCd(String dbName) {
        return connnection.getColumnData(dbName, null, new ColumnName("contract_cd"), rs -> rs.getString(3)).stream().
                sorted().map(TableName::new).collect(Collectors.toList());
    }
}
