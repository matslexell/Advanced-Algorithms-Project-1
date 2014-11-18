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
	public static BigInteger pollardRhoInner(BigInteger n, BigInteger startValue, UnaryOperator<BigInteger> g) throws FactorizationFailure{
		
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
			d = Naive.gcd(x.subtract(y).abs(), n);
			// System.out.println(x + ", " + y + ", " + d);
		}
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
