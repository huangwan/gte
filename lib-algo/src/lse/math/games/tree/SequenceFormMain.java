package lse.math.games.tree;

//import org.flexunit.Assert;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import lse.math.games.Rational;
import lse.math.games.lrs.HPolygon;
import lse.math.games.lrs.LrsAlgorithm;
import lse.math.games.lrs.VPolygon;
import lse.math.games.lrs.vertex;
//import lse.math.games.lcp.LCP;
import lse.math.games.tree.SequenceForm.ImperfectRecallException;
import lse.math.games.tree.SequenceForm.InvalidPlayerException;

public class SequenceFormMain {
	
	
	public static void main(String[] args) throws ImperfectRecallException, InvalidPlayerException
	{
		ExtensiveForm tree;
		//tree = testExample();
		//tree = MyersonExample();
	    tree = simpleLargeExample();
		
		long startTime = System.currentTimeMillis();
	
		extensiveFormSolver(tree);

		long endTime = System.currentTimeMillis();
		
		long totalTime = endTime - startTime;
		System.out.println("Running Time: " + totalTime + " Millis.");

		System.exit(0);
		
	}
	
	public static void extensiveFormSolver(ExtensiveForm tree) throws ImperfectRecallException, InvalidPlayerException{
		
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
		
		List<vertex> resultPair = PairVertices(vtlist1, vtlist2, nonRedSeq);
		
		//printPairVertics(resultPair, nonRedSeq);
		
		
	}
	

	
	/***
	 * Example 1.1 @ Wan's PhD thesis 
	 */
	
	public static ExtensiveForm testExample(){
		

	ExtensiveForm tree2 = new ExtensiveForm();		
	Player p1 = tree2.createPlayer("I");
	Player p2 = tree2.createPlayer("II");
	
	Iset h11 = tree2.createIset(p1);
	Iset h12 = tree2.createIset(p1);
	Iset h13 = tree2.createIset(p1);
	Iset h2 = tree2.createIset(p2);
	
	tree2.addToIset(tree2.root(), h11);
	
	Node A = tree2.createNode();			
	tree2.addToIset(A, h2);
	A.reachedby = tree2.createMove();
	A.reachedby.setIset(h11);
	tree2.root().addChild(A);
	
	Node B = tree2.createNode();		
	tree2.addToIset(B, h2);
	B.reachedby = tree2.createMove();
	B.reachedby.setIset(h11);
	tree2.root().addChild(B);
	
	Node a1 = tree2.createNode();		
	a1.reachedby = tree2.createMove();
	a1.reachedby.setIset(h2);
	Outcome pay1 = tree2.createOutcome(a1);
	pay1.setPay(tree2.firstPlayer(), Rational.valueOf(0));
	pay1.setPay(tree2.firstPlayer().next, Rational.valueOf(8));
	A.addChild(a1);
	
	Node b1 = tree2.createNode();		
	tree2.addToIset(b1, h12);
	b1.reachedby = tree2.createMove();
	b1.reachedby.setIset(h2);
	A.addChild(b1);
	
	Node c1 = tree2.createNode();		
	tree2.addToIset(c1, h13);
	c1.reachedby = tree2.createMove();
	c1.reachedby.setIset(h2);
	A.addChild(c1);
	
	Node C = tree2.createNode();		
	C.reachedby = tree2.createMove();
	C.reachedby.setIset(h12);
	Outcome pay2 = tree2.createOutcome(C);
	pay2.setPay(tree2.firstPlayer(), Rational.valueOf(9));
	pay2.setPay(tree2.firstPlayer().next, Rational.valueOf(7));
	b1.addChild(C);
	
	Node D = tree2.createNode();		
	D.reachedby = tree2.createMove();
	D.reachedby.setIset(h12);
	Outcome pay3 = tree2.createOutcome(D);
	pay3.setPay(tree2.firstPlayer(), Rational.valueOf(5));
	pay3.setPay(tree2.firstPlayer().next, Rational.valueOf(3));
	b1.addChild(D);
	
	Node E = tree2.createNode();		
	E.reachedby = tree2.createMove();
	E.reachedby.setIset(h13);
	Outcome pay4 = tree2.createOutcome(E);
	pay4.setPay(tree2.firstPlayer(), Rational.valueOf(1));
	pay4.setPay(tree2.firstPlayer().next, Rational.valueOf(1));
	c1.addChild(E);
	
	Node F = tree2.createNode();		
	F.reachedby = tree2.createMove();
	F.reachedby.setIset(h13);
	Outcome pay5 = tree2.createOutcome(F);
	pay5.setPay(tree2.firstPlayer(), Rational.valueOf(9));
	pay5.setPay(tree2.firstPlayer().next, Rational.valueOf(4));
	c1.addChild(F);
	
	Node a2 = tree2.createNode();		
	a2.reachedby = a1.reachedby;		
	Outcome pay6 = tree2.createOutcome(a2);
	pay6.setPay(tree2.firstPlayer(), Rational.valueOf(7));
	pay6.setPay(tree2.firstPlayer().next, Rational.valueOf(7));
	B.addChild(a2);
	
	Node b2 = tree2.createNode();		
	b2.reachedby = b1.reachedby;		
	Outcome pay7 = tree2.createOutcome(b2);
	pay7.setPay(tree2.firstPlayer(), Rational.valueOf(3));
	pay7.setPay(tree2.firstPlayer().next, Rational.valueOf(2));
	B.addChild(b2);
	
	Node c2 = tree2.createNode();		
	c2.reachedby = c1.reachedby;		
	Outcome pay8 = tree2.createOutcome(c2);
	pay8.setPay(tree2.firstPlayer(), Rational.valueOf(5));
	pay8.setPay(tree2.firstPlayer().next, Rational.valueOf(4));
	B.addChild(c2);
	
	tree2.autoname();
	 
	return tree2;
	
	}
	
