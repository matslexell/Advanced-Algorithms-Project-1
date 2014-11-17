import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
public class Test{
	
	public static void test() throws FactorizationFailure{
		// BigIntAndFactors b = new BigIntAndFactors(BigInteger.valueOf(102));
		// System.out.println(b);
		// b.computeAndSetFactors(Quant.getPrimesLessThan(18));
		// System.out.println(b);			

		testGauss();
		
		
//		BigInteger n = BigInteger.valueOf(100);
//		double logApprx = Math.log(n.doubleValue());
//		System.out.println(logApprx);
////		System.out.println("Input: " + n.intValue());
////		System.out.println("Log app: " + logApprx);
////		System.out.println("2^" + logApprx + " is " + Math.pow(2, logApprx));
//		System.out.println(Math.pow(Math.E, logApprx * 1.0 / 2));
//		System.out.println(Math.sqrt((double)100));
	}




	private static void testGauss(){
		ArrayList<BigIntAndFactors> cols = new ArrayList<BigIntAndFactors>();



		ArrayList<BigInteger> primes = Quant.getPrimesLessThan(20);
		
		


		cols.add(new BigIntAndFactors(BigInteger.valueOf(2)));
		cols.get(0).computeAndSetFactors(primes);

		cols.add(new BigIntAndFactors(BigInteger.valueOf(2)));
		cols.get(1).computeAndSetFactors(primes);

		cols.add(new BigIntAndFactors(BigInteger.valueOf(3)));
		cols.get(2).computeAndSetFactors(primes);
		
		cols.add(new BigIntAndFactors(BigInteger.valueOf(6)));
		cols.get(3).computeAndSetFactors(primes);

		Matrix m = new Matrix(cols);
		System.out.println(m);
		m.gaussEliminate();
		System.out.println("\n" + m);
		
		List<ArrayList<Integer>> solutions = m.getSomeNonTrivialSolutions();
		for(ArrayList<Integer> solution : solutions) {
			System.out.println("Product of " + Arrays.toString(solution.stream().mapToInt(i -> cols.get(i).getNumber().intValue()).toArray()) + " is a square");
		}
	}
}