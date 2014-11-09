
package predictionob

import grails.transaction.Transactional


import groovyx.net.http.HTTPBuilder
import groovyx.net.http.RESTClient
import groovyx.net.http.HttpResponseDecorator
import static groovyx.net.http.ContentType.*
import static groovyx.net.http.Method.POST
import groovy.json.JsonBuilder
import groovy.json.JsonOutput
import groovy.json.JsonSlurper

@Transactional
class GoogleAuthService {
	String PRIVATE_KEY			= 'MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBALgql2oS9dprM9k8DCFciSoJEqEPW6Ept+wVja8IO6/NMsh58P7ZBxyEWmxo22ZLtahMhw2BOBHpLMdW3E5VYTL/GIifhyVIOC1u6SHbVOIGLSoMiGy47RpM2MSN1cf2Raa8AuWzVsZjI4TtEvROejWGWGGCbTETpBPbj/+KLLDrAgMBAAECgYBQ7mabtrlTwsiGLl0mTAP69QG6xYT+zdaFaPNad8PC/enTL8M4Oydk4MTb3ziLO7oYu4id5qs9M7r53rHxupN7lsSsbsMwuf9X+D8C6jDfAB/jkhYYjAjCnanfaewp2LDRhaj0Wgw2xs8hBYvbc5K81RqTXFCQA+gzlZGgSvlcAQJBANue3sJvIvH33/mjJo7kwPUkU3wOnRNClecfaJCg3fCwqfiEwOXcMtFOtFEstuns4KdpeuHTI+N6LcSRXA39IYUCQQDWrEQk61sbzBbrZeHmtyiYcKClAxpv5PuFwVmJno0/8MWqgr+DhKq5md2CeCakbQshSP6aBlC0Nw8R1A/jftuvAkBzgWbfE9xpLljZznjlBmKFi4ls4bKzAn4chGRWTITtpbx/PDLHsbv4YVBssPuypCazZcJ/KrDhXszbaSflI5CRAkA7cGNF3azSW4YVE5ai7R/eZYD1b5CIXtyNwmVqR25Qv/fbKGyim78xuLquf1ojuNWWF8H3H7yEyaHwi8kCcoOdAkBgd7PL+CIaTvTAS0ecx0Y3LxV6GiE1z3hrGeixLGnHV9n+KNlErbXd44n1xvtbFITIrRUvtBeLFYHi5jpwDLu0'
	//'-----BEGIN PRIVATE KEY-----\nMIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBALgql2oS9dprM9k8\nDCFciSoJEqEPW6Ept+wVja8IO6/NMsh58P7ZBxyEWmxo22ZLtahMhw2BOBHpLMdW\n3E5VYTL/GIifhyVIOC1u6SHbVOIGLSoMiGy47RpM2MSN1cf2Raa8AuWzVsZjI4Tt\nEvROejWGWGGCbTETpBPbj/+KLLDrAgMBAAECgYBQ7mabtrlTwsiGLl0mTAP69QG6\nxYT+zdaFaPNad8PC/enTL8M4Oydk4MTb3ziLO7oYu4id5qs9M7r53rHxupN7lsSs\nbsMwuf9X+D8C6jDfAB/jkhYYjAjCnanfaewp2LDRhaj0Wgw2xs8hBYvbc5K81RqT\nXFCQA+gzlZGgSvlcAQJBANue3sJvIvH33/mjJo7kwPUkU3wOnRNClecfaJCg3fCw\nqfiEwOXcMtFOtFEstuns4KdpeuHTI+N6LcSRXA39IYUCQQDWrEQk61sbzBbrZeHm\ntyiYcKClAxpv5PuFwVmJno0/8MWqgr+DhKq5md2CeCakbQshSP6aBlC0Nw8R1A/j\nftuvAkBzgWbfE9xpLljZznjlBmKFi4ls4bKzAn4chGRWTITtpbx/PDLHsbv4YVBs\nsPuypCazZcJ/KrDhXszbaSflI5CRAkA7cGNF3azSW4YVE5ai7R/eZYD1b5CIXtyN\nwmVqR25Qv/fbKGyim78xuLquf1ojuNWWF8H3H7yEyaHwi8kCcoOdAkBgd7PL+CIa\nTvTAS0ecx0Y3LxV6GiE1z3hrGeixLGnHV9n+KNlErbXd44n1xvtbFITIrRUvtBeL\nFYHi5jpwDLu0\n-----END PRIVATE KEY-----\n'
	//'MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBALgql2oS9dprM9k8DCFciSoJEqEPW6Ept+wVja8IO6/NMsh58P7ZBxyEWmxo22ZLtahMhw2BOBHpLMdW3E5VYTL/GIifhyVIOC1u6SHbVOIGLSoMiGy47RpM2MSN1cf2Raa8AuWzVsZjI4TtEvROejWGWGGCbTETpBPbj/+KLLDrAgMBAAECgYBQ7mabtrlTwsiGLl0mTAP69QG6xYT+zdaFaPNad8PC/enTL8M4Oydk4MTb3ziLO7oYu4id5qs9M7r53rHxupN7lsSsbsMwuf9X+D8C6jDfAB/jkhYYjAjCnanfaewp2LDRhaj0Wgw2xs8hBYvbc5K81RqTXFCQA+gzlZGgSvlcAQJBANue3sJvIvH33/mjJo7kwPUkU3wOnRNClecfaJCg3fCwqfiEwOXcMtFOtFEstuns4KdpeuHTI+N6LcSRXA39IYUCQQDWrEQk61sbzBbrZeHmtyiYcKClAxpv5PuFwVmJno0/8MWqgr+DhKq5md2CeCakbQshSP6aBlC0Nw8R1A/jftuvAkBzgWbfE9xpLljZznjlBmKFi4ls4bKzAn4chGRWTITtpbx/PDLHsbv4YVBssPuypCazZcJ/KrDhXszbaSflI5CRAkA7cGNF3azSW4YVE5ai7R/eZYD1b5CIXtyNwmVqR25Qv/fbKGyim78xuLquf1ojuNWWF8H3H7yEyaHwi8kCcoOdAkBgd7PL+CIaTvTAS0ecx0Y3LxV6GiE1z3hrGeixLGnHV9n+KNlErbXd44n1xvtbFITIrRUvtBeLFYHi5jpwDLu0'
	String GOOGLE_ACCOUNT_URL 	= 'https://accounts.google.com/'
	String GOOGLE_TOKEN_URL 	= 'https://accounts.google.com/o/oauth2/token'
	String CLIENT_ID 			= '544022982002-d62o9h3m1ro41pcg3r57mus1429v54as.apps.googleusercontent.com'
	String EMAIL_ADDRESS 		= '544022982002-d62o9h3m1ro41pcg3r57mus1429v54as@developer.gserviceaccount.com'
	String SCOPE 				= 'https://www.googleapis.com/auth/prediction'
	String RESPONSE_TYPE_CODE 	= 'response_type code'
	String HEADER_ALGORITHM 	= 'RS256'
	String HEADER_TYPE 			= 'JWT'
	String GRANT_TYPE 			= 'urn:ietf:params:oauth:grant-type:jwt-bearer'
	
