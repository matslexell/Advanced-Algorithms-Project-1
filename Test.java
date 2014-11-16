import java.math.*;
import java.util.*;
import java.util.function.*;
public class Test{
	public static void test() throws FactorizationFailure{
		ArrayList<BigIntAndFactors> cols = new ArrayList<BigIntAndFactors>();

		cols.add(new BigIntAndFactors(BigInteger.valueOf(30)));
		cols.get(0).addFactor(BigInteger.valueOf(2));
		cols.get(0).addFactor(BigInteger.valueOf(3));
		cols.get(0).addFactor(BigInteger.valueOf(5));

		cols.add(new BigIntAndFactors(BigInteger.valueOf(5)));
		cols.get(1).addFactor(BigInteger.valueOf(5));

		cols.add(new BigIntAndFactors(BigInteger.valueOf(10)));
		cols.get(2).addFactor(BigInteger.valueOf(2));
		cols.get(2).addFactor(BigInteger.valueOf(5));

		cols.add(new BigIntAndFactors(BigInteger.valueOf(35)));
		cols.get(3).addFactor(BigInteger.valueOf(5));
		cols.get(3).addFactor(BigInteger.valueOf(7));

		Matrix m = new Matrix(cols);
		System.out.println(m);
		m.gaussEliminate();
		System.out.println("\n" + m);
	}
}