public class Printer {
	private static final boolean verbose = true;

	public static void print(String s) {
		if (verbose)
			System.out.print(s);
	}

	public static void println(String s) {
		if (verbose)
			print(s + "\n");
	}
}
