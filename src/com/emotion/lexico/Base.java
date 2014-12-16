package com.emotion.lexico;

import com.emotion.utilities.Constants;
import java.util.Arrays;

public class Base {

    private int columnLexema;
    private int rowLexema;
    private static int column;

    public Base(int columnLexema, int rowLexema) {
        super();
        this.columnLexema = columnLexema;
        this.rowLexema = rowLexema;
    }

    public int getColumnLexema() {
        return columnLexema;
    }

    public void setColumnLexema(int columnLexema) {
        this.columnLexema = columnLexema;
    }

    public int getRowLexema() {
        return rowLexema;
    }

    public void setRowLexema(int rowLexema) {
        this.rowLexema = rowLexema;
    }

    public int getLexema(char letter) {
        int intLetter = (int) letter;
//		System.out.println(letter + " ---> " + intLetter);
        if (intLetter > 96 && intLetter < 123 || intLetter == 164) {
            column = 0;
        } else if (intLetter > 64 && intLetter < 91 || intLetter == 165) {
            column = 1;
        } else if (intLetter > 47 && intLetter < 58) {
            column = 2;
        } else {
            switch (intLetter) {
                case 8: // retroceso
                    column = 26;
                    break;
                case 9: // tab
                    column = 25;
                    break;
                case 10: // salto de linea
                    column = 26;
                    break;
                case 13: // salto de linea
                    column = 26;
                    break;
                case 32: // espacio en blanco
                    column = 27;
                    break;
                case 33: // signo !
                    column = 11;
                    break;
                case 34: // signo "
                    column = 4;
                    break;
                case 35: // signo #
                    column = 5;
                    break;
                case 37: // signo %
                    column = 16;
                    break;
                case 38: // signo &
                    column = 6;
                    break;
                case 39: // signo '
                    column = 3;
                    break;
                case 40: // signo (
                    column = 19;
                    break;
                case 41: // signo )
                    column = 20;
                    break;
                case 42: // signo *
                    column = 14;
                    break;
                case 43: // signo +
                    column = 12;
                    break;
                case 44: // signo +
                    column = 21;
                    break;
                case 45: // signo -
                    column = 13;
                    break;
                case 46: // signo .
                    column = 23;
                    break;
                case 47: // signo /
                    column = 15;
                    break;
                case 59: // signo ;
                    column = 22;
                    break;
                case 60: // signo <
                    column = 10;
                    break;
                case 61: // signo =
                    column = 8;
                    break;
                case 62: // signo >
                    column = 9;
                    break;
                case 91: // signo [
                    column = 17;
                    break;
                case 93: // signo ]
                    column = 18;
                    break;
                case 95: // signo _
                    column = 24;
                    break;
                case 124: // signo |
                    column = 7;
                    break;
                default:
                    column = 28;
                    break;
            }
        }
//		System.out.println("[" + rowLexema + " - " + column + "] = " + getRow(rowLexema, column));
//        if (column == 1000) {
//            return 500;
//        }
        return getRow(rowLexema, column);
    }

    private int getRow(int row, int col) {
        return Constants.lexema[row][col];
    }
    
    public static boolean isValid(int intLetter){
        boolean valid = false;
        switch (intLetter) {
                case 33: // signo !
                    valid = true;
                    break;
                case 34: // signo "
                    valid = true;
                    break;
                case 35: // signo #
                    valid = true;
                    break;
                case 37: // signo %
                    valid = true;
                    break;
                case 38: // signo &
                    valid = true;
                    break;
                case 39: // signo '
                    valid = true;
                    break;
                case 40: // signo (
                    valid = true;
                    break;
                case 41: // signo )
                    valid = true;
                    break;
                case 42: // signo *
                    valid = true;
                    break;
                case 43: // signo +
                    valid = true;
                    break;
                case 44: // signo +
                    valid = true;
                    break;
                case 45: // signo -
                    valid = true;
                    break;
                case 46: // signo .
                    valid = true;
                    break;
                case 47: // signo /
                    valid = true;
                    break;
                case 59: // signo ;
                    valid = true;
                    break;
                case 60: // signo <
                    valid = true;
                    break;
                case 61: // signo =
                    valid = true;
                    break;
                case 62: // signo >
                    valid = true;
                    break;
                case 91: // signo [
                    valid = true;
                    break;
                case 93: // signo ]
                    valid = true;
                    break;
               case 124: // signo |
                    valid = true;
                    break;
                default:
                    valid = false;
                    break;
            }
        return valid;
    }

    public static boolean isReserved(String word){
        if(Arrays.asList(Constants.reservedWords).contains(word))
            return true;
        return false;
    }
}
