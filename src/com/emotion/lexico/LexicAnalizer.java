package com.emotion.lexico;

import java.io.File;

import com.emotion.exception.FileException;
import com.emotion.exception.LexicoException;
import com.emotion.utilities.ReadFile;
import java.util.ArrayList;

public class LexicAnalizer {

    private boolean status = true;
    private static int rowLexema = 0;
    private static ArrayList<String> tokens = new ArrayList();
    private static ArrayList<Integer> lexemas = new ArrayList();

    public void startAnalisis(File file) {
        try {
            ReadFile.readFile(file);

        } catch (FileException | LexicoException e) {
            setStatus(false);
        }
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
