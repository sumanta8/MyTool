package com.sumanta.mytool;

import com.sumanta.mytool.service.UnzipUtility;

public class Starter {
    public Starter() {
        super();
    }

    public static void main(String[] args) {
        Starter starter = new Starter();
        //String directory = args[0];
        String directory = "D:\\Sample files\\";
        System.out.println("Starting application...");
        System.out.println("Traversing Folder...");
        UnzipUtility unzipUtility = new UnzipUtility();
        String[] files = unzipUtility.copyFilesToTemp(directory);
        for(int i=0; i<files.length; i++) {
            System.out.println(files[i]);
        }
    }
}
