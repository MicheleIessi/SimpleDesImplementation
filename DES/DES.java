package DES;

import Matrices.*;
import Utilities.AsciiConverter;


public class DES {

    /** Array che contiene le 'sezioni sinistre' durante la cifratura */
    private static String[] leftSection = new String[17];
    /** Array che contiene le 'sezioni destre' durante la cifratura */
    private static String[] rightSection = new String[17];

    /**
     * Effettua la cifratura del messaggio utilizzando la chiave
     * @param plainText Il messaggio (in chiaro) da cifrare
     * @param key La chiave con cui cifrare il messaggio
     * @return Il messaggio cifrato
     */
    public static String encrypt(String plainText, String key) throws NoSuchFieldException, IllegalAccessException {
        return encryptDecrypt(plainText, key, "encrypt");
    }

    /**
     * Effettua la decifratura del messaggio utilizzando la chiave
     * @param cipheredText Il messaggio (criptato) da decifrare
     * @param key La chiave con cui decifrare il messaggio
     * @return Il messaggio decifrato
     */
    public static String decrypt(String cipheredText, String key) throws NoSuchFieldException, IllegalAccessException {
        return encryptDecrypt(cipheredText, key, "decrypt");
    }

    /**
     * Effettua una cifratura o una decifratura del messaggio in ingresso utilizzando l'algoritmo DES in modalità ECB
     * con la chiave passata come parametro
     * @param plainText Il messaggio da cifrare/decifrare
     * @param key La chiave da usare per la cifratura/decifratura
     * @param decision L'azione da compiere (encrypt/decrypt)
     * @return Il messaggio cifrato
     */
    private static String encryptDecrypt(String plainText, String key, String decision) throws NoSuchFieldException, IllegalAccessException {

        if(key.length() > 8) {
            System.err.println("Errore, la chiave è più lunga di 8 caratteri");
            System.exit(1);
        }

        StringBuilder encryptedText = new StringBuilder();
        int modulo = plainText.length() % 8;

        // Se il testo non è un multiplo di 64 bit (caratteri) aggiungo un padding
        if(modulo != 0) {
            plainText = addPadding(plainText, modulo);
        }

        int blockNumber = plainText.length() / 8;        // blocchi da 64 bit (8*8)

        for(int i=0; i<blockNumber; i++) {

            // Ricavo il blocco da 64 bit (8 caratteri)
            String block = plainText.substring(8*i, 8*(i+1));

            // Calcolo le chiavi di ciclo per il blocco in esame
            CycleKey.computeCycleKeys(key);

            // Converto il blocco in 64 bit
            String asciiBlock = AsciiConverter.stringToAscii(block);

            // Applico la permutazione PiGreco al blocco
            String permutedBlock = InitialFinalPermutation.permute("initial", asciiBlock);

            // Divido il blocco permutato in due sezioni destra e sinistra
            leftSection[0] = permutedBlock.substring(0,permutedBlock.length()/2);
            rightSection[0] = permutedBlock.substring(permutedBlock.length()/2, permutedBlock.length());

            // Inizio con i 16 cicli caratteristici del DES
            for(int j=0; j<16; j++) {

                // Espando la parte destra a 48 bit tramite la matrice E
                String expandedRight = Expansion.expand(rightSection[j]);
                String XORString = "";

                // Se voglio effettuare una encryption, allora prendo le chiavi di ciclo nell'ordine 1->16, altrimenti le prendo nell'ordine contrario
                if(decision.equals("encrypt")) {
                    XORString = XOR(expandedRight, CycleKey.getCycleKeys()[j]);
                }
                else if(decision.equals("decrypt")) {
                    XORString = XOR(expandedRight, CycleKey.getCycleKeys()[15-j]);
                }

                // Effettuo la sostituzione S usando gli S-Boxes, ricavando 32 bit
                String substituteString = Substitution.substitute(XORString);

                // Effettuo la permutazione
                String permutedString = Permutation.permute(substituteString);

                // Scrivo i valori per Ri e Li
                rightSection[j+1] = XOR(leftSection[j], permutedString);
                leftSection[j+1] = rightSection[j];
            }

            // Alla fine scambio gli ultimi due valori
            String finalString = rightSection[16] + leftSection[16];

            // Applico la permutazione PiGreco^-1
            String repermutedBlock = InitialFinalPermutation.permute("final", finalString);

            // Il blocco ora è criptato, lo converto in testo e lo aggiungo in coda al messaggio
            String convertedCipheredText = AsciiConverter.asciiToString(repermutedBlock);
            encryptedText.append(convertedCipheredText);
        }

        return encryptedText.toString();
    }

    /**
     * Aggiunge degli spazi vuoti in coda alla stringa
     * @param plain La stringa alla quale bisogna aggiungere spazi vuoti in coda
     * @param mod Il numero di spazi vuoti da inserire
     * @return La nuova stringa con il padding appena aggiunto
     */
    private static String addPadding(String plain, int mod) {

        StringBuilder sb = new StringBuilder(plain);
        String zeroWidthSpace = " ";

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
    private static String XOR(String expandedRight, String key) {

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
