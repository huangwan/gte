package lse.math.games.tree;

//import org.flexunit.Assert;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import lse.math.games.Rational;
import lse.math.games.lrs.HPolygon;
import lse.math.games.lrs.Lrs;
import lse.math.games.lrs.LrsAlgorithm;
import lse.math.games.lrs.VPolygon;
import lse.math.games.lrs.vertex;
import lse.math.games.matrix.BimatrixSolver;
import lse.math.games.matrix.Equilibria;
import lse.math.games.matrix.Equilibrium;
//import lse.math.games.lcp.LCP;
import lse.math.games.tree.SequenceForm.ImperfectRecallException;
import lse.math.games.tree.SequenceForm.InvalidPlayerException;

public class testMain {
	
	
	public static void main(String[] args) throws ImperfectRecallException, InvalidPlayerException
	{
		ExtensiveForm tree;
		//tree = testExample();
		//tree = MyersonExample();
	    tree = simpleLargeExample();
		
		long startTime = System.currentTimeMillis();
	
		SequenceFormSolver seqSolver = new SequenceFormSolver();
		Equilibria equ = seqSolver.extensiveFormSolver(tree);
		seqSolver.formatPrint(equ, System.out);

		long endTime = System.currentTimeMillis();
		
		long totalTime = endTime - startTime;
		System.out.println("Running Time: " + totalTime + " Millis.");
		
		Rational[][] A = simpleLargeStrategicA();
		Rational[][] B = simpleLargeStrategicB();
		BimatrixSolver Strategic = new BimatrixSolver();
		Lrs lrs = new LrsAlgorithm();
		Equilibria strategicEqu = Strategic.findAllEq(lrs, A, B);
		
		

		System.exit(0);
		
	}
	
	public boolean match(Equilibria strat, Equilibria seq){
		boolean flag = true;
		Iterator<Equilibrium> it = strat.iterator();
		while (it.hasNext()){
			Equilibrium stratEqu = it.next();
			Iterator<Equilibrium> itSeq = seq.iterator();
			List<Rational[]> SeqResponse = new ArrayList<Rational[]>(); //best responses to I's strategy in the equilibrium
			while(itSeq.hasNext()){
				Equilibrium seqEqu = itSeq.next();
				if (seqEqu.getVertex1() == stratEqu.getVertex1())
					SeqResponse.add(seqEqu.probVec2);
			}
			if (SeqResponse.size() == 0) return false;
			
				
		}
		return flag;
	}
	
	private Equilibrium tempConvert(Equilibrium strat){
		Equilibrium seq = new Equilibrium(strat.getVertex1(),strat.getVertex2());
		seq.probVec1 = new Rational[3];
		seq.probVec2 = new Rational[9];
		
		seq.probVec1[0] = Rational.valueOf("1");
		seq.probVec1[1] = strat.probVec1[0];
		seq.probVec1[2] = strat.probVec1[1];
		
		seq.probVec2[0] = Rational.valueOf("1");
		for (int i = 1; i < 9; ++i)
		    seq.probVec2[i] = Rational.valueOf("0");
		
		for (int j = 1; j < 5; ++j)
		    for (int i = 0; i < 4; ++i)
			    seq.probVec2[j] = seq.probVec2[j].add(strat.probVec2[4 * (j - 1) + i]);
		
		for (int j = 5; j < 9; ++j)
			for (int i = 0; i < 4; ++i)
				seq.probVec2[j] = seq.probVec2[j].add(strat.probVec2[4 * i + (j - 5)]);
		
		return seq;
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
/*
		Node A = tree.createNode();		
		tree.addToIset(A, h23);
		A.reachedby = tree.createMove();
		A.reachedby.setIset(h1);
		tree.root().addChild(A);
		*/
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
		/*
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
			*/	
		tree.autoname();
		
		return tree;
		
	}

