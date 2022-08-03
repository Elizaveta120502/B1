package com.test.task.database;

import com.test.task.logger.LoggerProvider;

import java.io.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class SelectDataToDB {

    private static SelectDataToDB instance;

    public static SelectDataToDB getInstance(){
        if (instance ==null){
            instance = new SelectDataToDB();
        }
        return instance;
    }

    public List<String> prepareDataToImport(String fileName) {


        File file = new File(fileName);
        FileReader fileReader = null;
        try {
            fileReader = new FileReader(file);
        } catch (FileNotFoundException e) {
            LoggerProvider.getLOG().error("Unfortunately file was not found");
        }
        BufferedReader bufferedReader = new BufferedReader(fileReader);

        List<String> elementArray = new ArrayList<>();

        String line;


        try {
                while ((line = bufferedReader.readLine()) != null) {

                    String[] split = line.split("[||]+");
                    for (int k = 0; k < split.length; k++) {

                        elementArray.add(split[k]);

                    }
                }
        } catch(IOException e){
            LoggerProvider.getLOG().error("IOException occurred");
        }

        return elementArray;
    }

}
