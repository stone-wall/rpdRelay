import kotlinx.serialization.Serializable

@Serializable
data class Call(
    val timeReceived: String,
    val agency: String,
    val dispatchArea: String,
    val unit: String,
    val callType: String,
    val location: String,
    val status: String
) {
    override fun equals(other: Any?): Boolean {
        if (other !is Call) return false
        return other.agency == this.agency && other.dispatchArea == this.dispatchArea && other.location == this.location
                && other.callType == this.callType
    }
}

enum class Precinct(val asString: String) {
    FIRST("Precinct 1"),
    SECOND("Precinct 2"),
    THIRD("Precinct 3"),
    FOURTH("Precinct 4");

}

fun String.asPrecinct(): Precinct? {
    return Precinct.values().find { it.asString == this }
}
