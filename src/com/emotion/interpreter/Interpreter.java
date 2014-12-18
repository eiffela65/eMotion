/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.emotion.interpreter;

import com.emotion.exception.FileException;
import com.emotion.semantic.Cuadruplos;
import com.emotion.semantic.Symols;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Neftali
 */
public class Interpreter {

    public Map<String, Symols> symbolTable = new HashMap<String, Symols>();
    public List<Cuadruplos> cuadruplos = new ArrayList<Cuadruplos>();

    void readFile(File file) throws FileException {
        BufferedReader br;
        String fileName = file.getName();
        try {
            br = new BufferedReader(new FileReader(file.getAbsolutePath()));
            String line = br.readLine();
            System.out.println("************  EXECUTING " + fileName + ".emn  **************");
            String arrayLine[];
            while (line != null) {
                System.out.println(line);
                arrayLine = line.split("\\|");
                if (arrayLine.length > 1) {
                    if (isInteger(arrayLine[0])) {

                        fillCuadruplos(arrayLine);
                    } else {
                        fillSymbolsTable(arrayLine);
                    }
                }
                line = br.readLine();
            }
            br.close();
            System.out.println("--------------------------------");
            showSymbolTable();
            System.out.println("················");
            showCuadruplos();
        } catch (FileNotFoundException e) {
            throw new FileException("ERROR: No se encontró el archivo");
        } catch (IOException e) {
            throw new FileException("ERROR: No se puede leer el archivo. Puede estar corrupto");
        }
    }

    private boolean isInteger(String s) {
        try {
//        System.out.println(s);
            Integer.parseInt(s.trim());
        } catch (NumberFormatException e) {
//        System.out.println("ERROR " + s);
            return false;
        }
        return true;
    }

    private void fillSymbolsTable(String[] arrayLine) {
        if (arrayLine.length < 3) {
            System.out.println("ERROR: No se pudo cargar en la tabla de simbolos");
        } else {
            Symols symbol = new Symols();
            symbol.setName(arrayLine[0].trim());
            symbol.setType(arrayLine[1].trim());
            symbolTable.put(arrayLine[0], symbol);
        }
    }

    private void fillCuadruplos(String[] arrayLine) {
        if (arrayLine.length < 6) {
            System.out.println("ERROR: No se pudo cargar cuadruplo");
        } else {
            Cuadruplos cuadruplo = new Cuadruplos();
            cuadruplo.setOperador(arrayLine[1].trim());
            cuadruplo.setOperando1(arrayLine[2].trim());
            cuadruplo.setOperando2(arrayLine[3].trim());
            cuadruplo.setResultado(arrayLine[4].trim());
            cuadruplo.setSalto(Integer.parseInt(arrayLine[5].trim()));
            cuadruplos.add(cuadruplo);
        }
    }

    private String showSymbolTable() {
        String data = "";
        System.out.println("--- TABLA DE SIMBOLOS ---");
        for (Map.Entry<String, Symols> entry : symbolTable.entrySet()) {
//            System.out.printf("Key : %s and Variable: %s and Type: %s and Value: %s %n", entry.getKey(), entry.getValue().getName(), entry.getValue().getType(), entry.getValue().getValue());
            System.out.printf("Variable: %s | Type: %s | Value: %s %n", entry.getValue().getName(), entry.getValue().getType(), entry.getValue().getValue());
            data += entry.getValue().getName() + " | " + entry.getValue().getType() + " | " + entry.getValue().getValue() + "\n";
        }
        data += "\n\n";
        return data;
    }

    private String showCuadruplos() {
        String data = "";
        System.out.println("---  CUADRUPLOS  ---");
        int i = 0;
        for (Cuadruplos cuadruplo : cuadruplos) {
            System.out.printf("%d Operador: %s  Operando1: %s  Operando2: %s  Resultado: %s  Apuntador: %d %n", i, cuadruplo.getOperador(), cuadruplo.getOperando1(), cuadruplo.getOperando2(), cuadruplo.getResultado(), cuadruplo.getSalto());
            i++;
            data += i + " | " + cuadruplo.getOperador() + " | " + cuadruplo.getOperando1() + " | " + cuadruplo.getOperando2() + " | " + cuadruplo.getResultado() + " | " + cuadruplo.getSalto() + "\n";
        }
        data += "\n\n";
        return data;
    }

    public void runCuadruplos() {
        int apt = 0;
        int size = cuadruplos.size();
        Cuadruplos cuadruplo;
        while (apt < size) {
            cuadruplo = cuadruplos.get(apt);
            if (cuadruplo.getOperador().equals("GOTO") || cuadruplo.getOperador().equals("GOTOF")) {
                apt = cuadruplo.getSalto() - 1;
                //Hacer la comparacion con el resultado y lo que se tenga dentro del GOTOF
            } else {
                String resultado = makeOperation(cuadruplo.getOperador(), cuadruplo.getOperando1(), cuadruplo.getOperando2());
                Symols symbol = symbolTable.get(cuadruplo.getResultado());
                symbol.setValue(resultado);
                symbolTable.put(cuadruplo.getResultado(), symbol);
            }
        }
        System.out.println("************  FIN DE LA EJECUCION  **************");
    }

