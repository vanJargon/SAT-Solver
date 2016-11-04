package sat;
import java.util.Locale;

import sat.env.*;
import sat.formula.*;
import sat.formula.Literal;
import sat.formula.PosLiteral;

/**
 * Created by Kygrykhon on 11/1/2016.
 */

public class MainCLass {

    private static Formula makeFm(Clause... e) {
        Formula f = new Formula();
        for (Clause c : e) {
            f = f.addClause(c);
        }
        return f;
    }

    private static Clause makeCl(Literal... e) {
        Clause c = new Clause();
        for (Literal l : e) {
            c = c.add(l);
        }
        return c;
    }

    public static void main(String[] args) {
        Literal a = PosLiteral.make("a");
        Literal b = PosLiteral.make("b");
        Literal c = PosLiteral.make("c");
        Literal na = a.getNegation();
        Literal nb = b.getNegation();
        Literal nc = c.getNegation();

        Formula theformula = makeFm(makeCl(nb,c),makeCl(a),makeCl(b,a),makeCl(a,nc));

        Long start = System.currentTimeMillis();

//        Environment e = SATSolver.solve(theformula);

        Long end = System.currentTimeMillis();
//        System.out.println(e);
        System.out.print("Time taken:" + (end-start) + "ms");
    }
}
