package com.ent.mytool;

import com.ent.mytool.service.Base64Utility;
import com.ent.mytool.service.CsvUtility;
import com.ent.mytool.service.UnzipUtility;
import com.ent.mytool.utility.Constants;

import java.io.File;

import java.nio.file.Path;
import java.nio.file.Paths;

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
                        
            /// PARSE CSV and convert to base64
            CsvUtility csvUtility = new CsvUtility();
            Base64Utility base64Utility = new Base64Utility();
            for(Path outputPath: outputPaths){
                System.out.printf("Reading %s.", outputPath);
                System.out.println();
                List<List<String>> data = csvUtility.readCsv(outputPath);
                System.out.println((data.size()-1) + " records found.");
                
                for(List<String> row: data){
                    String fileName = row.get(Constants.METADATA_FILENAME_INDEX) + "." + row.get(Constants.METADATA_EXTENSION_INDEX);
                    // Getting the file by creating object of File class
                    File f = new File(outputPath + "\\" + fileName);
             
                    // Checking if the specified file exists or not
                    if (f.exists()){
             
                        // Show if the file exists
                        System.out.println(fileName + " exists");
                        String base64Content = base64Utility.encode(Paths.get(outputPath + "\\" + fileName));
                        System.out.println("Reading " + fileName + "==============================");
                        System.out.println(base64Content);
                    }
                    else
                    {
                        // Show if the file does not exists
                        System.out.println(fileName + " does not Exists");
                    }
                }
                
                //String[] contents = outputPath.toFile().list();
            }
            
            
            // Optional- used for testing
            // Open temp folder which stores all data
            //unzipUtility.openWorkspace();
        }
    }
}