	String HEADER_JSON_ENCODED	= 'eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9'

	String EMAIL_SERVICE_PARAM  		 = 'iss'
	String SCOPE_SERVICE_PARAM  		 = 'scope'
	String TARGET_SERVICE_PARAM 		 = 'aud'
	String EXPIRATION_TIME_SERVICE_PARAM = 'exp'
	String ISSUED_TIME_SERVICE_PARAM 	 = 'iat'
	String HEADER_ALGORITHM_PARAM 		 = 'alg'
	String HEADER_TYPE_PARAM 			 = 'typ'
	String GRANT_TYPE_PARAM 			 = 'grant_type'
	String ASSERTION_PARAM	 			 = 'assertion'

//	String JSON_KEY = '{"private_key_id": "f9610f680f390e7115ac8001d80da059027eeebd","private_key": "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBALgql2oS9dprM9k8\nDCFciSoJEqEPW6Ept+wVja8IO6/NMsh58P7ZBxyEWmxo22ZLtahMhw2BOBHpLMdW\n3E5VYTL/GIifhyVIOC1u6SHbVOIGLSoMiGy47RpM2MSN1cf2Raa8AuWzVsZjI4Tt\nEvROejWGWGGCbTETpBPbj/+KLLDrAgMBAAECgYBQ7mabtrlTwsiGLl0mTAP69QG6\nxYT+zdaFaPNad8PC/enTL8M4Oydk4MTb3ziLO7oYu4id5qs9M7r53rHxupN7lsSs\nbsMwuf9X+D8C6jDfAB/jkhYYjAjCnanfaewp2LDRhaj0Wgw2xs8hBYvbc5K81RqT\nXFCQA+gzlZGgSvlcAQJBANue3sJvIvH33/mjJo7kwPUkU3wOnRNClecfaJCg3fCw\nqfiEwOXcMtFOtFEstuns4KdpeuHTI+N6LcSRXA39IYUCQQDWrEQk61sbzBbrZeHm\ntyiYcKClAxpv5PuFwVmJno0/8MWqgr+DhKq5md2CeCakbQshSP6aBlC0Nw8R1A/j\nftuvAkBzgWbfE9xpLljZznjlBmKFi4ls4bKzAn4chGRWTITtpbx/PDLHsbv4YVBs\nsPuypCazZcJ/KrDhXszbaSflI5CRAkA7cGNF3azSW4YVE5ai7R/eZYD1b5CIXtyN\nwmVqR25Qv/fbKGyim78xuLquf1ojuNWWF8H3H7yEyaHwi8kCcoOdAkBgd7PL+CIa\nTvTAS0ecx0Y3LxV6GiE1z3hrGeixLGnHV9n+KNlErbXd44n1xvtbFITIrRUvtBeL\nFYHi5jpwDLu0\n","client_email": "544022982002-pmn800idj168pa0mgrj7fugnr01h02d9@developer.gserviceaccount.com","client_id": "544022982002-pmn800idj168pa0mgrj7fugnr01h02d9.apps.googleusercontent.com","type": "service_account"}'

