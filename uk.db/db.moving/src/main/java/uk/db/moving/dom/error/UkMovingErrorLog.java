/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.db.moving.dom.error;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.Map;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.ReadOnlyStringWrapper;
import uk.db.moving.dom.table.TableInfo;

/**
 * エラーをためるログ
 */
public class UkMovingErrorLog {

    private final ReadOnlyStringWrapper logText = new ReadOnlyStringWrapper();

    public final String getText() {
        return textProperty().get();
    }

    public ReadOnlyStringProperty textProperty() {
        return logText.getReadOnlyProperty();
    }

    public void successCopyOut(TableInfo table) {
        this.log("COPY OUT成功　テーブル名： " + table.getTable());
    }

    public void successCopyIn(TableInfo table) {
        this.log("COPY IN成功　テーブル名： " + table.getTable());
    }

    public void successMoving(TableInfo table) {
        this.log("引越し成功　テーブル名： " + table.getTable());
    }

    public void endMoving() {
        this.log("uk.moving 終了");
    }

    public void failMoving(Map<String, UkMovingError> errorTables) {
        String errorEndLog = errorTables.entrySet().stream()
                .sorted(Comparator.comparing(entry -> entry.getKey()))
                .map(entry -> "テーブル名: " + entry.getKey() + "　エラー内容: " + entry.getValue().getMessage())
                .reduce((accum, value) -> {
                    return accum + "\n" + value;
                }).get();

        this.log("uk.moving 一部異常終了" + "\n" + errorEndLog);
    }

    public void log(String message) {
        Platform.runLater(() -> {
            LocalDateTime date1 = LocalDateTime.now();
            DateTimeFormatter dtformat = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss.SSS");

            String line = String.format("[%s] %s", dtformat.format(date1), message);
            if (logText.get() == null) {
                logText.set(line);
            }

            logText.set(line + "\n" + logText.get());
        });
    }

    public void log(UkMovingError error) {
        log(error.getMessage());
    }

}
