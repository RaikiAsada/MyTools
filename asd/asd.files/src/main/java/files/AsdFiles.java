/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package files;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class AsdFiles {
    
    public static Path move(File file, String toPath) {
        try{
          return Files.move(file.toPath(), Paths.get(toPath + "\\" + file.getName()));
        }catch(IOException ex){
          throw new RuntimeException(ex);
        }
    }
}
