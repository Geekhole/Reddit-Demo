package uk.geekhole.hotreddit.ui.feed

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import uk.geekhole.hotreddit.repositories.PostRepository
import javax.inject.Inject

@HiltViewModel
class FeedViewModel @Inject constructor(private val postRepository: PostRepository) : ViewModel() {

    val posts = postRepository.getHotPosts()
    fun getNextPage() = postRepository.getMorePostsFromApi()
    fun refresh() = postRepository.refresh()
}