package predictionob

class Model {

	long id;
	static hasMany = [events: Event]

    static constraints = {    	
    	id unique: true, blank: false
    }
}
