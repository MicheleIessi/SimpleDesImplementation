package DES;

/**
 * Created by Michele Iessi on 06/06/17.
 */
public interface BasicDESSignature {


    public String decrypt(String plainText, String key) throws NoSuchFieldException, IllegalAccessException;
    public String encrypt(String cipheredText, String key) throws NoSuchFieldException, IllegalAccessException;
    public String encryptDecrypt(String plainText, String key, String decision) throws NoSuchFieldException, IllegalAccessException;

}
