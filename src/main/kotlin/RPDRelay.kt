import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.runBlocking
import twitter4j.TwitterFactory
import kotlin.concurrent.fixedRateTimer

@ExperimentalCoroutinesApi
fun main() {
    val factory = TwitterFactory()
    val dispatcher = TwitterDispatcher(factory.instance)
    val scraper = Scraper("https://apps.richmondgov.com/applications/activecalls/Home/ActiveCalls?")
    val store = Datastore()
    runBlocking {
        fixedRateTimer("Relay", false, 0, 60000) {
            scraper.parseDoc()
        }
        newCallChannel.consumeEach {
            println(it)
            dispatcher.alert(it)
            store.save(it)
        }
    }
}