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

public class TableRepository {

    private final Connection connnection;

    public TableRepository(Connection connnection) {
        this.connnection = connnection;
    }

    public List<String> getTableNames(String dbName) {
        List<String> result = new ArrayList<>();

        try {
            DatabaseMetaData metaData = connnection.getMetaData();
            ResultSet rs = metaData.getColumns(dbName, null, null, "contract_cd");

            while (rs.next()) {
                result.add(rs.getString(3));
            }

            return result;
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }
}
