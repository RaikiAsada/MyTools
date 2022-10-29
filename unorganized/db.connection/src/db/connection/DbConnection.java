package db.connection;

import db.connection.log.LogManager;
import db.connection.query.UkResultSet;
import db.connection.query.UkStatement;
import db.connection.schema.ColumnName;
import db.connection.schema.TableName;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;


public class DbConnection {
    private final String jdbcDriverName;
    private final String connectionString;
    private final String userName;
    private final String password;
    private final DBType dbType;
    private Connection conn;

    private DbConnection(String jdbcDriverName, String connectionStringKey, String userName, String password, DBType dbType) {
        this.jdbcDriverName = jdbcDriverName;
        this.connectionString = connectionStringKey;
        this.userName = userName;
        this.password = password;
        this.dbType = dbType;
        loadJdbc();
    }
    
    public static DbConnection init(DBType type, String hostAndPort, String dbName, String userName, String password) {
        String connectionUrl = String.format(type.getConnectionUrlFormat(), hostAndPort, dbName);
        return new DbConnection(type.getDriverClassPath(), connectionUrl, userName, password, type);
    }
        
    private void loadJdbc() {
        try {
            Class.forName(this.jdbcDriverName);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("DB接続設定が無効です。:" + this.jdbcDriverName, e);
        }
    }

    private void connect() {
        try {
            this.conn = DriverManager.getConnection(this.connectionString, this.userName, this.password);
        } catch (SQLException e) {
            throw new RuntimeException("DB接続に失敗しました。", e);
        }
    }

    private void disConnect() {
        try {
            if (this.conn == null || this.conn.isClosed()) {
                return;
            }
            this.conn.close();
        } catch (SQLException e) {
            throw new RuntimeException("DB接続の切断に失敗しました。", e);
        }
    }

