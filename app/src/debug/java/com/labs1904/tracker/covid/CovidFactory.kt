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

	private var okHttp: OkHttpClient = OkHttpClient.Builder()
		.addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
		.build()

	private var moshi: Moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

	private var retrofit: Retrofit = Retrofit.Builder()
		.baseUrl(BASE_URL)
		.client(okHttp)
		.addCallAdapterFactory(RxJava3CallAdapterFactory.create())
		.addConverterFactory(MoshiConverterFactory.create(moshi))
		.build()

	var covidApi: CovidApi = retrofit.create(CovidApi::class.java)

	var covidRepo: CovidRepo = CovidRepository()

}
