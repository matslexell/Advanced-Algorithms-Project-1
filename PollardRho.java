import java.math.BigInteger;
import java.util.function.UnaryOperator;



public class PollardRho {
	/**
	* Return a non-trivial factor of given number, or throw
	* an exception if none can be found.
	* For instance pollardRho(1002) == 3
	*/
	public static BigInteger pollardRho(BigInteger n) throws FactorizationFailure{
		FactorizationFailure exception = null;
		
		for(BigInteger addInG = BigInteger.ONE; addInG.compareTo(n) <= 0; addInG = addInG.add(BigInteger.ONE)){
			// Printer.POLLARD.print("add: " + addInG);
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
	public static BigInteger pollardRhoInner(BigInteger n, BigInteger startValue, UnaryOperator<BigInteger> g) throws FactorizationFailure{
		
		//TODO My guess is that the chosen g() doesn't like the number 4.
		if(n.intValue() == 4){
			return BigInteger.valueOf(2);
		}

		BigInteger x = startValue;
		BigInteger y = startValue;
		BigInteger d = BigInteger.ONE;
		int i = 0; 
		while(d.equals(BigInteger.ONE)){
			i ++;
			if(i % (100 * 1000) == 0){
				Printer.POLLARD_RHO.print(".");
			}
			if(i == 110 * 1000){
				throw new FactorizationFailure("Took too long to factorize " + n);
			}
			
			x = x.pow(2).add(BigInteger.valueOf(2)).mod(n);
			y = y.pow(2).add(BigInteger.valueOf(2)).mod(n).pow(2).add(BigInteger.valueOf(2)).mod(n);
			
			BigInteger t;
			BigInteger a = x.subtract(y).abs();
			BigInteger b = n;
			while(!b.equals(BigInteger.ZERO)){
				t = b;
				b = a.mod(b);
				a = t;
			}
			d = a;
			
//			x = g.apply(x);
//			y = g.apply(g.apply(y));
//			d = Naive.gcd(x.subtract(y).abs(), n);
			// Printer.POLLARD.print(x + ", " + y + ", " + d);
		}
		Printer.POLLARD_RHO.println(".");
		if(d.equals(n)){
			throw new FactorizationFailure("Failed to factorize " + n + ". (x:" + x + ", y:" + y + ", d:" + d + ")");
		}
		return d;
	}

	public static class G{
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
}
