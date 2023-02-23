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

public class AsdDirectory {
    private final Path path;

    public static AsdDirectory inisialize(String path, String direcitoryName) {
        return new AsdDirectory(Paths.get(path)).addDirecitory(direcitoryName);
    }
    
    public AsdDirectory addDirecitory(String direcitoryName) {
        Path directoryFullPath = Paths.get(path.toString() + "\\" + direcitoryName);
        try {
            Files.createDirectories(directoryFullPath);
            return new AsdDirectory(directoryFullPath);
        } catch (IOException ex) {
            throw new RuntimeException();
        }
    }
    
    public AsdDirectory(Path path) {
        this.path = path;
    }
    
}
