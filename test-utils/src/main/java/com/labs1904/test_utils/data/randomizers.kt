package com.labs1904.test_utils.data

import kotlin.random.Random

private val CHARACTERS: List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')

fun randomInt(from: Int = 0, to: Int = Int.MAX_VALUE): Int = Random.nextInt(from, to)

fun randomDouble(from: Double = 0.0, to: Double = Double.MAX_VALUE): Double =
    Random.nextDouble(from, to)

fun randomLong(from: Long = 0L, to: Long = Long.MAX_VALUE): Long = Random.nextLong(from, to)

fun randomFloat(min: Float = 0F, max: Float = Float.MAX_VALUE): Float =
    Random.nextFloat() * (max - min) + min

fun randomCharacter(): Char = CHARACTERS[randomInt(0, CHARACTERS.size)]

fun randomString(minLength: Int = 5, maxLength: Int = 30): String {
    val randomLength = randomInt(minLength, maxLength + 1)
    return (1..randomLength)
        .map { randomInt(0, CHARACTERS.size) }
        .map(CHARACTERS::get)
        .joinToString("")
}

fun randomBoolean(): Boolean = randomInt() % 2 == 1
