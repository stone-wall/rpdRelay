package com.github.virginiaprivacycoalition.rvalerts

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements

class CallScraper {

    private val ioScope = CoroutineScope(Dispatchers.IO)

    private suspend fun parseDocAsync(): Document = suspendCancellableCoroutine { Jsoup.connect(RVA_URL).get() }

    val activeCallsState = MutableStateFlow(ActiveCalls(System.currentTimeMillis(), emptyList()))

    private suspend fun parseCalls(): List<CallAlert>? {
        val doc = parseDocAsync()
            .getElementsByTag("tbody")
            .firstOrNull()
            ?.getElementsByTag("tr")

        doc?.let {
            return@let getCalls(doc)
        }
        return null
    }

    fun updateActiveCalls() {
        ioScope.launch {
            activeCallsState.update {
                ActiveCalls(System.currentTimeMillis(), parseCalls() ?: emptyList())
            }
        }
    }

    private fun getCalls(activeCalls: Elements): List<CallAlert> {
        return activeCalls.map { elements ->
            with(elements.children().map { it.text() }) {
                CallAlert(this)
            }
        }
    }

    companion object {
        const val RVA_URL = "https://apps.richmondgov.com/applications/activecalls/Home/ActiveCalls?"
    }
}

