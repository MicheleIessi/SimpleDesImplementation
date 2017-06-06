import DES.DES;
import DES.SimplifiedDes;
import DES.TripleDES;
import Matrices.SimplifiedDES.SimplifiedCycleKey;
import Utilities.AsciiConverter;

public class Main {

    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException {

//        String message = "ciao Gaetano";
//        String key = "ciao1234";
//        String key2 = "1234ciao";
//        AsciiConverter asciiConverter = new AsciiConverter();
//        DES des = new DES();
//        TripleDES tripleDES = new TripleDES();
//
//
//        String encryptedString = des.encrypt(message, key);
//        System.out.println("messaggio criptato con DES: " + encryptedString);
//
//        String decryptedString = des.decrypt(encryptedString, key);
//        System.out.println("messaggio decriptato con DES: " + decryptedString);
//
//        encryptedString = tripleDES.encrypt(message, key, key2);
//        System.out.println("messaggio criptato con TripleDES: " + encryptedString);
//
//        decryptedString = tripleDES.decrypt(encryptedString, key, key2);
//        System.out.println("messaggio decriptato con TripleDES: " + decryptedString);


        String key = "010101101";
        String text = "ciao Gaetano sono un des semplificato!";
        SimplifiedDes sdes = new SimplifiedDes(3);

        String cipher = sdes.encrypt(text, key);

        System.out.println(cipher);

        String decipher = sdes.decrypt(cipher,key);
        System.out.println(decipher);
    }
}
