/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package direcctory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class GeneralDirectory {
    private final Path path;

    public static GeneralDirectory inisialize(String path, String direcitoryName) {
        return new GeneralDirectory(Paths.get(path)).addDirecitory(direcitoryName);
    }
    
    public GeneralDirectory addDirecitory(String direcitoryName) {
        Path directoryFullPath = Paths.get(path.toString() + "\\" + direcitoryName);
        try {
            Files.createDirectories(directoryFullPath);
            return new GeneralDirectory(directoryFullPath);
        } catch (IOException ex) {
            throw new RuntimeException();
        }
    }
    
    public GeneralDirectory(Path path) {
        this.path = path;
    }
    
}
