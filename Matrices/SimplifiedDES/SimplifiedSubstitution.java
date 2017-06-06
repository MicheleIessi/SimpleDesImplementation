package Matrices.SimplifiedDES;

import java.lang.reflect.Field;

/**
 * Created by Michele Iessi on 06/06/17.
 */

public class SimplifiedSubstitution {

    private String[] XORStringSections;

    private static int[][] S1 =
            {
                    {5, 2, 1, 6, 3, 4, 7, 0},
                    {1, 4, 6, 2, 0, 7, 5, 3}
            };

    private static int[][] S2 =
            {
                    {4, 0, 6, 5, 7, 1, 3, 2},
                    {5, 3, 0, 7, 6, 2, 1, 4}
            };



    public SimplifiedSubstitution() {
        this.XORStringSections = new String[3];
    }

    /**
     *
     * @param n
     * @return int[][]
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    private int[][] getS(int n) throws NoSuchFieldException, IllegalAccessException {
        String attributeName = 'S' + String.valueOf(n);
        Field f = SimplifiedSubstitution.class.getDeclaredField(attributeName);
        f.setAccessible(true);
        return (int[][]) f.get(SimplifiedSubstitution.class);
    }

    public String substitute(String XORString) throws NoSuchFieldException, IllegalAccessException {

        StringBuilder substituteString = new StringBuilder();

        // Divido in 2 blocchi da 4 bit
        for(int i=0; i<2; i++) {

            XORStringSections[i] = XORString.substring(4*i,4*(i+1));

            String b1 = String.valueOf(XORStringSections[i].charAt(0));
            String otherBits = String.valueOf(XORStringSections[i].charAt(1)) + String.valueOf(XORStringSections[i].charAt(2)) + String.valueOf(XORStringSections[i].charAt(3));

            int row = Integer.parseInt(b1, 2);
            int column = Integer.parseInt(otherBits, 2);

            int value = getS(i+1)[row][column];

            // Aggiungo un padding per rendere la stringa lunga 3 caratteri, se necessario
            String valueString = addPadding(Integer.toBinaryString(value));

            substituteString.append(valueString);
        }


        return substituteString.toString();
    }

    private String addPadding(String plain) {

        StringBuilder sb = new StringBuilder();
        for(int i=0; i<3-plain.length(); i++) {
            sb.append("0");
        }
        sb.append(plain);
        return sb.toString();
    }

}






















