package good.show

class Feed {

	String name
	String url
	User owner
	Category category

	static belongsTo = [User, Category]

	static constraints = {
		name(blank:false)
		url(blank:false)
	}

	String toString() {
		"${name}: ${url}"
	}
}
