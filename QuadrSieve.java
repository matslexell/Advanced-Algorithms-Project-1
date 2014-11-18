import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.TreeMap;
import java.util.stream.Stream;


public class QuadrSieve {

	public static final long SMOOTHNESS = 500 * 1000;
	public final static List<Long> primes = Naive.getPrimesLessThan(SMOOTHNESS + 1);

	/**
	 * Input is the number n that we want to factor, the amount parameter
	 * represents smoothness (how many a values will be created)
	 * 
	 * @param n
	 * @param amount
	 * @return map from p to a (p == a^2 - N)
	 */
	public static TreeMap<BigInteger, BigInteger> genPVals(BigInteger n, int amount) {
		System.out.println("generating p-values...");
		BigInteger a0 = biggerThanSqrt(n);
//		System.out.println("first a: " + a0);
		TreeMap<BigInteger, BigInteger> pVals = new TreeMap<BigInteger, BigInteger>();
		
		for (int i = 0; i < amount; i++) {
			BigInteger a = a0.add(BigInteger.valueOf(i));
			BigInteger p = a.pow(2).subtract(n);
			if(p.compareTo(BigInteger.ZERO) > 0){
				pVals.put(p, a);
			}
		}

		return pVals;
	}

	public static BigInteger biggerThanSqrt(BigInteger n) {
		return BigInteger.valueOf((long)Math.sqrt(n.doubleValue()));
	}

	

	public static TreeMap<BigIntAndFactors, BigInteger> smoothing(
			TreeMap<BigInteger, BigInteger> pVals) {
		System.out.println("Smoothing...");
		ArrayList<BigIntAndFactors> nonSmooth = copy(pVals.keySet());
		TreeMap<BigIntAndFactors, BigInteger> smooth = new TreeMap<BigIntAndFactors, BigInteger>();

		for (int j = 0; j < nonSmooth.size(); j++) {
			
			BigIntAndFactors b = nonSmooth.get(j);
//			System.out.print("check if " + b + " is smooth");
			boolean isSmooth = b.computeAndSetFactors(primes);
			if(isSmooth){
				smooth.put(b, pVals.get(b.getNumber()));
			}
		}

		return smooth;
	}
	
	
//	//not correct
//	public static HashMap<BigIntAndFactors, BigInteger> smoothing2(
//			TreeMap<BigInteger, BigInteger> pVals) {
//		BigInteger biggestPVal = pVals.keySet().stream().max(BigInteger::compareTo).get();
//		System.out.println("biggest p : " + biggestPVal);
//		HashMap<BigInteger, BigIntAndFactors> composites = new HashMap<BigInteger, BigIntAndFactors>();
//		HashSet<BigInteger> primes = new HashSet<BigInteger>();
//		for(BigInteger i = BigInteger.valueOf(2); i.compareTo(BigInteger.valueOf(SMOOTHNESS)) <= 0; i = i.add(BigInteger.ONE)){
//			if(!composites.containsKey(i)){
//				BigInteger prime = i;
//				System.out.println("p " + prime);
//				primes.add(prime);
//				for(BigInteger comp = prime.multiply(BigInteger.valueOf(2)); comp.compareTo(biggestPVal) <= 0; comp = comp.add(prime)){
//					if(comp.divideAndRemainder(BigInteger.valueOf(10000000))[1].equals(BigInteger.ZERO))
//						System.out.println(comp);
//				}
//			}
//		}
//
//		HashMap<BigIntAndFactors, BigInteger> smoothValues = new HashMap<BigIntAndFactors, BigInteger>();
//		for(BigInteger p : pVals.keySet()){
//			if(primes.contains(p)){
//				smoothValues.put(new BigIntAndFactors(p), pVals.get(p));
//			}else if(composites.containsKey(p) && composites.get(p).isOne()){
//				smoothValues.put(composites.get(p), pVals.get(p));
//			}
//		}
//		
//		return smoothValues;
//	}
	
	
//	public static TreeMap<BigIntAndFactors, BigInteger> smoothing3(
//			TreeMap<BigInteger, BigInteger> pVals) {
//		
//		TreeMap<BigInteger, BigIntAndFactors> pDivisions = new TreeMap<BigInteger, BigIntAndFactors>();
//		
//		BigInteger firstP = pVals.firstKey();
//		System.out.println(pVals);
//		
//		for(int primeIndex = 0; primeIndex < primes.size(); primeIndex ++){
//			
//			BigInteger prime = BigInteger.valueOf(primes.get(primeIndex));
//			
//			if(primeIndex % 1 == 0){
//				System.out.println("prime " + prime);
//			}
//			
//			BigInteger[] divResult = firstP.divideAndRemainder(prime);
//			BigInteger firstDivisibleP = null;
//			if(divResult[1].equals(BigInteger.ZERO)){
//				firstDivisibleP = firstP;
//				System.out.println("firstP =" + firstP);
//				
//			}else{
//				firstDivisibleP = divResult[0].add(BigInteger.ONE).multiply(firstP);
//			}
//			for(BigInteger maybeP = firstDivisibleP; maybeP.compareTo(pVals.lastKey()) <= 0; maybeP = maybeP.add(prime)){
//				if(pVals.containsKey(maybeP)){
//					divideWhileDivisible(pDivisions, maybeP, prime, primeIndex);
//				}
//			}
//		}
//		
//		TreeMap<BigIntAndFactors, BigInteger> smoothPVals = new TreeMap<BigIntAndFactors, BigInteger>();
//		
//		for(Entry<BigInteger, BigIntAndFactors> pDivEntry : pDivisions.entrySet()){
//			BigInteger originalNumber = pDivEntry.getKey();
//			BigIntAndFactors dividedNumberWithFactors = pDivEntry.getValue();
//			if(dividedNumberWithFactors.isOne()){
//				dividedNumberWithFactors.setNumber(originalNumber);
//				BigInteger a = pVals.get(originalNumber);
//				smoothPVals.put(dividedNumberWithFactors, a);
//			}
//		}
//		
//		return smoothPVals;
//		
//	}
	
//	/**
//	 * 
//	 * @param divisions map from x to y(<=x) where y is the result of dividing x by primes.
//	 * @param originalNumber x
//	 * @param prime the prime to divide x with this time.
//	 */
//	private static void divideWhileDivisible(TreeMap<BigInteger, BigIntAndFactors> divisions, BigInteger originalNumber, BigInteger prime, int primeIndex){
//		if(!divisions.containsKey(originalNumber)){
//			divisions.put(originalNumber, new BigIntAndFactors(originalNumber));
//		}
//		while(divisions.get(originalNumber).isDivisible(prime)){
//			divisions.get(originalNumber).divideAndStoreFactor(prime, primeIndex);
//		}
//	}
	
