package com.ent.mytool.service;

import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.Path;

import org.apache.commons.codec.binary.Base64;

public class Base64Utility {
    public Base64Utility() {
        super();
    }
    
    // Encode to base64
    public String encode(Path filePath) {
        String fileContent = "";
        try {
            byte[] byteArray = Base64.encodeBase64(Files.readAllBytes(filePath));
            fileContent = new String(byteArray);
        } catch (IOException e) {
            //TODO: Handle exception
            System.out.printf(e.getMessage());
        }
        return fileContent;
    }
}
