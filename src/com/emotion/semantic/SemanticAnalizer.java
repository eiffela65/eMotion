
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

    public void showSymbolTable() {
        System.out.println("************  ANALISIS SEMANTICO  **************");
        for (Map.Entry<String, Symols> entry : semanticSymbolTable.entrySet()) {
//            System.out.printf("Key : %s and Variable: %s and Type: %s and Value: %s %n", entry.getKey(), entry.getValue().getName(), entry.getValue().getType(), entry.getValue().getValue());
            System.out.printf("Variable: %s and Type: %s and Value: %s %n", entry.getValue().getName(), entry.getValue().getType(), entry.getValue().getValue());
        }
    }
}
