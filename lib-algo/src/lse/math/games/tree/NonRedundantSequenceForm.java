package lse.math.games.tree;

import lse.math.games.Rational;
import lse.math.games.lcp.LCP;
import lse.math.games.lrs.vertex;
import lse.math.games.tree.SequenceForm.InvalidPlayerException;

public class NonRedundantSequenceForm{
	
    /** 
     * original constraints and payoff matrices 
     */
	private Integer[][] constraints1;
    private Integer[][] constraints2;
    private Rational[][] pay1;
    private Rational[][] pay2;
    
    /**
     * constraints and payoff matrices after removing redundancy
     */
    private int n1, m1, s1, n2, m2, s2;
    private int[] N1, D1;
	public int[] I1;
	private int[] N2;
	private int[] D2;
	public int[] I2;
    private Integer[][] P1, P2, Q1, Q2;
    private Integer[] p1;
	public Integer[] p2;
	private Integer[] q1;
	public Integer[] q2;
    private Rational[][] _A, _B;
    private Rational[][] _a, _b; 
    
   	private Rational[][] P, Q, p, q;
   	
    public NonRedundantSequenceForm(SequenceForm game)
    	    throws InvalidPlayerException
        {    	
    	if (game.getFirstPlayer() == null || game.getFirstPlayer().next == null || game.getFirstPlayer().next.next != null) {
    		throw new InvalidPlayerException("Sequence Form must have two and only two players");
    	}
        	this.constraints1 = game.getConstraints1();
        	this.constraints2 = game.getConstraints2();
        	this.pay1 = game.getPay1();
        	this.pay2 = game.getPay2();
        	
	    	s1 = CountNonterminalSequence(constraints1);
	       	m1 = constraints1.length;
	    	n1 = constraints1[0].length;
	    	I1 = new int[n1-m1];	
	    	N1 = new int[s1];
	    	D1 = new int[m1-s1];
	    	p1 = new Integer[s1];
	    	p2 = new Integer[m1-s1];
	    	P1 = new Integer[s1][n1-m1];
	    	P2 = new Integer[m1-s1][n1-m1];
	    
	    	s2 = CountNonterminalSequence(constraints2);
	       	m2 = constraints2.length;
	    	n2 = constraints2[0].length;
	    	I2 = new int[n2-m2];	
	    	N2 = new int[s2];
	    	D2 = new int[m2-s2];
	    	q1 = new Integer[s2];
	    	q2 = new Integer[m2-s2];
	    	Q1 = new Integer[s2][n2-m2];
	    	Q2 = new Integer[m2-s2][n2-m2];

	    	IndependentSequence(constraints1, s1, P1, P2, p1, p2, N1, D1, I1, 1);
	    	IndependentSequence(constraints2, s2, Q1, Q2, q1, q2, N2, D2, I2, 2);
	    	
   	    	P = new Rational[n1][n1-m1];
	    	Q = new Rational[n2][n2-m2];
	    	p = new Rational[n1][1];
	    	q = new Rational[n2][1];
	    	    	
	    	for (int i = 0; i < s1; i++)
	    	{
	    		for (int j = 0; j < n1-m1; j++)
	    		    P[i][j] = Rational.valueOf(Integer.valueOf(P1[i][j]));
	    		p[i][0] = Rational.valueOf(Integer.valueOf(p1[i]));
	    	}
	    	for (int i = s1; i < m1; i++)
	    	{
	    		for (int j = 0; j < n1-m1; j++)
	    		    P[i][j] = Rational.valueOf(Integer.valueOf(P2[i-s1][j]));
	    		p[i][0] = Rational.valueOf(Integer.valueOf(p2[i-s1]));
	    	}
	    	for (int i = m1; i < n1; i++)
	    	{
	    		for(int j = 0; j < n1-m1; j++)
	    			if (i == j+m1)    P[i][j] = Rational.ONE; 
	    			else P[i][j] = Rational.ZERO;
	    		p[i][0] = Rational.ZERO;
	    	}
	    	
	    	for (int i = 0; i < s2; i++)
	    	{
	    		for (int j = 0; j < n2-m2; j++)
	    		    Q[i][j] = Rational.valueOf(Integer.valueOf(Q1[i][j]));
	    		q[i][0] = Rational.valueOf(Integer.valueOf(q1[i]));
	    	}
	    	for (int i = s2; i < m2; i++)
	    	{
	    		for (int j = 0; j < n2-m2; j++)
	    		    Q[i][j] = Rational.valueOf(Integer.valueOf(Q2[i-s2][j]));
	    		q[i][0] = Rational.valueOf(Integer.valueOf(q2[i-s2]));
	    	}
	    	for (int i = m2; i < n2; i++)
	    	{
	    		for(int j = 0; j < n2-m2; j++)
	    			if (i == j+m2)    Q[i][j] = Rational.ONE; 
	    			else Q[i][j] = Rational.ZERO;
	    		q[i][0] = Rational.ZERO;
	    	}
	    	
	    	_a = MatrixMultiply(MatrixTranspose(P), pay1);
	    	_a = MatrixMultiply(_a, q);
	    	_b = MatrixMultiply(MatrixTranspose(p), pay2);
	    	_b = MatrixMultiply(_b, Q);
	    	_A = MatrixMultiply(MatrixTranspose(P), pay1);
	    	_A = MatrixMultiply(_A, Q);
	    	_B = MatrixMultiply(MatrixTranspose(P), pay2);
	    	_B = MatrixMultiply(_B, Q);
	
        }
    
