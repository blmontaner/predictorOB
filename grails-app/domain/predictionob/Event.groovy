package predictionob

class Event {

	String whereItHappened
	Long whereItHappenedLat
	Long whereItHappenedLong
	Date dateItHappened
	String typeOfEvent

	static belongsTo = [ myModel: Model ]

    static constraints = {
    }
}
