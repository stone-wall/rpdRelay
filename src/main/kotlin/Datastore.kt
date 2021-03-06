import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import java.io.File

class Datastore(private val store: File = File("calls")) {

    private val serializer = Call.serializer()

    init {
        if (!store.exists()) {
            store.createNewFile()
        }
    }
    suspend fun save(call: Call) {
        withContext(Dispatchers.IO) {
            launch {
                store.appendText(Json.encodeToString(serializer, call) + "\n")
            }
        }
    }
}