	/********************
	 * Myerson's example 1986 
	 */
	public static ExtensiveForm MyersonExample(){
	
	ExtensiveForm tree = new ExtensiveForm();		
	Player p1 = tree.createPlayer("I");
	Player p2 = tree.createPlayer("II");
	
	Iset h11 = tree.createIset(p1);
	Iset h12 = tree.createIset(p1);
	Iset h2 = tree.createIset(p2);
	
	tree.addToIset(tree.root(), h11);
	
	Node Out = tree.createNode();		
	Out.reachedby = tree.createMove();
	Out.reachedby.setIset(h11);
	Outcome pay1 = tree.createOutcome(Out);
	pay1.setPay(tree.firstPlayer(), Rational.valueOf(2));
	pay1.setPay(tree.firstPlayer().next, Rational.valueOf(2));
	tree.root().addChild(Out);
	
	Node In = tree.createNode();		
	tree.addToIset(In, h12);
	In.reachedby = tree.createMove();
	In.reachedby.setIset(h11);
	tree.root().addChild(In);
	
	Node L = tree.createNode();		
	tree.addToIset(L, h2);
	L.reachedby = tree.createMove();
	L.reachedby.setIset(h12);
	In.addChild(L);
	
	Node B = tree.createNode();		
	tree.addToIset(B, h2);
	B.reachedby = tree.createMove();
	B.reachedby.setIset(h12);
	In.addChild(B);
	
	// Outcomes	
	
	Node l1 = tree.createNode();		
	l1.reachedby = tree.createMove();
	l1.reachedby.setIset(h2);
	Outcome pay2 = tree.createOutcome(l1);
	pay2.setPay(tree.firstPlayer(), Rational.valueOf(5));
	pay2.setPay(tree.firstPlayer().next, Rational.valueOf(1));
	L.addChild(l1);
	
	Node l2 = tree.createNode();		
	l2.reachedby = l2.reachedby;		
	Outcome pay3 = tree.createOutcome(l2);
	pay3.setPay(tree.firstPlayer(), Rational.valueOf(0));
	pay3.setPay(tree.firstPlayer().next, Rational.valueOf(0));
	B.addChild(l2);
	
	Node r1 = tree.createNode();		
	r1.reachedby = tree.createMove();
	r1.reachedby.setIset(h2);
	Outcome pay4 = tree.createOutcome(r1);
	pay4.setPay(tree.firstPlayer(), Rational.valueOf(0));
	pay4.setPay(tree.firstPlayer().next, Rational.valueOf(0));
	L.addChild(r1);
	
	Node r2 = tree.createNode();		
	r2.reachedby = r1.reachedby;		
	Outcome pay5 = tree.createOutcome(r2);
	pay5.setPay(tree.firstPlayer(), Rational.valueOf(1));
	pay5.setPay(tree.firstPlayer().next, Rational.valueOf(5));
	B.addChild(r2);
	
	tree.autoname();
	
	return tree;
	
}
	
