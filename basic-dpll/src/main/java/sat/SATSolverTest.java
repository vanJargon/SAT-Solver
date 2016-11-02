package sat;

/*
import static org.junit.Assert.*;

import org.junit.Test;
*/

import java.io.BufferedReader;
import java.util.ArrayList;
import java.io.FileInputStream;
import java.io.InputStreamReader;
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
		ArrayList<ArrayList<String>> list = new ArrayList<ArrayList<String>>();
		try {
<<<<<<< HEAD
			FileInputStream fis = new FileInputStream("D:\\Uni\\Year 2 Sophomore Term\\2D Materials\\SAT-Solver\\basic-dpll\\src\\main\\java\\s8Sat.cnf");
=======
			FileInputStream fis = new FileInputStream("C:\\Users\\student\\Desktop\\sampleCNF\\s8SAT.cnf");
>>>>>>> origin/master
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

	Formula formula = new Formula();

	for (ArrayList<String> clausestring: list) {
		Clause newClause = new Clause();
		for (String i:clausestring) {
//			System.out.print(i+":");
			if(i.matches("-.*")) {
				Variable newVar = new Variable(i.substring(1));
//				System.out.println(i.substring(1));
				newClause=newClause.add(PosLiteral.make(newVar).getNegation());
			} else {
				Variable newVar = new Variable(i);
				newClause=newClause.add(PosLiteral.make(newVar));
//				System.out.println(i);
			}
			formula = formula.addClause(newClause);

		}
	}

//	System.out.println(formula);
	Long start = System.currentTimeMillis();
	System.out.println("Result:"+SATSolver.solve(formula));
	Long end = System.currentTimeMillis();
	System.out.print("Time:"+(end-start)+"ms");




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