	public static ArrayList<BigIntAndFactors> copy(Collection<BigInteger> pVals) {

		ArrayList<BigIntAndFactors> array = new ArrayList<BigIntAndFactors>();
		for (BigInteger p : pVals) {
			array.add(new BigIntAndFactors(p));
		}

		return array;
	}
	
	public static BigInteger getFactor(BigInteger n) throws FactorizationFailure{
		
		TreeMap<BigInteger, BigInteger> pVals = QuadrSieve.genPVals(n, 20 * 1000);
		System.out.println(pVals.size() + " p-values: ");// + pVals);
		TreeMap<BigIntAndFactors, BigInteger> smoothPVals = smoothing(pVals);
		System.out.println(smoothPVals.size() + " smooth numbers: ");// + smoothPVals);
		List<BigIntAndFactors> columns = Arrays.asList(smoothPVals.keySet().toArray(new BigIntAndFactors[]{})); 
		Matrix m = new Matrix(columns);
		List<ArrayList<Integer>> solutions = m.gaussEliminateAndGetSolutions();
		System.out.println("num solutions: " + solutions.size());
		for(int solIndex = 0; solIndex < solutions.size(); solIndex++){
			ArrayList<Integer> solution = solutions.get(solIndex);
			System.out.println("\nsolution" + solIndex + ": " + solution);
			Stream<BigInteger> chosenNumbers = solution.stream().map(i -> columns.get(i).getNumber());
			
//			System.out.println("Product of " + Arrays.toString(chosenNumbers.toArray()) + " is a square");
			
			BigInteger S = BigInteger.ONE;
			BigInteger A = BigInteger.ONE;
			for(BigInteger chosen : (Iterable<BigInteger>)chosenNumbers::iterator){

//				System.out.println("chosen: " + chosen + "  (a = " + pVals.get(chosen) + ")");
				S = S.multiply(chosen);
				A = A.multiply(pVals.get(chosen));	
			}
			System.out.println("S = " + S);
			BigInteger sqrtS = BigInteger.valueOf((long)Math.round(Math.sqrt(S.doubleValue())));
			BigInteger containsFactor1 = A.subtract(sqrtS);
			BigInteger containsFactor2 = A.add(sqrtS);
			System.out.println("contains a factor: " + containsFactor1);
			System.out.println("also contains a factor: " + containsFactor2);
			BigInteger factor1 = Naive.gcd(n, containsFactor1);
			System.out.println("found factor 1: " + factor1);
			BigInteger factor2 = Naive.gcd(n, containsFactor2);
			System.out.println("found factor 2: " + factor2);
			List<BigInteger> nonTrivialFactors = new ArrayList<BigInteger>();
			if(! factor1.equals(BigInteger.ONE) && ! factor1.equals(n)){
				nonTrivialFactors.add(factor1);
			}
			if(! factor2.equals(BigInteger.ONE) && ! factor2.equals(n)){
				nonTrivialFactors.add(factor2);
			}
			if(! nonTrivialFactors.isEmpty()){
				return nonTrivialFactors.stream().max(BigInteger::compareTo).get();
			}
		}
		throw new FactorizationFailure("Couldn't factor " + n);
		
	}
	

}
