package sat;

import immutable.ImList;
import sat.env.Bool;
import sat.env.Environment;
import sat.env.Variable;
import sat.formula.Clause;
import sat.formula.Formula;
import sat.formula.Literal;
import sat.formula.PosLiteral;

/**
 * A simple DPLL SAT solver. See http://en.wikipedia.org/wiki/DPLL_algorithm
 */
public class SATSolver {
    /**
     * Solve the problem using a simple version of DPLL with backtracking and
     * unit propagation. The returned environment binds literals of class
     * bool.Variable rather than the special literals used in clausification of
     * class clausal.Literal, so that clients can more readily use it.
     * 
     * @return an environment for which the problem evaluates to Bool.TRUE, or
     *         null if no such environment exists.
     */
    public static Environment solve(Formula formula) {
        // TODO: implement this.
        // check for base case
//        System.out.println("Old:" + formula);
//        formula = NegateLink.negateLinkReduction(formula);
        if (formula==null || formula.getSize()==0) {
        	return new Environment();
        } else if (formula.getSize()==1 && (formula.getClauses().first()==null || formula.getClauses().first().chooseLiteral()==null)) {
        	return null;
        }
//    	System.out.println(">> START ------------------------");
//        System.out.println("Old:" + formula);
        //______________LATEST IMPLEMENTATION___________________________________
//        Formula sortedForm = formula.sortClauseSize();
        Clause inspectedClause = formula.getSmallestClause(); //ADDED ALGO: saves 7 seconds (67s->60s)
//        System.out.println("Smallest Clause="+inspectedClause);
//        System.out.println("-----------------");
        if (formula==null || formula.getSize()==0 ) {
//        	System.out.println("Null Formula - Success Case");
        	return new Environment();
        } else if (inspectedClause.isEmpty()){
//        	System.out.println("Empty Clause - Dead Case - BACKTRACKING");
        		return null;
        }
        ImList<Clause> clauselist = formula.getClauses();
        Variable assignVar = inspectedClause.chooseLiteral().getVariable();

        Literal inspectedLit = PosLiteral.make(assignVar);
//        System.out.println("Checking Literal"+inspectedLit.toString());
        Formula simplified = new Formula();
        Environment newEnv = new Environment();
//          3. Reduce the formula
        boolean shouldSetFalseInstead = false;
        for(Clause c: clauselist) {//formula.getClauses()){
            Clause newClause = c.reduce(inspectedLit);
//            System.out.println("newclause: "+newClause + "size:"+newClause.size());
            if (newClause!=null) {
            	if(newClause.size() > 0) {
                simplified = simplified.addClause(newClause);
            	} else {
            		shouldSetFalseInstead = true;
            	}
//                System.out.print(simplified);
            } else if(c.isUnit() && c.chooseLiteral().equals(inspectedLit.getNegation())){
                shouldSetFalseInstead = true;
        }
        }
        // 4. RECURSE:
        if(!shouldSetFalseInstead) {
            newEnv = solve(simplified);
            if (newEnv != null) {
                //          -> add the RECURSE envirinment to this environment
                //          -> return
//            	System.out.println("Successful Recursion - Add & Return");
                return newEnv.put(assignVar, Bool.TRUE);
            } else {
            	shouldSetFalseInstead = true;
            }
        }

        //      if RECURSE != null:
        // Set False Instead
        simplified = new Formula();
//        System.out.println("Failed to reduce PosLiteral - Moving to NegLiteral");
        for(Clause e: formula.getClauses()){//formula.getClauses()){
            Clause newerClause = e.reduce(inspectedLit.getNegation());
            if (newerClause!= null) {
                if (newerClause.size() >0) {
                    simplified = simplified.addClause(newerClause);
                } else {
//                    System.out.println("Reducing Posliteral and Negliteral result in empty clause - BACKTRACKING");
                    return null;
                }
            }
        }
        newEnv = solve(simplified);
        if (newEnv==null){
            return null;
        } else {
            return newEnv.putFalse(assignVar);
        }
        
    }
    

    /**
     * Takes a partial assignment of variables to values, and recursively
     * searches for a complete satisfying assignment.
     * 
     * @param clauses
     *            formula in conjunctive normal form
     * @param env
     *            assignment of some or all variables in clauses to true or
     *            false values.
     * @return an environment for which all the clauses evaluate to Bool.TRUE,
     *         or null if no such environment exists.
     */
    private static Environment solve(ImList<Clause> clauses, Environment env) {
        // TODO: implement this.
        // 1. remove clauses that is already assigned
        Formula formula = new Formula();
        for(Clause c: clauses) {
            for(Literal lit: c){
                Bool litvalue = env.get(lit.getVariable());
                if(litvalue == Bool.TRUE) {
                    c.reduce(PosLiteral.make(lit.getVariable()));
                } else if (litvalue== Bool.FALSE) {
                    c.reduce(PosLiteral.make(lit.getVariable()).getNegation());
                }
            }
            if(c!=null) {
                formula.addClause(c);
            }
        }

        return solve(formula);

//        throw new RuntimeException("not yet implemented.");
    }

    /**
     * given a clause list and literal, produce a new list resulting from
     * setting that literal to true
     * 
     * @param clauses
     *            , a list of clauses
     * @param l
     *            , a literal to set to true
     * @return a new list of clauses resulting from setting l to true
     */
    private static ImList<Clause> substitute(ImList<Clause> clauses,
            Literal l) {
        // TODO: implement this.
        for(Clause c: clauses){
            Clause newClause = c.reduce(l);
            clauses.remove(c);
            if (newClause!= null) {
                clauses.add(newClause);
            }
        }
        return clauses;
//        throw new RuntimeException("not yet implemented.");
    }

}
