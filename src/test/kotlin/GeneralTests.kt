//import com.github.virginiaprivacycoalition.rvalerts.CallScraper
//import org.junit.jupiter.api.Test
//import kotlin.test.assertNotNull
//
//class GeneralTests {
//    val calls1 = GeneralTests::class.java.classLoader.getResource("calls1.html")
//    private val scraper = CallScraper("", testMode = true)
//    private val doc = scraper.parseDoc()
//    private val rows = doc?.let { scraper.getTableRows(it) }
//    val calls = rows?.let { scraper.getCalls(it) }!!
//
//    @Test
//    fun `test parsing html`() {
//
//        assertNotNull(doc)
//        assertNotNull(rows)
//    }
//
//    @Test
//    fun `test converting html into Calls`() {
//
//        assertNotNull(rows)
//        assertNotNull(calls)
//    }
//
//    @Test
//    fun `getting correct number of calls from document`() {
//        assert(calls.size == 14)
//    }
//
//
//}