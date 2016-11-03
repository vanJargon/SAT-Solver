package sat.otherJ;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by privatejw on 3/11/2016.
 */

public class OtherStuff {
    ArrayList<ArrayList<String>> currentList;

    ArrayList<ArrayList<String>> nextList;
    ArrayList<String> decisionTree = new ArrayList<>();
    HashMap<String, ArrayList<String>> dList;
    int numVars = 0;

    HashMap<String, Boolean> varVals = new HashMap<>();
    boolean neg;
    boolean overallClause;
    int solvable = 0;
    boolean donePopping;
    String poppedVar;
    ArrayList<ArrayList<String>> readFile(String filename) {

        ArrayList<ArrayList<String>> fList = new ArrayList<>();
        ArrayList<String> clauseList;
        String line;
        int varNum = 0;
        try {
            FileInputStream fis = new FileInputStream(filename);
            BufferedReader br = new BufferedReader(new InputStreamReader(fis));

            while ((line = br.readLine()) != null) {
                clauseList = new ArrayList<String>();
                if (line.length() != 0) {
                    String[] subList = line.split("\\s+");
                    if (subList[0].equals("p")){
                        numVars = Integer.parseInt(subList[2]);
                    }
                    if (!subList[0].equals("c")){
                        for (String i : subList){
                            if (!i.equals("0")){
                                clauseList.add(i);
                                fList.add(new ArrayList<String>(clauseList));
                            }
                        }
                    }

                }
            }

            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fList;
    }

    void runMe() {
        currentList = readFile("D://SUTD Term 4//50 .002 - LI01 Computation Structures//2D cnf//findsolssat//optimisedAdderBti3.cnf");

        for (ArrayList<String> clause : currentList) {
            overallClause = false;
            for (String variable : clause) {
                if (solvable != 0) return;
                neg = false;
                if (variable.matches("-.*")) {
                    neg = true;
                    variable = variable.substring(1);
                }
                if (!varVals.containsKey(variable)) {
                    varVals.put(variable, !neg);
                    // put the key and empty forced list into decisionList
                    dList.put(variable, new ArrayList<String>());
                    checkVar(variable);
                }
            }
        }
    }

    void checkVar(String variable) {
        // if no conflicts here
        if (checkRestOfList(variable)) {
            // if all values assigned, solution found
            if (varVals.size() == numVars) {
                solvable = 1;
                System.out.println("Solution found");
                return;
            }
        } else {
            // if there's a conflict, invert the decision variable
            resetVarVals(variable);
            // try again
            if (checkRestOfList(variable)) {
                // if all values assigned, solution found
                if (varVals.size() == numVars) {
                    solvable = 1;
                    System.out.println("Solution found");
                    return;
                }
            } else {
                // if still no solution, check decision tree if anything forced this var
                for (String confVar: dList.keySet()) {
                    if (dList.get(confVar).contains(variable)) {
                        // reset all forced vars
                        resetVarVals(confVar);
                        // remove variable from decisionList
                        dList.remove(variable);
                        // try again
                        checkVar(confVar);
                    }
                }
                // if nothing forced this var, there is no solution
                solvable = -1;
                System.out.println("UNSAT");
            }
        }
    }

    boolean checkRestOfList(String dVar) {
        // initialise forcedList
        ArrayList<String> forcedList = dList.get(dVar);
        ArrayList<String> newClause;
        // loop through all clauses
        for (ArrayList<String> clause : currentList) {
            // only find forced variables to add to forcedList, so clause must contain dVar
            if (clause.contains("-"+dVar) && varVals.get(dVar)) {
                newClause = new ArrayList<>();
                newClause.addAll(clause);
                newClause.remove("-"+dVar);
                overallClause = false;
                for (String variable : newClause) {
                    neg = false;
                    if (variable.matches("-.*")) {
                        neg = true;
                        variable = variable.substring(1);
                    }
                    if (!varVals.containsKey(variable)) {
                        varVals.put(variable, !neg);
                        // put the key and empty forced list into decisionList
                        dList.put(variable, new ArrayList<String>());
                        checkVar(variable);
                    }
                }
            } else if (clause.contains(dVar) && !varVals.get(dVar)) {

            }
        }

        return false;
    }

    void resetVarVals(String variable) {
        // invert variable value
        varVals.put(variable, !varVals.get(variable));
        // reset forced values to null
        for (String r : dList.get(variable)) {
            varVals.remove(r);
        }
        dList.put(variable, new ArrayList<String>());
    }

    private class stack {
        ArrayList<String> s = new ArrayList<>();
        void add(String var) {
            s.add(var);
        }
        void pop() {}
    }

    private class node {
        String name;
        boolean value;
        node left;
        node right;
        node(String name, boolean value) {
            this.name = name;
            this.value = value;
        }
        void setLeft(node n) {
            this.left = n;
        }
        void setRight(node n) {
            this.right = n;
        }
    }

//    public static void main(String[] args) {
//        (new OtherStuff()).runMe();
//    }
}