    public Rational[] c(Rational[] v1, Rational[] v2){
    	Rational[] payoffc = new Rational[2];
    	Rational[][] x_I = new Rational[I1.length][1];
    	Rational[][] y_I = new Rational[I2.length][1];
    	for (int i = 0; i < I1.length; ++i)
    		x_I[I1.length - 1 - i][0] = v1[v1.length -1 -i];
    	for (int i = 0; i < I2.length; ++i)
    		y_I[I2.length - 1 - i][0] = v2[v2.length -1 - i];
    	
    	Rational[][] tmp1 = MatrixMultiply(MatrixTranspose(p), pay1);
    	Rational[][] tmp2 = MatrixAddition(q, MatrixMultiply(Q, y_I));
    	Rational[][] tmpA = MatrixAddition(_a, MatrixMultiply(_A, y_I));
    	Rational[][] tmpX = MatrixMultiply(MatrixTranspose(x_I), tmpA);
    	Rational[][] c1 = MatrixMultiply(tmp1, tmp2);
    	payoffc[0] = c1[0][0].add(tmpX[0][0]);
   
    	Rational[][] tmp3 = MatrixAddition(p, MatrixMultiply(P, x_I));
    	Rational[][] tmp4 = MatrixMultiply(pay2, q);
    	Rational[][] tmpB = MatrixAddition(_b, MatrixMultiply(MatrixTranspose(x_I), _B));
    	Rational[][] tmpY = MatrixMultiply(tmpB, y_I);
    	Rational[][] c2 = MatrixMultiply(MatrixTranspose(tmp3), tmp4);
    	payoffc[1] = c2[0][0].add(tmpY[0][0]);
    	
    	return payoffc;
    }
    
