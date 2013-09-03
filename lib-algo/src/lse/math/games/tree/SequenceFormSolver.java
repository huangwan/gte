package lse.math.games.tree;

//import org.flexunit.Assert;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import lse.math.games.Rational;
import lse.math.games.lrs.HPolygon;
import lse.math.games.lrs.LrsAlgorithm;
import lse.math.games.lrs.VPolygon;
import lse.math.games.lrs.vertex;
import lse.math.games.matrix.BimatrixSolver;
import lse.math.games.matrix.Equilibria;
import lse.math.games.matrix.Equilibrium;
//import lse.math.games.lcp.LCP;
import lse.math.games.tree.SequenceForm.ImperfectRecallException;
import lse.math.games.tree.SequenceForm.InvalidPlayerException;

public class SequenceFormSolver {
	
	private static final Logger log = Logger.getLogger(BimatrixSolver.class.getName());
	
	public Equilibria extensiveFormSolver(ExtensiveForm tree) 
			throws ImperfectRecallException, InvalidPlayerException{
		
		SequenceForm seq = new SequenceForm(tree);
		NonRedundantSequenceForm nonRedSeq = new NonRedundantSequenceForm(seq);
		
		HPolygon D1 = new HPolygon(nonRedSeq.getD1(), true);
		HPolygon D2 = new HPolygon(nonRedSeq.getD2(), true);
		
		List<vertex> vtlist1 = new ArrayList<vertex>();
		List<vertex> vtlist2 = new ArrayList<vertex>();
		
		LrsAlgorithm lrs = new LrsAlgorithm(); /* structure for holding static problem data            */
		
		Rational[] vertexInput = null;
		Integer[] labelInput = null;				
		
		System.out.println("Lrs on first polyhedra ====> ");
		VPolygon lrsout1 = lrs.run(D1);
		List<Rational[]> p1_vertex_array = lrsout1.vertices; //ToDo: what about rays?? Scaling?? 
		List<Integer[]> p1_vertex_labels = lrsout1.cobasis;
		List<Integer> ray1 = lrsout1.ray;
		Iterator<Rational[]> itVertex = p1_vertex_array.iterator();
		Iterator<Integer[]> itLabel = p1_vertex_labels.iterator();
		Iterator<Integer> itRay = ray1.iterator();
		while(itLabel.hasNext()){
			vertexInput = itVertex.next();
			labelInput = itLabel.next();
			if (itRay.next() == 0)  vtlist1.add(new vertex(vertexInput, labelInput, D1.get_m()));
		}
		System.out.println();
		
		System.out.println("Lrs on second polyhedra ====>");
		VPolygon lrsout2 = lrs.run(D2);	
		List<Rational[]> p2_vertex_array = lrsout2.vertices;
		List<Integer[]> p2_vertex_labels = lrsout2.cobasis;
		List<Integer> ray2 = lrsout2.ray;
		itVertex = p2_vertex_array.iterator();
		itLabel = p2_vertex_labels.iterator();
		itRay = ray2.iterator();
		while(itLabel.hasNext()){
			vertexInput = itVertex.next();
			labelInput = itLabel.next();
			if (itRay.next() == 0) vtlist2.add(new vertex(vertexInput, labelInput, D2.get_m()));
		}
		System.out.println();
		
		Iterator<vertex> it = vtlist2.iterator();
		while(it.hasNext())
			it.next().LabelMappingD2(nonRedSeq);
		
		return PairVertices(vtlist1, vtlist2, nonRedSeq);
		
	}
	
	public Equilibria PairVertices(List<vertex> vtlist1, List<vertex> vtlist2, NonRedundantSequenceForm seq){
		int n = 0;
		int n1 = 0;
		int n2 = 0;
		Iterator<vertex> it1 = vtlist1.iterator();
		Equilibria result = new Equilibria();
		while(it1.hasNext()){
			vertex v1 = it1.next();
			n1++;
			Iterator<vertex> it2 = vtlist2.iterator();
			n2 = 0;
			while(it2.hasNext()){
				vertex v2 = it2.next();
				n2++;
				if (v1.isPair(v2)){
					n++;
					
                    Equilibrium eq = new Equilibrium(n1, n2);					
					
                    Rational[] EquPay = seq.c(v1.v, v2.v);
            		eq.probVec1 = seq.extendVertex(v1, 1);
					eq.payoff1 = EquPay[0];
										
					eq.probVec2 = seq.extendVertex(v2,2);
					eq.payoff2 = EquPay[1];
					
					result.add(eq);
				}
			}
		}
		return result;
	}
	
	public void formatPrint(Equilibria equ, PrintStream out){
		Iterator<Equilibrium> it = equ.iterator();
		Equilibrium curr;
		Rational[] v1ext, v2ext;
		Rational payoff1, payoff2;
		int n, n1, n2;
		n = 1;
		
		while (it.hasNext()){
			curr = it.next();
			v1ext = curr.probVec1;
		    v2ext = curr.probVec2;
		    payoff1 = curr.payoff1;
		    payoff2 = curr.payoff2;
		    n1 = curr.getVertex1();
		    n2 = curr.getVertex2();
		
		    out.print(String.format("EE %d P1:(%d) ", n++, n1));
		    for (int i = 0; i < v1ext.length; ++i)
			out.print(v1ext[i].toString() + " ");
		    out.print("EP=" + payoff1.toString());
		    out.print(String.format(" P2=(%d) ", n2));
		    for (int i = 0; i < v2ext.length; ++i)
 			out.print(v2ext[i].toString() + " ");
    		out.print("EP=" + payoff2.toString());
	    	out.println();
		}
	}
}
