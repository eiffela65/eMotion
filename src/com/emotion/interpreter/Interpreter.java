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
import java.util.Scanner;

/**
 *
 * @author Neftali
 */
public class Interpreter {

    public Map<String, Symols> symbolTable = new HashMap<String, Symols>();
    public List<Cuadruplos> cuadruplos = new ArrayList<Cuadruplos>();
    Scanner in = new Scanner(System.in);
    int index = 0;

    void readFile(File file) throws FileException {
        BufferedReader br;
        String fileName = file.getName();
        try {
            br = new BufferedReader(new FileReader(file.getAbsolutePath()));
            String line = br.readLine();
            System.out.println("************  EXECUTING " + fileName + ".emn  **************");
            String arrayLine[];
            while (line != null) {
                arrayLine = line.split("#");
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
        } catch (FileNotFoundException e) {
            throw new FileException("ERROR: No se encontró el archivo");
        } catch (IOException e) {
            throw new FileException("ERROR: No se puede leer el archivo. Puede estar corrupto");
        }

        System.out.println("************  OUTPUT  **************");

        runCuadruplos();
        
        System.out.println("************  BUILD SUCCESS  **************");
        
        System.exit(0);
    }

    private boolean isInteger(String s) {
        try {
            Integer.parseInt(s.trim());
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    private void fillSymbolsTable(String[] arrayLine) {
        if (arrayLine.length < 2) {
            System.out.println("ERROR: No se pudo cargar en la tabla de simbolos");
        } else {
            Symols symbol = new Symols();
            symbol.setName(arrayLine[0].trim());
            symbol.setType(arrayLine[1].trim());
            symbolTable.put(arrayLine[0].trim(), symbol);
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
            try {
                cuadruplo.setSalto(Integer.parseInt(arrayLine[5].trim()));
            } catch (NumberFormatException e) {
                cuadruplo.setSalto(0);
            }
            cuadruplos.add(cuadruplo);
            index++;
        }
    }

    public void runCuadruplos() {
        int apt = 0;
        int size = cuadruplos.size();
        String variable, variable2;
        String tipo;
        String value;
        String resultado;
        Cuadruplos cuadruplo;
        Symols symbol;
        Symols symbolResultado;
        while (apt < size) {
            cuadruplo = cuadruplos.get(apt);
            String operador = cuadruplo.getOperador().trim();
            variable = cuadruplo.getOperando1().trim();
            variable2 = cuadruplo.getOperando2().trim();
            resultado = cuadruplo.getResultado().trim();
            switch (operador) {
                case "GOTO":
                    apt = cuadruplo.getSalto();
                    break;
                case "GOTOF":
                    symbol = symbolTable.get(variable);
                    if (symbol.getValue().equals("false")) {
                        apt = cuadruplo.getSalto();
                    } else {
                        apt++;
                    }
                    break;
                case "READ":
                    symbol = symbolTable.get(variable);
                    System.out.println("Estoy en el read para " + variable);
                    tipo = symbol.getType();
                    if (tipo.equals("int")) {
                        value = Integer.toString(in.nextInt());
                    } else if (tipo.equals("float")) {
                        value = Float.toString(in.nextFloat());
                    } else {
                        value = in.next();
                        if (symbol.getType().equals("char") && value.length() > 1) {
                        }
                    }
                    symbol.setValue(value);
                    symbolTable.put(variable, symbol);
                    apt++;
                    System.out.println("Fin del READ siguiente apt   " + apt);
                    break;
                case "WRITE":
                    value = variable;
                    if (isVariable(variable)) {
                        symbol = symbolTable.get(variable);
                        value = symbol.getValue();
                    }
                    System.out.println(value);
                    apt++;
                    break;
                case "!":
                    symbolResultado = symbolTable.get(resultado);
                    symbol = symbolTable.get(variable);
                    value = variable;
                    if (isVariable(variable)) {
                        symbol = symbolTable.get(variable);
                        value = symbol.getValue();
                    }
                    value = setNegacion(value);
                    symbolResultado.setValue(value);
                    symbolTable.put(variable, symbolResultado);
                    apt++;
                    break;
                case "=":
                    symbolResultado = symbolTable.get(resultado);
                    value = variable;
                    if (isVariable(variable)) {
                        symbol = symbolTable.get(variable);
                        value = symbol.getValue();
                    }
                    symbolResultado.setValue(value);
                    symbolTable.put(variable, symbolResultado);
                    apt++;
                    break;
                default:
                    symbolResultado = symbolTable.get(resultado);
                    tipo = getTipo(variable, variable2);
                    value = validateOperandosAndOperator(variable, variable2, operador, tipo);
                    symbolResultado.setValue(value);
                    symbolTable.put(resultado, symbolResultado);
                    apt++;
            }
        }
    }

    private String getTipo(String variable1, String variable2) {
        String resultado = null;
        Symols symbol = new Symols();
        if (isVariable(variable1)) {
            symbol = symbolTable.get(variable1);
            resultado = symbol.getType();
        } else if (isVariable(variable2)) {
            symbol = symbolTable.get(variable2);
            resultado = symbol.getType();
        } else {
            try {
                Integer.parseInt(variable1);
                resultado = "int";
            } catch (NumberFormatException e) {
                try {
                    Float.parseFloat(variable1);
                    resultado = "float";
                } catch (NumberFormatException f) {
                    try {
                        Boolean.parseBoolean(variable1);
                        resultado = "boolean";
                    } catch (NumberFormatException g) {
                        if(variable1.length() > 1)
                            resultado = "char";
                        else
                            resultado = "string";
                    }
                }
            }
        }
        return resultado;
    }

    public boolean isVariable(String variable) {
        return symbolTable.containsKey(variable);
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
        String resultado = null;
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
                        resultado = Integer.toString(op1 + op2);
                        break;
                    case "float":
                        opf1 = convertStringToFloat(operando2);
                        opf2 = convertStringToFloat(operando1);
                        resultado = Float.toString(opf1 + opf2);
                        break;
                    case "char":
                        ops1 = getStringValue(operando2);
                        ops2 = getStringValue(operando1);
                        resultado = "'" + ops1.replace("'", "") + ops2.replace("'", "") + "'";

                        break;
                    case "string":
                        ops1 = getStringValue(operando2);
                        ops2 = getStringValue(operando1);
                        resultado = "\"" + ops1.replace("\"", "") + ops2.replace("\"", "") + "\"";

                        break;
                    default:
                        resultado = null;
                }
                break;
            case "-":
                switch (tipo) {
                    case "int":
                        op1 = convertStringToInt(operando2);
                        op2 = convertStringToInt(operando1);
                        resultado = Integer.toString(op1 - op2);

                        break;
                    case "float":
                        opf1 = convertStringToFloat(operando2);
                        opf2 = convertStringToFloat(operando1);
                        resultado = Float.toString(opf1 - opf2);

                        break;
                    default:
                        resultado = null;
                }
                break;
            case "*":
                switch (tipo) {
                    case "int":
                        op1 = convertStringToInt(operando2);
                        op2 = convertStringToInt(operando1);
                        resultado = Integer.toString(op1 * op2);

                        break;
                    case "float":
                        opf1 = convertStringToFloat(operando2);
                        opf2 = convertStringToFloat(operando1);
                        resultado = Float.toString(opf1 * opf2);

                        break;
                    default:
                        resultado = null;
                }
                break;
            case "/":
                switch (tipo) {
                    case "int":
                        op1 = convertStringToInt(operando2);
                        op2 = convertStringToInt(operando1);
                        resultado = Integer.toString(op1 / op2);

                        break;
                    case "float":
                        opf1 = convertStringToFloat(operando2);
                        opf2 = convertStringToFloat(operando1);
                        resultado = Float.toString(opf1 / opf2);

                        break;
                    default:
                        resultado = null;
                }
                break;
            case "%":
                switch (tipo) {
                    case "int":
                        op1 = convertStringToInt(operando2);
                        op2 = convertStringToInt(operando1);
                        resultado = Integer.toString(op1 % op2);

                        break;
                    case "float":
                        opf1 = convertStringToFloat(operando2);
                        opf2 = convertStringToFloat(operando1);
                        resultado = Float.toString(opf1 % opf2);

                        break;
                    default:
                        resultado = null;
                }
                break;
            case "==":
                switch (tipo) {
                    case "int":
                        op1 = convertStringToInt(operando2);
                        op2 = convertStringToInt(operando1);
                        resultado = (op1 == op2) ? "true" : "false";

                        break;
                    case "float":
                        opf1 = convertStringToFloat(operando2);
                        opf2 = convertStringToFloat(operando1);
                        resultado = (opf1 == opf2) ? "true" : "false";

                        break;
                    case "char":
                        ops1 = getStringValue(operando2);
                        ops2 = getStringValue(operando1);
                        resultado = (ops1.equals(ops2)) ? "true" : "false";

                        break;
                    case "string":
                        ops1 = getStringValue(operando2);
                        ops2 = getStringValue(operando1);
                        resultado = (ops1.equals(ops2)) ? "true" : "false";

                        break;
                    case "boolean":
                        resultado = (operando2.equals(operando1)) ? "true" : "false";

                        break;
                    default:
                        resultado = null;
                }
                break;
            case "!=":
                switch (tipo) {
                    case "int":
                        op1 = convertStringToInt(operando2);
                        op2 = convertStringToInt(operando1);
                        resultado = (op1 != op2) ? "true" : "false";

                        break;
                    case "float":
                        opf1 = convertStringToFloat(operando2);
                        opf2 = convertStringToFloat(operando1);
                        resultado = (opf1 != opf2) ? "true" : "false";

                        break;
                    case "char":
                        ops1 = getStringValue(operando2);
                        ops2 = getStringValue(operando1);
                        resultado = (!ops1.equals(ops2)) ? "true" : "false";

                        break;
                    case "string":
                        ops1 = getStringValue(operando2);
                        ops2 = getStringValue(operando1);
                        resultado = (!ops1.equals(ops2)) ? "true" : "false";

                        break;
                    case "boolean":
                        resultado = (!operando2.equals(operando1)) ? "true" : "false";

                        break;
                    default:
                        resultado = null;
                }
                break;
            case "<":
                switch (tipo) {
                    case "int":

                        op1 = convertStringToInt(operando1);
                        op2 = convertStringToInt(operando2);
                        resultado = (op1 < op2) ? "true" : "false";

                        break;
                    case "float":
                        opf1 = convertStringToFloat(operando1);
                        opf2 = convertStringToFloat(operando2);
                        resultado = (opf1 < opf2) ? "true" : "false";

                        break;
                    default:
                        resultado = null;
                }
                break;
            case "<=":
                switch (tipo) {
                    case "int":
                        op1 = convertStringToInt(operando1);
                        op2 = convertStringToInt(operando2);
                        resultado = (op1 <= op2) ? "true" : "false";

                        break;
                    case "float":
                        opf1 = convertStringToFloat(operando1);
                        opf2 = convertStringToFloat(operando2);
                        resultado = (opf1 <= opf2) ? "true" : "false";

                        break;
                    default:
                        resultado = null;
                }
                break;
            case ">":
                switch (tipo) {
                    case "int":
                        op1 = convertStringToInt(operando1);
                        op2 = convertStringToInt(operando2);
                        resultado = (op1 > op2) ? "true" : "false";

                        break;
                    case "float":
                        opf1 = convertStringToFloat(operando1);
                        opf2 = convertStringToFloat(operando2);
                        resultado = (opf1 > opf2) ? "true" : "false";

                        break;
                    default:
                        resultado = null;
                }
                break;
            case ">=":
                switch (tipo) {
                    case "int":
                        op1 = convertStringToInt(operando1);
                        op2 = convertStringToInt(operando2);
                        resultado = (op1 >= op2) ? "true" : "false";

                        break;
                    case "float":
                        opf1 = convertStringToFloat(operando1);
                        opf2 = convertStringToFloat(operando2);
                        resultado = (opf1 >= opf2) ? "true" : "false";

                        break;
                    default:
                        resultado = null;
                }
                break;
            case "&&":
                switch (tipo) {
                    case "boolean":
                        opb1 = getBooleanValue(operando2);
                        opb2 = getBooleanValue(operando1);
                        resultado = (opb1 && opb2) ? "true" : "false";

                        break;
                    default:
                        resultado = null;
                }
                break;
            case "||":
                switch (tipo) {
                    case "boolean":
                        opb1 = getBooleanValue(operando2);
                        opb2 = getBooleanValue(operando1);
                        resultado = (opb1 || opb2) ? "true" : "false";
                        break;
                    default:
                        resultado = null;
                }
                break;
        }
        return resultado;
    }

    private String setNegacion(String value) {
        return (value.equals("true")) ? "false" : "true";
    }

}
