package predictionob

class PredictorController {
	
    Date date
    Prediction predictionPlace
    String processingMessage 
    Boolean proccesing = false

	def index() {

        for(Model m in Model.list()){
            println " SourceStatus-> "+m.sourceStatus  +" dataSetStatus-> "+m.dataSetStatus +" modelStatus-> "+m.modelStatus  +" id-> "+m.id
            
            if(Model.list().size()>0 && (m.sourceStatus != Model.STATUS_READY ||  m.dataSetStatus != Model.STATUS_READY || m.modelStatus != Model.STATUS_READY )){
                proccesing = true
            }

        }
        if(proccesing){
             processingMessage = "Se estan procesando modelos... "
        }
        
        [ date : new Date(),modelList : Model.list(), message : params.message, processingMessage: processingMessage, predictionPlace : params.predictionPlace != null ? Prediction.findById(params.predictionPlace) : new Prediction( "Universidad ORT Uruguay")]
        
	 }

	 def saveFile = {
        def modelInstance
	    def file = request.getFile('file')
        if(file.empty) {
            flash.message = "File cannot be empty"
        } else {
            modelInstance = new Model()
            modelInstance.filename = file.originalFilename
            modelInstance.filedata = file.getBytes()
            println modelInstance
            modelInstance.uploadModelToPredictionAPI()            
        }
        String m;
        if(modelInstance.modelStatus != Model.STATUS_READY){
            m = 'El modelo estara pronto en unos instantes vuelve a intentar mas tarde'
        }
        redirect(  controller: "predictor",  params: [message: m])

	}

    def predict(){
        long modelId = params.modelId.toLong()
        Date date = params.date                
        Model m = Model.findById(modelId)
        println m.id +' '+ m.filename 
        Prediction p = m.makePrediction(date)
        
        redirect( controller: "predictor",  params: [predictionPlace: p.id])

    }
}
