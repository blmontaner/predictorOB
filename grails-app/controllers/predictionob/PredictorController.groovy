package predictionob

class PredictorController {
	
    Date date
    Prediction predictionPlace
    

	def index() {
        for(Model m in Model.list()){
            println " SourceStatus-> "+m.sourceStatus  +" dataSetStatus-> "+m.dataSetStatus +" modelStatus-> "+m.modelStatus  +" id-> "+m.id
        }
        
        [ date : new Date(),modelList : Model.list(), message : params.message, predictionPlace : params.predictionPlace != null ? Prediction.findById(params.predictionPlace) : new Prediction( "Universidad ORT Uruguay")]
        
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
        println " date--->"+ date
        println " modelId--->"+ modelId
        
        Model m = Model.findById(modelId)
        println m.id +' '+ m.filename 
        Prediction p = m.makePrediction(date)
        println '=====>>'+p
        redirect( controller: "predictor",  params: [predictionPlace: p.id])

    }
}
