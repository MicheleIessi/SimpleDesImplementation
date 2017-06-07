import DES.SimplifiedDES.SimplifiedDes;
import DES.SimplifiedDES.DifferentialCryptoanalysis.ThreeRoundsDifferentialCriptoanalysis;

public class Main {

    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException {

        String key = "001111111";
        SimplifiedDes sdes = new SimplifiedDes(3, key);
        ThreeRoundsDifferentialCriptoanalysis trdc = new ThreeRoundsDifferentialCriptoanalysis(3);
        trdc.applyDifferential(sdes);
    }
}
