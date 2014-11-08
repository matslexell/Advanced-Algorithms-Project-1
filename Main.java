import java.math.*;

public class Main {

	public Main() {	
	}

	public static void main(String[] args) throws FactorizationFailure{
		Main m = new Main();
		BigInteger a = BigInteger.valueOf(42);
		BigInteger b = BigInteger.valueOf(12);
		BigInteger c = BigInteger.valueOf(1002);

		System.out.println("gcd(" + a + ", " + b + ") = " + m.gcd(a, b));
		System.out.println("pollardRho(" + c + ") = " + m.pollardRho(c));
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
	* Return a non-trivial factor of given number, or throw
	* an exception if none can be found.
	* For instance pollardRho(1002) == 3
	*/
	public BigInteger pollardRho(BigInteger n) throws FactorizationFailure{
		BigInteger x = BigInteger.valueOf(2);
		BigInteger y = BigInteger.valueOf(2);
		BigInteger d = BigInteger.ONE;
		while(d == BigInteger.ONE){
			x = g(x, n);
			y = g(g(y, n), n);
			d = gcd(x.subtract(y).abs(), n);
		}
		if(d == n){
			throw new FactorizationFailure();
		}
		return d;
	}

	// Function used by pollardRho
	public BigInteger g(BigInteger x, BigInteger n){
		return x.pow(2).add(BigInteger.ONE).mod(n);
	}

}
