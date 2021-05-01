package uk.geekhole.hotreddit.database.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import uk.geekhole.hotreddit.networking.models.ApiPost

@Entity(tableName = "posts")
class Post(@PrimaryKey val id: String, val title: String, val description: String, val author: String, val url: String, val thumbnail: String?) {

    // Constructor to use in the mapping between ApiPost and Post
    constructor(post: ApiPost) : this(post.id, post.title, post.selftext, post.author, post.permalink, post.thumbnail)

    fun areContentsTheSame(other: Post): Boolean {
        return other.id == id && other.title == title && other.description == description
    }
}