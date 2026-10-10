package io.github.hosseinkarami_dev.focusai.data

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.serialization.Serializable
import kotlin.time.Clock

@Serializable
data class TaskProposal(
    val title: String,
    val priority: String = "Medium",
    val estimateMinutes: Int = 25,
    val deadline: String? = null,
    val certainty: String = "Not provided"
)

interface AiProvider {
    suspend fun extractTasks(note: String): List<TaskProposal>
}

/** Clearly labeled sample output for the UI workflow; no server inference is claimed. */
class SampleAiProvider : AiProvider {
    override suspend fun extractTasks(note: String): List<TaskProposal> {
        val parts = note.split(',', '.', ';', '\n', '•')
            .map { it.trim().removePrefix("and ").removePrefix("I need to ") }
            .filter { it.length >= 4 }
            .distinctBy { it.lowercase() }
        return parts.take(5).map { TaskProposal(title = it.replaceFirstChar { c -> c.uppercase() }) }
    }
}

@Serializable
data class ExtractTasksRequest(
    val schemaVersion: String = "1",
    val requestId: String,
    val text: String
)

@Serializable
data class ExtractTasksResponse(
    val schemaVersion: String,
    val requestId: String,
    val proposals: List<TaskProposal>
)

/** Vendor-neutral backend adapter. The endpoint must come from app configuration, never a model key. */
class RemoteAiProvider(
    private val client: HttpClient,
    private val endpoint: String
) : AiProvider {
    override suspend fun extractTasks(note: String): List<TaskProposal> {
        require(endpoint.startsWith("https://")) { "AI backend must use HTTPS" }
        require(note.isNotBlank() && note.length <= 12_000) { "Note is empty or too long" }
        val requestId = Clock.System.now().toEpochMilliseconds().toString()
        val response = client.post(endpoint) {
            contentType(ContentType.Application.Json)
            setBody(ExtractTasksRequest(requestId = requestId, text = note))
        }.body<ExtractTasksResponse>()
        require(response.schemaVersion == "1") { "Unsupported AI response schema" }
        require(response.requestId == requestId) { "AI response does not match request" }
        return response.proposals.asSequence()
            .map { it.copy(title = it.title.trim()) }
            .filter { it.title.isNotBlank() && it.title.length <= 200 }
            .map { proposal ->
                proposal.copy(estimateMinutes = proposal.estimateMinutes.coerceIn(5, 1440))
            }
            .distinctBy { it.title.lowercase() }
            .take(20)
            .toList()
    }
}