    public Connection connection() {
        try {
            if (this.conn == null || this.conn.isClosed()) {
                this.connect();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return this.conn;
    }

    public <T> Optional<T> getSingle(PreparedStatement ps, SelectRequire<T> require) {
        return getList(ps, require).stream().findFirst();
    }

    public <T> Optional<T> getSingle(String sql, SelectRequire<T> require) {
        return getList(sql, require).stream().findFirst();
    }

    public <T> List<T> getList(PreparedStatement ps, SelectRequire<T> require) {
        List<T> result = new ArrayList<>();
        ResultSet rset = null;
        try {
            if (conn == null || conn.isClosed()) {
                this.connect();
            }

            rset = ps.executeQuery();
            while (rset.next()) {

                T domain = require.toEntity(new UkResultSet(this.dbType, rset));
                result.add(domain);
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        } finally {
            if (rset != null) {
                try {
                    rset.close();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }

        return result;
    }

    public <T> List<T> getList(String sql, SelectRequire<T> require) {

        List<T> result = new ArrayList<>();
        Statement stmt;
        ResultSet rset = null;
        try {
            if (conn == null || conn.isClosed()) {
                this.connect();
            }

            stmt = conn.createStatement();
            rset = stmt.executeQuery(sql);
            while (rset.next()) {

                T domain = require.toEntity(new UkResultSet(this.dbType, rset));
                result.add(domain);
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        } finally {
            if (rset != null) {
                try {
                    rset.close();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }

        return result;
    }

    public UkResultSet getResultSet(String sql) {

        Statement stmt;
        ResultSet rset = null;
        try {
            if (conn == null || conn.isClosed()) {
                this.connect();
            }

            stmt = conn.createStatement();
            rset = stmt.executeQuery(this.dbType.convert(sql));
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }

        return new UkResultSet(this.dbType, rset);
    }

    public void getForEach(PreparedStatement ps, Consumer<UkResultSet> consumer) {
        ResultSet rset = null;
        try {
            if (conn == null || conn.isClosed()) {
                this.connect();
            }

            rset = ps.executeQuery();
            while (rset.next()) {
                consumer.accept(new UkResultSet(this.dbType, rset));
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        } finally {
            if (rset != null) {
                try {
                    rset.close();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
    }

    public void getForEach(String sql, Consumer<UkResultSet> consumer) {
        Statement stmt;
        ResultSet rset = null;
        try {
            if (conn == null || conn.isClosed()) {
                this.connect();
            }

            stmt = conn.createStatement();
            rset = stmt.executeQuery(sql);
            while (rset.next()) {
                consumer.accept(new UkResultSet(this.dbType, rset));
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        } finally {
            if (rset != null) {
                try {
                    rset.close();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
    }
        
    public <T> List<T> getColumnData(String dbName, TableName tableName, ColumnName columnName, SelectRequire<T> require) {
        return this.getList((conn) -> {
            DatabaseMetaData metaData = conn.getMetaData();
            
            String table = tableName == null? null: this.dbType.convert(tableName.v());
            String column = columnName == null? null: this.dbType.convert(columnName.v());
            
            return metaData.getColumns(dbName, null, table, column);
        }, require);
    }
    
    public <T> List<T> getPrimaryKeyData(TableName tableName, SelectRequire<T> require) {
        return this.getList((conn) -> {
            DatabaseMetaData metaData = conn.getMetaData();
            return metaData.getPrimaryKeys(null, null, this.dbType.convert(tableName.v()));
        }, require);
    }
    
    public <T> void insert(InsertRequire require) {
        execute(require::insert);
    }

    public <T> void delete(DeleteRequire require) {
        execute(require::delete);
    }

    public <T> void truncateTable(TruncateTableRequire require) {
        execute(require::truncateTable);
    }

    public boolean existsTable(String tableName) {
        try {
            if (conn == null || conn.isClosed()) {
                this.connect();
            }
            DatabaseMetaData dbm = conn.getMetaData();
            ResultSet tables = dbm.getTables(null, null, tableName, null);
            return tables.next();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public <T> void execute(String query) {
        execute(conn -> conn.prepareStatement(query));
    }

    public <T> void execute(ExecuteRequire require) {
        PreparedStatement ps = null;
        try {
            if (conn == null || conn.isClosed()) {
                this.connect();
            }

            conn.setAutoCommit(false);
            ps = require.execute(conn);
            ps.executeUpdate();
        } catch (SQLException ex) {
            try {
                conn.rollback();
            } catch (SQLException e) {
                LogManager.err(e);
                throw new RuntimeException(ex);
            }
            throw new RuntimeException(ex);
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
    
    private <T> List<T> getList(ResultSetCreator resultSetCreator, SelectRequire<T> require) {
        List<T> result = new ArrayList<>();
        ResultSet rs = null;
        try {
            if (conn == null || conn.isClosed()) {
                this.connect();
            }
            
            rs = resultSetCreator.create(conn);
            
            while(rs.next()) {
                T pkInfo = require.toEntity(new UkResultSet(this.dbType, rs));
                result.add(pkInfo);
            }
            
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
        
        return result;
    }
    
    public interface ResultSetCreator {
        ResultSet create(Connection conn) throws SQLException;
    }
    

    public interface SelectRequire<T> {

        T toEntity(UkResultSet rs);
    }

    public interface InsertRequire {

        UkStatement insert(UkConnection conn);

        default PreparedStatement insert(Connection conn) {
            return insert(new UkConnection(conn)).getPs();
        }
    }

    public interface DeleteRequire {

        UkStatement delete(UkConnection conn);

        default PreparedStatement delete(Connection conn) {
            return delete(new UkConnection(conn)).getPs();
        }
    }

    public interface TruncateTableRequire {

        UkStatement truncateTable(UkConnection conn);

        default PreparedStatement truncateTable(Connection conn) {
            return truncateTable(new UkConnection(conn)).getPs();
        }
    }

    public interface ExecuteRequire {

        UkStatement execute(UkConnection conn);

        default PreparedStatement execute(Connection conn) {
            return execute(new UkConnection(conn)).getPs();
        }
    }
}
