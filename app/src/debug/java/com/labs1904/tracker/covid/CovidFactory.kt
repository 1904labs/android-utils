package com.labs1904.tracker.covid

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

object CovidFactory {

	private const val BASE_URL = "covidtracking.com/api/v1/"

	private var okHttp: OkHttpClient = OkHttpClient.Builder()
		.addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
		.build()

	private var retrofit: Retrofit = Retrofit.Builder()
		.baseUrl(BASE_URL)
		.client(okHttp)
		.addCallAdapterFactory(RxJava3CallAdapterFactory.create())
		.addConverterFactory(MoshiConverterFactory.create())
		.build()

	var covidApi: CovidApi = retrofit.create(CovidApi::class.java)

	var covidRepo: CovidRepo = CovidRepository()

}
