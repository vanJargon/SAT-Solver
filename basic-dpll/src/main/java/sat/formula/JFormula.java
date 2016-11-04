package sat.formula;

import java.util.ArrayList;

import immutable.EmptyImList;
import immutable.ImList;
import sat.Junction;

/**
 * Created by Kygrykhon on 11/3/2016.
 */

public class JFormula{
    private ArrayList<Junction> junctions;

    public ArrayList<Junction> getJunctions() {return junctions;}

    public void addJunction(Junction junc) {junctions.add(junc);}

    public JFormula(Formula formula){
        junctions = new ArrayList<>();
        for(Clause c: formula.getClauses()){
            junctions.add(new Junction(c));
        }
    }

    public JFormula(ArrayList<Junction> j){
        junctions = j;
    }

    public Junction getSmallesJunction() {
        return filterJunctionBySize(junctions);
    }

    public int getSize() {return junctions.size();}

    private Junction filterJunctionBySize(ArrayList<Junction> juncts) { //returns smallest clause
        if(juncts.size() == 0 || juncts==null ) {
            return null;
        } else if(juncts.size() == 1) {
            return juncts.get(0);
        } else if(juncts.size() == 2) {
//            System.out.println("Clause Size 2:"+clauses);
            if(juncts.get(0).size() < juncts.get(1).size()){
                return juncts.get(0);
            } else {
                return juncts.get(1);
            }
        } else {
            ArrayList<Junction> firstSeg = new ArrayList<>();
            ArrayList<Junction> secondSeg = new ArrayList<>();
            int counter = 0;
            int limit = juncts.size() /2;

            for(Junction c: juncts) {
                if(counter<limit) {
                    firstSeg.add(c);
                } else {
                    secondSeg.add(c);
                }
                counter++;
            }

            Junction firstJunc = filterJunctionBySize(firstSeg);
            Junction secondJunc = filterJunctionBySize(secondSeg);

            if(firstJunc==null){
                return secondJunc;
            } else if (secondJunc==null) {
                return firstJunc;
            } else if (firstJunc.size()==0 || secondJunc.size()==0) {
                return new Junction(new Clause());
            } else if (firstJunc.size() <= secondJunc.size()) {
                return firstJunc;
            } else {
                return secondJunc;
            }

        }
    }
}