	String JSON_KEY ='''	{
	  "private_key_id": "3c0477db6ec030fc367caa376736e9505994dd0c",
	  "private_key": "MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBAOq+DRln3wZo22ZZ\naPRbPxXzTHlpiSJOMxGSnSRteS/kGz8m5m+s3n5Sn/KZxlz5gjZ+tH2FDCAmg0Rd\nBR1B7tAzkhTXttH+fhy/KCSFeA5hJLf+w+n9u3TFodfZ6MBmoanAhVddLCPpfF2s\n8F6nfRBvRogPlmI3MO1zcFQcJwjhAgMBAAECgYEA285gR7Tawo63OqxoOb+jX/N1\nXY7z6RqeeUr3OGAGBVEsLkNHrbSKlnDZ8voJI5BDXLHiYlKOGyDU89PCcTYG3kTb\neP/dozQdoTD4DgtEutAit4cDXyx3Qtm6iRLBg2w0+Fz/vnpqMLyKhxpT6S+bk4Za\nh/FgLt0HdzI70GmogAECQQD7rUV+NkQOkdyJBJCtwegqC2YASl043Ip3kQS4HNlA\nTT+CKVrqebGMktsyFoKDtMeBGoC65haojgiMZuaOh8wBAkEA7sZP02tbrsRYAyPN\nf02iEj+uBWvFLasgnYeEPtMKMlrahvHzJyrCh7dQZE2nWIEbrDbXGmKxkKMm37DI\nTPy84QJBAItUv9ZBwKe4RLluV5J6eUddjBi5Eqb6IYZUOKKdBgE9Qt2Se4y31n2W\nh9dQ8uxrVs+4lm/iFcU27LCd/yIFOAECQQDLeWN9Hve6LsdxpYjw+w8pzepV5Ejb\nagEZPOqNPvppftLKPFShA+dSD/0J/MKmwR772p/jdEcsolv2M4f9U0MBAkAlO8ps\nh2XbpA97ySbCz8Fe5rh4MMPiTVJcTPWV3erKQ5ylMkXKeekACViGEtXFJ3V5L9nf\nB7I0fQRw3vAz3y3l",
	  "client_email": "544022982002-pmn800idj168pa0mgrj7fugnr01h02d9@developer.gserviceaccount.com",
	  "client_id": "544022982002-pmn800idj168pa0mgrj7fugnr01h02d9.apps.googleusercontent.com",
	  "type": "service_account"
	}'''


    def getAuthToken(){
	 	def http = new HTTPBuilder(GOOGLE_TOKEN_URL)
	 	def body = [ "$GRANT_TYPE_PARAM":"$GRANT_TYPE", "$ASSERTION_PARAM":getSignature()]
	 	print 'body-->' + body
	 	http.handler.success = { resp, reader -> "Success! ${resp.statusLine} ${reader}" } 
 		http.handler.failure = { resp, reader -> "Unexpected failure: ${resp.statusLine} ${reader}"	}
        println  "SIGNATURE "+ getSignature()
        http.request(POST) {
       		send URLENC, body
 		}
    }

    private def String getClaimSet(){
    	TimeZone.setDefault(TimeZone.getTimeZone('UTC'))
    	def time = (System.currentTimeMillis()/1000).toLong()
    	println "TIME-> " + time
		def claimSet = [
    		"$EMAIL_SERVICE_PARAM": EMAIL_ADDRESS,
    		"$SCOPE_SERVICE_PARAM": SCOPE,
    		//"sub":"blmontaner@gmail.com",
    		"$TARGET_SERVICE_PARAM": GOOGLE_TOKEN_URL,
    		"$EXPIRATION_TIME_SERVICE_PARAM": time + 3600,
    		"$ISSUED_TIME_SERVICE_PARAM": time
		]
		new JsonBuilder(claimSet).toString()
		
    }

    private def String getHeader(){
    	def header = [
    		"$HEADER_ALGORITHM_PARAM": HEADER_ALGORITHM,
    		"$HEADER_TYPE_PARAM": HEADER_TYPE    		
		]
		new JsonBuilder(header).toString()
    }

    private def String getSignature(){
    	def jsonKey = new JsonSlurper().parseText(JSON_KEY)
    	def stringToSign = EncoderService.encodeBase64URL(getHeader()) + '.' + EncoderService.encodeBase64URL(getClaimSet()) 
    	def hashSign = EncoderService.encodeHash256(jsonKey.private_key,stringToSign)
    	
    	stringToSign + '.' +  EncoderService.encodeBase64URL(hashSign)
    }


    //TODO: move keys to a config file and load here
    private void loadConfig(){
    	def props = new Properties()
		new File("Google.properties").withInputStream { s ->
		  props.load(s) 
		}
    }

}
