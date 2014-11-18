import java.math.BigInteger;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

class BigIntAndFactors {
	private BigInteger b;
	private TreeMap<Integer, Byte> map = new TreeMap<Integer, Byte>();

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
		for(int pInd = 0; pInd < allowedFactors.size(); pInd ++){
			BigInteger factor = allowedFactors.get(pInd);
			while(isDivisible(factor)){
				divideAndStoreFactor(factor, pInd);
			}
			if (isOne()) {
				setNumber(original);
				return true;
			}
		}
		return false;
	}
	
	public Iterator<Integer> getIndicesOfOnes(){
		return map.keySet().iterator();
	}
	
	public Iterator<Integer> getIndicesOfOnesFromBottom(){
		return map.descendingKeySet().iterator();
	}

	public byte getFactorFreq(int index) {
		Byte i = map.get(index);
		return i == null ? 0 : i.byteValue();
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
	
	public void addFactor(Integer primeIndex){
		increment(map, primeIndex);
	}

	public void divideAndStoreFactor(BigInteger f, int primeIndex) {
		BigInteger[] divResult = b.divideAndRemainder(f);
		b = divResult[0];
		if(! divResult[1].equals(BigInteger.ZERO)){
			throw new IllegalArgumentException("Remainder was " + divResult[1]);
		}
		increment(map, primeIndex);
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
	
//	@Override
//	public int hashCode(){
//		return b.hashCode(); 
//	}

}
