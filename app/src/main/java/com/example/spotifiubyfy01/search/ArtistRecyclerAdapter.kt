package com.example.spotifiubyfy01.search

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
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
        val artistImage: ImageView =  itemView.findViewById(R.id.artist_image)
       /* val button: ImageButton = itemView.findViewById(R.id.artist_button)
        button.setOnclickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                {Log.d("TAG", "message")})
            }
        }*/
        val artistUsername: TextView =  itemView.findViewById(R.id.artist_name)
        fun bind(artist: Artist) {
            artistUsername.setText(artist.username)
        /*    val requestOptions = RequestOptions()
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)*/
            Glide.with(itemView.context)
                .load(artist.image)
                .into(artistImage)
        }
        val button: ImageView =  itemView.findViewById(R.id.artist_image)
    }
}