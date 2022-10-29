/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ukmoving.dom;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import nts.gul.util.Either;
import ukmoving.dom.table.TableInfo;
import org.postgresql.copy.CopyManager;
import org.postgresql.core.BaseConnection;
import ukmoving.dom.directory.FileType;
import ukmoving.dom.error.UkMovingError;

/**
 * データをCSVから受け入れる
 *
 * @author raiki_asada
 */
public class ImportDataFromCSV {

    public static Either<UkMovingError, Void> input(Require require, TableInfo table) {
        return require.getReader(table.getTable(), FileType.FROM).mapEither(reader -> {
            try {
                CopyManager copyManger = new CopyManager((BaseConnection) require.getToConnection());
                copyManger.copyIn("COPY " + table.getTable() + "(" + table.getSelectColumns() + ")" + "FROM STDIN WITH CSV HEADER DELIMITER ','", reader);
                return Either.rightVoid();
            } catch (SQLException | IOException ex) {
                return Either.left(new UkMovingError("COPY IN が失敗しました。　" + ex.getMessage()));
            } finally {
                try {
                    reader.close();
                } catch (IOException ex) {
                    Logger.getLogger(ExportDataToCSV.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    public interface Require {

        Connection getToConnection();

        Either<UkMovingError, BufferedReader> getReader(String tableName, FileType type);
    }
}
