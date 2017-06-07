package DES;

/**
 * Created by Michele Iessi on 06/06/17.
 */
public interface BasicDESSignature {


    public String decrypt(String plainText) throws NoSuchFieldException, IllegalAccessException;
    public String encrypt(String cipheredText) throws NoSuchFieldException, IllegalAccessException;
    public String encryptDecrypt(String plainText, String key, String decision) throws NoSuchFieldException, IllegalAccessException;

}
