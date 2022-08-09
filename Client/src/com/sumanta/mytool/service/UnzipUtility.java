package com.sumanta.mytool.service;

import java.io.File;

import java.util.ArrayList;

public class UnzipUtility {
    public UnzipUtility() {
        super();
    }
    
    public String[] getFiles(String directory){
        //Creating a File object for directory
        File directoryPath;
        directoryPath = new File(directory);
        //List of all files and directories
        String contents[] = directoryPath.list();
        return contents;
    }
}
