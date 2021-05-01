package uk.geekhole.hotreddit.repositories

import io.reactivex.Flowable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import uk.geekhole.hotreddit.R
import uk.geekhole.hotreddit.database.dao.PostsDao
import uk.geekhole.hotreddit.database.models.Post
import uk.geekhole.hotreddit.global.Mapper
import uk.geekhole.hotreddit.global.PreferenceManager
import uk.geekhole.hotreddit.global.RxThrowable
import uk.geekhole.hotreddit.networking.models.ApiPost
import uk.geekhole.hotreddit.networking.services.PostsService
import javax.inject.Inject

class PostRepository @Inject constructor(private val hotPostsService: PostsService, private val postDao: PostsDao, private val preferenceManager: PreferenceManager) {

    private val disposables = CompositeDisposable()

    companion object {
        // static mapper for converting from an API post to one we can save into the db
        val postMapper = object : Mapper<ApiPost, Post> {
            override fun map(input: ApiPost): Post {
                return Post(input)
            }

            override fun mapList(input: List<ApiPost>): List<Post> {
                return input.map { Post(it) }
            }
        }
    }

    // Database first approach. When we subscribe to get the posts it will also fire off the network request and save the posts to the database, which in turn causes the flowable to be updated.
    fun getHotPosts(): Flowable<List<Post>> {
        return postDao.getHotPosts()
            .subscribeOn(Schedulers.io())
            .doOnSubscribe {
                getMorePostsFromApi()
            }
    }

    fun getMorePostsFromApi(nextPageId: String? = preferenceManager[PreferenceManager.KEY_PAGING_REFERENCE]) {
        val disposable = hotPostsService.getHotPosts(nextPageId)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io()) // All work for obtaining the data from api and then saving it should be on the io thread.
            .toObservable()
            .switchMapCompletable { response ->
                if (!response.isSuccessful || response.code() != 200) {
                    // Throw an error to be caught by the global error handler and display the message
                    throw(RxThrowable(R.string.error_fetching_posts))
                }

                // Save the reference we need to get the next page of results.
                preferenceManager[PreferenceManager.KEY_PAGING_REFERENCE] = response.body()?.data?.after

                val posts = response.body()?.data?.children?.mapNotNull { it.data } ?: return@switchMapCompletable null

                // We always insert because inserting will just overwrite anything that already existed with the same primary key.
                postDao.insert(*postMapper.mapList(posts).toTypedArray())
                    .subscribeOn(Schedulers.io())
            }
            .subscribe({}, {
                // Re-throw the error to be caught by the global error handler and display the message
                throw(it)
            })

        disposables.add(disposable)
    }

    fun refresh() {
        getMorePostsFromApi(null)
    }
}