	public static ExtensiveForm simpleLargeExample(){
		
		ExtensiveForm tree = new ExtensiveForm();		
		Player p1 = tree.createPlayer("I");
		Player p2 = tree.createPlayer("II");
		
		Iset h1 = tree.createIset(p1);
		Iset h21 = tree.createIset(p2);
		Iset h22 = tree.createIset(p2);
		Iset h23 = tree.createIset(p2);
		
		tree.addToIset(tree.root(), h1);
		
		Node T = tree.createNode();		
		tree.addToIset(T, h21);
		T.reachedby = tree.createMove();
		T.reachedby.setIset(h1);
		tree.root().addChild(T);
		
		Node B = tree.createNode();		
		tree.addToIset(B, h22);
		B.reachedby = tree.createMove();
		B.reachedby.setIset(h1);
		tree.root().addChild(B);

		Node A = tree.createNode();		
		tree.addToIset(A, h23);
		A.reachedby = tree.createMove();
		A.reachedby.setIset(h1);
		tree.root().addChild(A);
		
		// Outcomes	
		Node a = tree.createNode();		
		a.reachedby = tree.createMove();
		a.reachedby.setIset(h21);
		Outcome pay1 = tree.createOutcome(a);
		pay1.setPay(tree.firstPlayer(), Rational.valueOf(3));
		pay1.setPay(tree.firstPlayer().next, Rational.valueOf(-6));
		T.addChild(a);
		
		Node b = tree.createNode();		
		b.reachedby = tree.createMove();
		b.reachedby.setIset(h21);		
		Outcome pay2 = tree.createOutcome(b);
		pay2.setPay(tree.firstPlayer(), Rational.valueOf(-7));
		pay2.setPay(tree.firstPlayer().next, Rational.valueOf(-4));
		T.addChild(b);
		
		Node c = tree.createNode();		
		c.reachedby = tree.createMove();
		c.reachedby.setIset(h21);		
		Outcome pay3 = tree.createOutcome(c);
		pay3.setPay(tree.firstPlayer(), Rational.valueOf(4));
		pay3.setPay(tree.firstPlayer().next, Rational.valueOf(8));
		T.addChild(c);
		
		Node d = tree.createNode();		
		d.reachedby = tree.createMove();
		d.reachedby.setIset(h21);	
		Outcome pay4 = tree.createOutcome(d);
		pay4.setPay(tree.firstPlayer(), Rational.valueOf(0));
		pay4.setPay(tree.firstPlayer().next, Rational.valueOf(-6));
		T.addChild(d);
		
		
		Node e = tree.createNode();		
		e.reachedby = tree.createMove();
		e.reachedby.setIset(h22);
		Outcome pay5 = tree.createOutcome(e);
		pay5.setPay(tree.firstPlayer(), Rational.valueOf(4));
		pay5.setPay(tree.firstPlayer().next, Rational.valueOf(4));
		B.addChild(e);
		
		Node f = tree.createNode();		
		//f.reachedby = e.reachedby;
		f.reachedby = tree.createMove();
		f.reachedby.setIset(h22);
		Outcome pay6 = tree.createOutcome(f);
		pay6.setPay(tree.firstPlayer(), Rational.valueOf(1));
		pay6.setPay(tree.firstPlayer().next, Rational.valueOf(0));
		B.addChild(f);
		
		
		Node g = tree.createNode();		
		g.reachedby = tree.createMove();
		g.reachedby.setIset(h22);	
		Outcome pay7 = tree.createOutcome(g);
		pay7.setPay(tree.firstPlayer(), Rational.valueOf(8));
		pay7.setPay(tree.firstPlayer().next, Rational.valueOf(-2));
		B.addChild(g);
		
		Node h = tree.createNode();		
		h.reachedby = tree.createMove();
		h.reachedby.setIset(h22);		
		Outcome pay8 = tree.createOutcome(h);
		pay8.setPay(tree.firstPlayer(), Rational.valueOf(5));
		pay8.setPay(tree.firstPlayer().next, Rational.valueOf(4));
		B.addChild(h);
		
		Node i = tree.createNode();		
		i.reachedby = tree.createMove();
		i.reachedby.setIset(h23);
		Outcome pay9 = tree.createOutcome(i);
		pay9.setPay(tree.firstPlayer(), Rational.valueOf(5));
		pay9.setPay(tree.firstPlayer().next, Rational.valueOf(-8));
		A.addChild(i);
		
		Node j = tree.createNode();		
		j.reachedby = tree.createMove();
		j.reachedby.setIset(h23);		
		Outcome pay10 = tree.createOutcome(j);
		pay10.setPay(tree.firstPlayer(), Rational.valueOf(2));
		pay10.setPay(tree.firstPlayer().next, Rational.valueOf(-6));
		A.addChild(j);
		
		Node k = tree.createNode();		
		k.reachedby = tree.createMove();
		k.reachedby.setIset(h23);		
		Outcome pay11 = tree.createOutcome(k);
		pay11.setPay(tree.firstPlayer(), Rational.valueOf(-4));
		pay11.setPay(tree.firstPlayer().next, Rational.valueOf(0));
		A.addChild(k);
		
		Node l = tree.createNode();		
		l.reachedby = tree.createMove();
		l.reachedby.setIset(h23);		
		Outcome pay12 = tree.createOutcome(l);
		pay12.setPay(tree.firstPlayer(), Rational.valueOf(4));
		pay12.setPay(tree.firstPlayer().next, Rational.valueOf(0));
		A.addChild(l);
				
		tree.autoname();
		
		return tree;
		
	}

	
	
	
	public static List<vertex> PairVertices(List<vertex> vtlist1, List<vertex> vtlist2, NonRedundantSequenceForm seq){
		int n = 0;
		int n1 = 0;
		int n2 = 0;
		List<vertex> result = new ArrayList<vertex>();
		Iterator<vertex> it1 = vtlist1.iterator();
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
					result.add(v1);
					result.add(v2);
					formatPrint(v1, v2, seq, n, n1, n2);
				}
			}
		}
		return result;
	}
	
	public static void formatPrint(vertex v1, vertex v2, NonRedundantSequenceForm seq, int n, int n1, int n2){
		Rational[] v1ext = seq.extendVertex(v1, 1);
		Rational[] v2ext = seq.extendVertex(v2,2);
		Rational[] EquPay = seq.c(v1.v, v2.v);
		Rational payoff1 = EquPay[0];
		Rational payoff2 = EquPay[1]; 
		
		System.out.print(String.format("EE %d P1:(%d) ", n, n1));
		for (int i = 0; i < v1ext.length; ++i)
			System.out.print(v1ext[i].toString() + " ");
		System.out.print("EP=" + payoff1.toString());
		System.out.print(String.format(" P2=(%d) ", n2));
		for (int i = 0; i < v2ext.length; ++i)
			System.out.print(v2ext[i].toString() + " ");
		System.out.print("EP=" + payoff2.toString());
			
		System.out.println();
	}
	    
	public static void printPairVertics(List<vertex> vtlist, NonRedundantSequenceForm seq){
		int n = 1;
		int n1 = 1;
		int n2 = 1;
		Iterator<vertex> it = vtlist.iterator();
		if (!it.hasNext()){
			System.out.println("No pair found.");
			return;
		}
		vertex v1 = null;
		vertex v2 = null;
		Rational[] v1ext = null;
		Rational[] v2ext = null;
		Rational[] EquPay = null;
		Rational payoff1, payoff2; 
		while(it.hasNext()){
			v1 = it.next();
			v1ext = seq.extendVertex(v1, 1);
			if (!it.hasNext()){
				System.out.println("Error: Odd number of of vertices.");
				return;
			}
			v2 = it.next();
			v2ext = seq.extendVertex(v2,2);
			EquPay = seq.c(v1.v,v2.v);
			payoff1 = EquPay[0];
			payoff2 = EquPay[1];
			System.out.print(String.format("EE %d P1:(%d) ", n++, n1++));
		   // printOnePair(v1.v,v2.v, false);
			for (int i = 0; i < v1ext.length; ++i)
				System.out.print(v1ext[i].toString() + " ");
			System.out.print("EP=" + payoff1.toString());
			System.out.print(String.format(" P2=(%d) ", n2++));
			for (int i = 0; i < v2ext.length; ++i)
			    System.out.print(v2ext[i].toString() + " ");
			System.out.print("EP=" + payoff2.toString());
			
			//System.out.println("The original vertices before removing redundancy ===>");
			//printOnePair(seq.extendVertex(v1, 1), seq.extendVertex(v2, 2), true);
			System.out.println();
		}
	}
	
	public static void printOnePair(Rational[] v1, Rational[] v2, boolean extendFlag){
		if (v1 == null || v2 == null){
			System.out.println("No vertex to output.");
			return;
		}
		StringBuilder str = new StringBuilder();
		str.append(extendFlag ? "   x = [" : "    (v x_I) = [");
		for (int i = 0; i < v1.length - 1; ++i)
			str.append(v1[i].toString() + " ");
		str.append(v1[v1.length - 1].toString() + "];    ");	
		str.append(extendFlag ? "y = [" : "(u y_I) = [");
		for (int i = 0; i < v2.length - 1; ++i)
			str.append(v2[i].toString() + " ");
		str.append(v2[v2.length - 1].toString() + "];");
		System.out.println(str.toString());
		
	}
	
}
