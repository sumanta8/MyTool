package com.sumanta.mytool;

import com.sumanta.mytool.service.UnzipUtility;

import java.nio.file.Path;

import java.util.ArrayList;

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
        
        System.out.println("Copying files...");
        ArrayList<Path> filePaths = unzipUtility.copyFilesToTemp(directory);
        
        if(filePaths == null || filePaths.size() == 0){
            System.out.println("No file found to be copied.");
        }
        else{
            // Remove first entry from list
            // this bug needs to be fixed
            filePaths.remove(0);
            
            System.out.println("Extracting files...");
            unzipUtility.extractFiles(filePaths);
            System.out.println("Extraction complete.");
            
            // Optional- used for testing
            // Open temp folder which stores all data
            unzipUtility.openWorkspace();
        }
    }
}
