package org.example.demo

import org.testcontainers.containers.MongoDBContainer
import org.testcontainers.utility.DockerImageName

object MongoDbDockerContainer {

    private const val MONGODB_PORT = 27017

    private val mongoDbContainer = MongoDBContainer(DockerImageName.parse("mongo:4.0.10")).apply {
        withExposedPorts(MONGODB_PORT)
    }

    init {
        mongoDbContainer.start()
    }

    val url: String by lazy { "mongodb://localhost:${mongoDbContainer.getMappedPort(MONGODB_PORT)}" }
    val port: Int by lazy { mongoDbContainer.getMappedPort(MONGODB_PORT) }
}