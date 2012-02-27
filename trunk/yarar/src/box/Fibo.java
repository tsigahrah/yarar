package box;

/**
 * DOC empty type javadoc
 * 
 * @author Dimo Vanchev
 */
public class Fibo {
    private static double gr = (1 + Math.sqrt(5)) / 2;
    private static int[] levels = { 0, 1, 2, 3, 5, 8 };

    private static String[] labels = { ".", "a", "b", "c", "d", "e" };

    // private static int[] levels = { 0, 1, 2, 3, 5, 8, 13, 21, 34, 55, 89, 144, 233, 377, 610,
    // 987,
    // 1597, 2584, 4181, 6765, 10946, 17711, 28657, 46368, 75025, 121393, 196418 };
    //
    // private static String[] labels = { ".", "a", "b", "c", "d", "e", "f", "g", "h", "i", "j",
    // "k",
    // "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z" };

    private static int findLabelIndex(final String c) {
	int x = -1;
	for (int i = Fibo.labels.length - 1; i >= 0; i--) {
	    final String cx = Fibo.labels[i];
	    if (cx.equals(c)) {
		x = i;
		break;
	    }
	}
	return x;
    }

    private static int findLevelIndex(final int c) {
	int x = -1;
	for (int i = Fibo.levels.length - 1; i >= 0; i--) {
	    final int cx = Fibo.levels[i];
	    if (cx <= c) {
		x = i;
		break;
	    }
	}
	return x;
    }

    public static int decrypt(final String s) {
	int x = 0;
	for (int i = 0; i < s.length(); i++) {
	    final char[] aChar = { s.charAt(i) };
	    final String c = new String(aChar);
	    final int labelX = Fibo.findLabelIndex(c);
	    x += Fibo.levels[labelX];
	}
	return x;
    }

    public static String crypt(int c) {
	String s = "";
	if (c == 0) {
	    s = Fibo.labels[0];
	}
	while (c > 0) {
	    final int x = Fibo.findLevelIndex(c);
	    s += Fibo.labels[x];
	    c -= Fibo.levels[x];
	}
	return s;
    }

    /**
     * @param args
     */
    public static void main(final String[] args) {
	System.out.println("welcome to fibo");
	System.out.println(Fibo.crypt(20));
	// System.out.println(Fibo.decrypt("cba"));
	// System.out.println(Fibo.decrypt("sqnliea"));
    }

}
