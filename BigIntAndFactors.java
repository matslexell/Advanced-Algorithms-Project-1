import java.math.BigInteger;
import java.util.*;

class BigIntAndFactors {
	private BigInteger b;
	private HashMap<BigInteger, Byte> map = new HashMap<BigInteger, Byte>();

	// LinkedList<Factor> primeFactors = new LinkedList<Factor>();

	// private ArrayList<Integer> freq = new ArrayList<Integer>();

	public BigIntAndFactors(BigInteger b) {
		this.b = b;
	}
	
	private void setNumber(BigInteger p){
		b = p;
	}
	
	public BigInteger getNumber(){
		return b;
	}

	public boolean computeAndSetFactors(List<BigInteger> allowedFactors){
		BigInteger original = b;
		for(BigInteger factor : allowedFactors){
			while(isDivisible(factor)){
				divideAndStoreFactor(factor);
			}
			if (isOne()) {
				setNumber(original);
				return true;
			}
		}
		return false;
	}

	public byte getFactorFreq(int index) {
		Byte i = map.get(Quant.primes.get(index));
		return i == null ? 0 : i.byteValue();
	}

	private void addFactor(BigInteger f) {
		increment(map, f);
	}

	public void addFactorWithIndex(int index){
		increment(map, Quant.primes.get(index));
	}

	private <E> void increment(Map<E, Byte> map, E key) {
		if (map.containsKey(key)) {
			byte b = (byte) (map.get(key) + 1);
			// byte b = (byte) ((map.get(key) + 1) % 2);

			map.put(key, b);
		} else {
			map.put(key, (byte) 1);
		}
	}

	private void divideAndStoreFactor(BigInteger f) {
		b = b.divide(f);
		addFactor(f);
	}

	private boolean isOne() {
		return b.equals(BigInteger.ONE);
	}

	private boolean isDivisible(BigInteger d) {
		return b.mod(d).equals(BigInteger.ZERO);
	}

	public String toString() {
		return b.toString() + ": " + map.toString();
	}

}
