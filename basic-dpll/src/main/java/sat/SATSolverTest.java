package sat;

/*
import static org.junit.Assert.*;

import org.junit.Test;
*/

import java.io.BufferedReader;
import java.text.Normalizer;
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
		ArrayList<ArrayList<String>> list = new ArrayList<>();
		ArrayList<String> clauseList = new ArrayList<String>();
		int varNum = 0;
		try {
			FileInputStream fis = new FileInputStream("C://Users//student//Desktop//sampleCNF//largeSat.cnf");
			BufferedReader br = new BufferedReader(new InputStreamReader(fis));

			String line = null;

			while ((line = br.readLine()) != null) {
				if (line.length() != 0) {
					String[] subList = line.split("\\s+");
					if (subList[0].equals("p")){
						varNum = Integer.parseInt(subList[2]); 
					}
					else if (!subList[0].equals("c")){
						for (String i : subList){
							if (!i.equals("0")){
								clauseList.add(i);
							}
							else{
								list.add(new ArrayList<String>(clauseList));
								clauseList.clear();
							}
						}
					}
					
				}
			}

			br.close();
		} catch (Exception e) {
			System.out.println(e);
		}
//		System.out.println(list);
	Formula formula = convertMaptoFormula(list);
//	System.out.println(formula);
	Long start = System.currentTimeMillis();
	System.out.println("Result:"+SATSolver.solve(formula));
	Long end = System.currentTimeMillis();
	System.out.print("Time:"+(end-start)+"ms");

	}

	public static Formula convertMaptoFormula(ArrayList<ArrayList<String>> clausestrings) {
		Formula formula = new Formula();
		for (ArrayList<String> clausestring: clausestrings) {
//            System.out.print("From clause "+clausestring + " ");
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
			}
//            System.out.println("Add clause:" + newClause);
            formula = formula.addClause(newClause);
		}
//        System.out.println("-------------------------");
		return formula;
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
