package tool.moving;

import java.io.File;
import java.util.Arrays;
import java.util.List;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

public class Main {
    public static void main(String[] args) {
        String fromPath = args[0];
        String toPath = args[1];
        
        File file1 = new File(fromPath);
        File fileArray1[] = file1.listFiles();
        
        Arrays.stream(fileArray1).forEach(file -> {
            
        });
    }
}
