import java.math.*;

public class Main {

	public Main() {
		BigInteger a = BigInteger.valueOf(10);
		BigInteger b = BigInteger.valueOf(12);

		System.out.println("gcd(" + a + ", " + b + ") = " + gcd(a, b));
	}

	public static void main(String[] args) {
		new Main();
	}

	public BigInteger gcd(BigInteger a, BigInteger b){
		BigInteger t;
		while(b != BigInteger.ZERO){
			t = b;
			b = a.mod(b);
			a = t;
		}
		return a;
	}
}
