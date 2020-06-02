
import mu.KotlinLogging
import org.jsoup.Jsoup
import twitter4j.Twitter
import twitter4j.TwitterException
import twitter4j.TwitterFactory
import java.util.concurrent.CopyOnWriteArraySet
import kotlin.concurrent.fixedRateTimer

class Scraper(private val twitter: Twitter) {

    private val url = "https://apps.richmondgov.com/applications/activecalls/Home/ActiveCalls?"
    private val log = KotlinLogging.logger("Default")
    private var firstRun = true
    private val events = CopyOnWriteArraySet<Call>()
    private var previousTweetText = ""


    fun parseDoc() {
        val doc = Jsoup.connect(url).get()
        val table = doc.getElementsByTag("tbody")
        val activeCalls = table[0].getElementsByTag("tr")
        activeCalls.forEach { call ->
            with (call.children().map { it.text() }) {
                val callElement = Call(this[0], this[1], this[2], this[3], this[4], this[5], this[6])
                if (!firstRun && !events.contains(callElement)) {
                    alert(callElement)
                    events.add(callElement)
                }
                if (firstRun) {
                    events.add(callElement)
                }
            }
        }
        firstRun = false
    }


    private fun alert(call: Call) {
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
        log.info { message }
    }
}

fun main() {
    val factory = TwitterFactory()

    val scraper = Scraper(factory.instance)
    fixedRateTimer("Relay", false, 0, 60000) {
        scraper.parseDoc()
    }
}