/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serverlog.filter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 *
 * @author raiki_asada
 */
public class ServerlogFilter {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        final String rootPath = args[0];
        final int start = calcMnitus(args[1]);
        final int end = calcMnitus(args[2]);
        
        List<RequrestLine> allRequsetLine = new ArrayList<>();
        
        File rootDirectory = new File(rootPath);
        
        Arrays.asList(rootDirectory.listFiles()).forEach(file -> {
            BufferedReader bfr = null;
            try {
                Path filePath = file.toPath();
                bfr = new BufferedReader(new InputStreamReader(new FileInputStream(filePath.toString()), "UTF8"));
                List<RequrestLine> targetLines = bfr.lines().filter(line -> line.contains("(REQUEST:"))
                        .map(line -> new RequrestLine(line))
                        .filter(requestLine -> {
                            int time = calcMnitus(requestLine.time);
                            return (start <= time) && (end >= time);
                        })
                        .filter(RequrestLine::isWebAPIRequest)
                        .collect(Collectors.toList());

                exportRequestWebAPILog(filePath.getFileName().toString(), targetLines);
                allRequsetLine.addAll(targetLines);
            } catch (FileNotFoundException | UnsupportedEncodingException ex) {
                Logger.getLogger(ServerlogFilter.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    bfr.close();
                } catch (IOException ex) {
                    Logger.getLogger(ServerlogFilter.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        
        Comparator<CountResult> comparator = Comparator.comparing(countResult -> countResult.count);
        
        List<CountResult> countResult =  allRequsetLine.stream()
                .collect(Collectors.groupingBy(RequrestLine::getWebAPIURL))
                .entrySet().stream().map(entry -> new CountResult(entry.getKey(), entry.getValue().size()))
                .sorted(comparator.reversed())
                .collect(Collectors.toList());
        
        exportCountWebAPILog(countResult);
    }
    
    private static void exportRequestWebAPILog(String fileName, List<RequrestLine> lines) {
        FileWriter fw = null;
        try {
            String serverLogName = fileName.split(".log")[0];
            fw = new FileWriter("request_webapi_" + serverLogName +".txt", false);
            PrintWriter pw = new PrintWriter(new BufferedWriter(fw));
            lines.forEach(line -> pw.println(line.time + "  " +line.getWebAPIURL()));
            pw.close();
        } catch (IOException ex) {
            Logger.getLogger(ServerlogFilter.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                fw.close();
            } catch (IOException ex) {
                Logger.getLogger(ServerlogFilter.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    private static void exportCountWebAPILog(List<CountResult> countResult) {
        FileWriter fw = null;
        try {
            fw = new FileWriter("count_webapi.csv", false);
            PrintWriter pw = new PrintWriter(new BufferedWriter(fw));
            pw.print("webapi");
            pw.print(",");
            pw.print("count");
            pw.println();
            
            for(CountResult count: countResult) {
                pw.print(count.webAPI);
                pw.print(",");
                pw.print(count.count);
                pw.println();
            }
            
            pw.close();
        } catch (IOException ex) {
            Logger.getLogger(ServerlogFilter.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                fw.close();
            } catch (IOException ex) {
                Logger.getLogger(ServerlogFilter.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    private static class RequrestLine {
        public String time;
        String reqestUrl;
        
        private static String WEBAPI_MARKER ="webapi";
        
        public RequrestLine(String line) {
            String[] tmp = line.split(" ");
            this.time = tmp[1];
            this.reqestUrl = tmp[5];
        }
        
        public boolean isWebAPIRequest() {
            return reqestUrl.contains(WEBAPI_MARKER);
        }
        
        public String getWebAPIURL() {
            return splitUUID(reqestUrl.split(WEBAPI_MARKER)[1].replace(")", ""));
        }
        
        private String splitUUID(String url) {
            Path pathUrl = Paths.get(url);
                        
            if(pathUrl.getFileName().toString().matches("([0-9a-f]{8})-([0-9a-f]{4})-([0-9a-f]{4})-([0-9a-f]{4})-([0-9a-f]{12})")) {
                return splitUUID(pathUrl.getParent().toString());
            }
            
            return pathUrl.toString();
        }
    }
    
    private static class CountResult {
        public String webAPI;
        public int count;

        public CountResult(String webAPI, int count) {
            this.webAPI = webAPI;
            this.count = count;
        }
        
    }
    
    private static int calcMnitus(String time) {
        String[] tmp = time.split(":");
        return (Integer.parseInt(tmp[0]) * 60) + Integer.parseInt(tmp[1]);
    }
}
