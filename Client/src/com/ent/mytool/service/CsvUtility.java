package com.ent.mytool.service;

import com.ent.mytool.utility.Constants;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import java.nio.file.Path;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class CsvUtility {
    public CsvUtility() {
        super();
    }
    
    public List<List<String>> readCsv(Path filePath){
        List<List<String>> data = new ArrayList<>();
        String metadataFilePath = filePath.toString() + "//" + Constants.METADATA_FILENAME;
        File f = new File(metadataFilePath);
        // Checking if the specified file exists or not
        if (f.exists()){
 
            // Show if the file exists
            System.out.println("Metadata exists");
            
            try (BufferedReader br = new BufferedReader(new FileReader(metadataFilePath))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] values = line.split(Constants.COMMA_DELIMITER);
                    data.add(Arrays.asList(values));
                }
                // Remove first element in the list if header is not required.
            } catch (IOException e) {
            }
        }else{
 
            // Show if the file does not exists
            System.out.println("Metadata does not exist");
        }
        return data;
    }
}
