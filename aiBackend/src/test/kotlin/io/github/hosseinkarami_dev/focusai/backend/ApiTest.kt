package io.github.hosseinkarami_dev.focusai.backend

import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.server.testing.testApplication
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ApiTest {
    @Test fun healthEndpointResponds() = testApplication {
        application { focusAiModule(TaskExtractor { emptyList() }) }
        assertEquals(HttpStatusCode.OK, client.get("/health").status)
    }

    @Test fun extractEndpointReturnsVersionedProposalsAndNormalizesInput() = testApplication {
        application { focusAiModule(TaskExtractor { listOf(TaskProposal("  Plan the week  ", estimateMinutes = 9_999)) }) }
        val response = client.post("/v1/ai/extract-tasks") {
            contentType(ContentType.Application.Json)
            setBody("""{"schemaVersion":"1","requestId":"test-1","text":"Plan my week"}""")
        }
        assertEquals(HttpStatusCode.OK, response.status)
        val body = response.bodyAsText()
        assertTrue(body.contains("\"requestId\":\"test-1\""))
        assertTrue(body.contains("\"title\":\"Plan the week\""))
        assertTrue(body.contains("\"estimateMinutes\":1440"))
    }

    @Test fun unsupportedSchemaIsRejected() = testApplication {
        application { focusAiModule(TaskExtractor { emptyList() }) }
        val response = client.post("/v1/ai/extract-tasks") {
            contentType(ContentType.Application.Json)
            setBody("""{"schemaVersion":"2","requestId":"test-1","text":"Hello"}""")
        }
        assertEquals(HttpStatusCode.BadRequest, response.status)
    }

    @Test fun perClientRateLimitIsEnforced() = testApplication {
        application { focusAiModule(TaskExtractor { emptyList() }, InMemoryRateLimiter(maxRequests = 1)) }
        repeat(2) { index ->
            val response = client.post("/v1/ai/extract-tasks") {
                contentType(ContentType.Application.Json)
                setBody("""{"schemaVersion":"1","requestId":"test-$index","text":"Plan my week"}""")
            }
            assertEquals(if (index == 0) HttpStatusCode.OK else HttpStatusCode.TooManyRequests, response.status)
        }
    }
}
