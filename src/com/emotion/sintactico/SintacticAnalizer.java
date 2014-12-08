/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.emotion.sintactico;

import com.emotion.semantic.Cuadruplos;
import java.util.ArrayList;
import java.util.Stack;
import com.emotion.semantic.SemanticAnalizer;
import com.emotion.sintactico.Base;
import com.emotion.semantic.Symols;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

//import org.apache.commons.collections.MultiMap;
public class SintacticAnalizer {

    private ArrayList<Integer> lexemas = new ArrayList(); //numeros de producciones dados por el lexico
    private ArrayList<String> tokens = new ArrayList();   //nombres tambien dados por el lexico
    private int rowGramar = 0;  //renglon
    private int columnGramar = 0;   //columna
    private Stack<Integer> productions = new Stack<Integer>();
    private com.emotion.sintactico.Base sintacticBase = new Base();
    private String fileName;
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
        boolean status = true;
        boolean semanticStatus = true;
        int len = tokens.size();
        this.fileName = fileName;
        int semanticAction = 0;
        int i = 0;
        System.out.println("************  ANALISIS SINTACTICO  **************");
        while (i < len) {
            if (productions.empty() && rowGramar == 0) {
                status = fillFirtsProduction(lexemas.get(i), tokens.get(i));
            }

            int currentElement = productions.peek();
          // System.out.println("Tope de la pila:    " + currentElement);
           // System.out.println("Elemento del Lexico:    " + lexemas.get(i));
            if (currentElement == lexemas.get(i)) {  // comparacion con el resultado de lexico
                productions.pop();
                i++;
             //   System.out.println("_______________________________________________________________________________________");
                continue;
            } else {
                if (currentElement < 100) {
                //    System.out.println("Procesando a un no terminal");
                    productions.pop();
                    status = isValidToken(lexemas.get(i), tokens.get(i), currentElement);
                 //   System.out.println("_______________________________________________________________________________________");
                    if (status) {
                        continue;
                    }
                } else if (currentElement > 899) {
                    productions.pop();
                    if (!runSemanticAction(currentElement, tokens.get(i - 1), lexemas.get(i - 1))) {
                        semanticStatus = false;
                        status = false;
                        break;
                    }
                    continue;
                } else {
                    status = false;
                }
            }

            if (!status) {
                System.out.println("ERROR SINTACTICO cerca de: " + tokens.get(i));
                break;
            }
            i++;
       //     System.out.println("_______________________________________________________________________________________");
        }
        if (!semanticStatus) {
            System.out.println("ERROR SEMANTICO: " + semanticErrorMessage);
        }
        if (!productions.empty()) {
            System.out.println("ERROR SINTACTICO: La pila de producciones no esta vacia.");
            status = false;
            showStack();
        }
        sematicAalizer.showSymbolTable();
        return status;
    }

    private void showStack() {
        int size = productions.size();
        for (int i = 0; i < size; i++) {
        //    System.out.println("Elemento de la pila de producciones - " + productions.pop());
        }
    }

    private boolean fillFirtsProduction(int lexema, String token) {
        int column = sintacticBase.getColumnByToken(lexema, token);
      //  System.out.println("[" + rowGramar + " - " + column + "]");
        int prod = sintacticBase.gramar[rowGramar][column];
       // System.out.println("El valor de la matriz es:   " + prod);
        if (prod == 600) {
            return false;
        }
        insertNewElements(prod);
        return true;
    }

    private boolean isValidToken(int lexema, String token, int row) {
        int column = sintacticBase.getColumnByToken(lexema, token);
        int prod = sintacticBase.gramar[row][column];
     //   System.out.println("Columna en base a lexema: " + token);
     //   System.out.println("Produccion obtenida en posicion [" + row + " - " + column + "]: " + prod);
        if (prod == 600) {
            return false;
        }
        insertNewElements(prod);
        return true;
    }

    public void insertNewElements(int production) {
        List poductionLine = sintacticBase.getPoduccionesByIndex(production);
        int size = poductionLine.size();
        //Collections.reverse(poductionLine);
     //   System.out.println("Elementos de la produccion: " + production + "\n");
        for (int i = size - 1; i >= 0; i--) {
      //      System.out.print((int) poductionLine.get(i) + "   ");
            productions.add((int) poductionLine.get(i));
        }
    //    System.out.println("\n");

    }

    private boolean runSemanticAction(int semanticAction, String value, int lexema) {
      //  System.out.println("Accion: " + semanticAction + "  Valor a analizar: " + value + "  fileName " + fileName);
        boolean status = true;
        Symols symbol = new Symols();
        switch (semanticAction) {
            case 900:  //Nombre de la clase
                if (!fileName.equals(value + ".lya")) {
                    status = false;
                    semanticErrorMessage = "Nombre de la clase no es el mismo al del archivo fuente.";
                }
                break;
            case 901: //Agregar variable a tabla de simbolos/
                if (!sematicAalizer.isVariable(value)) {
                    symbol.setName(value);
                    sematicAalizer.semanticSymbolTable.put(value, symbol);  //se agrega la variable a la tabla de simbolos
                    variables.push(value);
                } else {
                    status = false;    //La variable ya existe
                    semanticErrorMessage = "La variable ya ha sido declarada con anterioridad.";
                }
                break;
            case 902: // asignar tipo a variable
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
            case 903: // Estatuto de asignacion. Valida si variable existe, Meter en pila de operandos y pila de tipos
                status = sematicAalizer.isVariable(value);
                semanticErrorMessage = "La variable no existe.";
                if (status) {
                    sematicAalizer.operandos.push(value);
                    sematicAalizer.tipos.push(sematicAalizer.semanticSymbolTable.get(value).getType());
                }
                break;
            case 904: // Estatuto de asignacion.  Meter en pila de operadores =
                sematicAalizer.operadores.push(value);
                break;
            case 905: // Estatuto de asignacion. Validamos pila de operandos no este vacia. Validamos tipos. Generamos cuadruplo
                sematicAalizer.operandos.push(value);
                sematicAalizer.tipos.push(getType(lexema));
                if (sematicAalizer.operandos.empty() || sematicAalizer.operadores.empty() || sematicAalizer.tipos.empty()) {
                    status = false;
                    semanticErrorMessage = "No se puede generar cuadruplo, la pila de operandos esta vacia.";
                }
                if (status) {
                    String variable = sematicAalizer.operandos.pop();
                    String tipoVariable = sematicAalizer.tipos.pop();
                    if (sematicAalizer.operandos.empty() || sematicAalizer.tipos.empty()) {
                        status = false;
                        semanticErrorMessage = "No se puede generar cuadruplo, la pila de operandos esta vacia.";
                        break;
                    }
                    String operador = sematicAalizer.operadores.pop();
                    String resultado = sematicAalizer.operandos.pop();
                    String resultadoTipo = sematicAalizer.tipos.pop();
                    if (tipoVariable.equals(resultadoTipo)) {
                        Cuadruplos cuadruplo = new Cuadruplos();
                        cuadruplo.setOperador(operador);
                        cuadruplo.setOperando1(variable);
                        cuadruplo.setResultado(resultado);
                        sematicAalizer.cuadruplos.add(cuadruplo);
                    } else {
                        status = false;
                        semanticErrorMessage = "Incompatibilidad de tipos.";
                    }

                }
                break;
            case 906: // Estatuto IF push a parentesis
                sematicAalizer.operadores.push(value);
                break;
            case 907: // Estatuto IF pop a parentesis, crea el cuadruplo
                if (sematicAalizer.operandos.empty() || sematicAalizer.operadores.empty() || sematicAalizer.tipos.empty()) {
                    status = false;
                }
                if (status) {
                    String operador = "";
                    String tipoOperando1 = "";
                    String valueOperando1 = "";
                    Cuadruplos cuadruplo = null;
                    while (!sematicAalizer.operadores.peek().equals("(")) {
                        String operando = sematicAalizer.operandos.pop();
                        String operandoTipo = sematicAalizer.tipos.pop();
                        if (operador.equals("")) {
                            cuadruplo = new Cuadruplos();
                            operador = sematicAalizer.operadores.pop();
                            cuadruplo.setOperador(operador);
                            cuadruplo.setOperando1(operando);
                            tipoOperando1 = operandoTipo;
//                            valueOperando1 = getValue(operando);
                        } else {
                            if (tipoOperando1.equals(operandoTipo)) {
                                cuadruplo.setOperando2(operando);
                                String temporal = sematicAalizer.temporal.pop();
//                                String tipoTemporal = getValue(cuadruplo.getOperando1(), cuadruplo.getOperando2(), cuadruplo.getOperador(), tipoOperando1, operandoTipo);
                                cuadruplo.setResultado(temporal);
                                sematicAalizer.cuadruplos.add(cuadruplo);
                                sematicAalizer.operandos.push(temporal);
                                sematicAalizer.tipos.push(operandoTipo);
                                operador = "";
                            } else {
                                status = false;
                                semanticErrorMessage = "Error de tipos.";
                            }
                        }
                        if (sematicAalizer.operandos.empty() || sematicAalizer.operadores.empty() || sematicAalizer.tipos.empty()) {
                            status = false;
                            semanticErrorMessage = "No se pudo completar de evaluar la expresion, las pilas estan vacias.";
                            break;
                        }
                    }
                }
                break;
            default:
                status = false;
        }
        return status;
    }

    private String getType(int lexema) {
        String type = "";
        switch (lexema) {
            case 102:
                type = "int";
                break;
            case 103:
                type = "float";
                break;
            case 104:
                type = "float";
                break;
            case 106:
                type = "char";
                break;
            case 107:
                type = "string";
                break;
        }
        return type;
    }

