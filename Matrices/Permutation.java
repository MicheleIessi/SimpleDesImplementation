package Matrices;

public class Permutation {

    /** Array che contiene la mappa di scambio dei bit relativi alla funzione di permutazione */
    private static int[] permutation =
            {
                    16, 7, 20, 21, 29, 12, 28, 17, 1, 15, 23, 26, 5, 18, 31, 10,
                    2, 8, 24, 14, 32, 27, 3, 9, 19, 13, 30, 6, 22, 11, 4, 25
            };

    /**
     * Effettua una permutazione (P) per la stringa in ingresso
     * @param substituteString La stringa da permutare
     * @return La stringa permutata
     */
    public static String permute(String substituteString) {

        StringBuilder permutedString = new StringBuilder();
        for (int aPermutation : permutation) {
            permutedString.append(substituteString.charAt(aPermutation - 1));
        }
        return permutedString.toString();
    }
}
