package app

import io.javalin.Javalin

fun main() {

    val app = Javalin.create {
        // TODO
    }.start(7000)

    app.get("/todo") { ctx ->
        // TODO
    }

}