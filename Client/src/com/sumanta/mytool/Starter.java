package com.sumanta.mytool;

import com.sumanta.mytool.service.CsvUtility;
import com.sumanta.mytool.service.UnzipUtility;

import java.nio.file.Path;

import java.util.ArrayList;
import java.util.List;

public class Starter {
    public Starter() {
        super();
    }

    public static void main(String[] args) {
        Starter starter = new Starter();
        String directory;
        if(args == null || args.length == 0){
            directory = "D:\\Sample_files\\";
        }else{
            directory = args[0];
        }
        System.out.println("Starting application...");
        UnzipUtility unzipUtility = new UnzipUtility();
        
        /// COPY FILES TO TEMP
        System.out.println("Copying files...");
        ArrayList<Path> filePaths = unzipUtility.copyFilesToTemp(directory);
        
        if(filePaths == null || filePaths.size() == 0){
            System.out.println("No file found to be copied.");
        }
        else{
            // Remove first entry from list
            // this bug needs to be fixed
            filePaths.remove(0);
            
            /// EXTRACT FILES
            System.out.println("Extracting files...");
            ArrayList<Path> outputPaths = unzipUtility.extractFiles(filePaths);
            System.out.println("Extraction complete.");
                        
            /// PARSE CSV
            CsvUtility csvUtility = new CsvUtility();
            for(Path outputPath: outputPaths){
                System.out.printf("Reading %s.", outputPath);
                System.out.println();
                List<List<String>> data = csvUtility.readCsv(outputPath);
                System.out.println((data.size()-1) + " records found.");
            }
            
            
            // Optional- used for testing
            // Open temp folder which stores all data
            //unzipUtility.openWorkspace();
        }
    }
}
