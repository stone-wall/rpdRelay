import mu.KLogger
import mu.KLogging
import mu.KotlinLogging
import org.jsoup.Jsoup
import java.time.Instant
import java.util.*
import java.util.concurrent.CopyOnWriteArraySet
import kotlin.concurrent.fixedRateTimer

class Scraper {

    val url = "https://apps.richmondgov.com/applications/activecalls/Home/ActiveCalls?"
    val log = KotlinLogging.logger("Default")
    var firstRun = false
    val events = CopyOnWriteArraySet<Call>()


    fun parseDoc() {
        val doc = Jsoup.connect(url).get()
        val table = doc.getElementsByTag("tbody")
        val activeCalls = table[0].getElementsByTag("tr")
        activeCalls.forEach { call ->
            with (call.children().map { it.text() }) {
                val call = Call(this[0], this[1], this[2], this[3], this[4], this[5], this[6])
                if (!firstRun && !events.contains(call)) {
                    alert(call)
                    events.add(call)
                }
            }
        }
        firstRun = false
    }
    fun alert(call: Call) {
        println("ALERT!!!! ${call.timeReceived}: ${call.callType} $call")
    }
}

fun main() {
    val scraper = Scraper()
    fixedRateTimer("RVAScraper", false, 0, 60000) {
        scraper.parseDoc()
    }
}