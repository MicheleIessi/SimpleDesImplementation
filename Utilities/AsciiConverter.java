package Utilities;


public class AsciiConverter {

    /**
     * Converte una stringa in una stringa binaria. I singoli caratteri vengono convertiti in decimale, in seguito
     * in binario e infine viene loro aggiunto un padding per renderli di lunghezza pari Client 8 bit
     * @param plainString La stringa da convertire in ASCII
     * @return La stringa convertita in ASCII
     */
    public static String stringToAscii(String plainString) {

        StringBuilder binary = new StringBuilder();

        for(char c : plainString.toCharArray()) {
            int ascii = (int) c;
            String asciiChar = addBinaryPadding(Integer.toBinaryString(ascii));
            binary.append(asciiChar);
        }

        return binary.toString();
    }

    /**
     * Converte una stringa binaria in una stringa testuale. Blocchi da 8 bit vengono convertiti singolarmente in caratteri
     * @param asciiString La stringa binaria da convertire in testo
     * @return La stringa testuale appena convertita
     */
    public static String asciiToString(String asciiString) {

        StringBuilder plainString = new StringBuilder();
        int asciiLength = asciiString.length();

        if((asciiLength % 8) == 0) {
            int asciiCharacters = asciiLength / 8;

            for (int i=0; i<asciiCharacters; i++) {
                String asciiCharacter = asciiString.substring(8*i, 8*(i+1));
                plainString.append((char) Integer.parseInt(asciiCharacter, 2));
            }

            return plainString.toString();
        }
        else {
            System.out.println("La ASCII String non è formattata correttamente");
        }
        return "";
    }

    /**
     * Aggiunge un padding in coda alla stringa. Il carattere scelto per il padding è lo spazio vuoto " ". Vengono
     * aggiunti tanti caratteri quanti sono necessari per far sì che la stringa abbia una lunghezza pari ad 8 caratteri.
     * @param key La stringa alla quale aggiungere il padding
     * @return La nuova stringa con il padding appena aggiunto
     */
    public static String addPadding(String key) {

        StringBuilder sb = new StringBuilder(key);
        while(sb.length() < 8) {
            sb.append(" ");
        }

        return sb.toString();
    }

    /**
     * Aggiunge un padding in testa ad una stringa binaria. Il carattere scelto per il padding è lo zero "0". Vengono
     * aggiunti tanti caratteri quanti sono necessari per far sì che la stringa abbia una lunghezza pari ad 8 caratteri.
     * @param asciiChar La stringa binaria alla quale aggiungere il padding
     * @return La nuova stringa binaria con il padding appena aggiunto
     */
    private static String addBinaryPadding(String asciiChar) {
        StringBuilder binaryPaddedAscii = new StringBuilder();
        while(binaryPaddedAscii.length() < (8-asciiChar.length())) {
            binaryPaddedAscii.append("0");
        }
        binaryPaddedAscii.append(asciiChar);
        return binaryPaddedAscii.toString();
    }

}
