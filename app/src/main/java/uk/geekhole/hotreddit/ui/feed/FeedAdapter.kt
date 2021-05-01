package uk.geekhole.hotreddit.ui.feed

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import uk.geekhole.hotreddit.database.models.Post
import uk.geekhole.hotreddit.databinding.RowHotPostBinding

class FeedAdapter(private val data: MutableList<Post>) : RecyclerView.Adapter<PostViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = RowHotPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.onBind(data[position])
    }

    override fun getItemCount() = data.size

    /** Calculates the differences in the data rather than updating the entire recyclerview which could be a costly processing expense. This also allows us to get nicer animations
     * when something is added or removed and can help remove "jank" when the user is scrolling through the list. */
    fun updateData(data: List<Post>) {
        val result = DiffUtil.calculateDiff(PostsDiffCallback(this.data, data.toMutableList()))
        this.data.clear()
        this.data.addAll(data)
        result.dispatchUpdatesTo(this)
    }

    private class PostsDiffCallback(private var oldList: MutableList<Post>, private var newList: MutableList<Post>) : DiffUtil.Callback() {
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean = oldList[oldItemPosition].areContentsTheSame(newList[newItemPosition])

        override fun getOldListSize(): Int = oldList.count()

        override fun getNewListSize(): Int = newList.count()

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean = oldList[oldItemPosition].areContentsTheSame(newList[newItemPosition])
    }
}