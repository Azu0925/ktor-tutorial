package com.example.plugins

import com.fasterxml.jackson.databind.SerializationFeature
import io.ktor.routing.*
import io.ktor.http.*
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.jackson.*
import io.ktor.response.*
import io.ktor.request.*
import java.util.*

data class Snippet(val text: String)

data class PostSnippet(val snippet: PostSnippet.Text){
    data class Text(val text: String)
}

val snippets = Collections.synchronizedList(mutableListOf(
    Snippet("hello"),
    Snippet("world")
))
fun Application.configureRouting() {
    install(ContentNegotiation){
        jackson{
            enable(SerializationFeature.INDENT_OUTPUT)
        }
    }
    // Starting point for a Ktor app:
    routing {
        get("/") {
            call.respondText("Hello World!")
        }
        get("/snippets"){
            call.respond(mapOf("snippets" to synchronized(snippets) { snippets.toList()}))
        }
        post("/snippets"){
            val post = call.receive<PostSnippet>()
            snippets += Snippet(post.snippet.text)
            call.respond(mapOf("OK" to true))
        }
    }
    routing {
    }
}
