import twitter4j.Twitter
import twitter4j.TwitterException

class TwitterDispatcher(private val twitter: Twitter) {
    private var previousTweetText = ""
    fun alert(call: Call) {
        val message = "ALERT(${call.timeReceived}): ${call.agency} ${call.status} ${call.location}: ${call.callType}"
        if (message != previousTweetText) {
            try {
                val status = twitter.updateStatus(message)
                previousTweetText = message
            } catch (e: TwitterException) {
                log.error { e.errorMessage }
            }

            return
        }
        log.warn { "Duplicate tweet.." }
    }
}