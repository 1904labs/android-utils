package com.labs1904.tracker.covid

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

object CovidFactory {

	private const val BASE_URL = "https://covidtracking.com/api/v1/"

	private val okHttp: OkHttpClient by lazy {
		OkHttpClient.Builder()
			.addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
			.build()
	}

	private val moshi: Moshi by lazy {
		Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
	}

	private val retrofit: Retrofit by lazy {
		Retrofit.Builder()
			.baseUrl(BASE_URL)
			.client(okHttp)
			.addCallAdapterFactory(RxJava3CallAdapterFactory.create())
			.addConverterFactory(MoshiConverterFactory.create(moshi))
			.build()
	}

	val covidApi: CovidApi by lazy {
		retrofit.create(CovidApi::class.java)
	}

	val covidRepo: CovidRepo by lazy {
		CovidRepository()
	}
}
