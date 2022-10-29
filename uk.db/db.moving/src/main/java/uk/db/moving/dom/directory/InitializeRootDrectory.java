/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ukmoving.dom.directory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import nts.gul.util.Either;
import ukmoving.dom.error.UkMovingError;

public class InitializeRootDrectory {

    public static Either<UkMovingError, String> initialize() {
        Path path = getDirectoryPath();

        try {
            Files.createDirectories(path);
            return Either.right(path.toString());
        } catch (IOException ex) {
            return Either.left(new UkMovingError("ディレクトリにアクセス出来ませんでした. path: " + path.toString() + " 理由: " + ex.getMessage()));
        }
    }

    public static Either<UkMovingError, Void> end(String rootDirectory) {
        Path dir = Paths.get(rootDirectory);
        if (deleteFolder(dir.toFile())) {
            return Either.rightVoid();
        }
        
        return Either.left(new UkMovingError("ディレクトリの削除に失敗しました"));
    }

    private static Path getDirectoryPath() {
        String root = System.getProperty("java.io.tmpdir");
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        return Paths.get(root + "\\" + "ukmoving_" + LocalDateTime.now().format(dateTimeFormatter));
    }

    private static boolean deleteFolder(File targetFolder) {
        String[] list = targetFolder.list();
        for (String file : list) {
            Path filePath = targetFolder.toPath().resolve(file);
            File f = filePath.toFile();
            if (f.isDirectory()) {
                deleteFolder(f);
            } else {
                f.delete();
            }
        }
        return targetFolder.delete();
    }
}
