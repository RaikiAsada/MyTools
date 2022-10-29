/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package db.structure;

import db.structure.table.TableLoader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


public class ExportDbStructure {
    
    public static void export(Require require, String dbName, String exportPath) {
        Path path = buildWorkDirectory(dbName, exportPath);
        
        TableLoader.load(require).forEach(table -> {
            ExportTableStructure.write(require, path, table);
        });
    }
    
    private static Path buildWorkDirectory(String dbName, String exportPath) {
        Path path = Paths.get(exportPath + "\\" + dbName);
        try {
            Files.createDirectories(path);
            return path;
        } catch (IOException ex) {
            throw new RuntimeException("ディレクトリにアクセス出来ませんでした. path: " + exportPath);
        }
    }
    
    public interface Require extends TableLoader.Require, ExportTableStructure.Require {
    }
}
