package sat;

import java.util.ArrayList;

import immutable.ImList;
import sat.env.Environment;
import sat.formula.Clause;
import sat.formula.Formula;
import sat.formula.JFormula;
import sat.formula.Literal;

/**
 * Created by Kygrykhon on 11/3/2016.
 */

public class ClauseLearning {
    //NegateLink Implementation
    public static JFormula CLReduce(Formula formula){
        if(formula.getSize()<=1) {
            return new JFormula(formula);
        } else {
            JFormula returned = new JFormula(new Formula());
            ArrayList<Junction> formJunc = returned.getJunctions();
            ImList<Clause> formClause = formula.getClauses();
            ArrayList<Junction> junctCollect = new ArrayList<>();

            Junction firstJunction = new Junction(formula.getLargestClause());
            junctCollect.add(firstJunction);
//            System.out.println("LArgest = "+firstJunction.getClause());
            formClause = formClause.remove(formula.getLargestClause());

            while(!formClause.isEmpty()){
                ArrayList<Junction> newJunct = new ArrayList<>();
                ArrayList<Junction> removeJunct = new ArrayList<>();
//                System.out.println("formCLause left:"+formClause);
                boolean chained = false;

//                System.out.println("FirstClause-=>"+formClause.first());

                for(Junction j:junctCollect){
                    if(j.link(formClause.first())){
                        formClause = formClause.remove(formClause.first());
//                        System.out.println("YES");
                        if (chained){
                            removeJunct.add(j);
                        }
                        chained = true;
                    }
                }

                if(!chained && formClause.first()!=null) {
                            newJunct.add(new Junction(formClause.first()));
//                            System.out.println("new junction collection:"+newJunct);
                            formClause = formClause.rest();
                        }

                junctCollect.addAll(newJunct);
//                System.out.println("Total Junction collection so far:"+junctCollect);
                for(Junction jk: junctCollect){
                    for (Junction mn: removeJunct){
//                        System.out.print("Checking "+jk.getClause()+" with "+mn.getClause());
                        jk.link(mn.getClause());
                    }
                }

                if(chained){
                    for(Junction jk: junctCollect){
                        for (Junction mn: junctCollect){
//                            System.out.print("Checking "+jk.getClause()+" with "+mn.getClause());
                            if(jk.link(mn.getClause())){
                                System.out.print("Chained! Removing:"+mn);
                                removeJunct.add(mn);
                            }
                        }
                    }
                    for(Junction trash: removeJunct){
                        junctCollect.remove(trash);
                    }
                }

                junctCollect.removeAll(removeJunct);
//                System.out.println("Total Junction collection so far:"+junctCollect);
            }

            return new JFormula(junctCollect);
        }
    }




}
