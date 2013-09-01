package lse.math.games.lrs;

import static lse.math.games.BigIntegerUtils.zero;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

import lse.math.games.Rational;
import lse.math.games.tree.NonRedundantSequenceForm;

public class vertex {
	/*
	 *  Label(min) = 1 + d;
	 *  Label(max) = 2 * d + m;
	 *  Full Label <==> Sum(Label) = (d + m)(3 * d + m + 1)/2 
	 */
	
	private int d; // dimension = |x_I|
	//private int m; // number of inequalities (not including non-negative inequalities)
	public Rational[] v; // result = [x_I]
	public List<Integer> label; // label of D1; use LabelMappingD2 to get label of D2
	
	public vertex(Rational[] _v, Integer[] _label, int _m){
		v = new Rational[_v.length - 1];
		label = new ArrayList<Integer>();
		for (int i = 0; i < v.length; ++i)
			v[i] = _v[i + 1];
		for (int i = 0; i < _label.length; ++i)
			label.add(_label[i]);
		Collections.sort(label);
		
		d = _v.length - 1;
		//m = _m;
	}
	
	public vertex(Dictionary P){
		this.d = P.d; 
		//this.m = P.m - P.d;
		this.v = new Rational[d]; 
		this.label = new ArrayList<Integer>();
		
		/* initialize */
		for (int i = 0; i < d; ++i)
			v[i] = Rational.ZERO;
	}
	
	public void LabelMappingD2(NonRedundantSequenceForm seq){
		ListIterator<Integer> it = label.listIterator();
		int diff1 = seq.p2.length + seq.I2.length;
		int diff2 = seq.q2.length + seq.I1.length;
		
		while (it.hasNext()){
			Integer value = it.next();
            if  (value <= diff2)
            	it.set(value + diff1);
            else 
            	it.set(value - diff2);
		}
		Collections.sort(label);
	}
	
	public boolean isPair(vertex vt){ //labels of vertices are sorted in ascendent order
	//	if (!(vt.d == this.m) || !(vt.m == this.d))
	//		return false;
	    Integer[] labelarr1 = new Integer[this.label.size()];
	    Integer[] labelarr2 = new Integer[vt.label.size()];
	    this.label.toArray(labelarr1);
	    vt.label.toArray(labelarr2);
	    int p1 = 0; //pointer to the index of label of this vertex
	    int p2 = 0; // pointer to the index of label of vt
	    int benchlabel = 1;
	    while (p1 < labelarr1.length || p2 < labelarr2.length){
	    	if (p1 == labelarr1.length){
	    		if (!(labelarr2[p2] == benchlabel))  return false;
	    		++p2; 
	    		++benchlabel;
	    	}
	    	else if (p2 == labelarr2.length){
	    		if (!(labelarr1[p1] == benchlabel)) return false;
	    		++p1;
	    		++benchlabel;
	    	}
	    	else if(labelarr1[p1] < labelarr2[p2]){
	    		if (!(labelarr1[p1] == benchlabel)) return false;
	    		++p1;
	    		++benchlabel;
	    	}
	    	else if (labelarr2[p2] < labelarr1[p1]){
	    		if (!(labelarr2[p2] == benchlabel)) return false;
	    		++p2;
	    		++benchlabel;
	    	}
	    	else{
	    		if (!(labelarr2[p2] == benchlabel)) return false;
	    		++p1;
	    		++p2;
	    		++benchlabel;
	    	}	
	    }
	    if (benchlabel <= d + vt.d) return false;
	    return true;
	}
	
	public void setVertexLabel(LrsAlgorithm lrs, Dictionary P){
		int i;
		for (int j = 1; j < P.A.length; ++j)
			if (zero(P.A[j][0])) label.add(lrs.inequality[j]);
		for (i = 0; i < P.d; ++i)
			label.add(lrs.inequality[P.C[i] - P.lastdv]);
		Collections.sort(label);		
		
	}
	
	public void setVertex(BigInteger[] output){
		for (int i = 0; i < v.length; ++i)
			v[i] = new Rational(output[i + 1], output[0]);	
	}

	public int getDim(){
		return this.d;
	}
}