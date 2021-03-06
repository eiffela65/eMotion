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
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
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
    private int tempIndex = 0;

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
        int i = 0;
        System.out.println("************  ANALISIS SINTACTICO  **************");
        while (i < len) {
            if (productions.empty() && rowGramar == 0) {
                status = fillFirtsProduction(lexemas.get(i), tokens.get(i));
            }
            
            if(productions.empty()){
                status = false;
                break;
            }

            int currentElement = productions.peek();
            if (currentElement == lexemas.get(i)) {  // comparacion con el resultado de lexico
                productions.pop();
                i++;
//                if (productions.size() < 1) {
//                    break;
//                }
                continue;
            } else {
                if (currentElement < 100) {
                    productions.pop();
                    status = isValidToken(lexemas.get(i), tokens.get(i), currentElement);
                    if (status) {
                        continue;
                    }
                } else if (currentElement > 899) {
                    productions.pop();
                    if (!runSemanticAction(currentElement, tokens.get(i - 1))) {
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
                break;
            }
            i++;
        }

        if (status) {
            if(productions.empty()){
                System.out.println("INFO: Analisis Sintactico completo sin errores");
                System.out.println("************  ANALISIS SEMANTICO  **************");
                generateFile();
            }else{
                System.out.println("ERROR: No hay mas elementos a analizar y la gramatica aun no se cumple.");
            }
        } else {
            if (!semanticStatus) {
                System.out.println("WARNING: Analisis Sintactico detenido.");
                System.out.println("************  ANALISIS SEMANTICO  **************");
                System.out.println(semanticErrorMessage);
            }else{
                System.out.println("ERROR: Analisis Sintactico detenido.");
            }
            if (!productions.empty()) {
                System.out.println("ERROR: La pila de producciones no esta vacia.");
                status = false;
            }
        }
        return status;
    }

    private boolean fillFirtsProduction(int lexema, String token) {
        int column = sintacticBase.getColumnByToken(lexema, token);
        int prod = sintacticBase.gramar[rowGramar][column];
        if (prod == 600) {
            return false;
        }
        insertNewElements(prod);
        return true;
    }

    private boolean isValidToken(int lexema, String token, int row) {
        int column = sintacticBase.getColumnByToken(lexema, token);
        int prod = sintacticBase.gramar[row][column];
        if (prod == 600) {
            return false;
        }
        insertNewElements(prod);
        return true;
    }

    public void insertNewElements(int production) {
        List poductionLine = sintacticBase.getPoduccionesByIndex(production);
        int size = poductionLine.size();
        for (int i = size - 1; i >= 0; i--) {
            productions.add((int) poductionLine.get(i));
        }

    }

    private boolean runSemanticAction(int semanticAction, String value) {
        boolean status = true;
        String operador = "";
        String variable = "";
        String tipoVariable = "";
        Symols symbol = new Symols();
        Cuadruplos cuadruplo = new Cuadruplos();
        String temVar = "";
        int retorno = 0;
        int falso = 0;
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
                temVar = "T" + tempIndex;
                symbol.setName(temVar);
                sematicAalizer.temporal.push(temVar);
                sematicAalizer.operandos.push(temVar);
                sematicAalizer.operandos.push(value);
                sematicAalizer.semanticSymbolTable.put(temVar, symbol);  //se agrega la variable a la tabla de simbolos
                tempIndex++;
                sematicAalizer.operadores.push("temp");
                break;
            case 909:// Se ejecuta la notacion polaca para vaciar la pila de operandos y tipos hasta llegar con un (
                if (sematicAalizer.operadores.peek().equals("=")) {
                    break;
                }

                while (!sematicAalizer.operandos.peek().equals("(")) {
                    status = buildCuadruplosByNotacionPolaca();
                    if (!status) {
                        break;
                    }
                }
                sematicAalizer.operandos.pop();
                break;
            case 910: //Si encontramos operador *,/,% en el tope de la pila, generar cuadruplo
                if (sematicAalizer.isEmptyOperadoresStack()) {
                    status = true;
                    break;
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
                        status = true;
                }
                break;
            case 911: //Agregamos * a pila de operadores
                sematicAalizer.operadores.push(value);
                break;
            case 912: //Agregamos / a pila de operadores
                sematicAalizer.operadores.push(value);
                break;
            case 913: //Agregamos % a pila de operadores
                sematicAalizer.operadores.push(value);
                break;
            case 914: //Si encontramos operador +,- en el tope de la pila, generar cuadruplo
                if (sematicAalizer.isEmptyOperadoresStack()) {
                    status = true;
                    break;
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
                        status = true;
                }
                break;
            case 915: //Agregamos + a pila de operadores
                sematicAalizer.operadores.push(value);
                break;
            case 916: //Agregamos - a pila de operadores
                sematicAalizer.operadores.push(value);
                break;
            case 917: //Agregamos operadores relacionales a pila de operadores
                sematicAalizer.operadores.push(value);
                break;
            case 918: //Generamos cuadruplo usando operadores relacionales
                status = buildCuadruplosByNotacionPolaca();
                break;
            case 919: //Agregamos ! a pila de operadores
                sematicAalizer.operadores.push(value);
                break;
            case 920: //Generamos cuadruplo de Negacion
                if (sematicAalizer.isEmptyOperadoresStack()) {
                    status = true;
                    break;
                }
                operador = sematicAalizer.operadores.peek();
                if (operador.equals("!")) {
                    status = buildCuadruplosByNotacionPolaca();
                }
                break;
            case 921: //Generamos cuadruplo con operador logico &&
                if (sematicAalizer.isEmptyOperadoresStack()) {
                    status = true;
                    break;
                }
                operador = sematicAalizer.operadores.peek();
                if (operador.equals("&&")) {
                    status = buildCuadruplosByNotacionPolaca();
                }
                break;
            case 922: //Agregamos && a pila de operadores
                sematicAalizer.operadores.push(value);
                break;
            case 923: //Generamos cuadruplo con operador logico ||
                if (sematicAalizer.isEmptyOperadoresStack()) {
                    status = true;
                    break;
                }
                operador = sematicAalizer.operadores.peek();
                if (operador.equals("||")) {
                    status = buildCuadruplosByNotacionPolaca();
                }
                break;
            case 924: //Agregamos || a pila de operadores
                sematicAalizer.operadores.push(value);
                break;
            case 925: //Agregamos identificador a pila de operandos
                status = sematicAalizer.isVariable(value);
                semanticErrorMessage = "ERROR: La variable no existe.";
                if (status) {
                    sematicAalizer.operandos.push(value);
                    sematicAalizer.tipos.push(sematicAalizer.semanticSymbolTable.get(value).getType());
                }
                break;
            case 926: //Agregamos = a pila de operadores
                sematicAalizer.operadores.push(value);
                break;
            case 927: //Generamos cuadruplo de asignacion
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
                    String resultadoTipo = sematicAalizer.tipos.pop();
                    String resultado = sematicAalizer.operandos.pop();

                    if (tipoVariable.equals(resultadoTipo)) {
                        cuadruplo = new Cuadruplos();
                        cuadruplo.setOperador(operador);
                        cuadruplo.setOperando1(variable);
                        cuadruplo.setResultado(resultado);
                        sematicAalizer.cuadruplos.add(cuadruplo);
                        if (sematicAalizer.isVariable(variable)) {
                            variable = sematicAalizer.semanticSymbolTable.get(variable).getValue();
                        }
                        symbol = sematicAalizer.semanticSymbolTable.get(resultado);
//                        symbol.setValue(variable);
                        sematicAalizer.semanticSymbolTable.put(resultado, symbol);
                    } else {
                        status = false;
                        semanticErrorMessage = "ERROR: Incompatibilidad de tipos.";
                    }
                }
                break;
            case 928: // Estatuto IF 
                if (sematicAalizer.isEmptyOperandosStack() || sematicAalizer.isEmptyTiposStack()) {
                    semanticErrorMessage = "ERROR: Pila de Operandos vacia.";
                    status = false;
                    break;
                }
                variable = sematicAalizer.operandos.pop();
                tipoVariable = sematicAalizer.tipos.pop();
                if (tipoVariable.equals("boolean")) {
                    cuadruplo = new Cuadruplos();

                    sematicAalizer.saltos.push(sematicAalizer.cuadruplos.size()); //cuadruplo pendiente a ser llenado (apuntador)
                    cuadruplo.setOperador("GOTOF");
                    cuadruplo.setOperando1(variable);
                    sematicAalizer.cuadruplos.add(cuadruplo);

                } else {
                    semanticErrorMessage = "ERROR: Tipo de dato no permitido en el estatuto IF, requerido Boolean.";
                    status = false;
                }
                break;
            case 929: // Estatuto ELSE, crea el cuadruplo
                if (sematicAalizer.isEmptySaltosStack()) {
                    status = true;
                    break;
                }

                cuadruplo = new Cuadruplos();
                cuadruplo.setOperador("GOTO");
                sematicAalizer.cuadruplos.add(cuadruplo);
                int remainCuadruplo = sematicAalizer.saltos.pop();
                int salto = sematicAalizer.cuadruplos.size();
                cuadruplo = sematicAalizer.cuadruplos.get(remainCuadruplo);
                cuadruplo.setSalto(salto);
                sematicAalizer.cuadruplos.set(remainCuadruplo, cuadruplo);
                sematicAalizer.saltos.push(salto - 1);
                break;
            case 930: // Estatuto ENDIF, crea el cuadruplo
                if (sematicAalizer.isEmptySaltosStack()) {
                    status = true;
                    break;
                }
                int saltoPendiente = sematicAalizer.saltos.pop();
                int apt = sematicAalizer.cuadruplos.size();
                cuadruplo = sematicAalizer.cuadruplos.get(saltoPendiente);
                cuadruplo.setSalto(apt);
                sematicAalizer.cuadruplos.set(saltoPendiente, cuadruplo);
                break;
            case 931: // Estatuto WHILE, crea el cuadruplo
                if (sematicAalizer.isEmptyOperandosStack() || sematicAalizer.isEmptyTiposStack()) {
                    semanticErrorMessage = "ERROR: Pila de Operandos vacia.";
                    status = false;
                    break;
                }
                variable = sematicAalizer.operandos.pop();
                tipoVariable = sematicAalizer.tipos.pop();
                if (tipoVariable.equals("boolean")) {
                    cuadruplo = new Cuadruplos();

                    sematicAalizer.saltos.push(sematicAalizer.cuadruplos.size()); //cuadruplo pendiente a ser llenado (apuntador)
                    cuadruplo.setOperador("GOTOF");
                    cuadruplo.setOperando1(variable);
                    sematicAalizer.cuadruplos.add(cuadruplo);

                } else {
                    semanticErrorMessage = "ERROR: Tipo de dato no permitido en el estatuto IF, requerido Boolean.";
                    status = false;
                }
                break;
            case 932: // Estatuto WHILE, crea el cuadruplo
                if (sematicAalizer.isEmptySaltosStack()) {
                    status = true;
                    break;
                }
                falso = sematicAalizer.saltos.pop();
                if (sematicAalizer.isEmptySaltosStack()) {
                    status = true;
                    break;
                }
                retorno = sematicAalizer.saltos.pop();
                cuadruplo = new Cuadruplos();
                cuadruplo.setOperador("GOTO");
                cuadruplo.setSalto(retorno);
                sematicAalizer.cuadruplos.add(cuadruplo);
                salto = sematicAalizer.cuadruplos.size();
                cuadruplo = sematicAalizer.cuadruplos.get(falso);
                cuadruplo.setSalto(salto);
                sematicAalizer.cuadruplos.set(falso, cuadruplo);
                break;
            case 933: // Inicio del do-while metiendo retorno a saltos
                sematicAalizer.saltos.push(sematicAalizer.cuadruplos.size());
                break;
            case 934: //Despues de analizar la expresion del do-while
                if (sematicAalizer.isEmptyOperandosStack() || sematicAalizer.isEmptyTiposStack()) {
                    semanticErrorMessage = "ERROR: Pila de Operandos vacia.";
                    status = false;
                    break;
                }
                variable = sematicAalizer.operandos.pop();
                tipoVariable = sematicAalizer.tipos.pop();
                if (tipoVariable.equals("boolean")) {
                    cuadruplo = new Cuadruplos();
                    cuadruplo.setOperador("GOTOF");
                    cuadruplo.setOperando1(variable);
                    cuadruplo.setSalto(sematicAalizer.cuadruplos.size() + 2);
                    sematicAalizer.cuadruplos.add(cuadruplo);
                    if (sematicAalizer.isEmptySaltosStack()) {
                        status = true;
                        break;
                    }
                    retorno = sematicAalizer.saltos.pop();
                    cuadruplo = new Cuadruplos();
                    cuadruplo.setOperador("GOTO");
                    cuadruplo.setSalto(retorno);
                    sematicAalizer.cuadruplos.add(cuadruplo);

                } else {
                    semanticErrorMessage = "ERROR: Tipo de dato no permitido en el estatuto IF, requerido Boolean.";
                    status = false;
                }
                break;
            case 935: // Estatuto DO-WHILE, crea el cuadruplo
                break;
            case 936:
                if (!sematicAalizer.isVariable(value)) {
                    status = false;
                    semanticErrorMessage = "ERROR: La Variable " + value + " no existe o no ha sido declarada.";
                    break;
                }
                variables.add(value);
                break;
            case 937:
                int tamano = variables.size();
                for(int i= 0; i < tamano ; i++){
                    cuadruplo = new Cuadruplos();
                    cuadruplo.setOperador("READ");
                    cuadruplo.setOperando1(variables.pop());
                    sematicAalizer.cuadruplos.add(cuadruplo);
                }
                break;
            case 938:
                sematicAalizer.saltos.push(sematicAalizer.cuadruplos.size());
                break;
            case 939:
                int size = sematicAalizer.operandos.size();
                for (int i = 0; i < size; i++) {
                    cuadruplo = new Cuadruplos();
                    cuadruplo.setOperador("WRITE");
                    String expresion = sematicAalizer.operandos.pop();
                    cuadruplo.setOperando1(expresion);
//                    if (sematicAalizer.isVariable(expresion)) {
//                        String valor = sematicAalizer.semanticSymbolTable.get(expresion).getValue();
//                        cuadruplo.setOperando1(valor);
//                    } else {
//                        cuadruplo.setOperando1(expresion);
//                    }
                    sematicAalizer.cuadruplos.add(cuadruplo);
                }
                break;
            default:
                status = false;
        }
        return status;
    }

    private boolean buildCuadruplosByNotacionPolaca() {
        Symols symbol = new Symols();
        String variableTiplo = null;
        String temVar = null;
        if (sematicAalizer.isEmptyOperadoresStack() || sematicAalizer.isEmptyOperandosStack() || sematicAalizer.isEmptyTiposStack()) {
            semanticErrorMessage = "ERROR: Pilas vacias, no se puede continuar con el analisis.";
            return false;
        }
        if (sematicAalizer.operadores.peek().equals("temp")) {  //  A ( B   A = B
            String valueTemp = sematicAalizer.operandos.pop();
            temVar = sematicAalizer.temporal.pop();
            String typeTemp = sematicAalizer.tipos.peek();
            sematicAalizer.operadores.pop();
            symbol = sematicAalizer.semanticSymbolTable.get(temVar);
//            if (sematicAalizer.isVariable(valueTemp)) {
//                valueTemp = sematicAalizer.semanticSymbolTable.get(valueTemp).getValue();
//            }
//            symbol.setValue(valueTemp);
            symbol.setType(typeTemp);
            sematicAalizer.semanticSymbolTable.put(temVar, symbol);
            Cuadruplos cuadruplo = new Cuadruplos();
            cuadruplo.setOperador("=");
            cuadruplo.setOperando1(valueTemp);
            cuadruplo.setResultado(temVar);
            sematicAalizer.cuadruplos.add(cuadruplo);
            return true;
        }
        String operando1 = sematicAalizer.operandos.pop();
        String operador = sematicAalizer.operadores.pop();
        String tipoOperador1 = sematicAalizer.tipos.pop();

        // Solo cuando es la negacion
        if (operador.equals("!")) {
            variableTiplo = setNegacion(tipoOperador1);
            if (variableTiplo == null) {
                semanticErrorMessage = "ERROR: La operacion '" + operador + "' no puede realizar con el operando: '" + operando1 + "' por ser de tipo '" + tipoOperador1 + "'";
                return false;
            }
            temVar = "T" + tempIndex;
            tempIndex++;
            symbol.setName(temVar);
            symbol.setType(variableTiplo);
//            symbol.setValue(triplo[0]);
            sematicAalizer.semanticSymbolTable.put(temVar, symbol);
            sematicAalizer.operandos.push(temVar);
            sematicAalizer.tipos.push(variableTiplo);
            Cuadruplos cuadruplo = new Cuadruplos();
            cuadruplo.setOperador(operador);
            cuadruplo.setOperando1(operando1);
            cuadruplo.setResultado(temVar);
            sematicAalizer.cuadruplos.add(cuadruplo);
            return true;
        }
        if (sematicAalizer.isEmptyOperandosStack() || sematicAalizer.isEmptyTiposStack()) {
            semanticErrorMessage = "ERROR: Pilas vacias, no se puede continuar con el analisis.";
            return false;
        }

        if (sematicAalizer.operandos.peek().equals("(")) {
            sematicAalizer.operandos.pop();
        }

        if (sematicAalizer.isEmptyOperandosStack() || sematicAalizer.isEmptyTiposStack()) {
            semanticErrorMessage = "ERROR: Pilas vacias, no se puede continuar con el analisis.";
            return false;
        }

        String operando2 = sematicAalizer.operandos.pop();
        String tipoOperador2 = sematicAalizer.tipos.pop();
        if (!isValidTypes(tipoOperador1, tipoOperador2)) {
            semanticErrorMessage = "ERROR: Los tipos son incompatibles.";
            return false;
        }
        variableTiplo = validateOperandosAndOperator(operando1, operando2, operador, tipoOperador1);
        if (variableTiplo == null) {
            semanticErrorMessage = "ERROR: La operacion '" + operador + "' no puede realizar entre los operandos: '" + operando1 + "' y '" + operando2 + "'";
            return false;
        }
        temVar = "T" + tempIndex;
        tempIndex++;
        symbol.setName(temVar);
        symbol.setType(variableTiplo);
//        symbol.setValue(triplo[0]);
        sematicAalizer.semanticSymbolTable.put(temVar, symbol);
        sematicAalizer.operandos.push(temVar);
        sematicAalizer.tipos.push(variableTiplo);
        Cuadruplos cuadruplo = new Cuadruplos();
        cuadruplo.setOperador(operador);
        cuadruplo.setOperando2(operando1);
        cuadruplo.setOperando1(operando2);
        cuadruplo.setResultado(temVar);
        sematicAalizer.cuadruplos.add(cuadruplo);
        return true;
    }

    private boolean isValidTypes(String tipo1, String tipo2) {
        if (tipo1.equals(tipo2)) {
            return true;
        }
        return false;
    }

    private String setNegacion(String value) {
        return (value.equals("true")) ? "false" : "true";
    }

    private String validateOperandosAndOperator(String operando1, String operando2, String operador, String tipo) {
        String variableTiplo = null;
        String ops1 = "";
        String ops2 = "";
        int op1 = 0;
        int op2 = 0;
        float opf1 = 0;
        float opf2 = 0;
        boolean opb1 = false;
        boolean opb2 = false;
        switch (operador) {
            case "+":
                switch (tipo) {
                    case "int":
//                        op1 = convertStringToInt(operando2);
//                        op2 = convertStringToInt(operando1);
//                        triplo[0] = Integer.toString(op1 + op2);
                        variableTiplo = tipo;
                        break;
                    case "float":
//                        opf1 = convertStringToFloat(operando2);
//                        opf2 = convertStringToFloat(operando1);
//                        triplo[0] = Float.toString(opf1 + opf2);
                        variableTiplo = tipo;
                        break;
                    case "char":
//                        ops1 = getStringValue(operando2);
//                        ops2 = getStringValue(operando1);
//                        triplo[0] = "'" + ops1.replace("'", "") + ops2.replace("'", "") + "'";
                        variableTiplo = tipo;
                        break;
                    case "string":
//                        ops1 = getStringValue(operando2);
//                        ops2 = getStringValue(operando1);
//                        triplo[0] = "\"" + ops1.replace("\"", "") + ops2.replace("\"", "") + "\"";
                        variableTiplo = tipo;
                        break;
                    default:
                        variableTiplo = null;
                }
                break;
            case "-":
                switch (tipo) {
                    case "int":
//                        op1 = convertStringToInt(operando2);
//                        op2 = convertStringToInt(operando1);
//                        triplo[0] = Integer.toString(op1 - op2);
                        variableTiplo = tipo;
                        break;
                    case "float":
//                        opf1 = convertStringToFloat(operando2);
//                        opf2 = convertStringToFloat(operando1);
//                        triplo[0] = Float.toString(opf1 - opf2);
                        variableTiplo = tipo;
                        break;
                    default:
                        variableTiplo = null;
                }
                break;
            case "*":
                switch (tipo) {
                    case "int":
//                        op1 = convertStringToInt(operando2);
//                        op2 = convertStringToInt(operando1);
//                        triplo[0] = Integer.toString(op1 * op2);
                        variableTiplo = tipo;
                        break;
                    case "float":
//                        opf1 = convertStringToFloat(operando2);
//                        opf2 = convertStringToFloat(operando1);
//                        triplo[0] = Float.toString(opf1 * opf2);
                        variableTiplo = tipo;
                        break;
                    default:
                        variableTiplo = null;
                }
                break;
            case "/":
                switch (tipo) {
                    case "int":
//                        op1 = convertStringToInt(operando2);
//                        op2 = convertStringToInt(operando1);
//                        triplo[0] = Integer.toString(op1 / op2);
                        variableTiplo = tipo;
                        break;
                    case "float":
//                        opf1 = convertStringToFloat(operando2);
//                        opf2 = convertStringToFloat(operando1);
//                        triplo[0] = Float.toString(opf1 / opf2);
                        variableTiplo = tipo;
                        break;
                    default:
                        variableTiplo = null;
                }
                break;
            case "%":
                switch (tipo) {
                    case "int":
//                        op1 = convertStringToInt(operando2);
//                        op2 = convertStringToInt(operando1);
//                        triplo[0] = Integer.toString(op1 % op2);
                        variableTiplo = tipo;
                        break;
                    case "float":
//                        opf1 = convertStringToFloat(operando2);
//                        opf2 = convertStringToFloat(operando1);
//                        triplo[0] = Float.toString(opf1 % opf2);
                        variableTiplo = tipo;
                        break;
                    default:
                        variableTiplo = null;
                }
                break;
            case "==":
                switch (tipo) {
                    case "int":
//                        op1 = convertStringToInt(operando2);
//                        op2 = convertStringToInt(operando1);
//                        triplo[0] = (op1 == op2) ? "true" : "false";
                        variableTiplo = "boolean";
                        break;
                    case "float":
//                        opf1 = convertStringToFloat(operando2);
//                        opf2 = convertStringToFloat(operando1);
//                        triplo[0] = (opf1 == opf2) ? "true" : "false";
                        variableTiplo = "boolean";
                        break;
                    case "char":
//                        ops1 = getStringValue(operando2);
//                        ops2 = getStringValue(operando1);
//                        triplo[0] = (ops1.equals(ops2)) ? "true" : "false";
                        variableTiplo = "boolean";
                        break;
                    case "string":
//                        ops1 = getStringValue(operando2);
//                        ops2 = getStringValue(operando1);
//                        triplo[0] = (ops1.equals(ops2)) ? "true" : "false";
                        variableTiplo = "boolean";
                        break;
                    case "boolean":
//                        triplo[0] = (operando2.equals(operando1)) ? "true" : "false";
                        variableTiplo = "boolean";
                        break;
                    default:
                        variableTiplo = null;
                }
                break;
            case "!=":
                switch (tipo) {
                    case "int":
//                        op1 = convertStringToInt(operando2);
//                        op2 = convertStringToInt(operando1);
//                        triplo[0] = (op1 != op2) ? "true" : "false";
                        variableTiplo = "boolean";
                        break;
                    case "float":
//                        opf1 = convertStringToFloat(operando2);
//                        opf2 = convertStringToFloat(operando1);
//                        triplo[0] = (opf1 != opf2) ? "true" : "false";
                        variableTiplo = "boolean";
                        break;
                    case "char":
//                        ops1 = getStringValue(operando2);
//                        ops2 = getStringValue(operando1);
//                        triplo[0] = (!ops1.equals(ops2)) ? "true" : "false";
                        variableTiplo = "boolean";
                        break;
                    case "string":
//                        ops1 = getStringValue(operando2);
//                        ops2 = getStringValue(operando1);
//                        triplo[0] = (!ops1.equals(ops2)) ? "true" : "false";
                        variableTiplo = "boolean";
                        break;
                    case "boolean":
//                        triplo[0] = (!operando2.equals(operando1)) ? "true" : "false";
                        variableTiplo = "boolean";
                        break;
                    default:
                        variableTiplo = null;
                }
                break;
            case "<":
                switch (tipo) {
                    case "int":
//                        op1 = convertStringToInt(operando2);
//                        op2 = convertStringToInt(operando1);
//                        triplo[0] = (op1 < op2) ? "true" : "false";
                        variableTiplo = "boolean";
                        break;
                    case "float":
//                        opf1 = convertStringToFloat(operando2);
//                        opf2 = convertStringToFloat(operando1);
//                        triplo[0] = (opf1 < opf2) ? "true" : "false";
                        variableTiplo = "boolean";
                        break;
                    default:
                        variableTiplo = null;
                }
                break;
            case "<=":
                switch (tipo) {
                    case "int":
//                        op1 = convertStringToInt(operando2);
//                        op2 = convertStringToInt(operando1);
//                        triplo[0] = (op1 <= op2) ? "true" : "false";
                        variableTiplo = "boolean";
                        break;
                    case "float":
//                        opf1 = convertStringToFloat(operando2);
//                        opf2 = convertStringToFloat(operando1);
//                        triplo[0] = (opf1 <= opf2) ? "true" : "false";
                        variableTiplo = "boolean";
                        break;
                    default:
                        variableTiplo = null;
                }
                break;
            case ">":
                switch (tipo) {
                    case "int":
//                        op1 = convertStringToInt(operando2);
//                        op2 = convertStringToInt(operando1);
//                        triplo[0] = (op1 > op2) ? "true" : "false";
                        variableTiplo = "boolean";
                        break;
                    case "float":
//                        opf1 = convertStringToFloat(operando2);
//                        opf2 = convertStringToFloat(operando1);
//                        triplo[0] = (opf1 > opf2) ? "true" : "false";
                        variableTiplo = "boolean";
                        break;
                    default:
                        variableTiplo = null;
                }
                break;
            case ">=":
                switch (tipo) {
                    case "int":
//                        op1 = convertStringToInt(operando2);
//                        op2 = convertStringToInt(operando1);
//                        triplo[0] = (op1 >= op2) ? "true" : "false";
                        variableTiplo = "boolean";
                        break;
                    case "float":
//                        opf1 = convertStringToFloat(operando2);
//                        opf2 = convertStringToFloat(operando1);
//                        triplo[0] = (opf1 >= opf2) ? "true" : "false";
                        variableTiplo = "boolean";
                        break;
                    default:
                        variableTiplo = null;
                }
                break;
            case "&&":
                switch (tipo) {
                    case "boolean":
//                        opb1 = getBooleanValue(operando2);
//                        opb2 = getBooleanValue(operando1);
//                        triplo[0] = (opb1 && opb2) ? "true" : "false";
                        variableTiplo = "boolean";
                        break;
                    default:
                        variableTiplo = null;
                }
                break;
            case "||":
                switch (tipo) {
                    case "boolean":
//                        opb1 = getBooleanValue(operando2);
//                        opb2 = getBooleanValue(operando1);
//                        triplo[0] = (opb1 || opb2) ? "true" : "false";
                        variableTiplo = "boolean";
                        break;
                    default:
                        variableTiplo = null;
                }
                break;
        }
        return variableTiplo;
    }

    private void generateFile() {
        String name = fileName.substring(0, fileName.length() - 4);
        File file = new File("./resources/build/" + name + ".emn");
        file.delete();
        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("./resources/build/" + name + ".emn", true)))) {
            String data = "Class Name: " + name + ".emn \n\n";
            out.println(data);
            data = sematicAalizer.showSymbolTable();
            out.println(data);
            data = sematicAalizer.showCuadruplos();
            out.println(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
