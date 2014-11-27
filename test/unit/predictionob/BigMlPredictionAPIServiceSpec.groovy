package predictionob


import grails.test.mixin.TestFor
import spock.lang.Specification
import groovy.mock.interceptor.MockFor
import groovyx.net.http.HTTPBuilder
import groovyx.net.http.RESTClient
import groovyx.net.http.HttpResponseDecorator
import groovyx.net.http.ContentType

import groovyx.net.http.Method
import static groovyx.net.http.Method.POST
import groovy.json.JsonBuilder
import groovy.json.JsonOutput
import groovy.json.JsonSlurper
import org.apache.http.entity.mime.MultipartEntity
import org.apache.http.entity.mime.*
import org.apache.http.entity.mime.content.ByteArrayBody
import org.apache.http.entity.mime.content.InputStreamBody
import org.codehaus.groovy.grails.web.json.JSONObject
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

    void "getStatusFromResponse"() {
        given:
            def service = new BigMlPredictionAPIService ();
            def code3 = new JsonBuilder();
            code3 status : [ code : 3]           
            def code5 = new JsonBuilder();
            code5 status : [ code : 5]
            def codeNone = new JsonBuilder();
            codeNone status : [ ]
        expect:
            service.getStatusFromResponse(code3) == Model.STATUS_SUBMITED
            service.getStatusFromResponse(code5) == Model.STATUS_READY
            service.getStatusFromResponse(codeNone) == Model.STATUS_NONE
    }


    

    void "testCreateDataSet"(){
        
            def requestDelegate = [response: [:]]
            def mocker = new MockFor(BigMlPredictionAPIService.class)
            
            mock.demand.createDataSet { 
                source -> " ${source}"
            }
        
            mock.use {
                assert createDataSet('test') == 'test'
            }

    }
    void "testcreateModel"(){
        
            def requestDelegate = [response: [:]]
            def mocker = new MockFor(BigMlPredictionAPIService.class)
            
            mock.demand.createModel { 
                model -> " ${model}"
            }
        
            mock.use {
                assert createModel('test') == 'test'
            }

    }
    void "testcreatePrediction"(){
        
            def requestDelegate = [response: [:]]
            def mocker = new MockFor(BigMlPredictionAPIService.class)
            
            mock.demand.createPrediction { 
                prediction -> " ${prediction}"
            }
        
            mock.use {
                assert createPrediction('test') == 'test'
            }

    }

    void "testMakeCallout"(){

        setup:
            def httpBuildMock = new MockFor(HTTPBuilder.class)
            def reqPar = []
            def success
            def service = new BigMlPredictionAPIService ();
     
            def requestDelegate = [ 
                    response: [:]
            ]
     
            httpBuildMock.demand.request(){
                Method met, ContentType type, Closure b ->
                b.delegate = requestDelegate
                b.call()
                reqPar << [method: met, type: type, source: b.body.source ]
            }
 
        when:
            httpBuildMock.use{
                service.makeCallout('test', Method.POST, 'testResource', ContentType.JSON)
                
            }
     
        then:
            assert reqPar[0].method == Method.POST
            assert reqPar[0].type == ContentType.JSON
            assert reqPar.body[0] == 'test'
            

    }
    


}
