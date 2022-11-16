package tool.moving;

import files.AsdFiles;
import java.io.File;
import java.util.Arrays;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

public class Main {
    public static void main(String[] args) {
        String fromPath = args[0];
        String toPath = args[1];
        
        moveSrcFiles(fromPath, toPath);
    }
    
    private static void moveSrcFiles(String fromPath, String toPath) {
        File fromDirectory = new File(fromPath);
        File srcDirecotry = Arrays.stream(fromDirectory.listFiles())
                .filter(file -> file.getName().equals("src"))
                .findAny().orElseThrow(() -> new RuntimeException("指定したファイル/ディレクトリが存在しません. path: " + fromPath));

        Predicate<File> checkDirectory = (dir) -> {
            //ディレクトリ内に、一つでもファイルがある
            return Arrays.stream(dir.listFiles()).anyMatch(File::isFile);
        };
        
        warkDirectory(srcDirecotry, checkDirectory).ifPresent(dir -> {
            Arrays.stream(dir.listFiles()).forEach(file -> {
                AsdFiles.move(file, toPath);
            });
        });
    }
    
    private static Optional<File> warkDirectory(File directory, Predicate<File> predicate) {
        if(predicate.test(directory)) {
            return Optional.ofNullable(directory);
        }
        
        return Arrays.stream(directory.listFiles())
                .flatMap(child -> warkDirectory(child, predicate).map(Stream::of).orElse(Stream.empty()))
                .findAny();
    } 
}
