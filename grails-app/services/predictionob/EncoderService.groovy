package predictionob

import grails.transaction.Transactional
import java.security.MessageDigest
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec; 
import java.security.InvalidKeyException;
import java.security.Signature
import java.security.KeyFactory
import java.security.spec.PKCS8EncodedKeySpec
import java.security.interfaces.RSAPrivateKey
import java.nio.charset.Charset
import org.apache.commons.codec.binary.Base64;
import java.security.KeyPair
import java.security.spec.EncodedKeySpec;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.KeyStore
import groovy.io.FileType

@Transactional
class EncoderService {

	//to encode base64Url we need to remove URL unsafe characters from base64 encoding
    def static encodeBase64URL(String toEncode) {
    	//return toEncode.bytes.encodeBase64().toString().replace('-','+').replace('/','_').replace('==','').replace(' ','')
        Base64.encodeBase64URLSafeString(toEncode.trim().replace('-','+').replace('/','_').replace('==','').replace(' ','').getBytes("UTF-8"));
    }

	//encodeHash256 from http://stackoverflow.com/questions/13419201/why-are-the-rsa-sha256-signatures-i-generate-with-openssl-and-java-different
    def static encodeHash256(String privateKeystr, String toEncode){
    	try {  

/*    		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            EncodedKeySpec privKeySpec = new PKCS8EncodedKeySpec(privateKeystr.getBytes("UTF-8").decodeBase64());

            PrivateKey privateKey = keyFactory.generatePrivate(privKeySpec);
    		Signature signature = Signature.getInstance("SHA256withRSA");
          signature.initSign(privateKey);
	        signature.update(toEncode.getBytes("UTF-8"));
	        byte[] signatureBytes = signature.sign();

    	  	FileOutputStream fos = new FileOutputStream("signature-java");
	        fos.write(signatureBytes);
	        fos.close();

    		return fos.toString()
*/

            KeyStore ks = KeyStore.getInstance("PKCS12");

            // In testing, point this at the key file downloaded from google
            FileInputStream fis = new FileInputStream("/Users/bmontaner/Documents/SAAS/predictorOB/grails-app/resources/My Project-6269d3aa75f0.p12");
            ks.load(fis, "notasecret".toCharArray());

            // get my private key from the kestore (.p12 file)
            PrivateKey key = ks.getKey("privatekey", "notasecret".toCharArray());


            Signature signature = Signature.getInstance("SHA256withRSA");
            signature.initSign(key);
            signature.update(toEncode.getBytes());
            byte[] signatureBytes = signature.sign();

            // encode signature
            String signature_string = Base64.encodeBase64URLSafeString(signatureBytes)

            // Debug - display request
            println("req: ${toEncode}.${signature_string}");
            return signature_string


	 	} catch (InvalidKeyException e) { 
	 		throw new RuntimeException("Invalid key exception while converting to RSA SHA256")
	 	}

    }
}
