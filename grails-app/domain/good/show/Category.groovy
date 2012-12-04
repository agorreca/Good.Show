package good.show

class Category {
	String name
	String description
	User user

	static belongsTo = [User]
	static hasMany = [feeds: Feed]

	static constraints = { name(blank:false) }

	String toString() {
		name
	}
}