package sat;
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

        Environment e = SATSolver.solve(makeFm(makeCl(a,b))	);
        System.out.print(e);
    }
}
