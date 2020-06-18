import twitter4j.Twitter
import twitter4j.TwitterException

class TwitterDispatcher(private val twitter: Twitter) {
    private var previousTweetText = ""
    fun alert(call: Call) {
        val message = "ALERT(${call.timeReceived}): ${call.agency} ${call.status} ${call.location}: ${call.callType}"
        if (message != previousTweetText) {
            try {
                val status = twitter.updateStatus(message)
                log.info(status.text)
                previousTweetText = message
            } catch (e: TwitterException) {
                log.warn { e.errorMessage }
            }

            return
        }
        log.warn { "Duplicate tweet.." }
    }
}