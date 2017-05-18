package Matrices;

import Utilities.AsciiConverter;

public class CycleKey {

    /** Array che contiene la mappa di scambio dei bit relativi alla prima iterazione */
    private static int[] initialKeyPermutation =
            {
                    57, 49, 41, 33, 25, 17, 9, 1, 58, 50, 42, 34, 26, 18,
                    10, 2, 59, 51, 43, 35, 27, 19, 11, 3, 60, 52, 44, 36,
                    63, 55, 47, 39, 31, 23, 15, 7, 62, 54, 46, 38, 30, 22,
                    14, 6, 61, 53, 45, 37, 29, 21, 13, 5, 28, 20, 12, 4
            };

    /** Array che contiene la mappa di scambio dei bit relativi al calcolo delle chiavi di ciclo */
    private static int[] cycleKeyPermutation =
            {
                    14, 17, 11, 24, 1, 5, 3, 28, 15, 6, 21, 10,
                    23, 19, 12, 4, 26, 8, 16, 7, 27, 20, 13, 2,
                    41, 52, 31, 37, 47, 55, 30, 40, 51, 45, 33, 48,
                    44, 49, 39, 56, 34, 53, 46, 42, 50, 36, 29, 32
            };

    /** Array che contiene le chiavi di rotazione ki calcolate durante l'esecuzione che serviranno per il calcolo delle chiavi di ciclo */
    private static String[] rotatedKeys = new String[17];

    /** Array che contiene le chiavi di ciclo Ki calcolate durante l'esecuzione */
    private static String[] cycleKeys = new String[16];

    /**
     * Calcola le 16 chiavi di ciclo, data una chiave iniziale
     * @param startingKey La chiave di partenza
     */
    public static void computeCycleKeys(String startingKey) {

        String startingAsciiKey = AsciiConverter.stringToAscii(AsciiConverter.addPadding(startingKey));    // 64 bit
        StringBuilder key56 = new StringBuilder();
        for (int anInitialKeyPermutation : initialKeyPermutation) {
            key56.append(startingAsciiKey.charAt((anInitialKeyPermutation) - 1));
        }

        String finalKey = key56.toString();
        rotatedKeys[0] = finalKey;
        for(int i=1; i<=16; i++) {
            rotatedKeys[i] = rotateKey(rotatedKeys[i-1],i);
        }

        for(int i=0; i<16; i++) {

            StringBuilder cycleKey = new StringBuilder();
            for (int aCycleKeyPermutation : cycleKeyPermutation) {
                cycleKey.append(rotatedKeys[i + 1].charAt(aCycleKeyPermutation - 1));
            }
            cycleKeys[i] = cycleKey.toString();
        }

    }

    /**
     * Esegue la rotazione delle chiavi. Le rotazioni vengono effettuate bit a bit verso sinistra, e variano di numero
     * in funzione dell'iterazione corrente della funzione secondo lo schema:
     *
     * <pre>
     *     Iterazione -- Numero rotazioni
     *     ------------------------------
     *     |    1     ->        1       |
     *     |    2     ->        1       |
     *     |    3     ->        2       |
     *     |    4     ->        2       |
     *     |    5     ->        2       |
     *     |    6     ->        2       |
     *     |    7     ->        2       |
     *     |    8     ->        2       |
     *     |    9     ->        1       |
     *     |    10    ->        2       |
     *     |    11    ->        2       |
     *     |    12    ->        2       |
     *     |    13    ->        2       |
     *     |    14    ->        2       |
     *     |    15    ->        2       |
     *     |    16    ->        1       |
     *     ------------------------------
     * </pre>
     * @param asciiKey La stringa binaria su cui effettuare la rotazione
     * @param iteration L'iterazione da considerare per la rotazione
     * @return La stringa binaria ruotata
     */
    private static String rotateKey(String asciiKey, int iteration) {

        String key1 = asciiKey.substring(0, 28);
        String key2 = asciiKey.substring(28, 56);

        // La rotazione dipenda dall'iterazione
        switch (iteration) {
            case 1:
            case 2:
            case 9:
            case 16: {  // Nelle iterazioni 1, 2, 9, 16 ruoto di un bit a sinistra
                key1 = key1.substring(1) + key1.substring(0,1);
                key2 = key2.substring(1) + key2.substring(0,1);
                break;
            }
            default: {  // Nelle altre iterazioni ruoto di due bit a sinistra
                key1 = key1.substring(2) + key1.substring(0,2);
                key2 = key2.substring(2) + key2.substring(0,2);
            }
        }
        return key1 + key2;
    }

    /**
     * Ritorna le chiavi di rotazione
     * @return Array di stringhe binarie contenente le chiavi di rotazione
     */
    public static String[] getRotatedKeys() {
        return rotatedKeys;
    }

    /**
     * Ritorna le chiavi di ciclo
     * @return Array di stringhe binarie contenente le chiavi di ciclo
     */
    public static String[] getCycleKeys() {
        return cycleKeys;
    }
}
