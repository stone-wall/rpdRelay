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
        return other.agency == this.agency && other.dispatchArea == this.dispatchArea &&
                other.unit == this.unit && other.location == this.location && other.callType == this.callType
    }
}