package uk.geekhole.hotreddit.database

import androidx.room.Database
import androidx.room.RoomDatabase
import uk.geekhole.hotreddit.database.dao.PostsDao
import uk.geekhole.hotreddit.database.models.Post

@Database(entities = [Post::class], version = 1, exportSchema = true)
abstract class RedditDatabase : RoomDatabase() {

    abstract fun postsDao(): PostsDao
}