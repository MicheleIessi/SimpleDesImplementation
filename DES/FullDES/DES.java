package DES.FullDES;

import Matrices.FullDES.*;
import DES.BasicDESSignature;
import Utilities.AsciiConverter;


public class DES implements BasicDESSignature {

    /** Array che contengono le 'sezioni sinistre/destre' durante la cifratura */
    private String[] leftSection, rightSection;
    private CycleKey cycleKey;
    private Expansion expansion;
    private InitialFinalPermutation initialFinalPermutation;
    private Permutation permutation;
    private Substitution substitution;
    private AsciiConverter asciiConverter;
    private String key;

    public DES(String key) {
        this.leftSection = new String[17];
        this.rightSection = new String[17];
        this.key = key;
        this.cycleKey = new CycleKey();
        this.expansion = new Expansion();
        this.initialFinalPermutation = new InitialFinalPermutation();
        this.permutation = new Permutation();
        this.substitution = new Substitution();
        this.asciiConverter = new AsciiConverter();
    }

    /**
     * Effettua la cifratura del messaggio utilizzando la chiave
     * @param plainText Il messaggio (in chiaro) da cifrare
     * @return Il messaggio cifrato
     */
    @Override
    public String encrypt(String plainText) throws NoSuchFieldException, IllegalAccessException {
        return this.encryptDecrypt(plainText, key, "encrypt");
    }

    /**
     * Effettua la decifratura del messaggio utilizzando la chiave
     * @param cipheredText Il messaggio (criptato) da decifrare
     * @return Il messaggio decifrato
     */
    @Override
    public String decrypt(String cipheredText) throws NoSuchFieldException, IllegalAccessException {
        return this.encryptDecrypt(cipheredText, key, "decrypt");
    }

    /**
     * Effettua una cifratura o una decifratura del messaggio in ingresso utilizzando l'algoritmo DES in modalità ECB
     * con la chiave passata come parametro
     * @param plainText Il messaggio da cifrare/decifrare
     * @param key La chiave da usare per la cifratura/decifratura
     * @param decision L'azione da compiere (encrypt/decrypt)
     * @return Il messaggio cifrato
     */
    @Override
    public String encryptDecrypt(String plainText, String key, String decision) throws NoSuchFieldException, IllegalAccessException {

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
            cycleKey.computeCycleKeys(key);

            // Converto il blocco in 64 bit
            String asciiBlock = asciiConverter.stringToAscii(block);

            // Applico la permutazione PiGreco al blocco
            String permutedBlock = initialFinalPermutation.permute("initial", asciiBlock);

            // Divido il blocco permutato in due sezioni destra e sinistra
            leftSection[0] = permutedBlock.substring(0,permutedBlock.length()/2);
            rightSection[0] = permutedBlock.substring(permutedBlock.length()/2, permutedBlock.length());

            // Inizio con i 16 cicli caratteristici del DES
            for(int j=0; j<16; j++) {

                // Espando la parte destra Client 48 bit tramite la matrice E
                String expandedRight = expansion.expand(rightSection[j]);
                String XORString = "";

                // Se voglio effettuare una encryption, allora prendo le chiavi di ciclo nell'ordine 1->16, altrimenti le prendo nell'ordine contrario
                if(decision.equals("encrypt")) {
                    XORString = XOR(expandedRight, cycleKey.getCycleKeys()[j]);
                }
                else if(decision.equals("decrypt")) {
                    XORString = XOR(expandedRight, cycleKey.getCycleKeys()[15-j]);
                }

                // Effettuo la sostituzione S usando gli S-Boxes, ricavando 32 bit
                String substituteString = substitution.substitute(XORString);

                // Effettuo la permutazione
                String permutedString = permutation.permute(substituteString);

                // Scrivo i valori per Ri e Li
                rightSection[j+1] = XOR(leftSection[j], permutedString);
                leftSection[j+1] = rightSection[j];
            }

            // Alla fine scambio gli ultimi due valori
            String finalString = rightSection[16] + leftSection[16];

            // Applico la permutazione PiGreco^-1
            String repermutedBlock = initialFinalPermutation.permute("final", finalString);

            // Il blocco ora è criptato, lo converto in testo e lo aggiungo in coda al messaggio
            String convertedCipheredText = asciiConverter.asciiToString(repermutedBlock);
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
    private String addPadding(String plain, int mod) {

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
