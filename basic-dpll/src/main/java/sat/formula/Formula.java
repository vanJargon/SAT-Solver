/**
 * Author: dnj, Hank Huang, 6.005 staff
 * Date: March 7, 2009
 * 6.005 Elements of Software Construction
 * (c) 2007-2009, MIT 6.005 Staff
 */
package sat.formula;

import immutable.EmptyImList;
import immutable.ImList;
import immutable.ImListIterator;
import immutable.NonEmptyImList;

import java.util.Iterator;

import sat.env.Variable;

/**
 * Formula represents an immutable boolean formula in conjunctive normal form,
 * intended to be solved by a SAT solver.
 */
public class Formula {
    private final ImList<Clause> clauses;

    // Rep invariant:
    // clauses != null
    // clauses contains no null elements (ensured by spec of ImList)
    //
    // Note: although a formula is intended to be a set,
    // the list may include duplicate clauses without any problems.
    // The cost of ensuring that the list has no duplicates is not worth paying.
    //
    //
    // Abstraction function:
    // The list of clauses c1,c2,...,cn represents
    // the boolean formula (c1 and c2 and ... and cn)
    //
    // For example, if the list contains the two clauses (a,b) and (!c,d), then
    // the
    // corresponding formula is (a or b) and (!c or d).

    void checkRep() {
        assert this.clauses != null : "SATProblem, Rep invariant: clauses non-null";
    }

    /**
     * Create a new problem for solving that contains no clauses (that is the
     * vacuously true problem)
     * 
     * @return the true problem
     */
    public Formula() {
        this(new EmptyImList<Clause>());
        checkRep();
    }

    /**
     * Create a new problem for solving that contains a single clause with a
     * single literal
     * 
     * @return the problem with a single clause containing the literal l
     */
    public Formula(Variable l) {
        this(new Clause(PosLiteral.make(l.getName())));
    }

    /**
     * Create a new problem for solving that contains a single clause
     * 
     * @return the problem with a single clause c
     */
    public Formula(Clause c) {
        this(new NonEmptyImList<Clause>(c));
    }

    private Formula(ImList<Clause> clauses) {
        this.clauses = clauses;
    }

    /**
     * Add a clause to this problem
     * 
     * @return a new problem with the clauses of this, but c added
     */
    public Formula addClause(Clause c) {
        return new Formula(clauses.add(c));
    }

    /**
     * Get the clauses of the formula.
     * 
     * @return list of clauses
     */
    public ImList<Clause> getClauses() {
        return clauses;
    }

    /**
     * Iterator over clauses
     * 
     * @return an iterator that yields each clause of this in some arbitrary
     *         order
     */
    public Iterator<Clause> iterator() {
        return new ImListIterator<Clause>(clauses);
    }

    /**
     * @return a new problem corresponding to the conjunction of this and p
     */
    public Formula and(Formula p) {
        ImList<Clause> pclauses = p.clauses;
        for (Clause c : clauses) {
            pclauses = pclauses.add(c);
        }
        return new Formula(pclauses);
    }

    /**
     * @return a new problem corresponding to the disjunction of this and p
     */
    public Formula or(Formula p) {
        ImList<Clause> result = new EmptyImList<Clause>();
        for (Clause c1 : p.clauses) {
            for (Clause c2 : clauses) {
                Clause c = c1.merge(c2);
                if (c != null)
                    result = result.add(c);
            }
        }
        return new Formula(result);
    }

    /**
     * @return a new problem corresponding to the negation of this
     */
    public Formula not() {
        Formula result = new Formula(new Clause());
        for (Clause c : clauses) {
            result = result.or(negate(c));
        }
        return result;
    }

    /*
     * Make a problem corresponding to the negation of a clause containing one
     * clause with a single negated literal for each literal of the original
     * clause.
     */
    private static Formula negate(Clause c) {
        // explode: make list of unit clauses
        ImList<Clause> result = new EmptyImList<Clause>();
        for (Literal l : c) {
            result = result.add(new Clause(l.getNegation()));
        }
        return new Formula(result);
    }

    /**
     * 
     * @return number of clauses in this
     */
    public int getSize() {
        return clauses.size();
    }

    public String toString() {
        String result = "Problem[";
        for (Clause c : clauses)
            result += "\n" + c;
        return result + "]";
    }

