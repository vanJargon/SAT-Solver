package sat;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import immutable.EmptyImList;
import immutable.ImList;
import immutable.ImMap;
import sat.formula.Clause;
import sat.formula.Formula;
import sat.formula.Literal;

/**
 * Created by Kygrykhon on 11/3/2016.
 */

public class NegateLink {



    public static Formula negateLinkReduction(Formula formula) {
        if(formula.getSize()<=1) {
            return formula;
        } else {
            ImList<Clause> formClause = formula.getClauses();
            Formula returned = new Formula();
            ArrayList<Junction> junctCollect = new ArrayList<>();
            Junction firstJunction = new Junction(formula.getLargestClause());
            junctCollect.add(firstJunction);
            formClause = formClause.remove(formula.getLargestClause());
            while(!formClause.isEmpty()){
                for(Junction j:junctCollect){
                    if(j.link(formClause.first())){
                        formClause = formClause.rest();
                    } else {
                        junctCollect.add(new Junction(formClause.first()));
                        formClause = formClause.rest();
                    }
                }
            }
            Formula newForm = new Formula();
            for(Junction k:junctCollect){
                newForm = newForm.addClause(k.getClause());
            }
            return newForm;
        }
    }
}
