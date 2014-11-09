import java.math.*;
import java.util.*;

public class Main {

	public Main() {	
	}

	public static void main(String[] args) throws FactorizationFailure{
		Main m = new Main();
		m.testSomeMethods();
	}

	public void testSomeMethods() throws FactorizationFailure{
		BigInteger a = BigInteger.valueOf(42);
		BigInteger b = BigInteger.valueOf(12);

		System.out.println("\ngcd(" + a + ", " + b + ") == " + gcd(a, b));

		System.out.println();
		for(BigInteger x = BigInteger.valueOf(2); x.compareTo(BigInteger.valueOf(10)) <= 0; x = x.add(BigInteger.ONE)){
			try{
				System.out.println("pollardRho(" + x + ") == " + pollardRho(x));	
			}catch(FactorizationFailure e){
				System.out.println(x + " is prime.");
			}
		}
		
		BigInteger x = BigInteger.valueOf(401 * 7 * 3 * 3);
		//This is slow because it takes a long time to determine that 401 is actually prime.
		System.out.println("\nfactors(" + x + ") == " + getPrimeFactors(x));	
	}

	/**
	* Return the greatest common divisor of two numbers.
	* For instance gcd(42, 12) == 6
	*/
	public BigInteger gcd(BigInteger a, BigInteger b){
		BigInteger t;
		while(b != BigInteger.ZERO){
			t = b;
			b = a.mod(b);
			a = t;
		}
		return a;
	}

	/** 
	* Return the prime factors of given number.
	* Factors are returned in a map with factors as keys and the factor-powers as values.
	* For instance getPrimeFactors(12) == {2:2, 3:1} since 12 = 2^2 * 3^1
	*/
	public Map<BigInteger, Integer> getPrimeFactors(BigInteger n){
		Map<BigInteger, Integer> primeFactors = new HashMap<BigInteger, Integer>();
		BigInteger factor;
		BigInteger smallerFactor;
		List<BigInteger> factors = new ArrayList<BigInteger>();
		factors.add(n);
		while(! factors.isEmpty()){
			factor = factors.remove(0);
			try{
				smallerFactor = pollardRho(factor);
				factors.add(smallerFactor);
				factors.add(factor.divide(smallerFactor));
			}catch(FactorizationFailure e){
				increment(primeFactors, factor);
			}
		}
		return primeFactors;
	}

	// Increment value of given key in the map with one. 
	// Add key first if it's not present.
	private <E> void increment(Map<E, Integer> map, E key){
		if(map.containsKey(key)){
			map.put(key, map.get(key) + 1);
		}else{
			map.put(key, 1);
		}
	}


	/**
	* Return a non-trivial factor of given number, or throw
	* an exception if none can be found.
	* For instance pollardRho(1002) == 3
	*/
	public BigInteger pollardRho(BigInteger n) throws FactorizationFailure{
		FactorizationFailure exception = null;
		for(BigInteger addInG = BigInteger.ONE; addInG.compareTo(n) <= 0; addInG = addInG.add(BigInteger.ONE)){
			// System.out.println("add: " + addInG);
			for(BigInteger startValue = BigInteger.valueOf(2); startValue.compareTo(n.add(BigInteger.ONE)) <= 0; startValue = startValue.add(BigInteger.ONE)){
				
				try{
					BigInteger answer = pollardRhoInner(n, startValue, addInG);
					return answer;
				}catch(FactorizationFailure e){ 
					exception = e;
				}
			}
		}
		throw exception;
	}

	//Try to return a non-trivial factor of given number.
	private BigInteger pollardRhoInner(BigInteger n, BigInteger startValue, BigInteger addInG) throws FactorizationFailure{
		
		//TODO My guess is that the chosen g() doesn't like the number 4.
		if(n.intValue() == 4){
			return BigInteger.valueOf(2);
		}

		BigInteger x = startValue;
		BigInteger y = startValue;
		BigInteger d = BigInteger.ONE;
		while(d.equals(BigInteger.ONE)){
			x = g(x, n, addInG);
			y = g(g(y, n, addInG), n, addInG);
			d = gcd(x.subtract(y).abs(), n);
			// System.out.println(x + ", " + y + ", " + d);
		}
		if(d.equals(n)){
			throw new FactorizationFailure("Failed to factorize " + n + ". (x:" + x + ", y:" + y + ", d:" + d + ")");
		}
		return d;
	}

	// Function used by pollardRho
	public BigInteger g(BigInteger x, BigInteger n, BigInteger toAdd){
		// System.out.println("g(" + x + ", " + n + ") == " + x.pow(2).add(BigInteger.ONE).mod(n));
		return x.pow(2).add(toAdd).mod(n);
	}

}
