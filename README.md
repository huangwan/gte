=================
  2013-08-30
=================

1. Modified files: lrs/VPolygon.java | lrs/LrsAlgorithm.java
   Add a new field ray to VPolygon, because the extensive form applies lrs to polyhedra instead of polytop.

2. New file: lrs/vertex.java
   New class added for extensive game with a non-redundancy representation.

3. New file: tree/NonRedundantSequenceForm.java
   Algorithm to remove redundancy.

4. Modified file: tree/SequenceForm.java
   Add a new orginPay field to save the payoff matrices A, B.

5. New file: tree/SequenceFormMain.java
   Tester.Including Myerson's example (1986), Wan's example (2011), and Bernhard's example (2013). 
