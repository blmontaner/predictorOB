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

@Transactional
class EncoderService {

	//to encode base64Url we need to remove URL unsafe characters from base64 encoding
    def static encodeBase64URL(String toEncode) {
    	return toEncode.bytes.encodeBase64().toString().replace('-','+').replace('/','_').replace('==','') 
    }

	//encodeHash256 from http://stackoverflow.com/questions/13419201/why-are-the-rsa-sha256-signatures-i-generate-with-openssl-and-java-different
    def static encodeHash256(String privateKeystr, String toEncode){
    	try {  

    		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
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

	 	} catch (InvalidKeyException e) { 
	 		throw new RuntimeException("Invalid key exception while converting to RSA SHA256")
	 	}

    }
}
