/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ukmoving.dom.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import nts.gul.util.Either;
import ukmoving.dom.error.UkMovingError;

/**
 * DBへの接続情報
 * 現状対応するのはPostgresのみ
 */
public class ConnectionInfo {
    private final String url;
    private final String user;
    private final String password;

    public ConnectionInfo(String hostAndPost, String dbName, String user, String password) {
        this.url = String.format("jdbc:postgresql://%s/%s", hostAndPost, dbName);
        this.user = user;
        this.password = password;
    }
    
    public Either<UkMovingError, Connection> initialize() {
        final String POSTGRES_DRIVER = "org.postgresql.Driver";
        
        try {
            Class.forName(POSTGRES_DRIVER);
            return Either.right(DriverManager.getConnection(url, user, password));
        } catch (ClassNotFoundException | SQLException ex) {
            return Either.left(new UkMovingError("データベースへの接続に失敗しました. 理由: " + ex.getMessage()));
        } 
    }
}
