package predictionob

import grails.test.mixin.TestFor
import spock.lang.Specification
import grails.test.GrailsMock
import groovy.json.JsonSlurper

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@TestFor(GoogleAuthService)
class GoogleAuthServiceSpec extends Specification {

	String expectedClaimSet = '{"iss":"544022982002-pmn800idj168pa0mgrj7fugnr01h02d9@developer.gserviceaccount.com","scope":"https://www.googleapis.com/auth/prediction","aud":"https://accounts.google.com/o/oauth2/token","exp":123}'
	String expectedHeader 	= '{"alg":"RS256","typ":"JWT"}'

    void "test claimSetJson"() {
    	given:
    		def service = new GoogleAuthService ();
    		def json = new JsonSlurper().parseText(service.getClaimSet())
    		def expectedJson = new JsonSlurper().parseText(expectedClaimSet)
    	expect:
    		json.iss == expectedJson.iss
    		json.scope == expectedJson.scope
    		json.aud == expectedJson.aud
    }

    void "test headerJson"(){
    	given:
    		def service = new GoogleAuthService ();
    		def json = new JsonSlurper().parseText(service.getHeader())
    		def expectedJson = new JsonSlurper().parseText(expectedHeader)
    	expect:
    		json == expectedJson    	
    }

   
}
