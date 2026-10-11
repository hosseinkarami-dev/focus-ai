package io.github.hosseinkarami_dev.focusai

import kotlin.test.Test
import kotlin.test.assertEquals
import io.github.hosseinkarami_dev.focusai.data.SampleAiProvider
import kotlinx.coroutines.runBlocking

class SharedLogicAndroidHostTest {

    @Test
    fun sampleSuggestionsAreDistinctAndCapped() = runBlocking {
        val proposals = SampleAiProvider().extractTasks(
            "I need to plan the launch, write the update, review the launch, prepare the demo, and send the invite"
        )
        assertEquals(5, proposals.size)
        assertEquals(5, proposals.map { it.title.lowercase() }.distinct().size)
    }
}
