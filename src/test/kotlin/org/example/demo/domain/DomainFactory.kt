package org.example.demo.domain

private val passNames = listOf(
    "Tesco - ClubCard",
    "Sainsbury - Nectar",
    "M&S - Sparks",
    "Waitrose - myWaitrose"
)

fun aPass(id: PassId = PassId.random(), points: Int = 0) =
    Pass(id, passNames.random(), "John Doe", points)
