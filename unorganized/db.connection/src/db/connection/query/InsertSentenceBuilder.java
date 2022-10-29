/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package db.connection.query;

import java.util.ArrayList;
import java.util.List;

public class InsertSentenceBuilder {
    private final String tableName;
    private final List<String> columns = new ArrayList<>();
    
    public InsertSentenceBuilder(String tableName) {
        this.tableName = tableName;
    }
    
    public InsertSentenceBuilder column(String columnName) {
        this.columns.add(columnName);
        return this;
    }
    
    public String build() {
        return "INSERT INTO " + tableName
                + "(" + createColumnsSentence(columns) + ")"
                + " VALUES (" + createParameterSentence(columns.size()) + ")";
    }
    
    private String createColumnsSentence(List<String> columns) {
        return columns.stream()
                .reduce((left, right) -> left + "," + right)
                .orElseThrow(() -> new RuntimeException("columns is Empty"));
    }
    
    private String createParameterSentence(int numberOfColumns) {
        if(numberOfColumns <= 0) {
            throw new RuntimeException("columns is Empty");
        }
        
        List<String> parameters = new ArrayList<>();
        
        for(int i = 1; i <= numberOfColumns; i++) {
            parameters.add("?");
        }
        
        return parameters.stream()
                .reduce((left, right) -> left + "," + right)
                .orElseThrow(() -> new RuntimeException("columns is Empty"));
    }
}