    private int CountNonterminalSequence(Integer[][] constraints)
    {
    	int s = 0;
       	for (int j = 0; j < constraints[0].length; j++)
    	{
    		for (int i =0; i < constraints.length; i++)
    		{
    			if (Integer.valueOf(constraints[i][j]) == -1)
    			{
    				s++;
    				break;
    			}
    		}
    	}
       	return s;
    }
    
    
    private void IndependentSequence(Integer[][] constraints, int s, Integer[][] P1, Integer[][] P2, Integer[] p1, Integer[] p2, int[] N, int[] D, int[] I, int player)
    {
    	int m = constraints.length;
    	int n = constraints[0].length;
    	Integer[][] constraints1 = new Integer[m][n];
    	Integer[] e = new Integer[m];
    	
    	e[0] = Integer.valueOf(1);
    	for (int i = 1; i < m; i++)
    		e[i] = Integer.valueOf(0);
    	
    	/***************
    	 * Step 1: Move all columns for non-terminal sequences to the left of the matrix. 
    	 ***************/
    	int count_n = 0;
    	int count_d = 0;
    	int count_i = 0;
    	int pos;
    	int[] independCheck = new int[m];
    	for (int i = 0; i < m; ++i)
    		independCheck[i] = 0;
    	for (int j = 0; j < n; j++)
    	{
    		//non-terminal sequences -- these columns are linear independent (otherwise circle in the tree)
    		//corresponding columns have one "1" and some "-1"
    		//note: each row has one (except first row) and only one "-1"
    		int nflag = 0;
    		for (nflag = 0; nflag < m; nflag++)
    			if (constraints[nflag][j] == -1)
    				break;
    		if(nflag < m)
    		{
    			N[count_n] = j;
    			for (int k = 0; k < m; k++) 
    				constraints1[k][count_n] = constraints[k][j];
    			++count_n;
    		}
    		
    		//terminal dependent sequences -- must choose linearly independent columns!!
    		//corresponding columns have one "1" and all other "0".
    		else if (independCheck[pos = findNonZero(constraints, j, -1)] == 0 && count_d < D.length)
    		{
    			independCheck[pos] = 1;
    			D[count_d] = j;
    			for (int k = 0; k < m; k++)
    				constraints1[k][s + count_d] = Integer.valueOf(constraints[k][j]);
    			++count_d;
    		}
    		else
    		{
    			I[count_i] = j;
    			for (int k = 0; k < m; k++)
    				constraints1[k][m + count_i] = Integer.valueOf(constraints[k][j]);
    			++count_i;
    		}	
    	}
    		
    	/**********
    	 * Step 2: Row Operations	
    	 ********/
    	// first row corresponds to empty sequence; no need to change;
    	
    	// getting upper triangle
    	for (int j = 0; j < m; j++)
    	{
    		if (constraints1[j][j] == 0){
    			pos = findNonZero(constraints1, j, j); //must exist because columns 0 ~ j-1 span the j-dimensional space. 
    			                                       //and column j is independent of columns 0 ~ j-1
    			for (int k = 0; k < n; k++)
    				constraints1[j][k] += constraints[pos][k];
    			e[j] += e[pos];
    		}
            if (constraints1[j][j] < 0 ){
    			for (int k = 0; k < n; k++)
    				constraints1[j][k] = -constraints1[j][k];
    			e[j] = -e[j];
    		}
    		for (int i = j + 1; i < m; i++)
    		{
    			if (!(constraints1[i][j] == 0))
    			{
    				Integer value = constraints1[i][j];
    				//row operation
    				for (int k = 0; k < n; k++)
    				    constraints1[i][k] = constraints1[i][k] - value * constraints1[j][k];
    			    e[i] = e[i] - value * e[j];
    		    }
    		}
    	}
 
    	
    	// getting I at the left
    	for (int i = m-1; i >= 0; i--)
    	{
    		for (int j = m-1; j > i; j--)
    		{
    			if (!(Integer.valueOf(constraints1[i][j]) == 0))
    			{
    				Integer value = Integer.valueOf(constraints1[i][j]);
    				for (int k = 0; k < n; k++)
    					constraints1[i][k] = Integer.valueOf(constraints1[i][k]) - value * Integer.valueOf(constraints1[j][k]);
    				e[i] = Integer.valueOf(e[i]) - value * Integer.valueOf(e[j]);
    			}
    					
    		}
    	}
    	
    	/******
    	 * Step 3: Fill up P1, P2, p1, p2	
    	 *******/
    	
    	for (int i = 0; i < s; i++)
    		for (int j = 0; j < n-m; j++)
    			P1[i][j] = Integer.valueOf(-constraints1[i][m+j]);
    	
    	for (int i = 0; i < m-s; i++)
    		for (int j = 0; j < n-m; j++)
    			P2[i][j] = Integer.valueOf(-constraints1[s+i][m+j]);
    	
    	for (int i = 0; i < s; i++)
    		p1[i] = Integer.valueOf(e[i]);
    	 
    	for (int i = 0; i < m-s; i++)
    		p2[i] = Integer.valueOf(e[s+i]);
    	
    	/*******
    	Step 4: Reorder payoff matrix
    	 *******/
        Rational [][] C1 = new Rational[pay1.length][pay1[0].length];
		for (int i = 0; i < C1.length; ++i)
    		for (int j = 0; j < C1[0].length; ++j)
    			C1[i][j] = pay1[i][j];
        Rational [][] C2 = new Rational[pay2.length][pay2[0].length];
		for (int i = 0; i < C2.length; ++i)
    		for (int j = 0; j < C2[0].length; ++j)
    			C2[i][j] = pay2[i][j];
    	
    	if (player == 1){
        	for (int i = 0; i < N1.length; ++i)
    			for (int j = 0; j < pay1[i].length; ++j){
    				pay1[i][j] = C1[N1[i]][j];
    				pay2[i][j] = C2[N1[i]][j];
    			}
    		for (int i = 0; i < D1.length; ++i)
    			for (int j = 0; j < pay1[i].length; ++j){
    				pay1[N1.length + i][j] = C1[D1[i]][j];
    				pay2[N1.length + i][j] = C2[D1[i]][j];
    			}
    	    for (int i = 0; i < I1.length; ++i)
    	    	for (int j = 0; j < pay1[i].length; ++j){
    	    		pay1[N1.length + D1.length + i][j] = C1[I1[i]][j];
    	    		pay2[N1.length + D1.length + i][j] = C2[I1[i]][j];
    	    	}
    	}else{
    		for (int j = 0; j < N2.length; ++j)
    			for (int i = 0; i < pay2.length; ++i){
    				pay1[i][j] = C1[i][N2[j]];
    				pay2[i][j] = C2[i][N2[j]];
    			}
    		for (int j = 0; j < D2.length; ++j)
    			for (int i = 0; i < pay2.length; ++i){
    				pay1[i][N2.length + j] = C1[i][D2[j]];
    				pay2[i][N2.length + j] = C2[i][D2[j]];
    			}
    		for (int j = 0; j < I2.length; ++j)
    			for (int i = 0; i < pay2.length; ++i){
    				pay1[i][N2.length + D2.length + j] = C1[i][I2[j]];
    				pay2[i][N2.length + D2.length + j] = C2[i][I2[j]];
    			}
    	}
    }
    
    
    private int findNonZero(Integer[][] c, int j, int rowBelow){
    	int i = rowBelow + 1;
    	for (; i < c.length; ++i)
    		if (!(c[i][j] == 0))
    			break;
    	if (i == c.length)  i = -1; 
    	return i;
    }
   

