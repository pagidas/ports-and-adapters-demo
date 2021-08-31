package org.example.demo

import org.testcontainers.containers.MongoDBContainer
import org.testcontainers.utility.DockerImageName

object TestContainersBase {

    private const val MONGODB_PORT = 27017

    private val mongoDbContainer = MongoDBContainer(DockerImageName.parse("mongo:4.0.10")).apply {
        withExposedPorts(MONGODB_PORT)
    }

    operator fun invoke() {
        mongoDbContainer.start()
    }

    val mongoDbContainerUrl: String by lazy { "mongodb://localhost:${mongoDbContainer.getMappedPort(MONGODB_PORT)}" }
}