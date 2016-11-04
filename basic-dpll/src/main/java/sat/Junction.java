package sat;

import com.sun.org.apache.regexp.internal.RE;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import immutable.EmptyImList;
import immutable.ImList;
import immutable.ImListMap;
import immutable.ImMap;
import sat.env.Bool;
import sat.env.Environment;
import sat.env.REnvironment;
import sat.env.Variable;
import sat.formula.Clause;
import sat.formula.Literal;
import sat.formula.PosLiteral;

/**
 * Created by Kygrykhon on 11/3/2016.
 */
public class Junction {//extends Clause {
    ImList<Literal> nodes;
    HashMap<Literal, ImList<Literal>> chains;
    HashMap<Literal, ArrayList<Literal>> shackles; //list of inside variables that wil ahve the same value as key, outside variable

    public Junction(Clause clause){
        nodes = clause.getLiterals();
        chains = new HashMap<>();
        shackles = new HashMap<>();
        for(Literal i: clause){
            ArrayList<Literal> whaddup = new ArrayList<>();
            whaddup.add(i);
            shackles.put(i, whaddup);
        }
    }

    public int size(){return nodes.size();}

    public REnvironment getDecide(REnvironment decideEnv){
        REnvironment decidedEnv = decideEnv;
        if(nodes.isEmpty() || nodes==null){
            REnvironment resultNev = new REnvironment();
            for(Literal a: chains.keySet()){
                if(a.equals(PosLiteral.make(a.getVariable()))){ //if a positive literal
                    resultNev.put(a.getVariable(),Bool.TRUE);
                } else {
                    resultNev.put(a.getVariable(),Bool.FALSE);
                }
            }
            return resultNev;
        }
        for(Literal n: nodes) {
//            System.out.println("Not null..");
            if (decidedEnv.get(n.getVariable()) == Bool.UNDEFINED) { //means decidedEnv does not dictate all nodes
                System.out.println("Not enough assignment");
                return null;
            } else {
                ArrayList<Literal> bundle = shackles.get(n);
                boolean nodePosLiteral = n.equals(PosLiteral.make(n.getVariable()));
                Bool nodeValue = decidedEnv.get(n.getVariable());
                for (Literal nn: bundle){
                    boolean nnPosLiteral = nn.equals(PosLiteral.make(nn.getVariable()));
                    if(nodePosLiteral && nnPosLiteral) {
                        decidedEnv.put(nn.getVariable(), nodeValue);
                    } else if (nodePosLiteral || nnPosLiteral) {
                        decidedEnv.put(nn.getVariable(), nodeValue.not());
                    } else {
                        decidedEnv.put(nn.getVariable(), nodeValue);
                    }
                }
            }
        }

        return decidedEnv;

    }

