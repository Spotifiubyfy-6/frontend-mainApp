package com.example.spotifiubyfy01.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.spotifiubyfy01.R
import com.google.android.gms.fido.fido2.api.common.RequestOptions

class ArtistRecyclerAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var items: List<Artist> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ArtistViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.layout_artist_list_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ArtistViewHolder -> {
                holder.bind(items[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun submitList(artistList: List<Artist>) {
        items = artistList
    }

    class ArtistViewHolder constructor(
        itemView: View
    ): RecyclerView.ViewHolder(itemView) {
        val artist_image: ImageView =  itemView.findViewById(R.id.artist_image)
        val artist_username: TextView =  itemView.findViewById(R.id.artist_name)

        fun bind(artist: Artist) {
            artist_username.setText(artist.username)

        /*    val requestOptions = RequestOptions()
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)
*/
            Glide.with(itemView.context)
                .load(artist.image)
                .into(artist_image)
        }
    }
}