//    private String getValue(String operando1, String operando2, String operador, String tipoOperando1, String operandoTipo) {
//        String stringValue1 = "";
//        String stringValue2 = "";
//        Object value1;
//        Object value2;
//        if (sematicAalizer.isVariable(operando1)) 
//            stringValue1 = sematicAalizer.semanticSymbolTable.get(operando1).getValue();
//        else
//            stringValue1 = operando1;
//        
//        if (sematicAalizer.isVariable(operando2)) 
//            stringValue2 = sematicAalizer.semanticSymbolTable.get(operando2).getValue();
//        else
//            stringValue2 = operando1;
//
//        if (operador.equals("int")) {
//            value1 = new Integer(stringValue1);
//        } else if (operador.equals("float")) {
//            value1 = new Float(stringValue1);
//        } else {
//            value1 = stringValue1;
//        }
//        
//        if (operador.equals("int")) {
//            value2 = new Integer(stringValue2);
//        } else if (operador.equals("float")) {
//            value2 = new Float(stringValue2);
//        } else {
//            value2 = stringValue2;
//        }
//        String resultResult;
//        switch(operador){
//            case "+":
//                Integer result;
//        result = (value1 + value2);
//        }
//    }
//    
//        
//    private Object getRealValue(String tipo, String operando){
//        if (tipo.equals("int")) {
//            return new Integer(operando);
//        } else if (tipo.equals("float")) {
//            return new Float(operando);
//        } else {
//            return new String(operando);
//        }
//    }
//
//    private void performOperation(Object value1, Object value2, String operator) {
//        Object result = null;
//        switch(operator){
//            case "+":
//                result = value1 + value2;
//                break;
//        }
//    }
}
