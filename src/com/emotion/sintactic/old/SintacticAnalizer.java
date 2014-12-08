/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.emotion.sintactic.old;

import java.util.ArrayList;
import java.util.Stack;
import com.emotion.semantic.SemanticAnalizer;
import com.emotion.semantic.Symols;

//import org.apache.commons.collections.MultiMap;
public class SintacticAnalizer {

    private ArrayList<Integer> lexemas = new ArrayList(); //numeros de producciones dados por el lexico
    private ArrayList<String> tokens = new ArrayList();   //nombres tambien dados por el lexico
    private int rowGramar = 0;  //renglon
    private int columnGramar = 0;   //columna
    private int nextRowGramar = 0;  
    private boolean matrix = true; //bandera para cambio de matrices
    private Stack<Integer> expresion = new Stack<Integer>(); //pila en la que se guardan los estados que acceden a la matriz expresion
    private int parenthesis = 0; //para los parentesis
    private com.emotion.semantic.Base semanticBase = new com.emotion.semantic.Base();
    private SemanticAnalizer sematicAalizer = new SemanticAnalizer();
    private Stack<String> variables = new Stack<String>();
    private String semanticErrorMessage;

    public SintacticAnalizer() {
        setLexemas(lexemas);
        setTokens(tokens);

    }

    public void setRowGramar(int rowGramar) {  //que comiencen desde 0 para el analisis
        this.rowGramar = rowGramar;
    }

    public void setColumnGramar(int columnGramar) {
        this.columnGramar = columnGramar;
    }

    public void setLexemas(ArrayList<Integer> lexemas) { //pasar lexemas 
        this.lexemas = lexemas;
    }

    public ArrayList<Integer> getLexemas() {
        return lexemas;
    }

    public void setTokens(ArrayList<String> tokens) { //pasar tokens
        this.tokens = tokens;
    }

    public ArrayList<String> getTokens() { //meter tokens en arrayList 
        return tokens;
    }

    public boolean startAnalisis(String fileName) { 
        int len = tokens.size();
        boolean status = false;
        String lexema = "";
        Base baseSintactical = new Base(); 
        int lastParenthesis = 0;
        int lastRow = 0;
        int i = 0;
        int semanticAction = 0;
        System.out.println("************  ANALISIS SINTACTICO  **************");
        while (i < len) { //para que recorra el programa tomando los tokens mientras que no sean 0
//            System.out.println("-------------------------------------------------------");
//            System.out.println(tokens.get(i) + " ----> [" + rowGramar + " - " + Base.getColumn(lexemas.get(i)) + "]");

            columnGramar = Base.getColumn(lexemas.get(i));
            baseSintactical.setColumnGrammar(columnGramar);
            baseSintactical.setRowGrammar(rowGramar);
            rowGramar = baseSintactical.getNewRow(matrix); 
//            System.out.println("Nuevo estado   " + rowGramar);
            if (rowGramar == 600) { //ERROR 
                lexema = tokens.get(i);
                break;
            }
            if (rowGramar == 400) { //si se resolvio la expresion
                int element = expresion.peek();
                if (expresion.empty()) {   //si la pila vacia se detiene
//                    System.out.println("Pila de expresiones vacia");
                    break;
                }
                if (columnGramar == 20) { //si es un parentesis que cierra se decrementa parentesis
                    parenthesis--;
                    if (element < 301) {
                        i++;
                    }
//                    System.out.println("@@@@@@@@@@ se quito un parentesis");
                }
                if (parenthesis > 0) { //si parenthesis es mayor aun no termina de evaluar la expresion                                                suma resta de parentesis para saber si se concluye de evaluar la expresion
                    matrix = false; //para indicar que se sigue evaluando en la matriz de expresion
                    rowGramar = 1;  //viene operador relacional aritmetico o logico
                } else {
                    matrix = true; //termino de evaluar expresion regresar a matriz principal
                    rowGramar = nextRow(expresion.pop()); 
                }
                continue;
            }
            if (!matrix && columnGramar == 19 && lastParenthesis != i) { //porque es parte de la gramatica no de la expresion
//                System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% se agrego un parentesis");
                parenthesis++;
            }
            if (rowGramar > 299 && rowGramar < 400) { //aqui se pasa a la matriz expresion
                matrix = false; //bandera en false para que se lea la matriz de expresion
                expresion.push(rowGramar); //metemos en la pila la produccion de entrada a expresion que puede ser 300 301
                rowGramar = 0; //empezar en el renglon 0
                if (columnGramar == 19) { //si se encuentra con un parentesis que abre 
                    parenthesis++; //aumenta parentesis para el control de los mismos
//                    System.out.println("############### se agrego un parentesis");
                    lastParenthesis = i;
                }
                continue; //porque los parentesis pueden ser infinitos
            }

//            System.out.println(lexemas.get(i) + "  -  " + tokens.get(i));
            if (rowGramar == 200) //manejo de estatutos y declaraciones
            {
                rowGramar = getRowByEstatus(tokens.get(i), lastRow);
            }

            if (rowGramar == 37) { //encontro un endclass
                status = true;
            }
            lastRow = rowGramar;
            i++;
//            System.out.println(status);

            if (semanticBase.hasSemanticAction(rowGramar)) {
                semanticAction = semanticBase.getActionByProduction(rowGramar);
                if (!runSemanticAction(semanticAction, tokens.get(i - 1), fileName)) {
                    System.out.println("Error semantico :  " + semanticErrorMessage);
                    break;
                }
            }
        }
        if (!status) {
            System.out.println("Error sintactico cerca de:  " + lexema);
            return status;
        }
        System.out.println("Fin de analisis sintactico");

        sematicAalizer.showSymbolTable();
        return status;
    }

