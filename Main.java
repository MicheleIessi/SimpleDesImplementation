import DES.DES;


public class Main {

    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException {

        String message = "ciao Michele";
        String key = "ciao123";

        String encryptedString = DES.encrypt(message, key);
        System.out.println("messaggio criptato: " + encryptedString);

        String decryptedString = DES.decrypt(encryptedString, key);
        System.out.println("messaggio decriptato: " + decryptedString);
    }
}