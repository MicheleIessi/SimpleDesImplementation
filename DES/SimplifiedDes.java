package DES;

import Matrices.SimplifiedDES.SimplifiedCycleKey;
import Matrices.SimplifiedDES.SimplifiedExpansion;
import Matrices.SimplifiedDES.SimplifiedSubstitution;
import Utilities.AsciiConverter;


/**
 * Created by Michele Iessi on 06/06/17.
 */
public class SimplifiedDes implements BasicDESSignature {

    private SimplifiedCycleKey simplifiedCycleKey;
    private SimplifiedExpansion simplifiedExpansion;
    private SimplifiedSubstitution simplifiedSubstitution;
    private AsciiConverter asciiConverter;
    private String[] leftSection;
    private String[] rightSection;
    private int ITERATIONS;

    public SimplifiedDes(int iterations) {

        this.ITERATIONS = iterations;
        this.asciiConverter = new AsciiConverter();

        this.simplifiedExpansion = new SimplifiedExpansion();
        this.simplifiedSubstitution = new SimplifiedSubstitution();
        this.simplifiedCycleKey = new SimplifiedCycleKey(ITERATIONS);
        this.leftSection = new String[ITERATIONS+1];
        this.rightSection = new String[ITERATIONS+1];
    }

    /**
     * Effettua la cifratura del messaggio utilizzando la chiave
     * @param plainText Il messaggio (in chiaro) da cifrare
     * @param key La chiave con cui cifrare il messaggio
     * @return Il messaggio cifrato
     */
    @Override
    public String encrypt(String plainText, String key) throws NoSuchFieldException, IllegalAccessException {
        return this.encryptDecrypt(plainText, key, "encrypt");
    }

    /**
     * Effettua la decifratura del messaggio utilizzando la chiave
     * @param cipheredText Il messaggio (criptato) da decifrare
     * @param key La chiave con cui decifrare il messaggio
     * @return Il messaggio decifrato
     */
    @Override
    public String decrypt(String cipheredText, String key) throws NoSuchFieldException, IllegalAccessException {
        return this.encryptDecrypt(cipheredText, key, "decrypt");
    }

    /**
     * Cifra o decifra il messaggio passato come parametro con la chiave desiderata
     * @param plainText Messaggio in chiaro da cifrare/decifrare
     * @param key Chiave binaria di cifratura/decifratura composta da 9 bit
     * @param decision Variabile usata per scegliere se cifrare o decifrare
     * @return Il testo cifrato/decifrato con la chiave passata come parametro
     */
    @Override
    public String encryptDecrypt(String plainText, String key, String decision) throws NoSuchFieldException, IllegalAccessException {

        if(key.length() != 9) {
            System.err.println("Errore, la chiave deve essere di 8 bit");
            System.exit(1);
        }

        StringBuilder encryptedAsciiText = new StringBuilder();
        int modulo = plainText.length() % 3;    // blocchi da 24 bit
        // Se il testo non è un multiplo di 24 bit (3 caratteri) aggiungo un padding
        // L'ho fatto di 24 bit perché il DES semplificato sul Trappe usa blocchi di 12 bit, e 24 è il più
        // piccolo multiplo di 12 che posso creare moltiplicando un numero per 8
        if(modulo != 0) {
            plainText = addPadding(plainText, modulo);
        }

        String asciiText = asciiConverter.stringToAscii(plainText); // divisibile per 12
        int blockNumber = asciiText.length() / 12;

        for(int i=0; i<blockNumber; i++) {

            String block = asciiText.substring(12*i, 12*(i+1));

            // Con il DES semplificato del Trappe non applichiamo la permutazione iniziale
            simplifiedCycleKey.computeCycleKeys(key);


            leftSection[0] = block.substring(0,6);
            rightSection[0] = block.substring(6, 12);

            for(int j=0; j<ITERATIONS; j++) {

                String expandedRight = simplifiedExpansion.expand(rightSection[j]);

                String XORString = "";

                if(decision.equals("encrypt")) {
                    XORString = XOR(expandedRight, simplifiedCycleKey.getCycleKeys()[j]);
                    System.out.println("encrypt con chiave " + simplifiedCycleKey.getCycleKeys()[j]);
                }
                else if(decision.equals("decrypt")) {
                    XORString = XOR(expandedRight, simplifiedCycleKey.getCycleKeys()[ITERATIONS-j-1]);
                    System.out.println("decrypt con chiave " + simplifiedCycleKey.getCycleKeys()[ITERATIONS-j-1]);
                }
                String substituteString = simplifiedSubstitution.substitute(XORString);

                rightSection[j+1] = XOR(leftSection[j], substituteString);
                leftSection[j+1] = rightSection[j];

            }

            String finalString = rightSection[ITERATIONS] + leftSection[ITERATIONS];

            //System.out.println(finalString);
            encryptedAsciiText.append(finalString);
        }

        String encryptedString = asciiConverter.asciiToString(encryptedAsciiText.toString());
        return encryptedString;

    }

    private String addPadding(String plain, int mod) {

        StringBuilder sb = new StringBuilder(plain);
        String zeroWidthSpace = " ";
        mod = 3-mod;
        for(int i=0; i<mod; i++) {
            sb.append(zeroWidthSpace);
        }

        return sb.toString();
    }

    /**
     * Effettua un OR esclusivo (XOR) tra due stringhe binarie
     * @param expandedRight La prima stringa binaria
     * @param key La seconda stringa binaria
     * @return Il risultato dell'OR esclusivo effettuato carattere per carattere tra le due stringhe
     */
    private String XOR(String expandedRight, String key) {

        StringBuilder XORString = new StringBuilder();

        for(int i=0; i<expandedRight.length(); i++) {
            if(expandedRight.charAt(i) == key.charAt(i)) {
                XORString.append("0");
            }
            else {
                XORString.append("1");
            }
        }

        return XORString.toString();
    }


}
