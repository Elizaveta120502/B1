package com.test.task;

import com.test.task.database.ConnectionToDB;
import com.test.task.database.SelectDataToDB;
import com.test.task.logger.LoggerProvider;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class GenerateRecords {

    private static final int START_OF_ASCII_HIGH_LATIN_REGISTER = 65;
    private static final int END_OF_ASCII_HIGH_LATIN_REGISTER = 90;

    private static final int START_OF_ASCII_LOW_LATIN_REGISTER = 97;
    private static final int END_OF_ASCII_LOW_LATIN_REGISTER = 122;

    private static final int NUMBER_OF_RECORDS = 10;
    private static final int NUMBER_OF_ELEMENTS_FOR_DELEGATE = NUMBER_OF_RECORDS * NUMBER_OF_RECORDS * ConnectionToDB.NUMBER_OF_COLUMNS_IN_DATA;
    private static int deletedStringsCounter = 0;

    private static Scanner sc = new Scanner(System.in);



    public static void main(String[] args) {

        createFiles();
        showTerms();
        int point = 0;
        while(point !=4){


        if (sc.hasNext()) {
            point = sc.nextInt();


            switch (point) {

                case 1:

                    try {
                        deleteStrings();
                    } catch (IOException e) {
                        LoggerProvider.getLOG().error("IOException occurred");
                    }
                    break;
                case 2:


                    ConnectionToDB.getInstance().executionOfProcedure(SelectDataToDB
                                    .getInstance()
                                    .prepareDataToImport("C:/TestTask-B1/src/main/resources/textFiles/unit.txt"),
                            (NUMBER_OF_ELEMENTS_FOR_DELEGATE - deletedStringsCounter)*ConnectionToDB.NUMBER_OF_COLUMNS_IN_DATA);
                    break;
                case 3:
                    ConnectionToDB.getInstance().executeSumOfBigNumbers();
                    ConnectionToDB.getInstance().executeMedianOfFloatNumbers();
                    break;
                case 4:
                    System.out.println("End of the program.");
                    break;
                default:
                    System.out.println("Something went wrong. Try again");
                    break;
            }
        }
            }



    }


    private static void createFiles() {
        FileWriter writer = null;
        for (int i = 0; i < NUMBER_OF_RECORDS; i++) {
            //path to file
            Path path = Paths.get("C:/TestTask-B1/src/main/resources/textFiles/file" + i + ".txt");
            try {
                for (int k = 0; k < NUMBER_OF_RECORDS; k++) {
                    String str = String.join("||", generateRandomDate(), generateRandomLatinString(), generateRandomRuString(),
                            generateEvenNumber(), generateNumberFromOneToTwenty());
                    //write information to file
                    writer = new FileWriter(String.valueOf(path), true);
                    writer.write(str);
                    writer.append('\n');
                    writer.flush();
                    writer.close();

                }


            } catch (IOException e) {
                LoggerProvider.getLOG().error("IOException occurred");

            }

        }
    }

    private static String generateRandomDate() {
        //start data
        LocalDate startDate = LocalDate.of(2017, 1, 1);
        long start = startDate.toEpochDay();
        //the date of today
        LocalDate endDate = LocalDate.now();
        long end = endDate.toEpochDay();
        //select random data
        long random = ThreadLocalRandom.current().nextLong(start, end);
        String randomDate = String.valueOf(LocalDate.ofEpochDay(random));

        return randomDate;
    }

    private static String generateRandomLatinString() {
        int[] finishRandomLatinCodes = new int[10];
        Random rand = new Random();
        //generate random strings with the help of symbols codes
        for (int i = 0; i < 10; i++) {
            int temp = rand.nextInt(2);

            if (temp == 0) {
                finishRandomLatinCodes[i] = customRandom(END_OF_ASCII_HIGH_LATIN_REGISTER, START_OF_ASCII_HIGH_LATIN_REGISTER);
            }
            if (temp == 1) {
                finishRandomLatinCodes[i] = customRandom(END_OF_ASCII_LOW_LATIN_REGISTER, START_OF_ASCII_LOW_LATIN_REGISTER);
            }

        }
        char[] codesToChars = new char[10];

        for (int i = 0; i < 10; i++) {
            codesToChars[i] = (char) finishRandomLatinCodes[i];
        }

        String finishRandomLatinString = String.valueOf(codesToChars);
        return finishRandomLatinString;

    }

    public static int customRandom(int min, int max) {
        max -= min;
        return (int) (Math.random() * ++max) + min;
    }

    private static String generateRandomRuString() {
        //generate random string with ru symbols
        String ruLet = new String("абвгдеёжзиклмнопрстуфхцчшщьыъэюяАБВГДЕЁЖЗИКЛМНОПРСТУФХЦЧШЩЭЮЯ");
        char buf[] = ruLet.toCharArray();
        char[] codesToChars = new char[10];
        for (int i = 0; i < 10; i++) {
            int randTemp = customRandom(0, ruLet.length() - 1);
            codesToChars[i] = buf[randTemp];
        }
        String finishRandomRuString = String.valueOf(codesToChars);
        return finishRandomRuString;

    }

    private static String generateEvenNumber() {

        int number = customRandom(1, 100_000_000);
        while (number % 2 != 0) {
            number = customRandom(1, 100_000_000);
        }
        String numberString = String.valueOf(number);

        return numberString;
    }

    private static String generateNumberFromOneToTwenty() {
        double number = Math.random() * 20 + 1;
        String temp = String.format("%.8s", number);
        String numberString = String.valueOf(temp);

        return numberString;

    }

    private static void deleteStrings() throws IOException {

        List<String> bufferCollection = new ArrayList<>();
        List<String> lines = new ArrayList<>();
        Scanner scanner = null;

        System.out.println("It is possible to delete lines with a given sequence of characters. Do you want to remove any lines?(yes/no)");
        if (sc.hasNext()) {
            String deleteAnswer = sc.next();
            if (deleteAnswer.equals("yes")) {

                System.out.println("Lines with the entered combination of characters will be deleted. Enter a sequence of characters: ");
                if (sc.hasNext()) {
                    String str = sc.next();

                    for (int i = 0; i < NUMBER_OF_RECORDS; i++) {
                        lines.removeAll(lines);
                        Path path = Paths.get("C:/TestTask-B1/src/main/resources/textFiles/file" + i + ".txt");

                        try {
                            scanner = new Scanner(Files.readString(path));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        while (scanner.hasNextLine()) {
                            lines.add(scanner.nextLine());
                        }


                        for (String s : lines) {
                            if (!s.contains(str.subSequence(0, str.length()))) {
                                bufferCollection.add(s);
                            } else {
                                deletedStringsCounter++;
                            }
                        }
                        scanner.close();

                    }
                    unitFiles(bufferCollection);

                }
            } else {
                //delete selected records in every file
                for (int i = 0; i < NUMBER_OF_RECORDS; i++) {
                    Path path = Paths.get("C:/TestTask-B1/src/main/resources/textFiles/file" + i + ".txt");

                    try {
                        scanner = new Scanner(Files.readString(path));
                    } catch (IOException e) {
                        LoggerProvider.getLOG().error("IOException occurred");
                    }

                    while (scanner.hasNextLine()) {
                        lines.add(scanner.nextLine());
                    }
                }

                unitFiles(lines);
            }
        }
        System.out.println("Amount of deleted strings:" + deletedStringsCounter);

    }

    private static boolean deleteCreatedFiles(Path path) throws IOException {


        return Files.deleteIfExists(path);

    }

    private static void unitFiles(List<String> bufferCollection) throws IOException {
        // unite files into one
        File unitFile = new File("C:/TestTask-B1/src/main/resources/textFiles/unit.txt");

        FileWriter writer = new FileWriter(unitFile.getPath(), true);
        try {
            for (String s : bufferCollection) {
                writer.write(String.valueOf(s));
                writer.append('\n');
                writer.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            writer.close();
        }
    }

    private static void showTerms(){
        //the terms of the program
        System.out.println("Choose action:");

        System.out.println("1) Merge all files into one \n" +
                "2) Create file import procedure\n" +
                "3) Calculate the sum of all integers and the median of all fractional numbers\n" +
                "4) Exit");
    }


}
