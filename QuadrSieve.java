import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.TreeMap;
import java.util.stream.Stream;


public class QuadrSieve {

	public static final long SMOOTHNESS = 25 * 1000 * 1000;
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
		Printer.QUADR_SIEVE.println("generating p-values...");
		BigInteger a0 = biggerThanSqrt(n);
		Printer.QUADR_SIEVE.println("a0: " + a0);
//		System.out.println(a0.pow(2));
//		System.out.println(n);
//		System.out.println(a0.pow(2).equals(n));
//		Printer.QUADR_SIEVE.println("first a: " + a0);
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
		double nDouble = n.doubleValue() * 1.1;
		return BigDecimal.valueOf(Math.sqrt(nDouble)).toBigInteger();
	}

	

	public static TreeMap<BigIntAndFactors, BigInteger> smoothing(
			TreeMap<BigInteger, BigInteger> pVals) {
		Printer.QUADR_SIEVE.println("Smoothing...");
		ArrayList<BigIntAndFactors> nonSmooth = copy(pVals.keySet());
		TreeMap<BigIntAndFactors, BigInteger> smooth = new TreeMap<BigIntAndFactors, BigInteger>();

		for (int j = 0; j < nonSmooth.size(); j++) {
			if(j % 1000 == 0){
				Printer.QUADR_SIEVE.print(".");	
			}
			BigIntAndFactors b = nonSmooth.get(j);
			boolean isSmooth = b.computeAndSetFactors(primes);
			if(isSmooth){
				smooth.put(b, pVals.get(b.getNumber()));
			}
		}
		Printer.QUADR_SIEVE.println(".");

		return smooth;
	}
	
	
//	//not correct
//	public static HashMap<BigIntAndFactors, BigInteger> smoothing2(
//			TreeMap<BigInteger, BigInteger> pVals) {
//		BigInteger biggestPVal = pVals.keySet().stream().max(BigInteger::compareTo).get();
//		Printer.QUADR_SIEVE.println("biggest p : " + biggestPVal);
//		HashMap<BigInteger, BigIntAndFactors> composites = new HashMap<BigInteger, BigIntAndFactors>();
//		HashSet<BigInteger> primes = new HashSet<BigInteger>();
//		for(BigInteger i = BigInteger.valueOf(2); i.compareTo(BigInteger.valueOf(SMOOTHNESS)) <= 0; i = i.add(BigInteger.ONE)){
//			if(!composites.containsKey(i)){
//				BigInteger prime = i;
//				Printer.QUADR_SIEVE.println("p " + prime);
//				primes.add(prime);
//				for(BigInteger comp = prime.multiply(BigInteger.valueOf(2)); comp.compareTo(biggestPVal) <= 0; comp = comp.add(prime)){
//					if(comp.divideAndRemainder(BigInteger.valueOf(10000000))[1].equals(BigInteger.ZERO))
//						Printer.QUADR_SIEVE.println(comp);
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
//		Printer.QUADR_SIEVE.println(pVals);
//		
//		for(int primeIndex = 0; primeIndex < primes.size(); primeIndex ++){
//			
//			BigInteger prime = BigInteger.valueOf(primes.get(primeIndex));
//			
//			if(primeIndex % 1 == 0){
//				Printer.QUADR_SIEVE.println("prime " + prime);
//			}
//			
//			BigInteger[] divResult = firstP.divideAndRemainder(prime);
//			BigInteger firstDivisibleP = null;
//			if(divResult[1].equals(BigInteger.ZERO)){
//				firstDivisibleP = firstP;
//				Printer.QUADR_SIEVE.println("firstP =" + firstP);
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
		
		TreeMap<BigInteger, BigInteger> pVals = QuadrSieve.genPVals(n,   500);
		Printer.QUADR_SIEVE.println(pVals.size() + " p-values: ");// + pVals);
		TreeMap<BigIntAndFactors, BigInteger> smoothPVals = smoothing(pVals);
		Printer.QUADR_SIEVE.println(smoothPVals.size() + " smooth numbers: ");// + smoothPVals);
		List<BigIntAndFactors> columns = Arrays.asList(smoothPVals.keySet().toArray(new BigIntAndFactors[]{})); 
		Matrix m = new Matrix(columns);
		List<ArrayList<Integer>> solutions = m.gaussEliminateAndGetSolutions();
		Printer.QUADR_SIEVE.println("num solutions: " + solutions.size());
		for(int solIndex = 0; solIndex < solutions.size(); solIndex++){
			ArrayList<Integer> solution = solutions.get(solIndex);
			Printer.QUADR_SIEVE.println("solution " + solIndex + ": " + solution);
			Stream<BigInteger> chosenNumbers = solution.stream().map(i -> columns.get(i).getNumber());
			
//			Printer.QUADR_SIEVE.println("Product of " + Arrays.toString(chosenNumbers.toArray()) + " is a square");
			
			BigInteger S = BigInteger.ONE;
			BigInteger A = BigInteger.ONE;
			for(BigInteger chosen : (Iterable<BigInteger>)chosenNumbers::iterator){

//				Printer.QUADR_SIEVE.println("chosen: " + chosen + "  (a = " + pVals.get(chosen) + ")");
				S = S.multiply(chosen);
				A = A.multiply(pVals.get(chosen));	
			}
//			Printer.QUADR_SIEVE.println("S = " + S);
			BigInteger sqrtS = BigInteger.valueOf((long)Math.round(Math.sqrt(S.doubleValue())));
			BigInteger containsFactor1 = A.subtract(sqrtS);
			BigInteger containsFactor2 = A.add(sqrtS);
//			Printer.QUADR_SIEVE.println("contains a factor: " + containsFactor1);
//			Printer.QUADR_SIEVE.println("also contains a factor: " + containsFactor2);
			BigInteger factor1 = Naive.gcd(n, containsFactor1);
			BigInteger factor2 = Naive.gcd(n, containsFactor2);	
			List<BigInteger> nonTrivialFactors = new ArrayList<BigInteger>();
			if(! factor1.equals(BigInteger.ONE) && ! factor1.equals(n)){
				nonTrivialFactors.add(factor1);
			}
			if(! factor2.equals(BigInteger.ONE) && ! factor2.equals(n)){
				nonTrivialFactors.add(factor2);
			}
			if(! nonTrivialFactors.isEmpty()){
				Printer.QUADR_SIEVE.println("found factors: " + factor1 + " and " + factor2);
				return nonTrivialFactors.stream().max(BigInteger::compareTo).get();
			}
		}
		throw new FactorizationFailure("Couldn't factor " + n);
		
	}
	

}
