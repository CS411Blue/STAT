package mySTAT;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;

/**
 *
 * @author Chris S. <chris.spillers@cspillers.com>
 */
public class STATCrypto {
    private String passPhrase;
    private byte[] key;

    public STATCrypto(String passPhrase) {
        setPassPhrase(passPhrase);
    }
    
    public void setPassPhrase(String passPhrase) {
        this.passPhrase = passPhrase;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            key = md.digest(this.passPhrase.getBytes("UTF-8"));
        }
        catch (Exception e) {
            System.out.println("Error while creating key: " + e);
        }
    }
    
    public String getPassPhrase() {
        return passPhrase;
    }
    
    public String getKey() {
        return DatatypeConverter.printHexBinary(key);
    }
    
    public String encrypt(String clearText) {
        String cipherText;
        
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            final SecretKeySpec AESKey = new SecretKeySpec(key, "AES");
            cipher.init(Cipher.ENCRYPT_MODE, AESKey);
            cipherText = DatatypeConverter.printBase64Binary(
                    cipher.doFinal(clearText.getBytes("UTF-8")));
            return cipherText;
        }
        catch (Exception e) {
            System.out.println("Error while encrypting: " + e);
        }
        return null;
    }
    
    public String decrypt(String cipherText) {
        String clearText;
        
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            final SecretKeySpec AESKey = new SecretKeySpec(key, "AES");
            cipher.init(Cipher.DECRYPT_MODE, AESKey);
            clearText = new String(cipher.doFinal(
                    DatatypeConverter.parseBase64Binary(cipherText)));
            return clearText;
        }
        catch (Exception e) {
            System.out.println("Error while decrypting: " + e);
        }
        return null;
    }
}
