package uk.geekhole.hotreddit.ui.feed

import android.content.Intent
import android.net.Uri
import android.view.View
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import uk.geekhole.hotreddit.database.models.Post
import uk.geekhole.hotreddit.databinding.RowHotPostBinding


class PostViewHolder(private val binding: RowHotPostBinding) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {

    companion object {
        const val REDDIT_BASE_URL = "https://reddit.com"
    }

    fun onBind(post: Post) {
        binding.post = post
        binding.executePendingBindings()

        binding.root.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        if (binding.post?.url.isNullOrBlank()) return

        val openBrowser = Intent(Intent.ACTION_VIEW)
        openBrowser.data = Uri.parse(REDDIT_BASE_URL + binding.post?.url)
        startActivity(v.context, openBrowser, null)
    }
}