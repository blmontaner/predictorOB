package predictionob
import groovy.json.JsonBuilder

class Model {
	String filename
    byte[] filedata
    String modelExternalId
    String dataSetExternalId
    String sourceExternalId
    String sourceStatus
    String dataSetStatus
    String modelStatus

    public static String STATUS_SUBMITED = 'submited'
    public static String STATUS_NONE     = 'none'
    public static String STATUS_READY    = 'ready'
   
    static constraints = {    	
    	filename(blank:false,nullable:false)
        filedata(nullable:true, maxSize:1073741824)
        modelExternalId nullable:true
    	dataSetExternalId nullable:true
     	sourceExternalId nullable:true    
    }

    public Model(){
        sourceStatus  = STATUS_NONE
        dataSetStatus = STATUS_NONE
        modelStatus   = STATUS_NONE
    }

    public Boolean uploadModelToPredictionAPI(){
    	if(filedata != null){
			
    		//create data Source from file
    		createSource()
    		//create Data Set
            createDataSet()
            //create Model
            createModel()

    		this.save()
    		return true
		}
    	return false
    }

    private void createSource(){
        if(filedata != null && sourceStatus == STATUS_NONE){
            def jsonSource = BigMlPredictionAPIService.createSource(filedata)
            sourceExternalId = jsonSource.resource
            println jsonSource.status.message
            println sourceExternalId
            println "********** createSource" 
            println jsonSource
            println "********** createSource"        
            sourceStatus = BigMlPredictionAPIService.getStatusFromResponse(jsonSource)
        }
        if(sourceStatus == STATUS_SUBMITED){
            def jsonResponse = BigMlPredictionAPIService.getResource(sourceExternalId)
            sourceStatus = BigMlPredictionAPIService.getStatusFromResponse(jsonResponse)
        }
    }

    private void createDataSet(){
        if(sourceStatus == STATUS_READY && dataSetStatus == STATUS_NONE){
            def jsonDataSet = BigMlPredictionAPIService.createDataSet(sourceExternalId)
            dataSetExternalId = jsonDataSet.resource
            println jsonDataSet.status.message
            println dataSetExternalId
            println "********** createDataSet"
            println jsonDataSet
            println "********** createDataSet"        
            dataSetStatus = BigMlPredictionAPIService.getStatusFromResponse(jsonDataSet)
        }
        if(dataSetStatus == STATUS_SUBMITED){
            def jsonResponse = BigMlPredictionAPIService.getResource(dataSetExternalId)
            dataSetStatus = BigMlPredictionAPIService.getStatusFromResponse(jsonResponse)
        }
    }

    private void createModel(){
        println "********** modelStatus1 "+modelStatus
        if(sourceStatus == STATUS_READY && dataSetStatus == STATUS_READY &&  modelStatus == STATUS_NONE){
            def jsonModel = BigMlPredictionAPIService.createModel(dataSetExternalId)
            modelExternalId = jsonModel.resource
            println jsonModel.status.message
            println modelExternalId
            println "********** createModel" 
            println jsonModel
            println "********** createModel"
            println 'helo model -->'+jsonModel.status.code
            println jsonModel.status.code == 1
            modelStatus = BigMlPredictionAPIService.getStatusFromResponse(jsonModel)
            println "********** modelStatus2 "+modelStatus
        }
        if(modelStatus == STATUS_SUBMITED){
            def jsonResponse = BigMlPredictionAPIService.getResource(modelExternalId)
            modelStatus = BigMlPredictionAPIService.getStatusFromResponse(jsonResponse)
            println 'helo model response --> '+jsonResponse
            println 'helo model 3 --> '+jsonResponse.status.code
            this.save()
        }
        println "********** modelStatus3 "+modelStatus
    }

    public Boolean isModelReadyForPredictions(){
        if(modelStatus != STATUS_READY){
            uploadModelToPredictionAPI()
        }
        return modelStatus == STATUS_READY
    }

    public Prediction makePrediction(Date date){
        Prediction p = new Prediction();
        def inputData = new JsonBuilder()
        inputData  '000000-1' : date[Calendar.MONTH],  '000000-2' : date[Calendar.DATE], '000000-3' : date[Calendar.DAY_OF_WEEK], '000000-4' : date[Calendar.HOUR_OF_DAY],  '000000-5' : date[Calendar.MINUTE]
        def jsonModel = BigMlPredictionAPIService.createPrediction(modelExternalId, inputData)
        println jsonModel
        p.externalId = jsonModel.resource
        p.predictionString = jsonModel.prediction.'000001'
        p.willHappen = date;
        p.save();
        println p.predictionString
        println p.externalId
    	return p
    }
}
