/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package db.connection.query;

import java.util.ArrayList;
import java.util.List;

public class DeleteSentenceBuilder {

    private final String tableName;
    private final List<String> columns = new ArrayList<>();

    public DeleteSentenceBuilder(String tableName) {
        this.tableName = tableName;
    }

    public DeleteSentenceBuilder column(String columnName) {
        this.columns.add(columnName);
        return this;
    }

    public String build() {
        return "DELETE FROM " + tableName
                + " WHERE " + createColumnsSentence(columns);
    }

    private String createColumnsSentence(List<String> columns) {
        return columns.stream()
                .map(c -> c + " = ?")
                .reduce((left, right) -> left + " AND " + right)
                .orElseThrow(() -> new RuntimeException("columns is Empty"));
    }
}
