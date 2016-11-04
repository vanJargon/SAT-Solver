package sat.formula;

import java.util.ArrayList;

/**
 * Created by Kygrykhon on 11/4/2016.
 */

public class RClause {
    private ArrayList<Literal> literals;

    public RClause(){
        literals = new ArrayList<>();
    }

    public RClause(ArrayList<Literal> litlist){literals = litlist;}

    public Literal chooseLiteral() {return literals.get(0);}

    public boolean isUnit() {return literals.size() == 1;}

    public boolean isEmpty() {return literals.size() == 0;
    }

    public int size() {
        return literals.size();
    }

    public ArrayList<Literal> getLiterals() {return literals;}

    public boolean contains(Literal l) {
        return literals.contains(l);
    }

    public void add(Literal l){
        if(!literals.contains(l)) {
            if(!literals.contains(l.getNegation())) {
                literals.add(l);
            } else {
                literals = null;
            }
        }
    }

    public void reduce(Literal l){
        if(literals.contains(l)) {
            literals = null;
        } else if (literals.contains(l.getNegation())){
            literals.remove(l);
        }
    }

    public String toString() {
        return "RClause" + literals;
    }

    public boolean equals (Object that) {
        if (this == that) return true;
        if (!(that instanceof RClause)) return false;
        RClause c = (RClause) that;
        if (size() != c.size()) return false;
        for (Literal l: literals)
            if (!(c.contains(l))) return false;
        return true;
    }
}
