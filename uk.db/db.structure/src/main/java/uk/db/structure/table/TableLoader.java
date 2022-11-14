/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.db.structure.table;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 引越し対象テーブルを読み込む
 */
public class TableLoader {
        
    public static List<String> load(Require require) {
        return require.getTableNames().stream()
                .sorted()
                .filter(table -> table.matches("[0-9a-zA-Z\\_]+"))
                .collect(Collectors.toList());
    }
    
    public interface Require {
        List<String> getTableNames();
    }
}
