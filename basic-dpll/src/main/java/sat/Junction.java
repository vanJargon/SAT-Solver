package sat;

import immutable.ImList;
import immutable.ImMap;
import sat.formula.Clause;
import sat.formula.Literal;

/**
 * Created by Kygrykhon on 11/3/2016.
 */
public class Junction extends Clause {
    ImList<Literal> nodes;
    ImMap<Literal, ImList<Literal>> chains;

    public Junction(Clause clause){
        nodes = clause.getLiterals();
    }

    public Clause getClause() {
        Clause clar = new Clause();
        for(Literal n: nodes){
            clar = clar.add(n);
        }
        return clar;
    }

    public boolean link(Clause clause){
        Clause modClause = clause;
        for(Literal n: nodes) {
            if(modClause.contains(n.getNegation())) {
                Clause rest = modClause.reduce(n.getNegation());
                ImList<Literal> clauserep = modClause.getLiterals();
                chains.put(n, clauserep);
                nodes = nodes.remove(n);
                for(Literal m:rest){
                    nodes = nodes.add(m);
                }
                return true;
            }
        }
        return false;
    }
}