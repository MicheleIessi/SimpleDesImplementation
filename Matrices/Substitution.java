package Matrices;

import java.lang.reflect.Field;

public class Substitution {

    /** Array contenente 8 stringhe binarie da 6 bit su cui effettuare le Sostituzioni */
    private static String[] XORStringSections = new String[8];

    /** S-Boxes */
    private static int[][] S1 =
            {
                    {14, 4, 13, 1, 2, 15, 11, 8, 3, 10, 6, 12, 5, 9, 0, 7},
                    {0, 15, 7, 4, 14, 2, 13, 1, 10, 6, 12, 11, 9, 5, 3, 8},
                    {4, 1, 14, 8, 13, 6, 2, 11, 15, 12, 9, 7, 3, 10, 5, 0},
                    {15, 12, 8, 2, 4, 9, 1, 7, 5, 11, 3, 14, 10, 0, 6, 13}
            };

    private static int[][] S2 =
            {
                    {15, 1, 8, 14, 6, 11, 3, 4, 9, 7, 2, 13, 12, 0, 5, 10},
                    {3, 13, 4, 7, 15, 2, 8, 14, 12, 0, 1, 10, 6, 9, 11, 5},
                    {0, 14, 7, 11, 10, 4, 13, 1, 5, 8, 12, 6, 9, 3, 2, 15},
                    {13, 8, 10, 1, 3, 15, 4, 2, 11, 6, 7, 12, 0, 5, 14, 9}
            };

    private static int[][] S3 =
            {
                    {10, 0, 9, 14, 6, 3, 15, 5, 1, 13, 12, 7, 11, 4, 2, 8},
                    {13, 7, 0, 9, 3, 4, 6, 10, 2, 8, 5, 14, 12, 11, 15, 1},
                    {13, 6, 4, 9, 8, 15, 3, 0, 11, 1, 2, 12, 5, 10, 14, 7},
                    {1, 10, 13, 0, 6, 9, 8, 7, 4, 15, 14, 3, 11, 5, 2, 12}
            };

    private static int[][] S4 =
            {
                    {7, 13, 14, 3, 0, 6, 9, 10, 1, 2, 8, 5, 11, 12, 4, 15},
                    {13, 8, 11, 5, 6, 15, 0, 3, 4, 7, 2, 12, 1, 10, 14, 9},
                    {10, 6, 9, 0, 12, 11, 7, 13, 15, 1, 3, 14, 5, 2, 8, 4},
                    {3, 15, 0, 6, 10, 1, 13, 8, 9, 4, 5, 11, 12, 7, 2, 14}
            };

    private static int[][] S5 =
            {
                    {2, 12, 4, 1, 7, 10, 11, 6, 8, 5, 3, 15, 13, 0, 14, 9},
                    {14, 11, 2, 12, 4, 7, 13, 1, 5, 0, 15, 10, 3, 9, 8, 6},
                    {4, 2, 1, 11, 10, 13, 7, 8, 15, 9, 12, 5, 6, 3, 0, 14},
                    {11, 8, 12, 7, 1, 14, 2, 13, 6, 15, 0, 9, 10, 4, 5, 3}
            };

    private static int[][] S6 =
            {
                    {12, 1, 10, 15, 9, 2, 6, 8, 0, 13, 3, 4, 14, 7, 5, 11},
                    {10, 15, 4, 2, 7, 12, 9, 5, 6, 1, 13, 14, 0, 11, 3, 8},
                    {9, 14, 15, 5, 2, 8, 12, 3, 7, 0, 4, 10, 1, 13, 11, 6},
                    {4, 3, 2, 12, 9, 5, 15, 10, 11, 14, 1, 7, 6, 0, 8, 13}
            };

    private static int[][] S7 =
            {
                    {4, 11, 2, 14, 15, 0, 8, 13, 3, 12, 9, 7, 5, 10, 6, 1},
                    {13, 0, 11, 7, 4, 9, 1, 10, 14, 3, 5, 12, 2, 15, 8, 6},
                    {1, 4, 11, 13, 12, 3, 7, 14, 10, 15, 6, 8, 0, 5, 9, 2},
                    {6, 11, 13, 8, 1, 4, 10, 7, 9, 5, 0, 15, 14, 2, 3, 12}
            };

    private static int[][] S8 =
            {
                    {13, 2, 8, 4, 6, 15, 11, 1, 10, 9, 3, 14, 5, 0, 12, 7},
                    {1, 15, 13, 8, 10, 3, 7, 4, 12, 5, 6, 11, 0, 14, 9, 2},
                    {7, 11, 4, 1, 9, 12, 14, 2, 0, 6, 10, 13, 15, 3, 5, 8},
                    {2, 1, 14, 7, 4, 10, 8, 13, 15, 12, 9, 0, 3, 5, 6, 11}
            };

    /**
     * Ritorna l'S-Box desiderato
     * @param n L'S-Box da restituire
     * @return L'S-Box desiderato
     */
    private static int[][] getS(int n) throws NoSuchFieldException, IllegalAccessException {
        String attributeName = 'S' + String.valueOf(n);
        Field f = Substitution.class.getDeclaredField(attributeName);
        f.setAccessible(true);
        return (int[][]) f.get(Substitution.class);
    }

    /**
     * Effettua l'operazione di Sostituzione (S) su una stringa binaria di 48 bit
     * @param XORString La stringa binaria di 48 bit su cui effettuare le sostituzioni
     * @return Una stringa binaria da 32 bit risultato delle sostituzioni
     */
    public static String substitute(String XORString) throws NoSuchFieldException, IllegalAccessException {

        StringBuilder substituteString = new StringBuilder();

        // Separo la XORString (lunga 48 bit) in 8 sezioni da 6 bit ciascuna

        for(int i=0; i<8; i++) {
            XORStringSections[i] = XORString.substring(6*i, 6*(i+1));
        }
        // Applico le sostituzioni utilizzando per ogni sezione l'S-Box corrispondente
        for(int i=0; i<8; i++) {

            String b1b6 = String.valueOf(XORStringSections[i].charAt(0)) + String.valueOf(XORStringSections[i].charAt(5));
            String middlebs = String.valueOf(XORStringSections[i].charAt(1)) + String.valueOf(XORStringSections[i].charAt(2)) +
                    String.valueOf(XORStringSections[i].charAt(3)) + String.valueOf(XORStringSections[i].charAt(4));

            // Ricavo gli interi che rappresentano la riga e la colonna dell'S-Box
            int row = Integer.parseInt(b1b6,2);
            int column = Integer.parseInt(middlebs, 2);

            // Ricavo il valore dall'S-Box
            int value = getS(i+1)[row][column];

            // Aggiungo un padding per rendere la stringa lunga 4 caratteri, se necessario
            String valueString = addPadding(Integer.toBinaryString(value));

            // Aggiungo la stringa al StringBuilder
            substituteString.append(valueString);
        }

        return substituteString.toString();
    }


    /**
     * Aggiunge un padding per rendere la stringa binaria in ingresso lunga almeno 4 bit. Il carattere selezionato per
     * il padding è il carattere zero "0". Il padding viene aggiunto in testa alla stringa, se necessario.
     * @param plain La stringa binaria alla quale aggiungere il padding
     * @return La stringa binaria lunga 4 bit, compreso il padding appena effettuato
     */
    private static String addPadding(String plain) {

        StringBuilder sb = new StringBuilder();
            for(int i=0; i<4-plain.length(); i++) {
            sb.append("0");
        }
        sb.append(plain);
        return sb.toString();
    }

}



