package com.labs1904.tracker.covid

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

object CovidFactory {

	private const val BASE_URL = "covidtracking.com/api/v1/"

	private val okHttp: OkHttpClient by lazy {
		OkHttpClient.Builder()
			.addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
			.build()
	}

	private val retrofit: Retrofit by lazy {
		Retrofit.Builder()
			.baseUrl(BASE_URL)
			.client(okHttp)
			.addCallAdapterFactory(RxJava3CallAdapterFactory.create())
			.addConverterFactory(MoshiConverterFactory.create())
			.build()
	}

	val covidApi: CovidApi by lazy {
		retrofit.create(CovidApi::class.java)
	}

	val covidRepo: CovidRepo by lazy {
		CovidRepository()
	}

}
