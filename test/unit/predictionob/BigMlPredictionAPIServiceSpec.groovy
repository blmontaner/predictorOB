package predictionob

import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@TestFor(BigMlPredictionAPIService)
class BigMlPredictionAPIServiceSpec extends Specification {

	String EXPECTED_FULL_AUTH_API_URL = 'https://bigml.io/dev/andromeda/source?username=bmontaner;api_key=52c63f8d1d9b915034e63f2851d0f481f882c7c9'
	String EXPECTED_API_URL = 'https://bigml.io/dev/andromeda/'

    void "APIURL"() {
    	given:
    		def service = new BigMlPredictionAPIService ();
    	expect:
    		service.getApiURL() == EXPECTED_API_URL
    }

    void "authURL"() {
    	given:
    		def service = new BigMlPredictionAPIService ();
    	expect:
    		service.getApiURLAuthenticated('source') == EXPECTED_FULL_AUTH_API_URL    
    }


}
