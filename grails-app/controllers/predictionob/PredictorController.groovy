package predictionob

class PredictorController {
	
    Date date
    Prediction predictionPlace
    

	def index() {
        [ date : new Date(),modelList : Model.list(), message : params.message, predictionPlace : params.predictionPlace != null ? Prediction.findById(params.predictionPlace) : new Prediction( "Universidad ORT Uruguay")]
        
	 }

	 def saveFile = {
	    def file = request.getFile('file')
        if(file.empty) {
            flash.message = "File cannot be empty"
        } else {
            def modelInstance = new Model()
            modelInstance.filename = file.originalFilename
            modelInstance.filedata = file.getBytes()
            println modelInstance
            modelInstance.save()
            modelInstance.uploadModelToPredictionAPI()
            
        }

        redirect(  controller: "predictor",  params: [message: 'El modelo estara pronto en unos instantes vuelve a intentar mas tarde'])

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
