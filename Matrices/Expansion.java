package Matrices;

public class Expansion {

    /** Array che contiene la mappa di scambio dei bit relativi alla funzione di espansione */
    private static int[] expansion =
            {
                    32, 1, 2, 3, 4, 5, 4, 5, 6, 7, 8, 9,
                    8, 9, 10, 11, 12, 13, 12, 13, 14, 15, 16, 17,
                    16, 17, 18, 19, 20, 21, 20, 21, 22, 23, 24, 25,
                    24, 25, 26, 27, 28, 29, 28, 29, 30, 31, 32, 1
            };

    /**
     * Effettua una espansione (E) per la stringa in ingresso
     * @param rightSect La stringa per la quale effettuare l'espansione
     * @return La stringa su cui è stata effettuata l'espansione
     */
    public static String expand(String rightSect) {

        StringBuilder expandedString = new StringBuilder();

        for (int anExpansion : expansion) {
            expandedString.append(rightSect.charAt(anExpansion - 1));
        }
        //System.out.println("Risultato espansione E: " + expandedString.toString());
        return expandedString.toString();
    }
}
