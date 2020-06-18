import java.time.Instant
import kotlin.time.Duration
import kotlin.time.ExperimentalTime

data class Metrics @ExperimentalTime constructor(
    val timeStamp: Long = Instant.now().toEpochMilli(),
    val totalActiveCalls: Int,
    val activeCalls: List<Call>
) {
}