	public static Rational[][] simpleLargeStrategicA(){
		//Rational[][] A = new Rational[3][64];
		Rational[][] A = new Rational[2][16];
		String[] strategy2 = {"aei", "aej", "aek", "ael", "afi", "afj", "afk", "afl", "agi", "agj", "agk", "agl", "ahi", "ahj", "ahk", "ahl",
				              "bei", "bej", "bek", "bel", "bfi", "bfj", "bfk", "bfl", "bgi", "bgj", "bgk", "bgl", "bhi", "bhj", "bhk", "bhl",
				              "cei", "cej", "cek", "cel", "cfi", "cfj", "cfk", "cfl", "cgi", "cgj", "cgk", "cgl", "chi", "chj", "chk", "chl"
				              //"dei", "dej", "dek", "del", "dfi", "dfj", "dfk", "dfl", "dgi", "dgj", "dgk", "dgl", "dhi", "dhj", "dhk", "dhl"
		};
		
		for (int j = 0; j < A[0].length; ++j){
				if (strategy2[j].contains("a")) A[0][j] = Rational.valueOf("3");
				if (strategy2[j].contains("b")) A[0][j] = Rational.valueOf("7");
				if (strategy2[j].contains("c")) A[0][j] = Rational.valueOf("4");
				if (strategy2[j].contains("d")) A[0][j] = Rational.valueOf("0");
		}
		for (int j = 0; j < A[0].length; ++j){
			if (strategy2[j].contains("e")) A[1][j] = Rational.valueOf("4");
			if (strategy2[j].contains("f")) A[1][j] = Rational.valueOf("1");
			if (strategy2[j].contains("g")) A[1][j] = Rational.valueOf("8");
			if (strategy2[j].contains("h")) A[1][j] = Rational.valueOf("5");
	    }	
		/*
		for (int j = 0; j < A[0].length; ++j){
			if (strategy2[j].contains("i")) A[2][j] = Rational.valueOf("5");
			if (strategy2[j].contains("j")) A[2][j] = Rational.valueOf("2");
			if (strategy2[j].contains("k")) A[2][j] = Rational.valueOf("-4");
			if (strategy2[j].contains("l")) A[2][j] = Rational.valueOf("4");
	    }
	   	*/
		return A;
	}
	
	public static Rational[][] simpleLargeStrategicB(){
		//Rational[][] A = new Rational[3][64];
		Rational[][] A = new Rational[2][16];
		String[] strategy2 = {"aei", "aej", "aek", "ael", "afi", "afj", "afk", "afl", "agi", "agj", "agk", "agl", "ahi", "ahj", "ahk", "ahl",
				              "bei", "bej", "bek", "bel", "bfi", "bfj", "bfk", "bfl", "bgi", "bgj", "bgk", "bgl", "bhi", "bhj", "bhk", "bhl",
				              "cei", "cej", "cek", "cel", "cfi", "cfj", "cfk", "cfl", "cgi", "cgj", "cgk", "cgl", "chi", "chj", "chk", "chl"
				            //  "dei", "dej", "dek", "del", "dfi", "dfj", "dfk", "dfl", "dgi", "dgj", "dgk", "dgl", "dhi", "dhj", "dhk", "dhl"
		};
		
		for (int j = 0; j < A[0].length; ++j){
				if (strategy2[j].contains("a")) A[0][j] = Rational.valueOf("-6");
				if (strategy2[j].contains("b")) A[0][j] = Rational.valueOf("-4");
				if (strategy2[j].contains("c")) A[0][j] = Rational.valueOf("8");
				if (strategy2[j].contains("d")) A[0][j] = Rational.valueOf("-6");
		}
		for (int j = 0; j < A[0].length; ++j){
			if (strategy2[j].contains("e")) A[1][j] = Rational.valueOf("4");
			if (strategy2[j].contains("f")) A[1][j] = Rational.valueOf("0");
			if (strategy2[j].contains("g")) A[1][j] = Rational.valueOf("-2");
			if (strategy2[j].contains("h")) A[1][j] = Rational.valueOf("4");
	    }
		/*
		for (int j = 0; j < A[0].length; ++j){
			if (strategy2[j].contains("i")) A[2][j] = Rational.valueOf("-8");
			if (strategy2[j].contains("j")) A[2][j] = Rational.valueOf("-6");
			if (strategy2[j].contains("k")) A[2][j] = Rational.valueOf("0");
			if (strategy2[j].contains("l")) A[2][j] = Rational.valueOf("0");
	    }
	   	*/
		return A;
	}
	
	
}
