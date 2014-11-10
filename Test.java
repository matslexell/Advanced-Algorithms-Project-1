import java.math.*;
import java.util.*;
import java.util.function.*;
public class Test{
	public static void test() throws FactorizationFailure{
		Main m = new Main();
		BigInteger a = BigInteger.valueOf(42);
		BigInteger b = BigInteger.valueOf(12);

		System.out.println("\ngcd(" + a + ", " + b + ") == " + m.gcd(a, b));

		System.out.println();
		for(BigInteger x = BigInteger.valueOf(2); x.compareTo(BigInteger.valueOf(10)) <= 0; x = x.add(BigInteger.ONE)){
			try{
				System.out.println("pollardRho(" + x + ") == " + m.pollardRho(x));	
			}catch(FactorizationFailure e){
				System.out.println(x + " is prime.");
			}
		}
		
		BigInteger x = BigInteger.valueOf(982451653).multiply(BigInteger.valueOf(7919 * 7 * 3 * 3));
		System.out.println("\nfactors(" + x + ") == " + m.getPrimeFactors(x));
		x = BigInteger.valueOf(982451632).pow(2);
		System.out.println("\nfactors(" + x + ") == " + m.getPrimeFactors(x));	
	}
}