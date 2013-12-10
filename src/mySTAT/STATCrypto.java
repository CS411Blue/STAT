package mySTAT;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.Mac;
import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.security.InvalidKeyException;
import javax.crypto.BadPaddingException;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.nio.charset.Charset;

/**
 *
 * @author Chris S.
 */
public class STATCrypto {
    private String passPhrase;
    private byte[] key;
    
    public void setPassPhrase(String passPhraseStr) {
        passPhrase = passPhraseStr;
        
        // TODO clean up try/catch
        try {
            // TODO replace MD5 with PBKDF2 for KDF
            final MessageDigest md = MessageDigest.getInstance("MD5");
            key = md.digest(passPhrase.getBytes(Charset.forName( "UTF-8" )));
        } catch (NoSuchAlgorithmException e) {
            System.out.println("Error while creating key: " + e);
        }
    }
    
    public boolean isTagCorrect(String tagBase64Str, String messageStr) {
        return getTag(messageStr).equals(tagBase64Str);
    }
    
    public String getTag(String messageStr) {
        
        // TODO clean up try/catch
        try {
            Mac tag = Mac.getInstance("HmacSHA256");
            SecretKeySpec HMACSHA256Key = 
                    new SecretKeySpec(key, "HmacSHA256");
            tag.init(HMACSHA256Key);
            String tagBase64 = DatatypeConverter.printBase64Binary(
                   tag.doFinal(messageStr.getBytes(Charset.forName("UTF-8"))));
            
            return tagBase64;
        
        } catch (NoSuchAlgorithmException e) {
            System.out.println("Error while creating tag:" + e);
            return null;
        } catch (InvalidKeyException e) {
            System.out.println("Bad pass phrase:" + e);
            return null;
        }
    }
    
    public String encrypt(String clearTextStr) {
        
        // TODO clean up try/catch
        try {
            final Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            final SecretKeySpec AESKey = new SecretKeySpec(key, "AES");
            final int blockSize = cipher.getBlockSize();
            final byte[] ivData = new byte[blockSize];
            final SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            random.nextBytes(ivData);
            final IvParameterSpec iv = new IvParameterSpec(ivData);
            
            cipher.init(Cipher.ENCRYPT_MODE, AESKey, iv);
            final byte[] cipherText = cipher.doFinal(
                    clearTextStr.getBytes(Charset.forName( "UTF-8" )));
            
            final byte[] ivWithCipherText = new byte[ivData.length 
                    + cipherText.length];
            System.arraycopy(ivData, 0, ivWithCipherText, 0, blockSize);
            System.arraycopy(cipherText, 0, ivWithCipherText,
                    blockSize, cipherText.length);
            
            final String ivWithCipherTextBase64 = 
                    DatatypeConverter.printBase64Binary(ivWithCipherText);
            
            return ivWithCipherTextBase64;
            
        } catch (InvalidKeyException e) {
            System.out.println("Bad pass phrase:" + e);
            return null;
        } catch (GeneralSecurityException e) {
            System.out.println("Error while encrypting: " + e);
            return null;
        }
    }
    
    public String decrypt(String cipherTextBase64Str) {
        
        // TODO clean up try/catch
        try {
            final Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            final SecretKeySpec AESKey = new SecretKeySpec(key, "AES");
            final int blockSize = cipher.getBlockSize();
            final byte[] ivData = new byte[blockSize];
            
            final byte[] ivWithCipherText = 
                    DatatypeConverter.parseBase64Binary(cipherTextBase64Str);
            System.arraycopy(ivWithCipherText, 0, ivData, 0, blockSize);
            final IvParameterSpec iv = new IvParameterSpec(ivData);
            
            final byte[] cipherText = 
                    new byte[ivWithCipherText.length - blockSize];
            System.arraycopy(ivWithCipherText, blockSize, cipherText, 0, 
                    (ivWithCipherText.length - blockSize));
            cipher.init(Cipher.DECRYPT_MODE, AESKey, iv);
            final String clearText = new String(cipher.doFinal(cipherText));

            return clearText;
            
        } catch (InvalidKeyException e) {
            System.out.println("Bad pass phrase: " + e);
            return null;
        } catch (BadPaddingException e) {
            return null;
        } catch (GeneralSecurityException e) {
            System.out.println("Error while decrypting: " + e);
            return null;
        }
    }
}