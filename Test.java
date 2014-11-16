import java.math.*;
import java.util.*;
import java.util.function.*;
public class Test{
	
	public static void test() throws FactorizationFailure{
		// BigIntAndFactors b = new BigIntAndFactors(BigInteger.valueOf(102));
		// System.out.println(b);
		// b.computeAndSetFactors(Quant.getPrimesLessThan(18));
		// System.out.println(b);			

		testGauss();
	}




	private static void testGauss(){
		ArrayList<BigIntAndFactors> cols = new ArrayList<BigIntAndFactors>();



		ArrayList<BigInteger> primes = Quant.getPrimesLessThan(20);


		cols.add(new BigIntAndFactors(BigInteger.valueOf(30)));
		cols.get(0).computeAndSetFactors(primes);

		cols.add(new BigIntAndFactors(BigInteger.valueOf(5)));
		cols.get(1).computeAndSetFactors(primes);

		cols.add(new BigIntAndFactors(BigInteger.valueOf(10)));
		cols.get(2).computeAndSetFactors(primes);

		cols.add(new BigIntAndFactors(BigInteger.valueOf(35)));
		cols.get(3).computeAndSetFactors(primes);

		Matrix m = new Matrix(cols);
		System.out.println(m);
		m.gaussEliminate();
		System.out.println("\n" + m);
	}
}