import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import org.apache.commons.math3.*;

public class Quant {

	/**
	 * This class performs the quantic seive algortihm
	 */

	public static final long SMOOTHNESS = 7;

	public static ArrayList<BigInteger> genPVals(BigInteger n, int amount) {
		BigInteger start = biggerThanSqrt(n);
		System.out.println(start);
		ArrayList<BigInteger> potentials = new ArrayList<BigInteger>();
		for (int i = 0; i < amount; i++) {
			potentials.add(start.add(BigInteger.valueOf(i)).pow(2).subtract(n));
		}

		return potentials;
	}

	public static BigInteger biggerThanSqrt(BigInteger n) {
		int logApprx = n.toString(2).length();
		System.out.println("Input: " + n.intValue());
		System.out.println("Log app: " + logApprx);
		System.out.println("2^" + logApprx + " is " + Math.pow(2, logApprx));
		return BigInteger.valueOf(2).pow((int) Math.ceil(logApprx * 1.0 / 2));
	}

	// Implements Sieve of Eratosthenes
	public static ArrayList<BigInteger> getPrimesLessThan(long max) { // TODO
																		// BigInteger
		// instead of long?
		// No?
		HashSet<Long> composites = new HashSet<Long>();
		ArrayList<BigInteger> primes = new ArrayList<BigInteger>();
		for (long i = 2; i < max; i++) {
			if (!composites.contains(i)) {
				long prime = i;
				primes.add(BigInteger.valueOf(prime));
				for (long comp = prime * 2; comp < max; comp += prime) {
					composites.add(comp);
				}
			}
		}
		return primes;
	}

	public static ArrayList<BigIntAndFactors> smoothing(
			ArrayList<BigInteger> pVals) {

		ArrayList<BigIntAndFactors> nonSmooth = copy(pVals);
		ArrayList<BigInteger> primes = getPrimesLessThan(SMOOTHNESS + 1);
		ArrayList<BigIntAndFactors> smooth = new ArrayList<BigIntAndFactors>();

		for (int j = 0; j < nonSmooth.size(); j++) {
			BigIntAndFactors b = nonSmooth.get(j);
			BigInteger original = b.b;

			for (int i = 0; i < primes.size(); i++) {

				// Keep on deviding while mod is zero!
				while (b.isDivisible(primes.get(i))) {

					b.divideAndStoreFactor(primes.get(i));

				}

				if (b.isOne()) {
					b.b = original;
					smooth.add(b);
					break;
				}

			}
		}

		return smooth;
	}
	
	//TODO, next step! Produce matrix! DO NOT USE int[][], read up on sparse matrix
	public static int[][] produceMatrix(){
		return null;
	}

	public static ArrayList<BigIntAndFactors> copy(ArrayList<BigInteger> pVals) {

		ArrayList<BigIntAndFactors> array = new ArrayList<BigIntAndFactors>();
		for (int i = 0; i < pVals.size(); i++) {
			array.add(new BigIntAndFactors(pVals.get(i), i));
		}

		return array;
	}

	//This class represents a number and all it's factors //TODO, seems legit, maybe test some more?
	static class BigIntAndFactors {
		BigInteger b;
		LinkedList<Factor> primeFactors = new LinkedList<Factor>();

		// private ArrayList<Integer> freq = new ArrayList<Integer>();

		BigIntAndFactors(BigInteger b, int idx) {
			this.b = b;
		}


		void addFactor(BigInteger f) {

			Factor factor = primeFactors.peekLast();
			if (factor != null && factor.equals(f)) {
				factor.inc();
				return;
			}
			
			primeFactors.add(new Factor(f));

		}

		void divideAndStoreFactor(BigInteger f) {
			b = b.divide(f);
			addFactor(f);
		}

		boolean isOne() {
			return b.equals(BigInteger.ONE);
		}

		boolean isDivisible(BigInteger d) {
			return b.mod(d).equals(BigInteger.ZERO);
		}

		public String toString() {
			return b.toString() + ": " + primeFactors.toString();
		}

	}

	//A factor is the number itself, and the frequency
	static class Factor {
		//The factor can also be very big
		BigInteger factor;
		int frequency;

		Factor(BigInteger f) {
			factor = f;
			frequency = 1;
		}

		boolean equals(BigInteger f) {
			return factor.equals(f);
		}

		void inc() {
			frequency++;
		}

		public String toString() {
			return factor.toString() + "^" + frequency;
		}

	}

	public static void main(String[] args) {
		BigInteger n = BigInteger.valueOf(20L);

		ArrayList<BigInteger> p = Quant.genPVals(n, 4);
		System.out.println(p);
		ArrayList<BigIntAndFactors> smoothNumbers = smoothing(p);
		System.out.println(smoothNumbers);

	}
}
