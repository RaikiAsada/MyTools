/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.db.structure;

import uk.db.structure.table.ColumnInfo;
import uk.db.structure.table.ColumnsRepository;
import uk.db.structure.table.PrimaryKeyInfo;
import uk.db.structure.table.PrimaryKeyRepository;
import uk.db.structure.table.TableRepository;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

public class DbStructure {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String hostAndPort = args[0];
        String dbName = args[1];
        String user = args[2];
        String password = args[3];
        String exportPath = args[4];

        ExportDbStructure.export(Require.initialize(hostAndPort, dbName, user, password), dbName, exportPath);
    }

    private static class Require implements ExportDbStructure.Require {

        private final String dbname;
        private final Connection connaction;

        public static Require initialize(String hostAndPort, String dbName, String user, String password) {
            final String connectionInfo = String.format("jdbc:postgresql://%s/%s", hostAndPort, dbName);

            final String POSTGRES_DRIVER = "org.postgresql.Driver";

            try {
                Class.forName(POSTGRES_DRIVER);
                return new Require(dbName, DriverManager.getConnection(connectionInfo, user, password));
            } catch (ClassNotFoundException | SQLException ex) {
                throw new RuntimeException("データベースへの接続に失敗しました. 理由: " + ex.getMessage());
            }
        }

        public Require(String dbName, Connection connaction) {
            this.dbname = dbName;
            this.connaction = connaction;
        }

        @Override
        public List<String> getTableNames() {
            return new TableRepository(this.connaction).getTableNames(dbname);
        }

        @Override
        public List<ColumnInfo> getColumns(String tableName) {
            return new ColumnsRepository(this.connaction).getColumns(this.dbname, tableName);
        }

        @Override
        public PrimaryKeyInfo getPrimaryKey(String tableName) {
            return new PrimaryKeyRepository(this.connaction).get(tableName);
        }
    }
}
