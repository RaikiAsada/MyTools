/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package db.connection;

import db.connection.schema.DataTypeDiff;
import java.util.Arrays;
import lombok.Getter;

public enum DBType {
    POSTGRES("p", "jdbc:postgresql://%s/%s", "org.postgresql.Driver", new DataTypeDiff.Postgres()) {
        @Override
        public String convert(String sql) {
            // そのまま
            return sql;
        }
    },
    SQLSERVER("m", "jdbc:sqlserver://%s;databaseName=%s", "com.microsoft.sqlserver.jdbc.SQLServerDriver", new DataTypeDiff.SqlServer()) {
        @Override
        public String convert(String sql) {
            // すべて大文字に変換する
            return sql.toUpperCase();
        }
    };
    
    private final String mark;
    @Getter
    private final String connectionUrlFormat;
    @Getter
    private final String driverClassPath;
    @Getter
    private final DataTypeDiff dataTypeDiff;

    private DBType(String mark, String connectionUrlFormat, String driverClassPath, DataTypeDiff dataTypeDiff) {
        this.mark = mark;
        this.connectionUrlFormat = connectionUrlFormat;
        this.driverClassPath = driverClassPath;
        this.dataTypeDiff = dataTypeDiff;
    }

    public static DBType toType (String mark) {
        return Arrays.asList(DBType.values()).stream()
                .filter(e -> e.mark.equals(mark))
                .findAny().orElseThrow(() -> new RuntimeException("不正なDBシステム p:postgres m:mssqlserver value:" + mark));
    } 
    
    public abstract String convert(String sql);
}
