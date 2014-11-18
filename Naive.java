import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;


public class Naive {
	public static BigInteger naiveGetFactor(BigInteger n) throws FactorizationFailure{
		BigInteger max = BigInteger.valueOf((long)Math.sqrt(n.doubleValue()) + 1); //max must be >= sqrt(n)
		for(BigInteger f = BigInteger.valueOf(2); f.compareTo(max) <= 0; f = f.add(BigInteger.ONE)){
			BigInteger[] div = n.divideAndRemainder(f);
			if(div[1].equals(BigInteger.ZERO) && ! div[0].equals(BigInteger.ONE)){
				
				return f;
			}
		}
		throw new FactorizationFailure(n + " is prime.");
	}
	
	/**
	* Return the greatest common divisor of two numbers.
	* For instance gcd(42, 12) == 6
	*/
	public static BigInteger gcd(BigInteger a, BigInteger b){
		BigInteger t;
		while(! b.equals(BigInteger.ZERO)){
			t = b;
			b = a.mod(b);
			a = t;
		}
		return a;
	}
	
	//Implements Sieve of Eratosthenes
	public static List<Long> getPrimesLessThan(long max){
		HashSet<Long> composites = new HashSet<Long>();
		List<Long> primes = new ArrayList<Long>();
		for(long i = 2; i < max; i++){
			if(! composites.contains(i)){
				long prime = i;
				primes.add(prime);
				for(long comp = prime * 2; comp < max; comp += prime){
					composites.add(comp);
				}
			}
		}
		return primes;
	}
	
	// Implements Sieve of Eratosthenes
//	public static ArrayList<BigInteger> getPrimesLessThan(long max) { // TODO
//																		// BigInteger
//		// instead of long?
//		// No?
//		HashSet<Long> composites = new HashSet<Long>();
//		ArrayList<BigInteger> primes = new ArrayList<BigInteger>();
//		for (long i = 2; i < max; i++) {
//			if (!composites.contains(i)) {
//				long prime = i;
//				primes.add(BigInteger.valueOf(prime));
//				for (long comp = prime * 2; comp < max; comp += prime) {
//					composites.add(comp);
//				}
//			}
//		}
//		return primes;
//	}
}