    public LCP getLemkeLCP()
    {
    	LCP lcp = new LCP(n1 + n2 - s1 - s2);
    				
    	/* fill  M  */
    	/* -A --> -_A      */
    	for (int i = 0; i < _A.length; ++i) {
    	    for (int j = 0; j < _A[i].length; ++j) {
    	        Rational value = _A[i][j].negate();
    	        lcp.setM(i, j + n1 - m1 + m2 -s2, value);
  	        }
    	}
    	        
        /* -E\T --> -P2\T     */
        for (int i = 0; i < P2.length; ++i) {
    	    for (int j = 0; j < P2[i].length; ++j) {
    	        Rational value = Rational.valueOf(-P2[i][j]);
    	        lcp.setM(j, i + n1 - m1 + n2 - s2, value);
    	    }
    	} 
    	        
    	/* F --> Q2        */
    	for (int i = 0; i < Q2.length; ++i) {
    	    for (int j = 0; j < Q2[i].length; ++j) {
    	        Rational value = Rational.valueOf(Q2[i][j]);
    	        lcp.setM(i + n1 - m1, j + n1 - m1 + m2 - s2, value);
    	    }
    	}
    	        
    	/* -B\T  --> -_B\T   */        
    	for (int i = 0; i < _B.length; ++i) {
    	    for (int j = 0; j < _B[i].length; ++j) {
    	        Rational value = _B[i][j].negate();
    	        lcp.setM(j + n1 - m1 + m2 - s2, i, value);
    	    }
    	} 
    	                
    	/* -F\T --> -Q2\T    */        
    	for (int i = 0; i < Q2.length; ++i) {
    	    for (int j = 0; j < Q2[i].length; ++j) {
    	        Rational value = Rational.valueOf(-Q2[i][j]);
    	        lcp.setM(j + n1 - m1 + m2 -s2, i + n1 - m1, value);
    	    }
    	}        
    	        
    	/* E --> P2       */        
    	for (int i = 0; i < P2.length; ++i) {
    	    for (int j = 0; j < P2[i].length; ++j) {
    	        Rational value = Rational.valueOf(P2[i][j]);
    	        lcp.setM(i + n1 - m1 + n2 -s2, j, value);
    	    }
    	}
    	      
    	/* define RHS q,  using special shape of SF constraints RHS e,f     */
    	for (int i = 0; i < _a.length; ++i){
    	   	Rational value = _a[i][0];
    	   	lcp.setq(i, value);
    	}
    	for (int i = 0; i < q2.length; ++i){
    	   	Rational value = Rational.valueOf(-q2[i]);
    	   	lcp.setq(i + n1 -m1, value);
    	}
    	for (int i = 0; i < _b[0].length; ++i){
    	   	Rational value = _b[0][i];
    	   	lcp.setq(i + n1 - m1 + m2 - s2, value);
    	}
    	for (int i = 0; i < p2.length; ++i){
    	   	Rational value = Rational.valueOf(-p2[i]);
    	  	lcp.setq(i + n1 - m1 + n2 - s2, value);
    	}
    	       
    	//return addCoveringVector(lcp);    
    	return lcp; //to be changed to addCoveringVector
    }
    
