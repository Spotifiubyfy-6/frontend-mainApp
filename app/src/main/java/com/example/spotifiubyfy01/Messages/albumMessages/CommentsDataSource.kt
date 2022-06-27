package com.example.spotifiubyfy01.Messages.albumMessages

import android.content.Context
import com.example.spotifiubyfy01.search.Artist
import com.example.spotifiubyfy01.search.VolleyCallBack
import java.time.LocalDateTime

var image_link = "https://he.cecollaboratory.com/public/layouts/images/group-default-logo.png"

class CommentsDataSource {

    companion object {

        fun getCommentsOfAlbum(context: Context, albumId: Int, callBack: VolleyCallBack<Comment>) {
            val list = ArrayList<Comment>()
            list.add(Comment(Artist(13, "El killer", image_link), "Sick album broo!1 keep up the good work homie",
            LocalDateTime.of(2022, 6, 25, 5, 1)))
            list.add(Comment(Artist(13, "N00by", image_link), "this shit wack, listen to my shit, it better",
                LocalDateTime.of(2022, 6, 26, 9, 1)))
            list.add(Comment(Artist(13, "Pedro", image_link), "hello, I just wanna say thank you.",
                LocalDateTime.of(2022, 6, 27, 9, 1)))
            callBack.updateData(list)
        }
    }
}