    private int nextRow(int typeExpresion) {
        int row = 0;
        switch (typeExpresion) {
            case 300: //ASIGNACION
                row = 18;
                break;
            case 301: //ASIGNACION [ ]
                row = 16;
                break;
            case 302:  //IF
                row = 21;
                break;
            case 303:  //WHILE
                row = 25;
                break;
            case 304: //WRITE
                row = 34;
                break;
        }
        return row;
    }

    private int getRowByEstatus(String word, int curentRow) {
        int row = 600;
        switch (word) {
            case "declare":
                row = 5;
                break;
            case "if":
                row = 19;
                break;
            case "else":
                if (curentRow == 4) {
                    row = 600;
                } else {
                    row = 12;
                }
                break;
            case "endif":
                if (curentRow == 4) {
                    row = 600;
                } else {
                    row = 22;
                }
                break;
            case "while":
                row = 23;
                break;
            case "endwhile":
                if (curentRow == 4) {
                    row = 600;
                } else {
                    row = 26;
                }
                break;
            case "do":
                row = 12;
                break;
            case "dowhile":
                row = 23;
                break;
            case "enddo":
                if (curentRow == 4) {
                    row = 600;
                } else {
                    row = 27;
                }
                break;
            case "read":
                row = 28;
                break;
            case "write":
                row = 32;
                break;
            case "endclass":
                row = 37;
                break;
        }
        return row;
    }

    private boolean runSemanticAction(int semanticAction, String value, String fileName) {
//        System.out.println("Accion: " + semanticAction + "  Valor a analizar: " + value + "  fileName " + fileName);
        boolean status = true;
        Symols symbol = new Symols();
        switch (semanticAction) {
            case 900:
                if (!fileName.equals(value + ".lya")) {
                    status = false;
                    semanticErrorMessage = "Nombre de la clase no es el mismo al del archivo fuente.";
                }
                break;
            case 901:
                if (!sematicAalizer.isVariable(value)) {
                    symbol.setName(value);
                    sematicAalizer.semanticSymbolTable.put(value, symbol);  //se agrega la variable a la tabla de simbolos
                    variables.push(value);
                } else {
                    status = false;    //La variable ya existe
                    semanticErrorMessage = "La variable ya ha sido declarada con anterioridad.";
                }
                break;
            case 902:
                if (variables.empty()) {
                    status = false;    //La variable ya existe
                    semanticErrorMessage = "No existen variables en la pila de variables.";
                } else {
                    for (int i = 0; i < variables.size(); i++) {
                        String variable = variables.pop();
                        if (!sematicAalizer.isVariable(variable)) {
                            status = false;    //La variable ya existe
                            semanticErrorMessage = "No se puede asignar tipo a variable que no ha sido declarada.";
                        } else {
                            if (sematicAalizer.semanticSymbolTable.get(variable).getType() != null) { //si ya tiene un tipo asignado
                                status = false;
                                semanticErrorMessage = "La variable ya tiene un tipo asignado.";
                                break;
                            }
                            symbol = sematicAalizer.semanticSymbolTable.get(variable);
                            symbol.setType(value);
                            sematicAalizer.semanticSymbolTable.put(variable, symbol);
                        }
                    }
                }
                break;
            default:
                status = false;
        }
        return status;
    }
}
