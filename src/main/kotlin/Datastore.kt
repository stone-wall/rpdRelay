import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.UnstableDefault
import kotlinx.serialization.json.Json
import java.io.File
import java.nio.file.Files

class Datastore(private val store: File = File("calls")) {

    private val serializer = Call.serializer()

    init {
        if (!store.exists()) {
            store.createNewFile()
        }
    }

    @OptIn(UnstableDefault::class)
    suspend fun save(call: Call) {
        withContext(Dispatchers.IO) {
            launch {
                store.appendText(Json.stringify(serializer, call) + "\n")
            }
        }
    }
}