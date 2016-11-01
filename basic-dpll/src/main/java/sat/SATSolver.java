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
        if (formula==null || formula.getSize()==0) {
            return new Environment();
        }

        //1b. New literal to save
        Variable assignVar = new Variable("dummy");
        //2. Find unit clause:
        //      if found:
        //          -> save the literal
        //          -> add

        for(Clause c: formula.getClauses()) {
            if (c.isUnit()) {
                assignVar = new Variable(c.chooseLiteral().getVariable().toString());
                break;
            }
        }

        if (assignVar.toString() == "dummy") {
        //      if not found:
        //          -> save the first variable
            assignVar = formula.getClauses().first().chooseLiteral().getVariable();
        }

        Literal inspectedLit = PosLiteral.make(assignVar);

        Formula simplified = new Formula();
        //  3. Reduce the formula
        for(Clause c: formula.getClauses()){
            Clause newClause = c.reduce(inspectedLit);
            if (newClause!= null) {
                simplified.addClause(newClause);
            }
        }
        // 4. RECURSE:

        Environment newEnv = solve(simplified);
        //      if RECURSE != null:
        if (newEnv != null) {
            //          -> add the RECURSE envirinment to this environment
            //          -> return
            System.out.println("checkd "+assignVar.getName()+" equals true");
            newEnv.put(assignVar, Bool.TRUE);
            System.out.println(newEnv.get(assignVar));
            return newEnv;
            //      if RECURSE == null:
            //          -> in environment, set literal to be 0 instead of 1
            //          -> RECURSE2
            //              - if RECURSE2 != null:
            //                  > add environment
            //                  > return
            //              - else: RETURN NULL
        } else {
            simplified = new Formula();
            for(Clause c: formula.getClauses()){
                Clause newClause = c.reduce(inspectedLit.getNegation());
                if (newClause!= null) {
                    simplified.addClause(newClause);
                }
            }
            newEnv = solve(simplified);
            if (newEnv==null){
                return null;
            } else {
                newEnv.putFalse(assignVar);
                System.out.println("checkd "+assignVar.getName()+" equals false");
                return newEnv;
            }

        }

//        throw new RuntimeException("not yet implemented.");
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
        throw new RuntimeException("not yet implemented.");
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
        throw new RuntimeException("not yet implemented.");
    }

}