    public Rational[] extendVertex(vertex vt, int player){
    	/*
    	 * |x_N| = s; |x_D| = m - s; |x_I| = n - m; |x| = |x_N| + |x_D| + |x_I| = n
    	 */
    	int s, m, n; 
    	
    	int[] N, D, I;
    	if(player == 1) {
    		N = N1;  
    		D = D1; 
    	    I = I1;
    	    s = p1.length;       
        	m = s + p2.length;  
        	n = m + _a.length;
    	}
    	else {
    		N = N2; 
    		D = D2; 
    		I = I2;
    		s = q1.length;
    		m = s + q2.length;
    		n = m + _b[0].length;
    	}
    	
    	Rational x_I[][] = new Rational[n - m][1];
    	Rational x_N[][] = null;
    	Rational x_D[][] = null; 
    	for (int i = 0; i < n - m; ++i)
    		x_I[x_I.length - i - 1][0] = vt.v[vt.v.length - i - 1]; //vt.v = [v x_I] copy backwards
    	
    	Rational x[] = new Rational[n];
    	if (player == 1){
    	    x_N = MatrixAddition(VectorToMatrix(p1), MatrixMultiply(CRationalMatrix(P1), x_I));
    	    x_D = MatrixAddition(VectorToMatrix(p2), MatrixMultiply(CRationalMatrix(P2), x_I));
    	}else{
    		x_N = MatrixAddition(VectorToMatrix(q1), MatrixMultiply(CRationalMatrix(Q1), x_I));
    	    x_D = MatrixAddition(VectorToMatrix(q2), MatrixMultiply(CRationalMatrix(Q2), x_I));
    	}
    	
    	for (int i = 0; i < s; ++i)
    		x[N[i]] = x_N[i][0];
        for (int i = 0; i < m - s; ++i)
        	x[D[i]] = x_D[i][0];
        for (int i = 0; i < n - m; ++i)
        	x[I[i]] = x_I[i][0];
    	
    	return x;
    }
   
   
    public Rational[][] getD1()
    {
    	Rational[][] D1 = new Rational[p2.length + _b[0].length][P2[0].length + Q2.length + 1];
    	
    	for (int i = 0; i < D1.length; ++i)
    		for (int j = 0; j < D1[i].length; ++j)
    			D1[i][j] = Rational.ZERO;
    	
    	/*
    	 *  |p2     0         P2|
    	 *  |-_b\T  -Q2\T  -_B\T| 
    	 */
    	
    	/*   p2 0 P2   */
    	for (int i = 0; i < p2.length; ++i) {
    		/* p2 */
    		D1[i][0] = Rational.valueOf(p2[i]);
    	    /* 0 */
    	    for (int j = 1; j < Q2.length + 1; ++j) 
    	    	D1[i][j] = Rational.ZERO;
    	    /* P2 */
    	    for (int j = 0; j < P2[i].length; ++j) 
    	        D1[i][1 + Q2.length + j] = Rational.valueOf(P2[i][j]);
 }
    	
    	/* -_b\T  -Q2\T  -_B\T */
    	for (int i = 0; i < n2 - m2; ++i)
    	{
    		/* -_b\T */
    		D1[p2.length + i][0] = _b[0][i].negate();
    		/* -Q2\T */
    		for (int j = 0; j < Q2.length; ++j)
    			D1[p2.length + i][1 + j] = Rational.valueOf(-Q2[j][i]);
    		/* -_B\T */
    		for (int j = 0; j < _B.length; ++j)
    			D1[p2.length + i][1 + Q2.length + j] = _B[j][i].negate();
    	}
    	        
    	return D1;        
    }
   
