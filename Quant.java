import java.math.BigInteger;
import java.util.ArrayList;

public class Quant {
	
	/**
	 * This class performs the quantic seive algortihm
	 */
	

	
//	public Quant(BigInteger n) {
//		this.n = n;
//		start = biggerThanSqrt(n);
//	}
	
	public static ArrayList<BigInteger> genPVals(BigInteger n, int amount){
		BigInteger start = biggerThanSqrt(n);
		System.out.println(start);
		ArrayList<BigInteger> potentials = new ArrayList<BigInteger>();
		for (int i = 0; i < amount; i++) {
			potentials.add(start.add(BigInteger.valueOf(i)).pow(2).subtract(n));
		}
		
		return potentials;
	}
	
	
		
	public static BigInteger biggerThanSqrt(BigInteger n){
		int logApprx = n.toString(2).length();
		System.out.println("Input: " + n.intValue());
		System.out.println("Log app: " + logApprx);
		System.out.println("2^" + logApprx + " is " + Math.pow(2, logApprx));
		return BigInteger.valueOf(2).pow((int) Math.ceil(logApprx * 1.0 / 2));
	}
	

	public static void main(String[] args) {
		BigInteger n = BigInteger.valueOf(20L);
		
		ArrayList<BigInteger> p = Quant.genPVals(n, 10);
		System.out.println(p);
		
	}
}
