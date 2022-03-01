package com.github.virginiaprivacycoalition.rvalerts

import org.jsoup.select.Elements

data class CallAlert(
    private val elements: List<String>
) {
    val timeReceived: String = elements[0]
    val agency = parseAgency(elements[1])
    val dispatchArea = parseDispatchArea(elements[2])
    val unit = elements[3]
    val callType = elements[4]
    val location = elements[5]
    val callStatus = parseCallStatus(elements[6])
}




sealed class CallDetailItem(open val title: String)


interface ParsedItem {
    val text: String

}

enum class DispatchArea(override val text: String) : ParsedItem {
    FIRE("FIRE"),
    Precinct1("Precinct 1"),
    Precinct2("Precinct 2"),
    Precinct3("Precinct 3"),
    Precinct4("Precinct 4"),
    Unknown("Unknown");
}

fun parseDispatchArea(text: String): DispatchArea =
    DispatchArea.values().firstOrNull { it.text.lowercase() == text.trim().lowercase() } ?: DispatchArea.Unknown


enum class CallStatus(override val text: String) : ParsedItem {
    Dispatched("Dispatched"),
    Arrived("Arrived"),
    Enroute("Enroute"),
    Unknown("Unknown");
}

fun parseCallStatus(text: String): CallStatus =
    CallStatus.values().firstOrNull { it.text.lowercase() == text.trim().lowercase() } ?: CallStatus.Unknown

enum class Agency(override val text: String) : ParsedItem {
    RPD("RPD"),
    RFD("RFD"),
    Unknown("Unknown");
}

fun parseAgency(text: String): Agency = Agency.values().firstOrNull { it.text.lowercase() == text.trim().lowercase() } ?: Agency.Unknown