    public Rational[][] getD2()
    {
    	Rational[][] D2 = new Rational[q2.length + _a.length][1 + P2.length + Q2[0].length];
       	
    	for (int i = 0; i < D2.length; ++i)
    		for (int j = 0; j < D2[i].length; ++j)
    			D2[i][j] = Rational.ZERO;
    
    	/*
    	 *  |q2     0       Q2|
    	 *  |-_a   -P2\T   -_A| 
    	 */
    	
    	/*   q2   0   Q2    */
    	for (int i = 0; i < q2.length; ++i) {
    		/* q2 */
    		D2[i][0] = Rational.valueOf(q2[i]);
    	    /* 0 */
    	    for (int j = 1; j < P2.length + 1; ++j) 
    	    	D2[i][j] = Rational.ZERO;
    		/* Q2 */
    	    for (int j = 0; j < Q2[i].length; ++j) 
    	        D2[i][1 + P2.length + j] = Rational.valueOf(Q2[i][j]);
    	}
    	
    	/* -_a   -P2\T  -_A */
    	for (int i = 0; i < _a.length; ++i)
    	{
    		/* -_a\T */
    		D2[q2.length + i][0] = _a[i][0].negate();
    		/* -P2\T */
    		for (int j = 0; j < P2.length; ++j)
    			D2[q2.length + i][1 + j] = Rational.valueOf(-P2[j][i]);
    		/* -_A */
    		for (int j = 0; j < _A[i].length; ++j)
    			D2[q2.length + i][1 + P2.length + j] = _A[i][j].negate();
    	}       
    	return D2;        
    }
    
    public static Rational[][] MatrixAddition(Rational[][] A, Rational[][] B)
    {
    	int m = A.length;
    	int n = A[0].length;
    	
    	Rational[][] Result = new Rational[m][n];
    	
    	if(!(B.length == m && B[0].length ==n)){
    		System.out.println("Invalid Matrix Dimension in multiplication");
			return Result;
    	}
    	
    	for (int i = 0; i < m; ++i)
    		for (int j = 0; j < n; ++j)
    			Result[i][j] = A[i][j].add(B[i][j]);
    	return Result;
    }
   
 
	public static Rational[][] MatrixMultiply(Rational[][] A, Rational[][] B)
	{
		int m = A.length;
		int n = A[0].length;
		int t = B[0].length;
		
		Rational[][] Result = new Rational[m][t];
		
		if (!(n == B.length))
		{
			System.out.println("Invalid Matrix Dimension in multiplication");
			return Result;
		}	
		
	    for (int i = 0; i < m; i++)
	    {
	    	for (int j = 0; j < t; j++)
	    	{
	    		Result[i][j] = Rational.ZERO;
	    	    for (int k = 0; k < n; k++)
	    	    	Result[i][j] = Result[i][j].add(A[i][k].multiply(B[k][j]));
	    	}
	    }
	    return Result;
	}
	
	public static Rational[][] CRationalMatrix(Integer[][] A)
	{
		int m = A.length;
		int n = A[0].length;
		
		Rational[][] RA = new Rational[m][n];
		
		for (int i = 0; i < m; i++)
			for (int j = 0; j < n; j++)
				RA[i][j] = Rational.valueOf(Integer.valueOf(A[i][j]));
		return RA;
	}
	
	public static Rational[] CRationalVector(Integer[] e)
	{
		int n = e.length;
		
		Rational[] Re = new Rational[n];
		
		for (int i = 0; i < n; i++)
			Re[i] = Rational.valueOf(Integer.valueOf(e[i]));
		return Re;
	}
	
	public static Rational[][] VectorToMatrix(Integer[] q)
	{
		int n = q.length;
		
		Rational[][] Matrixq = new Rational[n][1];
		
		for (int i = 0; i < n; i++)
			Matrixq[i][0] = Rational.valueOf(Integer.valueOf(q[i]));
		
		return Matrixq;
	}
	
	public static Rational[][] VectorToMatrix(Rational [] q)
	{
		int n = q.length;
		
		Rational[][] Matrixq = new Rational[n][1];
		
		for (int i = 0; i < n; i++)
			Matrixq[i][0] = q[i];
		
		return Matrixq;
	}
	
    public static Rational[][] MatrixTranspose(Rational[][] A)
    {
    	int m = A[0].length;
    	int n = A.length;
    	Rational[][] AT = new Rational[m][n];
    	for (int i = 0; i < m; i++)
    		for (int j = 0; j < n; j++)
    			AT[i][j] = A[j][i];
    	return AT;
    }
    
}