    public REnvironment getDecisions(REnvironment decidedEnv) { //Take decision of values for nodes and return all other env
//        System.out.println("Chains:"+chains);
        if(nodes.isEmpty() || nodes==null){
            REnvironment resultNev = new REnvironment();
            for(Literal a: chains.keySet()){
                if(a.equals(PosLiteral.make(a.getVariable()))){ //if a positive literal
                    resultNev.put(a.getVariable(),Bool.TRUE);
                } else {
                    resultNev.put(a.getVariable(),Bool.FALSE);
                }
            }
            return resultNev;
        }
        for(Literal n: nodes) {
//            System.out.println("Not null..");
            if (decidedEnv.get(n.getVariable()) == Bool.UNDEFINED) { //means decidedEnv does not dictate all nodes
                System.out.println("Not enough assignment");
                return null;
            }
        }

         REnvironment grownEnv = decidedEnv;
         HashMap<Literal, ImList<Literal>> disChains = (HashMap<Literal, ImList<Literal>>)chains.clone();

        Literal assignLit = nodes.first();
        Bool set = grownEnv.get(assignLit.getVariable());
        boolean assignPos = assignLit.equals(PosLiteral.make(assignLit.getVariable()));
        nodes = nodes.remove(assignLit);
//        System.out.println("assigning "+assignLit);

            while (!disChains.isEmpty()){
//                System.out.print(".-");
                for(Map.Entry<Literal, ImList<Literal>> checkedClause: disChains.entrySet()) {
                    if (checkedClause.getValue().contains(assignLit)) {
//                        System.out.println(checkedClause.getValue()+" contains node "+assignLit);
                        for (Literal dm : checkedClause.getValue()) {
                            if (!dm.equals(assignLit.getNegation())) {
                                boolean dmPos = dm.equals(PosLiteral.make(dm.getVariable()));
                                if (dmPos && assignPos) {
                                    if(grownEnv.get(dm.getVariable())==Bool.UNDEFINED) {
                                        grownEnv.put(dm.getVariable(), set.not());
//                                        System.out.println("Setting "+dm+" to "+set.not());
                                    }
                                } else if (dmPos || assignPos) {
                                    if(grownEnv.get(dm.getVariable())==Bool.UNDEFINED) {
                                        grownEnv.put(dm.getVariable(), set);
//                                        System.out.println("Setting "+dm+" to "+set);
                                    }
                                } else {
                                    if(grownEnv.get(dm.getVariable())==Bool.UNDEFINED) {
                                        grownEnv.put(dm.getVariable(), set.not());

//                                        System.out.println("Setting "+dm+" to "+set.not());
                                    }
                                }
                            }
                        }
                        disChains.remove(assignLit);
//                        System.out.println("Dischain:" + disChains);
                        if(!nodes.isEmpty()){
                        assignLit = nodes.first();
                        set = grownEnv.get(assignLit.getVariable());
                        assignPos = assignLit.equals(PosLiteral.make(assignLit.getVariable()));
                        nodes = nodes.remove(assignLit);
                        } else {
                            disChains = new HashMap<>();
                        }

                    }
                }

//                    for(Literal a: checkedClause){
//                        if(!a.equals(assignLit)){
//                            Bool set = grownEnv.get(assignLit.getVariable());
//                            if(assignLit.equals(PosLiteral.make(assignLit.getVariable()).getNegation())){
//                                if(a.equals(PosLiteral.make(a.getVariable()))){
//                                    grownEnv.put(a.getVariable(),set);
//                                } else {
//                                    grownEnv.put(a.getVariable(),grownEnv.get(assignLit.getVariable()));
//                                }
//                            } else {
//                                if(a.equals(PosLiteral.make(a.getVariable()))){
//                                    grownEnv.put(a.getVariable(),grownEnv.get(assignLit.getVariable()));
//                                } else {
//                                    grownEnv.put(a.getVariable(),set);
//                                }
//                            }
//                            disChains.remove(assignLit);
//                        }
//                    }
//                } else {
//                    if(!nodes.isEmpty()){
//                        assignLit = nodes.first();
//                        nodes = nodes.remove(assignLit);
//                    } else {
//                        disChains = new HashMap<>();
//                    }
//                }
            }
        return grownEnv;
    }

    public Clause getClause() {
        if(nodes==null || nodes.isEmpty()) {
            return null;
        }
        Clause clar = new Clause();
        for(Literal n: nodes){
            clar = clar.add(n);
        }
        return clar;
    }

    public String toString() {return nodes.toString();}

    public boolean link(Clause clause){
        Clause modClause = clause;
        if(modClause==null){
            return false;
        } else if (modClause.size() ==1){
//            System.out.println(modClause);
            return false;
        }
        for(Literal n: nodes) {
//            System.out.println("Checking node "+n+"..");
//            System.out.println(n.getNegation());
//            System.out.println(modClause);
            if(modClause.contains(n.getNegation()) && modClause.size()>1) {
//                System.out.print(modClause + " linking through "+n+" --- ");
//                Clause rest = modClause.reduce(n);
//                System.out.println("->"+rest);

                ImList<Literal> clauserep = modClause.getLiterals();
                chains.put(n, clauserep);
//                System.out.print("Original:"+nodes);
//                System.out.println("Chain++ "+chains);
//                System.out.print("OrigiNodes:"+nodes);
                for(Literal m:clause){
//                    if(!nodes.contains(m) && !nodes.contains(m.getNegation())) {
////                        System.out.print("Add "+m+" ");
//                        nodes = nodes.add(m);
//                    } else if(!nodes.contains(m) && nodes.contains(m.getNegation())){
////                        nodes = new EmptyImList<>();
////                        nodes = nodes.remove(m);
////                        nodes = nodes.remove(m.getNegation());
//                    }
                    ArrayList<Literal> dummy = shackles.get(n);
                    dummy.add(m);
                    shackles.put(m, dummy);
                    shackles.remove(n);
                }
                nodes = nodes.remove(n);
//                System.out.print("to be"+nodes);
//                System.out.println(" and NewNodes:"+nodes);
                return true;
            }
        }
//        System.out.println("seems to have no link");
        return false;
    }
}