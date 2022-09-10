package leo.werwolf.utils;

public class MathUtils {
	public static int levenshtein(String token1, String token2) {
		int[] levi = new int[token2.length() + 1];
		int[] levi_1 = new int[token2.length() + 1];

		for (int j = 0; j <= token2.length(); j++)
			levi_1[j] = j;

		for (int i = 1; i <= token1.length(); i++) {
			levi[0] = i;
			for (int j = 1; j <= token2.length(); j++) {
				levi[j] = Math.min(Math.min(levi_1[j] + 1, levi[j - 1] + 1),
						levi_1[j - 1] + indicator(token1.charAt(i - 1), token2.charAt(j - 1)));
			}

			System.arraycopy(levi, 0, levi_1, 0, levi.length);
		}

		return levi[token2.length()];
	}

	private static int indicator(char c, char d) {
		return (c == d) ? 0 : 1;
	}
}
