/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package db.connection.query;

import db.connection.UkConnection;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import nts.arc.time.GeneralDate;


public class DeleteStatementBuilder {

    private final DeleteSentenceBuilder sqlSentenceBuilder;
    private final List<Consumer<UkStatement>> setColumnParameters = new ArrayList<>();

    public DeleteStatementBuilder(String tableName) {
        this.sqlSentenceBuilder = new DeleteSentenceBuilder(tableName);
    }

    public UkStatement buildStatement(UkConnection conn) {
        UkStatement ps = conn.prepareStatement(this.sqlSentenceBuilder.build());
        this.setColumnParameters.forEach(consumer -> consumer.accept(ps));
        return ps;
    }

    public DeleteStatementBuilder column(String columnName, String parameter) {
        return column(columnName, (statement, index) -> statement.setString(index, parameter));
    }

    public DeleteStatementBuilder column(String columnName, int parameter) {
        return column(columnName, (statement, index) -> statement.setInt(index, parameter));
    }

    public DeleteStatementBuilder column(String columnName, boolean parameter) {
        return column(columnName, (statement, index) -> statement.setBoolean(index, parameter));
    }

    public DeleteStatementBuilder column(String columnName, GeneralDate parameter) {
        return column(columnName, (statement, index) -> statement.setGeneralDate(index, parameter));
    }

    public DeleteStatementBuilder column(String columnName, BigDecimal parameter) {
        return column(columnName, (statement, index) -> statement.setBigDecimal(index, parameter));
    }

    public DeleteStatementBuilder column(String columnName, double parameter) {
        return column(columnName, (statement, index) -> statement.setDouble(index, parameter));
    }

    private DeleteStatementBuilder column(String columnName, BiConsumer<UkStatement, Integer> setParameter) {
        this.sqlSentenceBuilder.column(columnName);

        int index = this.setColumnParameters.size() + 1;
        this.setColumnParameters.add((statement -> setParameter.accept(statement, index)));
        return this;
    }
}
