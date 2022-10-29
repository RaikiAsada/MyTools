/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ukmoving.dom;

import java.io.IOException;
import java.io.Writer;
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
 * データをCSVに出力する
 */
public class ExportDataToCSV {

    public static Either<UkMovingError, Void> exportFrom(RequireFrom require, String contractCode, TableInfo table) {
        return require.getWriterForFrom(table.getTable()).mapEither(writer -> {
            return export(contractCode, table, require.getFromConnection(), writer);
        });
    }

    public static Either<UkMovingError, Void> exportTo(RequireTo require, String contractCode, TableInfo table) {
        return require.getWriterForTo(table.getTable()).mapEither(writer -> {
            return export(contractCode, table, require.getToConnection(), writer);
        });
    }

    private static Either<UkMovingError, Void> export(String contractCode, TableInfo table, Connection connection, Writer writer) {
        try {
            CopyManager copyManger = new CopyManager((BaseConnection) connection);
            copyManger.copyOut("COPY (" + table.createSQL(contractCode) + ") TO STDOUT WITH CSV HEADER", writer);
            return Either.rightVoid();
        } catch (SQLException | IOException ex) {
            return Either.left(new UkMovingError("COPY OUT失敗 テーブル名: " + table.getTable() + " 理由: " + ex.getMessage()));
        } finally {
            try {
                writer.close();
            } catch (IOException ex) {
                Logger.getLogger(ExportDataToCSV.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public interface RequireFrom extends RequireBase {

        Connection getFromConnection();

        default Either<UkMovingError, Writer> getWriterForFrom(String tableName) {
            return this.getWriter(tableName, FileType.FROM);
        }
    }

    public interface RequireTo extends RequireBase {

        Connection getToConnection();

        default Either<UkMovingError, Writer> getWriterForTo(String tableName) {
            return this.getWriter(tableName, FileType.TO);
        }
    }

    public interface RequireBase {

        Either<UkMovingError, Writer> getWriter(String tableName, FileType fileType);
    }
}
