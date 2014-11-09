package predictionob

class PruebaGoogleCredsController {

     def index() {
    	[ object: new BigMlPredictionAPIService() ]
	 }
}
