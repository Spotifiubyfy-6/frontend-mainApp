package com.example.spotifiubyfy01.Messages

import android.content.Context
import com.example.spotifiubyfy01.search.Artist
import com.example.spotifiubyfy01.search.VolleyCallBack

var image_link = "https://he.cecollaboratory.com/public/layouts/images/group-default-logo.png"

class MessagesDataSource {
    companion object {
        fun getChatsOfArtistWithID(context: Context, artist_id: Int,
                                   callBack: VolleyCallBack<Artist>) {
            val artistList = ArrayList<Artist>()
            artistList.add(Artist(1, "The Beatles", image_link))
            artistList.add(Artist(1, "The Rollings", image_link))
            artistList.add(Artist(1, "Adele", image_link))
            artistList.add(Artist(1, "Perrito", image_link))
            artistList.add(Artist(1, "El Bicho", image_link))
            callBack.updateData(artistList)
        }
    }
}