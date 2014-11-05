package predictionob

import grails.test.mixin.TestFor
import spock.lang.Specification


/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@TestFor(EncoderService)
class EncoderServiceSpec extends Specification {
	String testHeader 			= '{"alg":"RS256","typ":"JWT"}'
    String testHeaderEncoded	= 'eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9'
    String testClaim = '''{
                            "iss":"761326798069-r5mljlln1rd4lrbhg75efgigp36m78j5@developer.gserviceaccount.com",
                            "scope":"https://www.googleapis.com/auth/prediction",
                            "aud":"https://accounts.google.com/o/oauth2/token",
                            "exp":1328554385,
                            "iat":1328550785
                        }'''
    String encodedClaim = 'eyJpc3MiOiI3NjEzMjY3OTgwNjktcjVtbGpsbG4xcmQ0bHJiaGc3NWVmZ2lncDM2bTc4ajVAZGV2ZWxvcGVyLmdzZXJ2aWNlYWNjb3VudC5jb20iLCJzY29wZSI6Imh0dHBzOi8vd3d3Lmdvb2dsZWFwaXMuY29tL2F1dGgvcHJlZGljdGlvbiIsImF1ZCI6Imh0dHBzOi8vYWNjb3VudHMuZ29vZ2xlLmNvbS9vL29hdXRoMi90b2tlbiIsImV4cCI6MTMyODU1NDM4NSwiaWF0IjoxMzI4NTUwNzg1fQ'

    void "test base64urlEncode"() {
    	given:
    		def service = new EncoderService ();
    	expect:
    		service.encodeBase64URL(testHeader) == testHeaderEncoded
            service.encodeBase64URL(testClaim) == encodedClaim
    }

    void "test hash256Encode"(){
    	given:
    		def hash256EncodedTestWord = 'test1234'
			def wordTohash256ENcode    = 'test1234'
			def keyTohash256ENcode 	   = 'MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBALgql2oS9dprM9k8\nDCFciSoJEqEPW6Ept+wVja8IO6/NMsh58P7ZBxyEWmxo22ZLtahMhw2BOBHpLMdW\n3E5VYTL/GIifhyVIOC1u6SHbVOIGLSoMiGy47RpM2MSN1cf2Raa8AuWzVsZjI4Tt\nEvROejWGWGGCbTETpBPbj/+KLLDrAgMBAAECgYBQ7mabtrlTwsiGLl0mTAP69QG6\nxYT+zdaFaPNad8PC/enTL8M4Oydk4MTb3ziLO7oYu4id5qs9M7r53rHxupN7lsSs\nbsMwuf9X+D8C6jDfAB/jkhYYjAjCnanfaewp2LDRhaj0Wgw2xs8hBYvbc5K81RqT\nXFCQA+gzlZGgSvlcAQJBANue3sJvIvH33/mjJo7kwPUkU3wOnRNClecfaJCg3fCw\nqfiEwOXcMtFOtFEstuns4KdpeuHTI+N6LcSRXA39IYUCQQDWrEQk61sbzBbrZeHm\ntyiYcKClAxpv5PuFwVmJno0/8MWqgr+DhKq5md2CeCakbQshSP6aBlC0Nw8R1A/j\nftuvAkBzgWbfE9xpLljZznjlBmKFi4ls4bKzAn4chGRWTITtpbx/PDLHsbv4YVBs\nsPuypCazZcJ/KrDhXszbaSflI5CRAkA7cGNF3azSW4YVE5ai7R/eZYD1b5CIXtyN\nwmVqR25Qv/fbKGyim78xuLquf1ojuNWWF8H3H7yEyaHwi8kCcoOdAkBgd7PL+CIa\nTvTAS0ecx0Y3LxV6GiE1z3hrGeixLGnHV9n+KNlErbXd44n1xvtbFITIrRUvtBeL\nFYHi5jpwDLu0\n'

			def service = new EncoderService ();
    	expect:
    		service.encodeHash256(keyTohash256ENcode,wordTohash256ENcode) == hash256EncodedTestWord
    	}



}