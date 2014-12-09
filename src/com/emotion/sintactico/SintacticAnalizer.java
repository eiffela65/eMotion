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
                System.out.println("ERROR: Gramatica no se cumple cerca de: " + tokens.get(i));
                break;
            }
            i++;
            //     System.out.println("_______________________________________________________________________________________");
        }

        if (status) {
            System.out.println("INFO: Analisis Sintactico completo sin errores");
        } else {
            System.out.println("WARNING: Analisis Sintactico detenido.");
            System.out.println("************  ANALISIS SEMANTICO  **************");
            if (!semanticStatus) {
                System.out.println("ERROR: " + semanticErrorMessage);
            }
            if (!productions.empty()) {
                System.out.println("ERROR: La pila de producciones no esta vacia.");
                status = false;
                showStack();
            }
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
        String operador = "";
        String variable = "";
        String tipoVariable = "";
        Symols symbol = new Symols();
        Cuadruplos cuadruplo = new Cuadruplos();
        switch (semanticAction) {
            case 900:  //Nombre de la clase
                if (!fileName.equals(value + ".lya")) {
                    status = false;
                    semanticErrorMessage = "ERROR: Nombre de la clase no es el mismo al del archivo fuente.";
                }
                break;
            case 901: //Agregar variable a tabla de simbolos/
                if (!sematicAalizer.isVariable(value)) {
                    symbol.setName(value);
                    sematicAalizer.semanticSymbolTable.put(value, symbol);  //se agrega la variable a la tabla de simbolos
                    variables.push(value);
                } else {
                    status = false;    //La variable ya existe
                    semanticErrorMessage = "ERROR: La variable ya ha sido declarada con anterioridad.";
                }
                break;
            case 902: // asignar tipo a variable
                if (variables.empty()) {
                    status = false;    //La variable ya existe
                    semanticErrorMessage = "ERROR: No existen variables en la pila de variables.";
                } else {
                    for (int i = 0; i < variables.size(); i++) {
                        variable = variables.pop();
                        if (!sematicAalizer.isVariable(variable)) {
                            status = false;    //La variable ya existe
                            semanticErrorMessage = "ERROR: La variable " + variable + " no existe.";
                        } else {
                            if (sematicAalizer.semanticSymbolTable.get(variable).getType() != null) { //si ya tiene un tipo asignado
                                status = false;
                                semanticErrorMessage = "ERROR: La variable " + variable + " ya tiene un tipo asignado.";
                                break;
                            }
                            symbol = sematicAalizer.semanticSymbolTable.get(variable);
                            symbol.setType(value);
                            sematicAalizer.semanticSymbolTable.put(variable, symbol);
                        }
                    }
                }
                break;
            case 903:// Se agrega una constante entera a la pila de operandos con su tipo
                sematicAalizer.operandos.push(value);
                sematicAalizer.tipos.push("int");
                break;
            case 904:// Se agrega una constante float a la pila de operandos con su tipo
                sematicAalizer.operandos.push(value);
                sematicAalizer.tipos.push("float");
                break;
            case 905:// Se agrega una constante notacion a la pila de operandos con su tipo
                sematicAalizer.operandos.push(value);
                sematicAalizer.tipos.push("float");
                break;
            case 906:// Se agrega una constante char a la pila de operandos con su tipo
                sematicAalizer.operandos.push(value);
                sematicAalizer.tipos.push("char");
                break;
            case 907:// Se agrega una constante string a la pila de operandos con su tipo
                sematicAalizer.operandos.push(value);
                sematicAalizer.tipos.push("string");
                break;
            case 908:// Se agrega ( dentro de una expresion
                sematicAalizer.operandos.push(value);
                break;
            case 909:// Se ejecuta la notacion polaca para vaciar la pila de operandos y tipos hasta llegar con un 
                while (!sematicAalizer.operadores.peek().equals(")")) {
                    status = buildCuadruplosByNotacionPolaca();
                    if (!status) {
                        break;
                    }
                }
                break;
            case 910:
                if (sematicAalizer.isEmptyOperadoresStack()) {
                    status = false;
                    semanticErrorMessage = "ERROR: La pila de operadores esta vacia.";
                }
                operador = sematicAalizer.operadores.peek();
                switch (operador) {
                    case "*":
                        status = buildCuadruplosByNotacionPolaca();
                        break;
                    case "/":
                        status = buildCuadruplosByNotacionPolaca();
                        break;
                    case "%":
                        status = buildCuadruplosByNotacionPolaca();
                        break;
                    default:
                        status = false;
                        semanticErrorMessage = "ERROR: Operador no permitido. Se esperaba *, /, % obteniendo " + operador;
                }
                break;
            case 911: //optimizacion al usar una accion para meter operadores en la pila englobando todos enn una
                sematicAalizer.operadores.push(value);
                break;
            case 912: //optimizacion al usar una accion para meter operadores en la pila englobando todos enn una
                sematicAalizer.operadores.push(value);
                break;
            case 913: //optimizacion al usar una accion para meter operadores en la pila englobando todos enn una
                sematicAalizer.operadores.push(value);
                break;
            case 914:
                if (sematicAalizer.isEmptyOperadoresStack()) {
                    status = false;
                    semanticErrorMessage = "ERROR: La pila de operadores esta vacia.";
                }
                operador = sematicAalizer.operadores.peek();
                switch (operador) {
                    case "+":
                        status = buildCuadruplosByNotacionPolaca();
                        break;
                    case "-":
                        status = buildCuadruplosByNotacionPolaca();
                        break;
                    default:
                        status = false;
                        semanticErrorMessage = "ERROR: Operador no permitido. Se esperaba +, - obteniendo " + operador;
                }
                break;
            case 915: //optimizacion al usar una accion para meter operadores en la pila englobando todos enn una
                sematicAalizer.operadores.push(value);
                break;
            case 916: //optimizacion al usar una accion para meter operadores en la pila englobando todos enn una
                sematicAalizer.operadores.push(value);
                break;
            case 917: //optimizacion al usar una accion para meter operadores en la pila englobando todos enn una
                sematicAalizer.operadores.push(value);
                break;
            case 918: //optimizacion al usar una accion para meter operadores en la pila englobando todos enn una
                status = buildCuadruplosByNotacionPolaca();
                break;
            case 919: //optimizacion al usar una accion para meter operadores en la pila englobando todos enn una
                sematicAalizer.operadores.push(value);
                break;
            case 920: //optimizacion al usar una accion para meter operadores en la pila englobando todos enn una
                status = buildCuadruplosByNotacionPolaca();
                break;
            case 921: //optimizacion al usar una accion para meter operadores en la pila englobando todos enn una
                status = buildCuadruplosByNotacionPolaca();
                break;
            case 922: //optimizacion al usar una accion para meter operadores en la pila englobando todos enn una
                sematicAalizer.operadores.push(value);
                break;
            case 923: //optimizacion al usar una accion para meter operadores en la pila englobando todos enn una
                status = buildCuadruplosByNotacionPolaca();
                break;
            case 924: //optimizacion al usar una accion para meter operadores en la pila englobando todos enn una
                sematicAalizer.operadores.push(value);
                break;
            case 925: // Estatuto de asignacion. Valida si variable existe, Meter en pila de operandos y pila de tipos
                status = sematicAalizer.isVariable(value);
                semanticErrorMessage = "ERROR: La variable no existe.";
                if (status) {
                    sematicAalizer.operandos.push(value);
                    sematicAalizer.tipos.push(sematicAalizer.semanticSymbolTable.get(value).getType());
                }
                break;
            case 926: // Estatuto de asignacion.  Meter en pila de operadores =
                sematicAalizer.operadores.push(value);
                break;
            case 927: // Estatuto de asignacion. Validamos pila de operandos no este vacia. Validamos tipos. Generamos cuadruplo
                if (sematicAalizer.isEmptyOperadoresStack() || sematicAalizer.isEmptyOperandosStack() || sematicAalizer.isEmptyTiposStack()) {
                    semanticErrorMessage = "ERROR: Pilas vacias, no se puede continuar con el analisis.";
                    status = false;
                    break;
                }
                if (status) {
                    variable = sematicAalizer.operandos.pop();
                    tipoVariable = sematicAalizer.tipos.pop();
                    operador = sematicAalizer.operadores.pop();
                    if (sematicAalizer.isEmptyOperandosStack() || sematicAalizer.isEmptyTiposStack()) {
                        semanticErrorMessage = "ERROR: Pilas vacias, no se puede continuar con el analisis.";
                        status = false;
                        break;
                    }
                    String resultado = sematicAalizer.operandos.pop();
                    String resultadoTipo = sematicAalizer.tipos.pop();
                    if (tipoVariable.equals(resultadoTipo)) {
                        cuadruplo = new Cuadruplos();
                        cuadruplo.setOperador(operador);
                        cuadruplo.setOperando1(variable);
                        cuadruplo.setResultado(resultado);
                        sematicAalizer.cuadruplos.add(cuadruplo);
                    } else {
                        status = false;
                        semanticErrorMessage = "ERROR: Incompatibilidad de tipos.";
                    }

                }
                break;
            case 928: // Estatuto IF 
                if (sematicAalizer.isEmptySaltosStack()) {
                    semanticErrorMessage = "ERROR: Pila de saltos vacia, no se puede continuar con el analisis.";
                    status = false;
                    break;
                }
                variable = sematicAalizer.operandos.pop();
                tipoVariable = sematicAalizer.tipos.pop();
                if (tipoVariable.equals("boolean")) {
                    cuadruplo = new Cuadruplos();
                    if (variable.equals("FALSE")) {
                        sematicAalizer.saltos.push(sematicAalizer.cuadruplos.size());
                        cuadruplo.setOperador("GOTOF");
                        sematicAalizer.cuadruplos.add(cuadruplo);
                    }
                } else {
                    semanticErrorMessage = "ERROR: Tipo de dato no permitido en el estatuto IF.";
                    status = false;
                }
                break;
            case 929: // Estatuto ELSE, crea el cuadruplo
                if (sematicAalizer.isEmptySaltosStack()) {
                    semanticErrorMessage = "ERROR: Pila de saltos vacia, no se puede continuar con el analisis.";
                    status = false;
                    break;
                }
                sematicAalizer.saltos.push(sematicAalizer.cuadruplos.size());
                cuadruplo = new Cuadruplos();
                cuadruplo.setOperador("GOTOF");
                sematicAalizer.cuadruplos.add(cuadruplo);
                break;
            case 930: // Estatuto ENDIF, crea el cuadruplo
                if (sematicAalizer.isEmptySaltosStack()) {
                    semanticErrorMessage = "ERROR: Pila de saltos vacia, no se puede continuar con el analisis.";
                    status = false;
                    break;
                }
                int saltoPendiente = sematicAalizer.saltos.pop();
                cuadruplo = sematicAalizer.cuadruplos.get(saltoPendiente);
                cuadruplo.setSalto(sematicAalizer.cuadruplos.size());
                sematicAalizer.cuadruplos.set(saltoPendiente, cuadruplo);
                break;
            case 931: // Estatuto WHILE, crea el cuadruplo
                sematicAalizer.saltos.push(sematicAalizer.cuadruplos.size());
                break;
            case 932: // Estatuto WHILE, crea el cuadruplo
                if (sematicAalizer.isEmptySaltosStack()) {
                    semanticErrorMessage = "ERROR: Pila de saltos vacia, no se puede continuar con el analisis.";
                    status = false;
                    break;
                }
                variable = sematicAalizer.operandos.pop();
                tipoVariable = sematicAalizer.tipos.pop();
                if (tipoVariable.equals("boolean")) {
                    cuadruplo = new Cuadruplos();
                    if (variable.equals("FALSE")) {
                        cuadruplo.setOperador("GOTOF");
                        sematicAalizer.cuadruplos.add(cuadruplo);
                    }
                } else {
                    semanticErrorMessage = "ERROR: Tipo de dato no permitido en el estatuto IF.";
                    status = false;
                }
                break;
            default:
                status = false;
        }
        return status;
    }

    private boolean buildCuadruplosByNotacionPolaca() {
        String[] triplo = new String[2];
        if (sematicAalizer.isEmptyOperadoresStack() || sematicAalizer.isEmptyOperandosStack() || sematicAalizer.isEmptyTiposStack()) {
            semanticErrorMessage = "ERROR: Pilas vacias, no se puede continuar con el analisis.";
            return false;
        }
        String operando1 = sematicAalizer.operandos.pop();
        String operador = sematicAalizer.operadores.pop();
        String tipoOperador1 = sematicAalizer.tipos.pop();

        // Solo cuando es la negacion
        if (operador.equals("!")) {
            triplo = setNegation(operando1, tipoOperador1);
            if (triplo == null) {
                semanticErrorMessage = "ERROR: La operacion '" + operador + "' no puede realizar con el operando: '" + operando1 + "' por ser de tipo '" + tipoOperador1 + "'";
                return false;
            }
            return true;
        }

        if (sematicAalizer.isEmptyOperandosStack()|| sematicAalizer.isEmptyTiposStack()) {
            semanticErrorMessage = "ERROR: Pilas vacias, no se puede continuar con el analisis.";
            return false;
        }
        String operando2 = sematicAalizer.operandos.pop();
        String tipoOperador2 = sematicAalizer.tipos.pop();
        if (!isValidTypes(tipoOperador1, tipoOperador2)) {
            semanticErrorMessage = "ERROR: Pilas vacias, no se puede continuar con el analisis.";
            return false;
        }
        triplo = validateOperandosAndOperator(operando1, operando2, operador, tipoOperador1);
        if (triplo == null) {
            semanticErrorMessage = "ERROR: La operacion '" + operador + "' no puede realizar entre los operandos: '" + operando1 + "' y '" + operando2 + "'";
            return false;
        }
        Cuadruplos cuadruplo = new Cuadruplos();
        cuadruplo.setOperador(operador);
        cuadruplo.setOperando1(operando1);
        cuadruplo.setOperando2(operando2);
        cuadruplo.setResultado(triplo[0]);
        sematicAalizer.cuadruplos.add(cuadruplo);
        sematicAalizer.operandos.push(triplo[0]);
        sematicAalizer.tipos.push(triplo[1]);
        return true;
    }

    private boolean isValidTypes(String tipo1, String tipo2) {
        if (tipo1.equals(tipo2)) {
            return true;
        }
        return false;
    }

    private String[] setNegation(String operando1, String tipo) {
        String[] triplo = new String[2];
        if (tipo.equals("boolean")) {
            if (operando1.equals("true")) {
                triplo[0] = "false";
                triplo[1] = tipo;
            } else {
                triplo[0] = "true";
                triplo[1] = tipo;
            }
            return triplo;
        }
        return null;
    }

    private String[] validateOperandosAndOperator(String operando1, String operando2, String operador, String tipo) {
        String[] triplo = new String[2];
        switch (operador) {
            case "+":
                switch (tipo) {
                    case "int":
                        int op1 = Integer.parseInt(operando1);
                        int op2 = Integer.parseInt(operando2);
                        triplo[0] = Integer.toString(op1 + op2);
                        triplo[1] = tipo;
                        break;
                    case "float":
                        float opf1 = Float.parseFloat(operando1);
                        float opf2 = Float.parseFloat(operando2);
                        triplo[0] = Float.toString(opf1 + opf2);
                        triplo[1] = tipo;
                        break;
                    case "char":
                        triplo[0] = operando1 + operando2;
                        triplo[1] = tipo;
                        break;
                    case "string":
                        triplo[0] = operando1 + operando2;
                        triplo[1] = tipo;
                        break;
                    default:
                        triplo = null;
                }
                break;
            case "-":
                switch (tipo) {
                    case "int":
                        int op1 = Integer.parseInt(operando1);
                        int op2 = Integer.parseInt(operando2);
                        triplo[0] = Integer.toString(op1 - op2);
                        triplo[1] = tipo;
                        break;
                    case "float":
                        float opf1 = Float.parseFloat(operando1);
                        float opf2 = Float.parseFloat(operando2);
                        triplo[0] = Float.toString(opf1 - opf2);
                        triplo[1] = tipo;
                        break;
                    default:
                        triplo = null;
                }
                break;
            case "*":
                switch (tipo) {
                    case "int":
                        int op1 = Integer.parseInt(operando1);
                        int op2 = Integer.parseInt(operando2);
                        triplo[0] = Integer.toString(op1 * op2);
                        triplo[1] = tipo;
                        break;
                    case "float":
                        float opf1 = Float.parseFloat(operando1);
                        float opf2 = Float.parseFloat(operando2);
                        triplo[0] = Float.toString(opf1 * opf2);
                        triplo[1] = tipo;
                        break;
                    default:
                        triplo = null;
                }
                break;
            case "/":
                switch (tipo) {
                    case "int":
                        int op1 = Integer.parseInt(operando1);
                        int op2 = Integer.parseInt(operando2);
                        triplo[0] = Integer.toString(op1 / op2);
                        triplo[1] = tipo;
                        break;
                    case "float":
                        float opf1 = Float.parseFloat(operando1);
                        float opf2 = Float.parseFloat(operando2);
                        triplo[0] = Float.toString(opf1 / opf2);
                        triplo[1] = tipo;
                        break;
                    default:
                        triplo = null;
                }
                break;
            case "%":
                switch (tipo) {
                    case "int":
                        int op1 = Integer.parseInt(operando1);
                        int op2 = Integer.parseInt(operando2);
                        triplo[0] = Integer.toString(op1 % op2);
                        triplo[1] = tipo;
                        break;
                    case "float":
                        float opf1 = Float.parseFloat(operando1);
                        float opf2 = Float.parseFloat(operando2);
                        triplo[0] = Float.toString(opf1 % opf2);
                        triplo[1] = tipo;
                        break;
                    default:
                        triplo = null;
                }
                break;
            case "==":
                switch (tipo) {
                    case "int":
                        int op1 = Integer.parseInt(operando1);
                        int op2 = Integer.parseInt(operando2);
                        triplo[0] = (op1 == op2) ? "true" : "false";
                        triplo[1] = "boolean";
                        break;
                    case "float":
                        float opf1 = Float.parseFloat(operando1);
                        float opf2 = Float.parseFloat(operando2);
                        triplo[0] = (opf1 == opf2) ? "true" : "false";
                        triplo[1] = "boolean";
                        break;
                    case "char":
                        triplo[0] = (operando1.equals(operando2)) ? "true" : "false";
                        triplo[1] = "boolean";
                        break;
                    case "string":
                        triplo[0] = (operando1.equals(operando2)) ? "true" : "false";
                        triplo[1] = "boolean";
                        break;
                    case "boolean":
                        triplo[0] = (operando1.equals(operando2)) ? "true" : "false";
                        triplo[1] = "boolean";
                        break;
                    default:
                        triplo = null;
                }
                break;
            case "!=":
                switch (tipo) {
                    case "int":
                        int op1 = Integer.parseInt(operando1);
                        int op2 = Integer.parseInt(operando2);
                        triplo[0] = (op1 != op2) ? "true" : "false";
                        triplo[1] = "boolean";
                        break;
                    case "float":
                        float opf1 = Float.parseFloat(operando1);
                        float opf2 = Float.parseFloat(operando2);
                        triplo[0] = (opf1 != opf2) ? "true" : "false";
                        triplo[1] = "boolean";
                        break;
                    case "char":
                        triplo[0] = (!operando1.equals(operando2)) ? "true" : "false";
                        triplo[1] = "boolean";
                        break;
                    case "string":
                        triplo[0] = (!operando1.equals(operando2)) ? "true" : "false";
                        triplo[1] = "boolean";
                        break;
                    case "boolean":
                        triplo[0] = (!operando1.equals(operando2)) ? "true" : "false";
                        triplo[1] = "boolean";
                        break;
                    default:
                        triplo = null;
                }
                break;
            case "<":
                switch (tipo) {
                    case "int":
                        int op1 = Integer.parseInt(operando1);
                        int op2 = Integer.parseInt(operando2);
                        triplo[0] = (op1 < op2) ? "true" : "false";
                        triplo[1] = "boolean";
                        break;
                    case "float":
                        float opf1 = Float.parseFloat(operando1);
                        float opf2 = Float.parseFloat(operando2);
                        triplo[0] = (opf1 < opf2) ? "true" : "false";
                        triplo[1] = "boolean";
                        break;
                    default:
                        triplo = null;
                }
                break;
            case "<=":
                switch (tipo) {
                    case "int":
                        int op1 = Integer.parseInt(operando1);
                        int op2 = Integer.parseInt(operando2);
                        triplo[0] = (op1 <= op2) ? "true" : "false";
                        triplo[1] = "boolean";
                        break;
                    case "float":
                        float opf1 = Float.parseFloat(operando1);
                        float opf2 = Float.parseFloat(operando2);
                        triplo[0] = (opf1 <= opf2) ? "true" : "false";
                        triplo[1] = "boolean";
                        break;
                    default:
                        triplo = null;
                }
                break;
            case ">":
                switch (tipo) {
                    case "int":
                        int op1 = Integer.parseInt(operando1);
                        int op2 = Integer.parseInt(operando2);
                        triplo[0] = (op1 > op2) ? "true" : "false";
                        triplo[1] = "boolean";
                        break;
                    case "float":
                        float opf1 = Float.parseFloat(operando1);
                        float opf2 = Float.parseFloat(operando2);
                        triplo[0] = (opf1 > opf2) ? "true" : "false";
                        triplo[1] = "boolean";
                        break;
                    default:
                        triplo = null;
                }
                break;
            case ">=":
                switch (tipo) {
                    case "int":
                        int op1 = Integer.parseInt(operando1);
                        int op2 = Integer.parseInt(operando2);
                        triplo[0] = (op1 >= op2) ? "true" : "false";
                        triplo[1] = "boolean";
                        break;
                    case "float":
                        float opf1 = Float.parseFloat(operando1);
                        float opf2 = Float.parseFloat(operando2);
                        triplo[0] = (opf1 >= opf2) ? "true" : "false";
                        triplo[1] = "boolean";
                        break;
                    default:
                        triplo = null;
                }
                break;
            case "&&":
                switch (tipo) {
                    case "boolean":
                        boolean op1 = Boolean.parseBoolean(operando1);
                        boolean op2 = Boolean.parseBoolean(operando2);
                        triplo[0] = (op1 && op2) ? "true" : "false";
                        triplo[1] = "boolean";
                        break;
                    default:
                        triplo = null;
                }
                break;
            case "||":
                switch (tipo) {
                    case "boolean":
                        boolean op1 = Boolean.parseBoolean(operando1);
                        boolean op2 = Boolean.parseBoolean(operando2);
                        triplo[0] = (op1 || op2) ? "true" : "false";
                        triplo[1] = "boolean";
                        break;
                    default:
                        triplo = null;
                }
                break;
        }
        return null;
    }

}
