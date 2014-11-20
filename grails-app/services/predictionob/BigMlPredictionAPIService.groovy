package predictionob

import grails.transaction.Transactional
import groovyx.net.http.HTTPBuilder
import groovyx.net.http.RESTClient
import groovyx.net.http.HttpResponseDecorator
import static groovyx.net.http.ContentType.*
import static groovyx.net.http.Method.*
import static groovyx.net.http.Method.POST
import groovy.json.JsonBuilder
import groovy.json.JsonOutput
import groovy.json.JsonSlurper
import org.apache.http.entity.mime.MultipartEntity
import org.apache.http.entity.mime.*
import org.apache.http.entity.mime.content.ByteArrayBody
import org.apache.http.entity.mime.content.InputStreamBody
import org.codehaus.groovy.grails.web.json.JSONObject

@Transactional
class BigMlPredictionAPIService {

 	static String API_URL = 'https://bigml.io'
 	static String API_MODE = 'dev'
 	static String API_VERSION = 'andromeda'
 	static String BIGML_USERNAME='bmontaner'
 	static String BIGML_API_KEY='52c63f8d1d9b915034e63f2851d0f481f882c7c9'
	static String BIGML_AUTH="username=$BIGML_USERNAME;api_key=$BIGML_API_KEY"

	static String SOURCE = 'source'
	static String DATASET = 'dataset'
	static String MODEL = 'model'
	static String PREDICTION = 'prediction'
	static String PREDICTION_FIELD ='000001'


 	public static String getApiURL(){
 		return API_URL + '/' + API_MODE + '/' + API_VERSION +'/'
 	}

 	public static String getApiURLAuthenticated(String resource){
 		return getApiURL() + "$resource?" + BIGML_AUTH
 	}


 	public static Map makeCallout(String body, groovyx.net.http.Method method, String resource, groovyx.net.http.ContentType contentType){

 		String url = getApiURLAuthenticated(resource)
 		def http = new HTTPBuilder(url)
	 	print 'body-->' + body
	 	print 'method-->' + method
	 	print 'resource-->' + resource
	 	print 'contentType-->' + contentType
	 	print 'url-->' + url
	 	
	 	http.handler.success = { resp, reader -> reader } 
 		http.handler.failure = { resp, reader -> reader	}
        
        if(method != groovyx.net.http.Method.GET){
	        http.request(method) {
	       		send contentType, body
	 		}
 		}else{
	        http.request(method) {
	 		}
 		}
 	}


 	public static Map makeCallout(byte[] fileBytes, groovyx.net.http.Method method, String resource, String contentType){

	 		String url = getApiURLAuthenticated(resource)
	 		def http = new HTTPBuilder(url)

		 	print 'method-->' + method
		 	print 'resource-->' + resource
		 	print 'contentType-->' + contentType
		 	print 'url-->' + url

	        http.request (method) { req ->
	            	
	        requestContentType = contentType
 
		   	MultipartEntity multipartRequestEntity = new MultipartEntity()		
      		multipartRequestEntity.addPart("file", new ByteArrayBody(fileBytes, 'fileSource.csv'))
  
          	req.entity = multipartRequestEntity  

            response.success = { resp, data -> data }
            response.failure = { resp, data -> log.error data }
	                
	        }

       }


 	public static String testCreateSource(){
 		String resource = "a,b,c,d\n1,2,3,4\n5,6,7,8"
 		def json = createSource(resource)
 		print '1->>'+json.resource
 		print '2->>'+json.status.message
 		json.status.message
 	}

 	public static String testCreateModel(){
 		String resource = "dataset/545fd19490a1e97e150009d9"
 		def json = createModel(resource)
 		print '1->>'+json.resource
 		print '2->>'+json.status.message
 		json
 	}

 	public static String testCallout(){
 		String resource = "model/5462a9d5c4063745130000d1"
 		def inputData = new JsonBuilder()
 		inputData  '000003' : 5.0 
 		def json = createPrediction(resource,inputData)
 		print '1->>'+json.resource
 		print '2->>'+json.status.message
 		print '3->>'+json.prediction
 		json
 	}

 	public static Map getResource(String idResource){
 		makeCallout(null,groovyx.net.http.Method.GET,idResource,groovyx.net.http.ContentType.JSON)
 	}

 	//creates Source with inline data 
 	public static Map createSource(String data,String test){
 		def json = new JsonBuilder()
		json  data : "$data" 
		makeCallout(json.toString(),groovyx.net.http.Method.POST,SOURCE,groovyx.net.http.ContentType.JSON)
 	}

 	public static Map createSource(byte[] f){ 		
		makeCallout(f,groovyx.net.http.Method.POST,SOURCE,'multipart/form-data')
 	}

 	//creates dataSet from source 
 	public static Map createDataSet(String source){
 		def json = new JsonBuilder()
		json  source : "$source" 
		makeCallout(json.toString(),groovyx.net.http.Method.POST,DATASET,groovyx.net.http.ContentType.JSON)
 	}

 	//creates Model from dataSet 
 	public static Map createModel(String dataset){
 		def json = new JsonBuilder()
		json  dataset : "$dataset" , objective_field: PREDICTION_FIELD
		makeCallout(json.toString(),groovyx.net.http.Method.POST,MODEL,groovyx.net.http.ContentType.JSON)
 	}

 	//creates Prediction from Model
 	public static Map createPrediction(String model, JsonBuilder inputData){
 		if(model != null){
	 		def json = new JsonBuilder()
			json  model : "$model" , input_data : inputData.content
			makeCallout(json.toString(),groovyx.net.http.Method.POST,PREDICTION,groovyx.net.http.ContentType.JSON)
		}
 	}

 	public static String getStatusFromResponse(def jsonSource){
 		if(jsonSource.status.code != 5){
            return Model.STATUS_SUBMITED
        }
        if(jsonSource.status.code == 5){
            return Model.STATUS_READY
        }
        return Model.STATUS_NONE
 	}

}
