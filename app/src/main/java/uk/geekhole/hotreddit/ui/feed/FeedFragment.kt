package uk.geekhole.hotreddit.ui.feed

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import uk.geekhole.hotreddit.database.models.Post
import uk.geekhole.hotreddit.databinding.FragmentHotFeedBinding

@AndroidEntryPoint
class FeedFragment : Fragment() {

    private lateinit var binding: FragmentHotFeedBinding
    private val viewModel: FeedViewModel by viewModels()
    private val disposables = CompositeDisposable()

    private var adapter: FeedAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentHotFeedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setRecyclerViewScrollListener()
        setRefreshListener()

        disposables.add(viewModel.posts
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { posts ->
                    createOrUpdateAdapter(posts)
                    if (binding.hotPostList.adapter == null) binding.hotPostList.adapter = adapter
                    binding.refresh.isRefreshing = false
                })
    }

    private fun setRecyclerViewScrollListener() {
        binding.hotPostList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (newState != RecyclerView.SCROLL_STATE_IDLE) return
                val lastPosition = (binding.hotPostList.layoutManager as? LinearLayoutManager)?.findLastVisibleItemPosition() ?: 0

                // If we have less than ten posts left to go before the end of our data then go get some more posts. This way it should be quicker / more seamless to the user rather than waiting
                // until we get to the complete bottom of the list.
                if ((binding.hotPostList.adapter?.itemCount ?: 0) - lastPosition < 10) viewModel.getNextPage()
            }
        })
    }

    private fun setRefreshListener() {
        binding.refresh.setOnRefreshListener {
            viewModel.refresh()
        }
    }

    private fun createOrUpdateAdapter(posts: List<Post>) {
        if (adapter == null) {
            adapter = FeedAdapter(posts.toMutableList())
        } else {
            adapter?.updateData(posts)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.dispose()
        disposables.clear()
    }

}