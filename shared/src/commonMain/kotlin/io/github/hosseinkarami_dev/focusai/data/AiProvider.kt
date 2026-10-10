package io.github.hosseinkarami_dev.focusai.data

import kotlinx.serialization.Serializable

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
