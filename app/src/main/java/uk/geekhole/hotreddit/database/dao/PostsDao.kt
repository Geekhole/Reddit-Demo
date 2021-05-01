package uk.geekhole.hotreddit.database.dao

import androidx.room.Dao
import androidx.room.Query
import io.reactivex.Flowable
import uk.geekhole.hotreddit.database.models.Post

@Dao
interface PostsDao : BaseDao<Post> {

    @Query("SELECT * FROM posts")
    fun getHotPosts(): Flowable<List<Post>>
}