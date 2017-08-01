package com.company;

import org.apache.poi.ss.usermodel.Cell;

import java.util.*;

import static com.company.Main.*;

public class WO {
    char c ='t';
    ArrayList<String> massive = new ArrayList<>();
    int number, wo, ata;
    String aircraft, status, type, parts, ref, mel, dd;
    Date date12;
    Date date22;
    String issuer;
    ArrayList<String> description = new ArrayList<>();
    HashMap<String, Integer> topWords = new HashMap<>();

    public WO(ArrayList<Cell> rowCell, ArrayList<String> headline) {

        for(int i = 0; i < headline.size(); i++) {
            switch (headline.get(i)) {
                case ("No"):
                    this.number = (int) rowCell.get(i).getNumericCellValue();
                    break;
                case ("W/O"):
                    this.wo = (int) rowCell.get(i).getNumericCellValue();
                    break;
                case ("A/C"):
                    this.aircraft = rowCell.get(i).getStringCellValue();
                    break;
                case ("State"):
                    this.status = rowCell.get(i).getStringCellValue();
                    break;
                case ("Issue-Date"):
                    this.date12 = rowCell.get(i).getDateCellValue();
                    break;
                case("Due-/C.-Date"):
                    this.date22 = rowCell.get(i).getDateCellValue();
                    break;
                case("ATA"):
                    this.ata = (int) rowCell.get(i).getNumericCellValue();
                    break;
                case("Type"):
                    this.type = rowCell.get(i).getStringCellValue();
                    break;
                case("Parts"):
                    this.parts = rowCell.get(i).getStringCellValue();
                    break;
                case("Ref."):
                    this.ref = rowCell.get(i).getStringCellValue();
                    break;
                case("Mel"):
                    this.mel = rowCell.get(i).getStringCellValue();
                    break;
                case("DD"):
                    this.dd = rowCell.get(i).getStringCellValue();
                    break;
                case("Iss"):
                    this.issuer = rowCell.get(i).getStringCellValue();
                    break;
                case("Workorder-description and/or complaint"):
                    for (String retval2 : rowCell.get(13).getStringCellValue().split(" " )) {
                        if(retval2.length() > 2) {
                            String str1 = removeUnnecessary(retval2);
                            if(!badWord.contains(str1)) {
                                DescriptionAddWord(str1);
                            }

                        }



                        /*if(retval2.length() < 3 || badWord.contains(retval2)) {
                        }
                        else {
                            if(retval2.indexOf('.') == (retval2.length()-1)) {
                                int start = 0;
                                int end = retval2.length()-1;
                                char[] buf = new char[end - start];
                                retval2.getChars(start, end, buf, 0);
                                DescriptionAddWord(new String(buf));
                            } else {
                                DescriptionAddWord(retval2);
                            }
                        }*/
                    }
                    break;
            }
        }
    }

    public String removeUnnecessary(String str) {
        ArrayList<Character> charlist = new ArrayList<>();

        for(char c: str.toCharArray()) {
            charlist.add(c);
        }

        for(int ic = charlist.size()-1; ic >= 0; ic--) {
            if(!((charlist.get(ic) == 45) || ((charlist.get(ic) > 47) && (charlist.get(ic) <58)) || ((charlist.get(ic) > 64) && (charlist.get(ic) < 91)) || ((charlist.get(ic) > 96) && (charlist.get(ic) < 123)))) {
                charlist.remove(ic);
            }
        }

        String word = new String();
        for(char c:charlist){
            word= word+ c;
        }
        if(changeWord.containsKey(word)) {
            System.out.print(word);
            word = changeWord.get(word);
            System.out.println("       " + word);
        }
        return word;
    }

    public void DescriptionAddWord(String str) {
        int numberOfaddedWords = 0;
        if(wordList.contains(str)) {
            description.add(str);
            numberOfaddedWords++;
        } else {
            for(String word:wordList) {
                if(str.contains(word)) {
                    description.add(word);
                    numberOfaddedWords++;
                }
            }
        }

        if(numberOfaddedWords==0) {
            //System.out.println(str);
            numberOfErrors++;
        }

//
//
//
//        for (String word:wordList) {
//            if(str.contains(word)) {
//                description.add(word);
//                System.out.println("Добавлен word " + word);
//            } else {
//                description.add(str);
//                System.out.println("добавлен STR " + str);
//                break;
//            }
//        }
    }

}
