import java.math.BigInteger;
import java.util.ArrayList;

public class NGenerator {

	final static long mats = 8811091712L;
	final static long jonathan = 9001011234L;
	final static long j = 0;

	public NGenerator() {
		System.out.println(genNumbers());
	}

	public static ArrayList<BigInteger> genNumbers() {

		ArrayList<BigInteger> array = new ArrayList<BigInteger>();

		for (int i = 1; i <= 100; i++)
			array.add(equation(mats, i));

		for (int i = 1; i <= 100; i++)
			array.add(equation(jonathan, i));
		
		return array;
	}

	private static BigInteger equation(long P, int i) {
		BigInteger big = BigInteger.valueOf(10).pow((int) (60 + j));
		return BigInteger.valueOf(P).multiply(big).add(BigInteger.valueOf(i));
	}

	public static void main(String[] args) {
		new NGenerator();
	}
}
