
package com.emotion.semantic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;


public class SemanticAnalizer {

    public Map<String, Symols> semanticSymbolTable = new HashMap<String, Symols>();
    public Stack<String> operandos = new Stack();
    public Stack<String> operadores = new Stack();
    public Stack<String> tipos = new Stack();
    public Stack<String> temporal = new Stack();
    public Stack<Integer> saltos = new Stack();
    public List<Cuadruplos> cuadruplos = new ArrayList<Cuadruplos>();

    public Symols getVariable(String variable) {
        if (isVariable(variable)) {
            return semanticSymbolTable.get(variable);
        }
        return null;
    }

    public boolean isVariable(String variable) {
        return semanticSymbolTable.containsKey(variable);
    }
    
    public boolean isEmptyOperandosStack(){
        if(operandos.empty())
            return true;
        return false;
    }
    
    public boolean isEmptyOperadoresStack(){
        if(operadores.empty())
            return true;
        return false;
    }
    
    public boolean isEmptyTiposStack(){
        if(tipos.empty())
            return true;
        return false;
    }
    
    public boolean isEmptySaltosStack(){
        if(saltos.empty())
            return true;
        return false;
    }
    
    public boolean isEmptyTemporalStack(){
        if(temporal.empty())
            return true;
        return false;
    }

    public String showSymbolTable() {
        String data = "";
        System.out.println("--- TABLA DE SIMBOLOS ---");
        for (Map.Entry<String, Symols> entry : semanticSymbolTable.entrySet()) {
//            System.out.printf("Key : %s and Variable: %s and Type: %s and Value: %s %n", entry.getKey(), entry.getValue().getName(), entry.getValue().getType(), entry.getValue().getValue());
            System.out.printf("Variable: %s | Type: %s | Value: %s %n", entry.getValue().getName(), entry.getValue().getType(), entry.getValue().getValue());
            data += entry.getValue().getName() + " # " + entry.getValue().getType() + "\n";
        }
        data += "\n\n";
        return data;
    }
    
    public String showCuadruplos() {
        String data = "";
        System.out.println("---  CUADRUPLOS  ---");
        int i = 0;
        for (Cuadruplos cuadruplo : cuadruplos) {
            System.out.printf("%d Operador: %s   Operando1: %s   Operando2: %s   Resultado: %s   Apuntador: %d %n", i, cuadruplo.getOperador(), cuadruplo.getOperando1(), cuadruplo.getOperando2(), cuadruplo.getResultado(), cuadruplo.getSalto());
            i++;
            data += i + " # " + cuadruplo.getOperador() + " # " + cuadruplo.getOperando1() + " # " + cuadruplo.getOperando2() + " # " + cuadruplo.getResultado() + " # " + cuadruplo.getSalto() + "\n";
        }
        data += "\n\n";
        return data;
    }
}
