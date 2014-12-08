package com.emotion.utilities;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JOptionPane;

import com.emotion.exception.FileException;
import com.emotion.exception.LexicoException;
import com.emotion.lexico.Base;
import com.emotion.sintactico.SintacticAnalizer;
import java.util.ArrayList;

public class ReadFile {

    private static int rowLexema = 0;
    private static int columnLexema = 0;
//    private static final SintacticAnalizer sintactic = new SintacticAnalizer(0,0);

    private static ArrayList<String> tokens = new ArrayList();
    private static ArrayList<Integer> lexemas = new ArrayList();
    private static boolean status = true;

    /**
     *
     */
    public static final String newLine = System.getProperty("line.separator");

    public static boolean validateFile(File source) {
        return source != null;
    }

    public static boolean readFile(File file) throws FileException, LexicoException {
        BufferedReader br;
        String fileName = file.getName();
        try {
            br = new BufferedReader(new FileReader(file.getAbsolutePath()));
            String line = br.readLine();
            int count = 1;
            System.out.println("************  ANALISIS LEXICO  **************");
            while (line != null) {
                isLexema(line);
                line = br.readLine();
                count++;
            }
            br.close();
        } catch (FileNotFoundException e) {
            throw new FileException("No se encontró el archivo");
        } catch (IOException e) {
            throw new FileException("No se puede leer el archivo. Puede estar corrupto");
        }
        if (!status) {
            throw new LexicoException("Error lexico encontrado en la linea: ");
        }
        SintacticAnalizer sintactic = new SintacticAnalizer();
        sintactic.setRowGramar(0);
        sintactic.setColumnGramar(0);
        sintactic.setLexemas(lexemas);
        sintactic.setTokens(tokens);
        status = sintactic.startAnalisis(fileName);
        if(status)
            System.out.println("Analisis Sintactico completo sin errores");
        return status;
    }

    private static void isLexema(String line) {
        line += newLine;
        Base baseLexico = new Base(0, 0);
//        System.out.println("Line   " + line);
        StringBuilder token = new StringBuilder();
        int i = 0;
        while (i < line.length()) {
//            System.out.println(line.charAt(i) + "  -  [" + baseLexico.getRowLexema() + " - " + baseLexico.getColumnLexema() + "]");
            rowLexema = baseLexico.getLexema(line.charAt(i));
            if (rowLexema == 6) {
                if ((int)line.charAt(i) != 101) {
                    if ((int)line.charAt(i) != 69) {
                        rowLexema = 501;
                    }
                }
            }
            baseLexico.setRowLexema(rowLexema);
            if (rowLexema < 200 && rowLexema > 99) {

                if (token.toString().trim().equals("")) {
                    token.append(line.charAt(i));
                }
                if (rowLexema == 106 || rowLexema == 110 || rowLexema == 112 || rowLexema == 114 || rowLexema == 116 || rowLexema == 117 || rowLexema == 105) {
                    token.append(line.charAt(i));
                    i++;
                }
                if (!Base.isReserved(token.toString().trim()) && rowLexema == 100) {
                    rowLexema = 101;
                }
                System.out.println("token ---->   " + token.toString().trim() + "  -  " + rowLexema);
                if(rowLexema != 108){
                    tokens.add(token.toString().trim());
                    lexemas.add(rowLexema);
                }
                token.setLength(0);
                baseLexico.setRowLexema(0);
                if (rowLexema > 117) {
                    i++;
                }
                continue;

            } else if (rowLexema > 199) {
                token.append(line.charAt(i+1));
                System.out.println("ERROR LEXICO Token invalido ---->   " + token.toString().trim() + "  -  " + rowLexema);
                baseLexico.setRowLexema(0);
                token.setLength(0);
                status = false;
            }
            token.append(line.charAt(i));
            i++;
        }
    }

    public static void showMessage(String message, String title) {
        JOptionPane.showMessageDialog(null, message, "InfoBox: " + title, JOptionPane.INFORMATION_MESSAGE);
    }

}
