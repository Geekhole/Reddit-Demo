package uk.geekhole.hotreddit.ui

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import uk.geekhole.hotreddit.R

@BindingAdapter("imageUrl")
fun loadPostThumnailImage(view: ImageView, url: String?) {
    Glide.with(view)
        .load(url.orEmpty())
        .placeholder(R.drawable.icn_thumbnail_placeholder)
        .into(view)
}