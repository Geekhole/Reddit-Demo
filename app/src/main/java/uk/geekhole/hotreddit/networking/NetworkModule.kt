package uk.geekhole.hotreddit.networking

import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import uk.geekhole.hotreddit.networking.services.PostsService
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    // Use the same Gson instance for all our deserialisation and serialisation needs. Keep it consistent.
    @Provides
    @Singleton
    fun provideGson(): Gson = GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create()

    // Gson factory for Retrofit.
    @Provides
    @Singleton
    fun provideGsonConverterFactory(gson: Gson): GsonConverterFactory = GsonConverterFactory.create(gson)

    @Provides
    @Singleton
    fun provideRetrofit(converter: GsonConverterFactory): Retrofit = Retrofit.Builder()
        .baseUrl("https://reddit.com/")
        .addConverterFactory(converter)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()

    @Provides
    @Singleton
    fun provideHotPostsService(retrofit: Retrofit): PostsService = retrofit.create(PostsService::class.java)
}