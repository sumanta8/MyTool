package com.sumanta.mytool.service;

import com.sumanta.mytool.utility.Constants;

import java.awt.Desktop;
import java.awt.Desktop.Action;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class UnzipUtility {
    public UnzipUtility() {
        super();
    }
    
    private String folderName = "";
    
    private String[] getFiles(String directory){
        //Creating a File object for directory
        File directoryPath;
        directoryPath = new File(directory);
        //List of all files and directories
        String contents[] = directoryPath.list();
        return contents;
    }
    
    // Copy files to temp
    public ArrayList<Path> copyFilesToTemp(String directory){
        ArrayList<Path> filePaths = new ArrayList<Path>();
        
        SimpleDateFormat formatter;
        formatter = new SimpleDateFormat(Constants.DATE_TIME_FORMAT);
        String tmpdir = System.getProperty("java.io.tmpdir");
        folderName = tmpdir + formatter.format(new Date()) + "\\";
        
        // Create directory
        new File(folderName).mkdir();
        
        String targetFolderName = folderName + Constants.ZIP_INPUT_FOLDER_NAME + "\\";
        
        // Create directory
        new File(targetFolderName).mkdir();
        
        Path sourceDirectory = Paths.get(directory);
        Path targetDirectory = Paths.get(targetFolderName);
        try{
            filePaths = copy(sourceDirectory, targetDirectory);
        }catch (IOException ioe){
            // TODO: Handle exception
        }catch (Exception ex){
            // TODO: Handle exception
        }
        return filePaths;
    }
    
    // Copy source to destiunation recursively
    private ArrayList<Path> copy(Path sourceDirectory, Path targetDirectory) throws IOException {
        ArrayList<Path> filePaths = new ArrayList<Path>();
        Files.walk(sourceDirectory)
             .forEach(sourcePath -> {
                 Path targetPath = targetDirectory.resolve(sourceDirectory.relativize(sourcePath));
                 System.out.printf("Copying %s to %s%n", sourcePath, targetPath);
                 try{
                    Files.copy(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
                    filePaths.add(targetPath);
                 }catch (IOException ioe){
                     // TODO: Handle exception
                 }
             });
        return filePaths;
    }
    
    public void extractFiles(ArrayList<Path> filePaths){
        String outputFolder = folderName + Constants.ZIP_EXTRACTED_FOLDER_NAME + "\\";
        
        // Create directory
        new File(outputFolder).mkdir();
        
        for(Path filePath: filePaths){
            String fileName = getFileName(filePath);        
            String targetFolderName = outputFolder + "\\" + fileName;
        
            // Create directory
            new File(targetFolderName).mkdir();
            
            Path targetPath = Paths.get(targetFolderName);
            
            System.out.printf("Extracting %s to %s%n", fileName, targetPath);
            try {
                unzipFolder(filePath, targetPath);
            } catch (IOException e) {
                // TODO: Handle exception
            }
        }
    }
    
    private String getFileName(Path filePath){
        String fileName = filePath.getFileName().toString();
        if(!fileName.contains("."))
            System.out.println("File Name = " + fileName);
        else {
            fileName = fileName.substring(0,fileName.lastIndexOf("."));
            System.out.println("File Name = " + fileName);
        }
        return fileName;
    }
    
    private void unzipFolder(Path source, Path target) throws IOException {

            try (ZipInputStream zis = new ZipInputStream(new FileInputStream(source.toFile()))) {

                // list files in zip
                ZipEntry zipEntry = zis.getNextEntry();

                while (zipEntry != null) {

                    boolean isDirectory = false;
                    // example 1.1
                    // some zip stored files and folders separately
                    // e.g data/
                    //     data/folder/
                    //     data/folder/file.txt
                    if (zipEntry.getName().endsWith(File.separator)) {
                        isDirectory = true;
                    }

                    Path newPath = zipSlipProtect(zipEntry, target);

                    if (isDirectory) {
                        Files.createDirectories(newPath);
                    } else {

                        // example 1.2
                        // some zip stored file path only, need create parent directories
                        // e.g data/folder/file.txt
                        if (newPath.getParent() != null) {
                            if (Files.notExists(newPath.getParent())) {
                                Files.createDirectories(newPath.getParent());
                            }
                        }

                        // copy files, nio
                        Files.copy(zis, newPath, StandardCopyOption.REPLACE_EXISTING);

                        // copy files, classic
                        /*try (FileOutputStream fos = new FileOutputStream(newPath.toFile())) {
                            byte[] buffer = new byte[1024];
                            int len;
                            while ((len = zis.read(buffer)) > 0) {
                                fos.write(buffer, 0, len);
                            }
                        }*/
                    }

                    zipEntry = zis.getNextEntry();

                }
                zis.closeEntry();

            }

        }

    // protect zip slip attack
    private Path zipSlipProtect(ZipEntry zipEntry, Path targetDir)
        throws IOException {

        // test zip slip vulnerability
        // Path targetDirResolved = targetDir.resolve("../../" + zipEntry.getName());

        Path targetDirResolved = targetDir.resolve(zipEntry.getName());

        // make sure normalized file still has targetDir as its prefix
        // else throws exception
        Path normalizePath = targetDirResolved.normalize();
        if (!normalizePath.startsWith(targetDir)) {
            throw new IOException("Bad zip entry: " + zipEntry.getName());
        }

        return normalizePath;
    }
    
    public void openWorkspace(){
        //Check if the feature supported on your platform
        if (Desktop.getDesktop().isSupported(Action.BROWSE)) {
          //Open directory with browse_file_dir option
            try {
                Desktop.getDesktop().browse(Paths.get(folderName).toUri());
            } catch (IOException e) {
            // TODO: Handle exception
            }
        }
    }
}
