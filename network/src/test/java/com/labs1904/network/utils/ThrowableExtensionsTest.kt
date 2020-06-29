package com.labs1904.network.utils

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response

class ThrowableExtensionsTest {

    @Test
    fun `is401Unauthorized returns false when it is not an HttpException`() {
        val testObject = RuntimeException()

        assertFalse(testObject.is401Unauthorized())
    }

    @Test
    fun `is401Unauthorized returns false when HttpException but not a 401`() {
        val response: Response<String> = Response.error(
            403,
            "".toResponseBody(
                "application/json".toMediaTypeOrNull()
            )
        )
        val testObject = HttpException(response)

        assertFalse(testObject.is401Unauthorized())
    }

    @Test
    fun `is401Unauthorized returns true when HttpException with a 401 code`() {
        val response: Response<String> = Response.error(
            401,
            "".toResponseBody(
                "application/json".toMediaTypeOrNull()
            )
        )
        val testObject = HttpException(response)

        assertTrue(testObject.is401Unauthorized())
    }
}