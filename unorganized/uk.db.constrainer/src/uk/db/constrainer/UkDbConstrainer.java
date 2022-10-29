/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.db.constrainer;

import column.ColumnInfo;
import column.ColumnsRepository;
import db.connection.DBType;
import db.connection.DbConnection;
import db.connection.query.UkResultSet;
import db.connection.schema.DataTypeDiff;
import db.connection.schema.TableName;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import table.BuildSql;
import table.TableRepository;
import uk.db.constrainer.export.ExportValidateResult;
import uk.db.constrainer.rules.ConstrainRules;
import uk.db.constrainer.validate.ValidateTableData;

public class UkDbConstrainer {
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        DBType dbType = DBType.toType(args[0]);
        String hostAndPort = args[1];
        String dbName = args[2];
        String user = args[3];
        String password = args[4];
        String exportPath = args[5];
        
        DbConnection connection = DbConnection.init(dbType, hostAndPort, dbName, user, password);
        Require require = new Require(dbType, dbName, connection);
        
        List<ValidateTableData.Result> results = new ArrayList<>();
        
        new TableRepository(connection).getTablesHaveContractCd(dbName).forEach(tableName -> {
            results.addAll(ValidateTableData.validate(require, tableName, Arrays.asList(ConstrainRules.values())));
        });
        
        ExportValidateResult.export(dbName, exportPath, results);
    }
    
    public static class Require implements BuildSql.Require, ValidateTableData.Require {
        private final Map<TableName, List<ColumnInfo>> columnInfo = new HashMap<>();
        private final DBType dbType;
        private final String dbname;
        private final DbConnection connection;
        private final ColumnsRepository columnsRepository;

        public Require(DBType dbType, String dbname, DbConnection connection) {
            this.dbType = dbType;
            this.dbname = dbname;
            this.connection = connection;
            this.columnsRepository = new ColumnsRepository(connection);
        }

        @Override
        public List<ColumnInfo> getColumnInfo(TableName tableName) {
            if(!columnInfo.containsKey(tableName)) {
                this.columnInfo.put(tableName, columnsRepository.getColumns(dbname, tableName));
            }
            
            return this.columnInfo.get(tableName);
        }

        @Override
        public UkResultSet getUkResultSet(String SQL) {
            return this.connection.getResultSet(SQL);
        }

        @Override
        public DataTypeDiff getDataTypeDiff() {
            return dbType.getDataTypeDiff();
        }
    }
}
