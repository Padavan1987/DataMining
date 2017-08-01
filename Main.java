package com.company;

import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class Main {
    static ArrayList<String> wordList = new ArrayList<>();
    static ArrayList<String> badWord = new ArrayList<>();
    static HashMap<String, String> changeWord = new HashMap<>();
    static ArrayList<WO> test = new ArrayList<>();
    static ArrayList<String> headline = new ArrayList<>();
    static ArrayList<Cell> rowCells = new ArrayList<>();
    static int numberOfErrors = 0;
    static int index = 0;

    public static void main(String[] args) {
        uploadDicts();
        uploadFile("C:/Games/test.xls", 347);



        //****************************************************************************************
        //****************************************************************************************
        System.out.println(numberOfErrors);

        for(int i1 = 0; i1 < 1; i1++) {
            System.out.println("Загрузка WO Номер " + (i1+1));
            wordCorrection(test.get(i1));
        }
        //****************************************************************************************
        //****************************************************************************************
        System.out.println("Запись в файл");
        downloadDicts();
    }




    public static void wordCorrection (WO wo) {
        boolean exit = false;
        int choice;
        BufferedReader read = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Start word Correction");
        try {
            while (!exit) {
                choice = Integer.parseInt(read.readLine());
                switch(choice) {
                    case (1):
                        for(String word: wo.description) {
                            if(wordList.contains(word) || badWord.contains(word)) {
                                System.out.println(word + " уже присутствует в словаре");
                            }
                            else {
                                System.out.println(word + " - 1 - Good Word. 2- Bad Word. 3 - пропуск");
                                choice = Integer.parseInt(read.readLine());
                                switch (choice) {
                                    case (1):
                                        wordList.add(word);
                                        break;
                                    case (2):
                                        badWord.add(word);
                                        break;
                                    case (3):
                                        break;
                                }
                            }
                        }
                        break;
                    case(2):
                        exit = true;
                        break;
                    case (3):
                    exit = true;
                }
            }
        }
        catch (IOException e) {
            System.out.println(e);
        }
    }




    public static void uploadDicts() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream("C:/Games/wordList.txt"), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                wordList.add(line);
            }
        }
        catch (IOException e) {
            System.out.println(e);
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream("C:/Games/BadWordList.txt"), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                badWord.add(line);
            }
        } catch (IOException e) {
            System.out.println(e);
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream("C:/Games/CorrectionList.txt"), StandardCharsets.UTF_8))) {
            String line;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length >= 2)
                {
                    String key = parts[0];
                    String value = parts[1];
                    changeWord.put(key, value);
                } else {
                    System.out.println("ignoring line: " + line);
                }
            }

            reader.close();
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public static void downloadDicts() {
        try(FileWriter writer = new FileWriter("C:/Games/wordList.txt", false))
        {
            for(String text: wordList) {
                writer.write(text);
                writer.write("\n");
            }
            writer.close();
        }
        catch(IOException ex){
            System.out.println(ex.getMessage());
        }


        try(FileWriter writer = new FileWriter("C:/Games/BadWordList.txt", false))
        {
            for(String text: badWord) {
                writer.write(text);
                writer.write("\n");
            }
        }
        catch(IOException ex){
            System.out.println(ex.getMessage());
        }


        try(FileWriter writer = new FileWriter("C:/Games/CorrectionList.txt", false))
        {
            for (Map.Entry entry : changeWord.entrySet()) {
                writer.write(entry.getKey() + ":" + entry.getValue());
                writer.write("\n");
            }
        }
        catch(IOException ex){
            System.out.println(ex.getMessage());
        }

    }



    public static void uploadFile(String text, int rowNumbers) {
        try {
            Workbook wb = new HSSFWorkbook(new POIFSFileSystem(new FileInputStream(text)));
            for(int e = 0; e < 30; e++) {
                Cell cell =  wb.getSheetAt(0).getRow(0).getCell(e);
                if(cell == null) {
                    break;
                } else {
                    headline.add(wb.getSheetAt(0).getRow(0).getCell(e).getStringCellValue());
                }
            }

            for(int i = 1; i < rowNumbers; i++) {
                rowCells.clear();

                if (wb.getSheetAt(0).getRow(i).getCell(0) == null) {
                    System.out.println("Завершено");
                    break;
                }


                for (int e = 0; e < headline.size(); e++) {
                    Cell cell = wb.getSheetAt(0).getRow(i).getCell(e);
                    rowCells.add(cell);

                }
                test.add(index, new WO(rowCells, headline));
                index ++;
            }

            wb.close();
        } catch(Exception ioe) {
            ioe.printStackTrace();
        }
    }
}


