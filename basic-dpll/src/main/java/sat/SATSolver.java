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
    	System.out.println(">> START ------------------------");
        System.out.println("Old:" + formula);
        
//        System.out.println(formula.getSize());
        //1b. New literal to save
        Variable assignVar = new Variable("dummy");
        //2. Find unit clause:
        //      if found:
        //          -> save the literal
        //          -> add

        //______________LATEST IMPLEMENTATION___________________________________
        Formula sortedForm = formula.sortClauseSize();
        System.out.println("New:"+sortedForm);
        System.out.println("-----------------");
                //Problem - shouldnt call this for every iteration
        if (formula==null || formula.getSize()==0 ) {
        	System.out.println("Null Formula - Success Case");
        	return new Environment();
        	//|| formula.getSize()==1 && formula.getClauses().first()==null) {
       
        } else if (sortedForm.getClauses().first().isEmpty()){
        	System.out.println("Empty Clause - Dead Case - BACKTRACKING");
        		return null;
        }
        ImList<Clause> clauselist = sortedForm.getClauses();
//        System.out.println(sortedForm.getClauses().first());
        assignVar = sortedForm
                .getClauses()
                .first()
                .chooseLiteral()
                .getVariable();
                // Once formula is sorted, the first clause will usually be unit clause

        Literal inspectedLit = PosLiteral.make(assignVar);
        System.out.println("Checking Literal"+inspectedLit.toString());
        Formula simplified = new Formula();
        Environment newEnv = new Environment();
//          3. Reduce the formula
        boolean shouldSetFalseInstead = false;
        for(Clause c: sortedForm.getClauses()) {//formula.getClauses()){
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
            	System.out.println("Successful Recursion - Add & Return");
                Environment finalEnv = newEnv.put(assignVar, Bool.TRUE);
                return finalEnv;

            } else {
            	shouldSetFalseInstead = true;
            }
        }

        //      if RECURSE != null:
        
            //      if RECURSE == null:
            //          -> in environment, set literal to be 0 instead of 1
            //          -> RECURSE2
            //              - if RECURSE2 != null:
            //                  > add environment
            //                  > return
            //              - else: RETURN NULL
        simplified = new Formula();
        System.out.println("Failed to reduce PosLiteral - Moving to NegLiteral");
        for(Clause e: sortedForm.getClauses()){//formula.getClauses()){
            Clause newerClause = e.reduce(inspectedLit.getNegation());
            if (newerClause!= null) {
                simplified = simplified.addClause(newerClause);
            }
        }
        newEnv = solve(simplified);
        if (newEnv==null){
            return null;
        } else {
            Environment finalEnv = newEnv.putFalse(assignVar);
            return finalEnv;
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
