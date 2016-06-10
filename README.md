This program JavaLisp implements Lisp using Java.

1. JavaLisp supports checks different inputs, e.g.,
	> j
	J
	java.lang.IllegalArgumentException: Unbounded Value

	at JavaLisp.Print(JavaLisp copy 4.java:193)
	at JavaLisp.main(JavaLisp copy 4.java:46)

2. JavaLisp supports basic computations, like multiplication, plus, division and subtraction, e.g.,
	> (+ 5   (- 2 (* 12 (/ 4 2))))
	-17
	
3. JavaLisp supports CONS, T, NIL and so forth, e.g.,
	> nil
	NIL
	> t
	T
	> 8
	8
	> (CONS 3 4)
	(3 . 4)
