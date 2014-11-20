package predictionob

class Prediction {

	
	String predictionString
	String externalId
	Date createdDate
	Date willHappen


    static constraints = {

    }

    public Prediction(String s){
    	this.predictionString = s;
    	createdDate = new Date()
    	willHappen = new Date()

    }

    public Prediction(){
    	createdDate = new Date()
    }
}
