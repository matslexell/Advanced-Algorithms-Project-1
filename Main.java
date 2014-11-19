import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class Main {
	
	public Main() {	
	}

	public static void main(String[] args) throws FactorizationFailure{
		project1(1, 199);
//		System.out.println(Naive.naiveGetFactor(BigInteger.valueOf(5030441351213302113L)));
	}
	
	private static void test1(){
		BigInteger n = BigInteger.valueOf(179426549L);
		n = n.multiply(BigInteger.valueOf(179426491L));
		n = n.multiply(BigInteger.valueOf(179426453L));
		n = n.multiply(BigInteger.valueOf(1570882310L));
		n = n.multiply(BigInteger.valueOf(9987231111L));
		n = n.multiply(BigInteger.valueOf(1119502020L));
		testFactorN(n);
	}
	
	private static void test2(){
		Map<BigInteger, Integer> factors = new HashMap<BigInteger, Integer>();
		factors.put(BigInteger.valueOf(179426549L), 2);
		factors.put(BigInteger.valueOf(179426231L), 1);
		factors.put(BigInteger.valueOf(2237), 1);
		factors.put(BigInteger.valueOf(3), 4);
		testFactorProductOf(factors);
	}
	
	private static void project1(int first, int last){
		ArrayList<BigInteger> numbers = NGenerator.genNumbers();
		for(int i = first; i <= last; i++){
			BigInteger number = numbers.get(i);
			long startTime = System.currentTimeMillis();
			Map<BigInteger, Integer> factors = factorNumber(number);
			System.out.println(factors);
			long passedTimeSec = (System.currentTimeMillis() - startTime)/1000;
			System.out.println("Factoring took " + passedTimeSec + " s.");
			System.out.println("[" + i + "] : " + validateFactors(factors, number));
		}
	}
	
	private static boolean validateFactors(Map<BigInteger, Integer> factors, BigInteger product){
		BigInteger accumulated = BigInteger.ONE;
		for(Entry<BigInteger,Integer> entry : factors.entrySet()){
			for(int i = 0; i < entry.getValue(); i++){
				accumulated = accumulated.multiply(entry.getKey());
			}
		}
		return accumulated.equals(product);
	}
	
	private static void testFactorN(BigInteger n){
		Map<BigInteger,Integer> factors = factorNumber(n);
		System.out.println("factors: " + factorNumber(n));
		System.out.println("CORRECT: " + validateFactors(factors, n));
	}
	
	private static void testFactorProductOf(Map<BigInteger, Integer> primeFactors){
		BigInteger n = BigInteger.ONE;
		for(BigInteger f: primeFactors.keySet()){
			for(int i = 0; i < primeFactors.get(f); i++){
				n = n.multiply(f);
			}
		}
//		List<BigInteger> resultFactors = factorNumber(n);
		Map<BigInteger, Integer> resultFactors = factorNumber(n);
		System.out.println("factors: " + primeFactors);
		System.out.println("result: " + resultFactors);
		boolean correct =primeFactors.equals(resultFactors);
		if(correct){
			System.out.println("CORRECT!");
		}else{
			System.out.println("WRONG!");
		}
	}



	public static BigInteger getFactor(BigInteger n) throws FactorizationFailure{
		BigInteger smallerFactor;
		
		if(n.compareTo(BigInteger.valueOf(Long.MAX_VALUE)) <= 0){
			Printer.MAIN.print("naiveGetFactor(" + n + ")");
			smallerFactor = Naive.naiveGetFactor(n);
			Printer.MAIN.println("  FOUND: " + smallerFactor);
			return smallerFactor;
		}
	
		
		
//		PollardRho.G g = new PollardRho.G(n, BigInteger.valueOf(2));
		try {
			Printer.MAIN.print("pollardRhoInner(" + n + ")");
			final BigInteger maxAdd = BigInteger.valueOf(1);
			final BigInteger maxStart = BigInteger.valueOf(2);
			smallerFactor = PollardRho.pollardRhoQuick(n, maxAdd, maxStart);
		} catch (FactorizationFailure e) {
			Printer.MAIN.println(" FAILED");
			try {
				Printer.MAIN.print("QuadrSieve(" + n + ")");
				smallerFactor = QuadrSieve.getFactor(n);
			} catch (FactorizationFailure e1) {
				throw new FactorizationFailure("cant' factor " + n );
			}
		}
		Printer.MAIN.println("  FOUND: " + smallerFactor);
		return smallerFactor;
	}

	

	

	/** 
	* Return the prime factors of given number.
	* Factors are returned in a map with factors as keys and the factor-powers as values.
	* For instance getPrimeFactors(12) == {2:2, 3:1} since 12 = 2^2 * 3^1
	*/
	public static Map<BigInteger, Integer> factorNumber(BigInteger n){
		Printer.MAIN.println("\n------------------------------------------------------------------");
		Printer.MAIN.println("getPrimeFactors(" + n + ")\n");
		Map<BigInteger, Integer> primeFactors = new HashMap<BigInteger, Integer>();
		BigInteger factor;
		BigInteger smallerFactor;
		List<BigInteger> factors = new ArrayList<BigInteger>();
		factors.add(n);
		while(! factors.isEmpty()){
			factor = factors.remove(0);
			try{
				// Printer.MAIN.println("getFactor(" + factor + ")");
				smallerFactor = getFactor(factor);
				factors.add(smallerFactor);
				factors.add(factor.divide(smallerFactor));
			}catch(FactorizationFailure e){
				Printer.MAIN.println("   PRIME");
				increment(primeFactors, factor);
			}
		}
		return primeFactors;
	}
	

	// Increment value of given key in the map with one. 
	// Add key first if it's not present.
	private static <E> void increment(Map<E, Integer> map, E key){
		if(map.containsKey(key)){
			map.put(key, map.get(key) + 1);
		}else{
			map.put(key, 1);
		}
	}


	
	public static void testGauss(){
		ArrayList<BigIntAndFactors> cols = new ArrayList<BigIntAndFactors>();

		List<Long> primes = Naive.getPrimesLessThan(20);

		cols.add(new BigIntAndFactors(BigInteger.valueOf(2)));
		cols.get(0).computeAndSetFactors(primes);

		cols.add(new BigIntAndFactors(BigInteger.valueOf(2)));
		cols.get(1).computeAndSetFactors(primes);

		cols.add(new BigIntAndFactors(BigInteger.valueOf(3)));
		cols.get(2).computeAndSetFactors(primes);
		
		cols.add(new BigIntAndFactors(BigInteger.valueOf(6)));
		cols.get(3).computeAndSetFactors(primes);

		Matrix m = new Matrix(cols);
		Printer.MAIN.println(m);
		m.gaussEliminate();
		Printer.MAIN.println("\n" + m);
		
		List<ArrayList<Integer>> solutions = m.getSomeNonTrivialSolutions();
		for(ArrayList<Integer> solution : solutions) {
			Printer.MAIN.println("Product of " + Arrays.toString(solution.stream().mapToInt(i -> cols.get(i).getNumber().intValue()).toArray()) + " is a square");
		}
	}

	
	
	
}