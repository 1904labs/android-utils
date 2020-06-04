package com.labs1904.test_utils.data

import kotlin.random.Random

private val CHARACTERS: List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')

/**
* @param  from (optional) Starting value (inclusive)
* @param  to (optional) Ending value (exclusive)
* @return A random integer in the specified range.
*/
fun randomInt(from: Int = 0, to: Int = Int.MAX_VALUE): Int = Random.nextInt(from, to)

/**
 * @param  from (optional) Starting value (inclusive)
 * @param  to (optional) Ending value (exclusive)
 * @return A random double in the specified range.
 */
fun randomDouble(from: Double = 0.0, to: Double = Double.MAX_VALUE): Double =
    Random.nextDouble(from, to)

/**
 * @param  from (optional) Starting value (inclusive)
 * @param  to (optional) Ending value (exclusive)
 * @return A random long in the specified range.
 */
fun randomLong(from: Long = 0L, to: Long = Long.MAX_VALUE): Long = Random.nextLong(from, to)

/**
 * @param  min (optional) Minimum value (inclusive)
 * @param  max (optional) Maximum value (inclusive)
 * @return A random float in the specified range.
 */
fun randomFloat(min: Float = 0F, max: Float = Float.MAX_VALUE): Float =
    Random.nextFloat() * (max - min) + min

/**
 * @return A random character in the character set (a-z, A-Z, 0-9)
 */
fun randomCharacter(): Char = CHARACTERS[randomInt(0, CHARACTERS.size)]

/**
 * @param  minLength (optional) Minimum length (inclusive)
 * @param  maxLength (optional) Maximum length (inclusive)
 * @return A random string consisting of characters within the character set (a-z, A-Z, 0-9) of a
 * random length between the minimum and maximum values specified.
 */
fun randomString(minLength: Int = 5, maxLength: Int = 30): String {
    val randomLength = randomInt(minLength, maxLength + 1)
    return (1..randomLength)
        .map { randomInt(0, CHARACTERS.size) }
        .map(CHARACTERS::get)
        .joinToString("")
}

/**
 * @return A random boolean
 */
fun randomBoolean(): Boolean = randomInt() % 2 == 1
