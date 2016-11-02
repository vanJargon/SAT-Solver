package sat;

/*
import static org.junit.Assert.*;

import org.junit.Test;
*/

import sat.env.*;
import sat.formula.*;


public class SATSolverTest {
    Literal a = PosLiteral.make("a");
    Literal b = PosLiteral.make("b");
    Literal c = PosLiteral.make("c");
    Literal na = a.getNegation();
    Literal nb = b.getNegation();
    Literal nc = c.getNegation();

public static void main(String[] args){
		ArrayList<ArrayList> list = new ArrayList<ArrayList>();
		try {
			FileInputStream fis = new FileInputStream("cnf file directory");
			BufferedReader br = new BufferedReader(new InputStreamReader(fis));

			String line = null;
			ArrayList subList = new ArrayList();
			while ((line = br.readLine()) != null) {
				if (line.length() != 0) {
					if (line.charAt(0) != 'c' && line.charAt(0) != 'p') {
						String var = "";
						for (int i = 0; i < line.length(); i++) {
							if (line.charAt(i) == ' ') {
								if (!var.equals("0")) {
									subList.add(var);
									var = "";
								} else if (var.equals("0")) {
									subList.remove("0");
									list.add(new ArrayList(subList));
									subList.clear();
								}
							} else if (i == line.length() - 1) {
								if (line.charAt(i) == '0'){
										list.add(new ArrayList(subList));
										subList.clear();
								}
								else{
									var += line.charAt(i);
									subList.add(var);
								}
							} else {
								var += line.charAt(i);
							}

						}
					}
				}
			}

			br.close();
		} catch (Exception e) {
			System.out.println(e);
		}
		System.out.println(list);
	}

	
    public void testSATSolver1(){
    	// (a v b)
    	Environment e = SATSolver.solve(makeFm(makeCl(a,b))	);
/*
    	assertTrue( "one of the literals should be set to true",
    			Bool.TRUE == e.get(a.getVariable())  
    			|| Bool.TRUE == e.get(b.getVariable())	);
    	
*/    	
    }
    
    
    public void testSATSolver2(){
    	// (~a)
    	Environment e = SATSolver.solve(makeFm(makeCl(na)));
/*
    	assertEquals( Bool.FALSE, e.get(na.getVariable()));
*/    	
    }
    
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
    
    
}
