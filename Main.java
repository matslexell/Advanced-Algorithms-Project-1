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
		BigInteger c = BigInteger.valueOf(4);
		BigInteger d = BigInteger.valueOf(60);

		System.out.println("gcd(" + a + ", " + b + ") == " + gcd(a, b));
		System.out.println("pollardRho(" + c + ") == " + pollardRho(c));
		for(BigInteger x = BigInteger.valueOf(2); x.compareTo(d) <= 0; x = x.add(BigInteger.ONE)){
			System.out.println("factors(" + x + ") == " + getPrimeFactors(x));	
		}
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
		for(BigInteger startValue = BigInteger.valueOf(2); startValue.compareTo(n) <= 0; startValue = startValue.add(BigInteger.ONE)){
			// System.out.println("start: " + startValue);
			try{
				BigInteger answer = pollardRhoInner(n, startValue);
				return answer;
			}catch(FactorizationFailure e){ 
				if(startValue.equals(n)){
					throw e;
				}
			}
		}
		throw new IllegalStateException(); //Should not be reached.
	}

	//Try to return a non-trivial factor of given number.
	private BigInteger pollardRhoInner(BigInteger n, BigInteger startValue) throws FactorizationFailure{
		
		//TODO My guess is that the chosen g() doesn't like the number 4.
		if(n.intValue() == 4){
			return BigInteger.valueOf(2);
		}

		BigInteger x = startValue;
		BigInteger y = startValue;
		BigInteger d = BigInteger.ONE;
		while(d.equals(BigInteger.ONE)){
			x = g(x, n);
			y = g(g(y, n), n);
			d = gcd(x.subtract(y).abs(), n);
			// System.out.println(x + ", " + y + ", " + d);
		}
		if(d.equals(n)){
			throw new FactorizationFailure("Failed to factorize " + n + ". (x:" + x + ", y:" + y + ", d:" + d + ")");
		}
		return d;
	}

	// Function used by pollardRho
	public BigInteger g(BigInteger x, BigInteger n){
		// System.out.println("g(" + x + ", " + n + ") == " + x.pow(2).add(BigInteger.ONE).mod(n));
		return x.pow(2).add(BigInteger.ONE).mod(n);
	}

}
