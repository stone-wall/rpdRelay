import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.serialization.UnstableDefault
import kotlinx.serialization.json.Json
import java.io.File

class Datastore(private val store: File = File("calls")) {

    private val serializer = Call.serializer()

    val police by lazy {
        readAll().filter { it.agency == "RPD" }
    }

    init {
        if (!store.exists()) {
            store.createNewFile()
        }
    }

    @OptIn(UnstableDefault::class)
    suspend fun saveCall(call: Call) {
        withContext(Dispatchers.IO) {
            launch {
                store.appendText(Json.stringify(serializer, call) + "\n")
            }
        }
    }

    private fun byPrecinct(): Map<String, List<Call>> {
        val allCalls = readAll()
        return allCalls.groupBy {
            it.dispatchArea
        }
    }
    fun byUnit(): Map<String, List<Call>> {
        return readAll().groupBy { it.unit }
    }

    fun precinct(precinct: Precinct): List<Call>? {
        return byPrecinct()[precinct.asString]
    }

    fun unique(): HashSet<Call> {
        return readAll().toHashSet()
    }

    @OptIn(UnstableDefault::class)
    fun readAll(): List<Call> {
        val calls = mutableListOf<Call>()
        store.readLines().forEach {
            calls.add(Json.parse(serializer, it))
        }
        return calls
    }

    fun combine(other: Datastore) {
        runBlocking {
            other.readAll().forEach {
                saveCall(it)
            }
        }
    }

    companion object {
        @ExperimentalCoroutinesApi
        suspend fun conjoin(first: Datastore,
                            second: Datastore,
                            newName: String = "${first.store.name}.combined.with.${second.store.name}"
        ): Datastore {
            val output = Datastore(File(newName))
            withContext(Dispatchers.IO) {
                merge(flowFromDatastore(first), flowFromDatastore(second))
                    .collect {
                    output.saveCall(it)
                }
            }
            return output
        }

        @OptIn(UnstableDefault::class)
        private fun CoroutineScope.flowFromDatastore(first: Datastore): Flow<Call> {
            return flow {
                first.store.forEachLine {
                    launch {
                        emit(Json.parse(first.serializer, it))
                    }
                }
            }
        }
    }
}

fun main() {
    val store = Datastore(File("calls"))
    println(store.readAll().filter { it.agency == "RPD" }.groupBy { it.unit }.forEach { (unit, calls) ->
        println("$unit: ${calls.size} calls")

    })
}