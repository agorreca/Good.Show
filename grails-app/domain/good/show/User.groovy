package good.show

class User {
	String userName
	String firstName
	String lastName

	static hasMany = [feeds: Feed, categories: Category]

	static constraints = {
		userName(blank:false,unique:true)
		firstName(blank:false)
		lastName(blank:false)
	}

	String toString() {
		"$lastName, $firstName"
	}
}