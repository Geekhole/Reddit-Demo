package uk.geekhole.hotreddit.database

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context) = Room.databaseBuilder(context, RedditDatabase::class.java, "hot_reddit_database").build()

    @Provides
    @Singleton
    fun provideHotPostDao(db: RedditDatabase) = db.postsDao()
}