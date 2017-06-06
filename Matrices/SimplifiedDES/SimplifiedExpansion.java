package Matrices.SimplifiedDES;

/**
 * Created by Michele Iessi on 06/06/17.
 */
public class SimplifiedExpansion {


    private static int[] expansion =
            {
                    0, 1, 3, 2, 3, 2, 4, 5
            };

    public String expand(String rightSect) {

        StringBuilder expandedString = new StringBuilder();


        for (int anExpansion : expansion) {
            expandedString.append(rightSect.charAt(anExpansion));
        }

        return expandedString.toString();
    }
}
