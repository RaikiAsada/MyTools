/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.db.constrainer.export;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import uk.db.constrainer.UkDbConstrainer;
import uk.db.constrainer.validate.ValidateTableData;

public class ExportValidateResult {
    
    public static void export(String dbName, String exportPath, List<ValidateTableData.Result> results) {
        FileWriter fw = null;
        
        try {
            fw = new FileWriter(exportPath + "\\" + dbName + "_validate_result.csv", false);
            PrintWriter pw = new PrintWriter(new BufferedWriter(fw));
            pw.print("テーブル名");
            pw.print(",");
            pw.print("カラム名");
            pw.print(",");
            pw.print("検証内容");
            pw.print(",");
            pw.print("値（※サンプル）");
            pw.println();
            for(ValidateTableData.Result result: results) {
                pw.print(result.getTableName());
                pw.print(",");
                pw.print(result.getColumnName());
                pw.print(",");
                pw.print(result.getRules().getMessage());
                pw.print(",");
                pw.print(result.getSampleValue());
                pw.println();
            }
            
            pw.close();
        } catch (IOException ex) {
            Logger.getLogger(UkDbConstrainer.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                fw.close();
            } catch (IOException ex) {
                Logger.getLogger(UkDbConstrainer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
