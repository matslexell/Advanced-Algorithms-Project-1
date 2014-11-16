import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

class BigIntAndFactors {
	private BigInteger b;
	private HashMap<BigInteger, Byte> map = new HashMap<BigInteger, Byte>();

	// LinkedList<Factor> primeFactors = new LinkedList<Factor>();

	// private ArrayList<Integer> freq = new ArrayList<Integer>();

	public BigIntAndFactors(BigInteger b) {
		this.b = b;
	}
	
	public void setPrime(BigInteger p){
		b = p;
	}
	
	public BigInteger getPrime(){
		return b;
	}

	public byte getFactorFreq(int index) {
		Byte i = map.get(Quant.primes.get(index));
		return i == null ? 0 : i.byteValue();
	}

	public void addFactor(BigInteger f) {
		increment(map, f);
	}

	private <E> void increment(Map<E, Byte> map, E key) {
		if (map.containsKey(key)) {
//			byte b = (byte) (map.get(key) + 1);
			byte b = (byte) ((map.get(key) + 1) % 2);

			map.put(key, b);
		} else {
			map.put(key, (byte) 1);
		}
	}

	public void divideAndStoreFactor(BigInteger f) {
		b = b.divide(f);
		addFactor(f);
	}

	public boolean isOne() {
		return b.equals(BigInteger.ONE);
	}

	public boolean isDivisible(BigInteger d) {
		return b.mod(d).equals(BigInteger.ZERO);
	}

	public String toString() {
		return b.toString() + ": " + map.toString();
	}

}
