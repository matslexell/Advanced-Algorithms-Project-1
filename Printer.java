import java.util.HashMap;
import java.util.Map;

public enum Printer {
		MAIN("MAIN"),
		MATRIX("MATRIX"),
		NAIVE("NAIVE"),
		POLLARD_RHO("POLLARD_RHO"),
		QUADR_SIEVE("QUADR_SIEVE");
		
		
	String printerType;
	Printer(String type){
		this.printerType = type;
	}
	
	private static final Map<String, Boolean> VERBOSE = new HashMap<String, Boolean>();
	static{
		VERBOSE.put("MAIN", true);
		VERBOSE.put("MATRIX", false);
		VERBOSE.put("NAIVE", false);
		VERBOSE.put("POLLARD_RHO", false);
		VERBOSE.put("QUADR_SIEVE", false);
	}

	public void print(Object msg) {
		if (VERBOSE.get(printerType))
			System.out.print(msg);
	}

	public void println(Object msg) {
		if (VERBOSE.get(printerType))
			System.out.println(msg);
	}
}