    // ADDED FUNCTION
     private ImList<Clause> sortByClauseSize(ImList<Clause> clauses) {
         //divide and conquer sorting
         //base cases
//         System.out.println("Clauses :" + clauses);
         if(clauses.size() <=1) {
             return clauses;
         } else if (clauses.size() == 2) {
             if(clauses.first().size() <= clauses.rest().first().size()){
                 return clauses;
             } else {
                 ImList<Clause> swapClause = new EmptyImList<Clause>();
                 swapClause = swapClause.add(clauses.first());              // Note for ImList:
                 swapClause = swapClause.add(clauses.rest().first());       // ADDED LAST = .first()
                 return swapClause;
             }
         } else {
             ImList<Clause> firstSeg = new EmptyImList<Clause>();

             ImList<Clause> secondSeg = new EmptyImList<Clause>();

             int counter = 0;
             int limit = clauses.size() /2;
             for(Clause e: clauses) {
                 if (counter > limit) {
                     firstSeg = firstSeg.add(e);
                 } else {
                     secondSeg = secondSeg.add(e);
                 }
                 counter++;
             }

             ImList<Clause> sortedFirst = sortByClauseSize(firstSeg);
             ImList<Clause> sortedSecond = sortByClauseSize(secondSeg);
             ImList<Clause> finalList = new EmptyImList<Clause>();
             while (sortedFirst.size()>0 || sortedSecond.size()>0) {
                 if((sortedFirst.first()!=null) && (sortedSecond.first()!=null)){
                     if (sortedFirst.first().size() > sortedSecond.first().size()) {
                         finalList = finalList.add(sortedFirst.first());
                         sortedFirst = sortedFirst.remove(sortedFirst.first());
                     } else {
                         finalList = finalList.add(sortedSecond.first());
                         sortedSecond = sortedSecond.remove(sortedSecond.first());
                     }
                 } else if (sortedFirst.first()==null && sortedSecond.first()!=null){
                     finalList = finalList.add(sortedSecond.first());
                     sortedSecond = sortedSecond.remove(sortedSecond.first());
                 } else if (sortedFirst.first()!=null && sortedSecond.first()==null){
                     finalList = finalList.add(sortedFirst.first());
                     sortedFirst = sortedFirst.remove(sortedFirst.first());
                 }
             }
             return finalList;
         }
     }

    public Formula sortClauseSize() {
        return new Formula(sortByClauseSize(clauses));
    }
    
    private Clause filterClauseBySize(ImList<Clause> clauses) { //returns smallest clause
    	if(clauses.size() == 0 ) {
    		return null;
    	} else if(clauses.size() == 1) {
    		return clauses.first();
    	} else if(clauses.size() == 2) {
    		if(clauses.first().size() < clauses.rest().first().size()){
    			return clauses.first();
    		} else {
    			return clauses.rest().first();
    		}
    	} else {
    		ImList<Clause> firstSeg = new EmptyImList<>();
    		ImList<Clause> secondSeg = new EmptyImList<>();
    		int counter = 0;
    		int limit = clauses.size() /2;
    		
    		for(Clause c: clauses) {
    			if(counter<limit) {
    				firstSeg = firstSeg.add(c);
    			} else {
    				secondSeg = secondSeg.add(c);
    			}
                counter++;
    		}
    		
    		Clause firstClause = filterClauseBySize(firstSeg);
    		Clause secondClause = filterClauseBySize(secondSeg);
    		
    		if(firstClause==null){
    			return secondClause;
    		} else if (secondClause==null) {
    			return firstClause;
    		} else if (firstClause.isEmpty() || secondClause.isEmpty()) {
    			return new Clause();
    		} else if (firstClause.size() <= secondClause.size()) {
    			return firstClause;
    		} else {
    			return secondClause;
    		}
    		
    	}
    }

    private Clause filterClauseByHeave(ImList<Clause> clauses) { //returns largest clause
        if(clauses.size() == 0 ) {
            return null;
        } else if(clauses.size() == 1) {
            return clauses.first();
        } else if(clauses.size() == 2) {
            if(clauses.first().size() > clauses.rest().first().size()){
                return clauses.first();
            } else {
                return clauses.rest().first();
            }
        } else {
            ImList<Clause> firstSeg = new EmptyImList<>();
            ImList<Clause> secondSeg = new EmptyImList<>();
            int counter = 0;
            int limit = clauses.size() /2;

            for(Clause c: clauses) {
                if(counter<limit) {
                    firstSeg = firstSeg.add(c);
                } else {
                    secondSeg = secondSeg.add(c);
                }
                counter++;
            }

            Clause firstClause = filterClauseBySize(firstSeg);
            Clause secondClause = filterClauseBySize(secondSeg);

            if(firstClause==null){
                return secondClause;
            } else if (secondClause==null) {
                return firstClause;
            }  else if (firstClause.size() >= secondClause.size()) {
                return firstClause;
            } else {
                return secondClause;
            }
        }
    }

    public Clause getLargestClause() {
        return filterClauseByHeave(clauses);
    }
    
    public Clause getSmallestClause() {
    	return filterClauseBySize(clauses);
    }
}
