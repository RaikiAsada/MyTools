/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ukmoving.dom.directory;

/**
 *  対象とするファイルのType
 */
public enum FileType {
    FROM("from"),
    TO("to");

    public final String prefix;

    private FileType(String prefix) {
        this.prefix = prefix;
    }
    
    public String getFilePaht(String directory) {
        return String.format("%s\\%s.csv", directory, this.prefix);
    }
}
