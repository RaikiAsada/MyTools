/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ukmoving;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import nts.gul.util.Either;
import ukmoving.dom.CheckMovingData;
import ukmoving.dom.ExportDataToCSV;
import ukmoving.dom.ImportDataFromCSV;
import ukmoving.dom.connection.ConnectionInfo;
import ukmoving.dom.directory.FileType;
import ukmoving.dom.directory.InitializeRootDrectory;
import ukmoving.dom.directory.WorkDirectory;
import ukmoving.dom.error.UkMovingError;
import ukmoving.dom.error.UkMovingErrorLog;
import ukmoving.dom.table.TableInfo;
import ukmoving.dom.table.TableLoader;

public class FXMLDocumentController implements Initializable {

    @FXML
    private TextField contractCode;

    @FXML
    private TextField fromHostAndPort;

    @FXML
    private TextField fromDbName;

    @FXML
    private TextField fromDbUser;

    @FXML
    private PasswordField fromPassword;

    @FXML
    private TextField toHostAndPort;

    @FXML
    private TextField toDbName;

    @FXML
    private TextField toDbUser;

    @FXML
    private PasswordField toPassword;

    @FXML
    private TextArea logTextArea;

    @FXML
    private void excute(ActionEvent event) {
        UkMovingErrorLog logger = new UkMovingErrorLog();
        logTextArea.textProperty().bind(logger.textProperty());

        String contract = this.contractCode.getText();
        String dbName = this.fromDbName.getText();
        ConnectionInfo from = new ConnectionInfo(this.fromHostAndPort.getText(), this.fromDbName.getText(), this.fromDbUser.getText(), this.fromPassword.getText());
        ConnectionInfo to = new ConnectionInfo(this.toHostAndPort.getText(), this.toDbName.getText(), this.toDbUser.getText(), this.toPassword.getText());

        Executor executor = Executors.newSingleThreadExecutor();
        InitializeRootDrectory.initialize().ifRight(rootDirectory -> {
            executor.execute(this.getExcuteTask(from, to, contract, dbName, rootDirectory, logger));
        }).ifLeft(error -> logger.log(error));
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    private Task<Void> getExcuteTask(ConnectionInfo from, ConnectionInfo to, String contract, String dbName, String rootDirectory, UkMovingErrorLog logger) {
        return new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                try {
                    Map<String, UkMovingError> errorTables = new HashMap<>();

                    Require.initialize(from, to, rootDirectory).ifRight(require -> {
                        List<TableInfo> tables = TableLoader.load(require, dbName);
                        tables.forEach(table -> {
                            movingTable(require, contract, table, logger)
                                    .ifRight(result -> logger.successMoving(table))
                                    .ifLeft(error -> {
                                        errorTables.put(table.getTable(), error);
                                        logger.log(error);
                                    });
                        });

                        if (errorTables.isEmpty()) {
                            InitializeRootDrectory.end(rootDirectory).ifLeft(error -> logger.log(error));
                            tables.forEach(table -> deleteTable(require, table, contract));
                            logger.endMoving();
                        } else {
                            exportDeleteSql(contract, rootDirectory, tables);
                            logger.failMoving(errorTables);
                        }

                    }).ifLeft(error -> logger.log(error));
                } catch (Exception e) {
                    StringWriter sw = new StringWriter();
                    PrintWriter pw = new PrintWriter(sw);
                    e.printStackTrace(pw);
                    logger.log(sw.toString());
                }
                return null;
            }
        };
    }

    private Either<UkMovingError, Void> movingTable(Require require, String contract, TableInfo table, UkMovingErrorLog logger) {
        return ExportDataToCSV.exportFrom(require, contract, table).mapEither(exmported -> {
            logger.successCopyOut(table);
            return ImportDataFromCSV.input(require, table).mapEither(imported -> {
                logger.successCopyIn(table);
                return CheckMovingData.check(require, contract, table);
            });
        });
    }

    private void deleteTable(Require require, TableInfo table, String contractCode) {
        try {
            require.getFromConnection().createStatement().executeUpdate(table.deleteSQL(contractCode));
        } catch (SQLException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void exportDeleteSql(String contract, String rootDirectory, List<TableInfo> tables) {
        try {
            FileWriter file = new FileWriter(rootDirectory + "\\delete_sql_contract.sql");
            PrintWriter pw = new PrintWriter(new BufferedWriter(file));
            tables.forEach(table -> pw.println(table.deleteSQL(contract) + ";"));
            pw.close();
        } catch (IOException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static class Require implements TableLoader.Require, ExportDataToCSV.RequireFrom, ImportDataFromCSV.Require, CheckMovingData.Require {

        private final Connection from;
        private final Connection to;
        private final String rootDirectory;
        private final Map<String, WorkDirectory> directoys;

        public static Either<UkMovingError, Require> initialize(ConnectionInfo fromInfo, ConnectionInfo toInfo, String rootDirectory) {
            Either<UkMovingError, Connection> from = fromInfo.initialize();
            Either<UkMovingError, Connection> to = toInfo.initialize();

            return from.mapEither(f -> to.map(t -> new Require(f, t, rootDirectory)));
        }

        public Require(Connection from, Connection to, String rootDirectory) {
            this.from = from;
            this.to = to;
            this.rootDirectory = rootDirectory;
            this.directoys = new HashMap<>();
        }

        @Override
        public Connection getFromConnection() {
            return this.from;
        }

        @Override
        public Connection getToConnection() {
            return this.to;
        }

        @Override
        public Either<UkMovingError, Writer> getWriter(String tableName, FileType fileType) {
            if (!directoys.containsKey(tableName)) {
                WorkDirectory.initialize(this.rootDirectory, tableName)
                        .ifRight(directory -> this.directoys.put(tableName, directory));
            }

            return this.directoys.get(tableName).initializeWriter(fileType);
        }

        @Override
        public Either<UkMovingError, BufferedReader> getReader(String tableName, FileType fileType) {
            if (!directoys.containsKey(tableName)) {
                WorkDirectory.initialize(this.rootDirectory, tableName)
                        .ifRight(directory -> this.directoys.put(tableName, directory));
            }

            return this.directoys.get(tableName).initializeReader(fileType);
        }

        @Override
        public Either<UkMovingError, Long> getFileSize(String tableName, FileType fileType) {
            if (!directoys.containsKey(tableName)) {
                WorkDirectory.initialize(this.rootDirectory, tableName)
                        .ifRight(directory -> this.directoys.put(tableName, directory));
            }

            return this.directoys.get(tableName).getFileSize(fileType);
        }

    }

}
