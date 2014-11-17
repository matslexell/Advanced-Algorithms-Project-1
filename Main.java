import java.math.*;
import java.util.*;
import java.util.function.*;

public class Main {

	public Main() {	
	}

	public static void main(String[] args) throws FactorizationFailure{
		Test.test();
	}

	

	public BigInteger getFactor(BigInteger n) throws FactorizationFailure{
		
		if(n.compareTo(BigInteger.valueOf(Long.MAX_VALUE)) <= 0){
			naiveGetFactor(n);
		}
		return pollardRho(n);
	}

	public BigInteger naiveGetFactor(BigInteger n) throws FactorizationFailure{
		double log = Math.log(n.doubleValue());
		BigInteger max = BigInteger.valueOf((long) Math.pow(Math.E, Math.ceil(log/2)));
		if(max.compareTo(n) >= 0){
			max = n.subtract(BigInteger.ONE);
		}
		// System.out.println("Small n: " + n);
		for(BigInteger f = BigInteger.valueOf(2); f.compareTo(max) <= 0; f = f.add(BigInteger.ONE)){
			if(n.divideAndRemainder(f)[1].equals(BigInteger.ZERO)){
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
				// System.out.println("getFactor(" + factor + ")");
				smallerFactor = getFactor(factor);
				factors.add(smallerFactor);
				factors.add(factor.divide(smallerFactor));
			}catch(FactorizationFailure e){
				// System.out.println("it's prime");
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
					G g = new G(n, addInG);
					BigInteger answer = pollardRhoInner(n, startValue, x -> g.g(x));
					return answer;
				}catch(FactorizationFailure e){ 
					exception = e;
				}
			}
		}
		throw exception;
	}

	//Try to return a non-trivial factor of given number.
	private BigInteger pollardRhoInner(BigInteger n, BigInteger startValue, UnaryOperator<BigInteger> g) throws FactorizationFailure{
		
		//TODO My guess is that the chosen g() doesn't like the number 4.
		if(n.intValue() == 4){
			return BigInteger.valueOf(2);
		}

		BigInteger x = startValue;
		BigInteger y = startValue;
		BigInteger d = BigInteger.ONE;
		while(d.equals(BigInteger.ONE)){
			x = g.apply(x);
			y = g.apply(g.apply(y));
			d = gcd(x.subtract(y).abs(), n);
			// System.out.println(x + ", " + y + ", " + d);
		}
		if(d.equals(n)){
			throw new FactorizationFailure("Failed to factorize " + n + ". (x:" + x + ", y:" + y + ", d:" + d + ")");
		}
		return d;
	}

	private class G{
		private BigInteger n;
		private BigInteger toAdd;

		G(BigInteger n, BigInteger toAdd){
			this.n = n;
			this.toAdd = toAdd;
		}

		BigInteger g(BigInteger x){
			return x.pow(2).add(toAdd).mod(n);
		}
	}

	//Implements Sieve of Eratosthenes
	public List<Long> getPrimesLessThan(long max){
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

}
