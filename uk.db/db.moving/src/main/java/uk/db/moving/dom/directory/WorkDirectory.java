/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.db.moving.dom.directory;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import nts.gul.util.Either;
import uk.db.moving.dom.error.UkMovingError;

/**
 * 作業ディレクトリ
 */
public class WorkDirectory {
    private String path;

    public static Either<UkMovingError, WorkDirectory> initialize(String rootDirecotry, String tableName) {
                
        try {
            Path path = Paths.get(rootDirecotry + "\\" + tableName);
            Files.createDirectories(path);
            return Either.right(new WorkDirectory(path.toString()));
        } catch (IOException ex) {
            return Either.left(new UkMovingError("ディレクトリにアクセス出来ませんでした. path: " + rootDirecotry));
        }
    }
    
    public WorkDirectory(String path) {
        this.path = path;
    }
    
    public Either<UkMovingError, Writer> initializeWriter(FileType fileType) {        
        final String filePath = fileType.getFilePaht(path);

        try {
            FileOutputStream file = new FileOutputStream(filePath);
            return Either.right(new OutputStreamWriter(file, "UTF8"));
        } catch (FileNotFoundException | UnsupportedEncodingException ex) {
            return Either.left(new UkMovingError("ファイルににアクセス出来ませんでした. path: " + filePath));
        }
    }
    
    public Either<UkMovingError, BufferedReader> initializeReader(FileType fileType) {
        final String filePath = fileType.getFilePaht(path);
        
        try {
            return Either.right(new BufferedReader(new InputStreamReader(new FileInputStream(filePath), "UTF8")));
        } catch (FileNotFoundException | UnsupportedEncodingException ex) {
            return Either.left(new UkMovingError("ファイルににアクセス出来ませんでした. path: " + filePath));
        }
    }
    
    public Either<UkMovingError, Long> getFileSize(FileType fileType) {
        final String filePath = fileType.getFilePaht(path);
        
        try {
            return Either.right(Files.size(Paths.get(filePath)));
        } catch (IOException ex) {
            return Either.left(new UkMovingError("ファイルににアクセス出来ませんでした. path: " + filePath));
        }
    }
}
