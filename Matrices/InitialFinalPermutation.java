package Matrices;

import java.lang.reflect.Field;

public class InitialFinalPermutation {

    /** Array che contiene la mappa di scambio dei bit relativi alla funzione di permutazione iniziale */
    private static int[] initialPi =
            {
                    58, 50, 42, 34, 26, 18, 10, 2, 60, 52, 44, 36, 28, 20, 12, 4,
                    62, 54, 46, 38, 30, 22, 14, 6, 64, 56, 48, 40, 32, 24, 16, 8,
                    57, 49, 41, 33, 25, 17, 9, 1, 59, 51, 43, 35, 27, 19, 11, 3,
                    61, 53, 45, 37, 29, 21, 13, 5, 63, 55, 47, 39, 31, 23, 15, 7
            };

    /** Array che contiene la mappa di scambio dei bit relativi alla funzione di permutazione finale */
    private static int[] finalPi =
            {
                    40, 8, 48, 16, 56, 24, 64, 32, 39, 7, 47, 15, 55, 23, 63, 31,
                    38, 6, 46, 14, 54, 22, 62, 30, 37, 5, 45, 13, 53, 21, 61, 29,
                    36, 4, 44, 12, 52, 20, 60, 28, 35, 3, 43, 11, 51, 19, 59, 27,
                    34, 2, 42, 10, 50, 18, 58, 26, 33, 1, 41, 9, 49, 17, 57, 25
            };

    /**
     * Ritorna l'array di permutazione iniziale o finale
     * @param permutation String (initial o final)
     * @return initialPi o finalPi
     */
    private static int[] getPermutation(String permutation) throws NoSuchFieldException, IllegalAccessException {

        String attributeName = permutation + "Pi";
        Field f = InitialFinalPermutation.class.getDeclaredField(attributeName);
        f.setAccessible(true);
        return (int[]) f.get(InitialFinalPermutation.class);
    }

    /**
     * Effettua una Permutazione Iniziale (PI) o finale (PI^-1) al blocco in ingresso
     * @param type Il tipo di permutazione da applicare (initial/final)
     * @param asciiBlock Stringa binaria rappresentante il blocco da 64 bit da permutare
     * @return Stringa binaria che rappresenta il blocco da 64 bit permutato
     */
    public static String permute(String type, String asciiBlock) throws NoSuchFieldException, IllegalAccessException {
        //System.out.println("Applico una permutazione PI o PI-1");
        StringBuilder permutedBlock = new StringBuilder();
        int[] permutation = getPermutation(type);
        for(int i=0; i<64; i++) {
            permutedBlock.append(asciiBlock.charAt(permutation[i]-1));
        }
        return permutedBlock.toString();
    }

}