    private String makeOperation(String operador, String operando1, String operando2) {
        String resultado = "";
        Symols symbol = symbolTable.get(operando1);
        String tipo = symbol.getType();
        if (operando2.equals("null")) {
            if(operador.equals("WRITE"))
                System.out.println(operando1);
            else if(operador.equals("READ")){
                //Colocar el buffer reader para la lectura por consola
                //Hacer la conversion de acuerdo al tipo de la variable
                    //Si hay error mostrar un error en tiempo de ejecucion
            } else{
                //Hacer la operacion (la unica disponible al parecer es la negacion)
            }
            System.out.printf("%s %s = %s %n", operando1, operador, resultado);
        } else {
            resultado = validateOperandosAndOperator(operando1,operando2, operador, tipo);
            symbol = symbolTable.get(operando1);
           
            System.out.printf("%s %s %s = %s %n", operando1, operador, operando2, resultado);
        }
        return resultado;
    }
   
    public boolean isVariable(String variable) {
        return symbolTable.containsKey(variable);
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
   
    private int convertStringToInt(String operando) {
        int op = 0;
        if (isVariable(operando)) {
            op = Integer.parseInt(symbolTable.get(operando).getValue());
        } else {
            op = Integer.parseInt(operando);
        }
        return op;
    }
   
    private float convertStringToFloat(String operando) {
        float op = 0;
        if (isVariable(operando)) {
            op = Float.parseFloat(symbolTable.get(operando).getValue());
        } else {
            op = Float.parseFloat(operando);
        }
        return op;
    }

    private String getStringValue(String operando) {
        String op = "";
        if (isVariable(operando)) {
            op = symbolTable.get(operando).getValue();
        } else {
            op = operando;
        }
        return op;
    }

    private Boolean getBooleanValue(String operando) {
        boolean op = false;
        if (isVariable(operando)) {
            op = Boolean.parseBoolean(symbolTable.get(operando).getValue());
        } else {
            op = Boolean.parseBoolean(operando);
        }
        return op;
    }
   
    private String validateOperandosAndOperator(String operando1, String operando2, String operador, String tipo) {
//        System.out.printf("Elementos a evaluar: OP1 = %s OP2 = %s OPR = %s TYPE = %s %n", operando2, operando1, operador, tipo);
        String[] triplo = new String[2];
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
                        op1 = convertStringToInt(operando2);
                        op2 = convertStringToInt(operando1);
                        triplo[0] = Integer.toString(op1 + op2);
                        triplo[1] = tipo;
                        break;
                    case "float":
                        opf1 = convertStringToFloat(operando2);
                        opf2 = convertStringToFloat(operando1);
                        triplo[0] = Float.toString(opf1 + opf2);
                        triplo[1] = tipo;
                        break;
                    case "char":
                        ops1 = getStringValue(operando2);
                        ops2 = getStringValue(operando1);
                        triplo[0] = "'" + ops1.replace("'", "") + ops2.replace("'", "") + "'";
                        triplo[1] = tipo;
                        break;
                    case "string":
                        ops1 = getStringValue(operando2);
                        ops2 = getStringValue(operando1);
                        triplo[0] = "\"" + ops1.replace("\"", "") + ops2.replace("\"", "") + "\"";
                        triplo[1] = tipo;
                        break;
                    default:
                        triplo = null;
                }
                break;
            case "-":
                switch (tipo) {
                    case "int":
                        op1 = convertStringToInt(operando2);
                        op2 = convertStringToInt(operando1);
                        triplo[0] = Integer.toString(op1 - op2);
                        triplo[1] = tipo;
                        break;
                    case "float":
                        opf1 = convertStringToFloat(operando2);
                        opf2 = convertStringToFloat(operando1);
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
                        op1 = convertStringToInt(operando2);
                        op2 = convertStringToInt(operando1);
                        triplo[0] = Integer.toString(op1 * op2);
                        triplo[1] = tipo;
                        break;
                    case "float":
                        opf1 = convertStringToFloat(operando2);
                        opf2 = convertStringToFloat(operando1);
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
                        op1 = convertStringToInt(operando2);
                        op2 = convertStringToInt(operando1);
                        triplo[0] = Integer.toString(op1 / op2);
                        triplo[1] = tipo;
                        break;
                    case "float":
                        opf1 = convertStringToFloat(operando2);
                        opf2 = convertStringToFloat(operando1);
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
                        op1 = convertStringToInt(operando2);
                        op2 = convertStringToInt(operando1);
                        triplo[0] = Integer.toString(op1 % op2);
                        triplo[1] = tipo;
                        break;
                    case "float":
                        opf1 = convertStringToFloat(operando2);
                        opf2 = convertStringToFloat(operando1);
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
                        op1 = convertStringToInt(operando2);
                        op2 = convertStringToInt(operando1);
                        triplo[0] = (op1 == op2) ? "true" : "false";
                        triplo[1] = "boolean";
                        break;
                    case "float":
                        opf1 = convertStringToFloat(operando2);
                        opf2 = convertStringToFloat(operando1);
                        triplo[0] = (opf1 == opf2) ? "true" : "false";
                        triplo[1] = "boolean";
                        break;
                    case "char":
                        ops1 = getStringValue(operando2);
                        ops2 = getStringValue(operando1);
                        triplo[0] = (ops1.equals(ops2)) ? "true" : "false";
                        triplo[1] = "boolean";
                        break;
                    case "string":
                        ops1 = getStringValue(operando2);
                        ops2 = getStringValue(operando1);
                        triplo[0] = (ops1.equals(ops2)) ? "true" : "false";
                        triplo[1] = "boolean";
                        break;
                    case "boolean":
                        triplo[0] = (operando2.equals(operando1)) ? "true" : "false";
                        triplo[1] = "boolean";
                        break;
                    default:
                        triplo = null;
                }
                break;
            case "!=":
                switch (tipo) {
                    case "int":
                        op1 = convertStringToInt(operando2);
                        op2 = convertStringToInt(operando1);
                        triplo[0] = (op1 != op2) ? "true" : "false";
                        triplo[1] = "boolean";
                        break;
                    case "float":
                        opf1 = convertStringToFloat(operando2);
                        opf2 = convertStringToFloat(operando1);
                        triplo[0] = (opf1 != opf2) ? "true" : "false";
                        triplo[1] = "boolean";
                        break;
                    case "char":
                        ops1 = getStringValue(operando2);
                        ops2 = getStringValue(operando1);
                        triplo[0] = (!ops1.equals(ops2)) ? "true" : "false";
                        triplo[1] = "boolean";
                        break;
                    case "string":
                        ops1 = getStringValue(operando2);
                        ops2 = getStringValue(operando1);
                        triplo[0] = (!ops1.equals(ops2)) ? "true" : "false";
                        triplo[1] = "boolean";
                        break;
                    case "boolean":
                        triplo[0] = (!operando2.equals(operando1)) ? "true" : "false";
                        triplo[1] = "boolean";
                        break;
                    default:
                        triplo = null;
                }
                break;
            case "<":
                switch (tipo) {
                    case "int":
                        op1 = convertStringToInt(operando2);
                        op2 = convertStringToInt(operando1);
                        triplo[0] = (op1 < op2) ? "true" : "false";
                        triplo[1] = "boolean";
                        break;
                    case "float":
                        opf1 = convertStringToFloat(operando2);
                        opf2 = convertStringToFloat(operando1);
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
                        op1 = convertStringToInt(operando2);
                        op2 = convertStringToInt(operando1);
                        triplo[0] = (op1 <= op2) ? "true" : "false";
                        triplo[1] = "boolean";
                        break;
                    case "float":
                        opf1 = convertStringToFloat(operando2);
                        opf2 = convertStringToFloat(operando1);
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
                        op1 = convertStringToInt(operando2);
                        op2 = convertStringToInt(operando1);
                        triplo[0] = (op1 > op2) ? "true" : "false";
                        triplo[1] = "boolean";
                        break;
                    case "float":
                        opf1 = convertStringToFloat(operando2);
                        opf2 = convertStringToFloat(operando1);
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
                        op1 = convertStringToInt(operando2);
                        op2 = convertStringToInt(operando1);
                        triplo[0] = (op1 >= op2) ? "true" : "false";
                        triplo[1] = "boolean";
                        break;
                    case "float":
                        opf1 = convertStringToFloat(operando2);
                        opf2 = convertStringToFloat(operando1);
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
                        opb1 = getBooleanValue(operando2);
                        opb2 = getBooleanValue(operando1);
                        triplo[0] = (opb1 && opb2) ? "true" : "false";
                        triplo[1] = "boolean";
                        break;
                    default:
                        triplo = null;
                }
                break;
            case "||":
                switch (tipo) {
                    case "boolean":
                        opb1 = getBooleanValue(operando2);
                        opb2 = getBooleanValue(operando1);
                        triplo[0] = (opb1 || opb2) ? "true" : "false";
                        triplo[1] = "boolean";
                        break;
                    default:
                        triplo = null;
                }
                break;
        }
        return triplo[0];
    }

}
