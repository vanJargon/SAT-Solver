package sat.env;

import java.util.HashMap;

import immutable.ImListMap;
import immutable.ImMap;

/**
 * Created by Kygrykhon on 11/3/2016.
 */

public class REnvironment {
    private HashMap<Variable, Bool> bindings;

    private REnvironment(HashMap <Variable, Bool> bindings) {
        this.bindings = bindings;
    }

    public REnvironment() {
        this (new HashMap<Variable, Bool>());
    }

    /**
     * @return a new environment in which l has the value b
     * if a binding for l already exists, overwrites it
     */
    public void put(Variable v, Bool b) {
        bindings.put(v,b);
    }

    public HashMap<Variable, Bool> getBindings() { return bindings;}

    /**
     * @return a new environment in which l has the value Bool.TRUE
     * if a binding for l already exists, overwrites it
     */
    public void putTrue(Variable v) {
        bindings.put (v, Bool.TRUE);
    }


    /**
     * @return a new environment in which l has the value Bool.FALSE
     * if a binding for l already exists, overwrites it
     */
    public void putFalse(Variable v) {
        bindings.put (v, Bool.FALSE);
    }

    /**
     * @return the boolean value that l is bound to, or
     * the special UNDEFINED value of it is not bound
     */
    public Bool get(Variable v){
        Bool b = bindings.get(v);
        if (b==null) return Bool.UNDEFINED;
        else return b;
    }

    @Override
    public String toString () {
        return "Environment:" + bindings;
    }
}
