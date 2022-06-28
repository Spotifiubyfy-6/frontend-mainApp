package com.example.spotifiubyfy01.Messages.albumMessages

import android.content.Context
import android.content.Intent
import android.util.Log
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.example.spotifiubyfy01.Messages.MessagesDataSource
import com.example.spotifiubyfy01.MyRequestQueue
import com.example.spotifiubyfy01.PopUpWindow
import com.example.spotifiubyfy01.search.Artist
import com.example.spotifiubyfy01.search.VolleyCallBack
import org.json.JSONArray
import org.json.JSONObject
import java.time.LocalDateTime

var image_link = "https://he.cecollaboratory.com/public/layouts/images/group-default-logo.png"

class CommentList(
    private val numberOfComments: Int,
    private val callBack: VolleyCallBack<Comment>
) {
    private var commentInserted = 0
    private val commentList = ArrayList<Comment>()
    init {
        val localDate = LocalDateTime.now()
        for (i in 0 until numberOfComments)
            commentList.add(Comment(Artist(0,"kotfu", "cklin"), "hateyou", localDate))
    }

    fun addArtistCommentWithIdToPositionInList(position: Int, comment: Comment) {
        synchronized(this) {
            commentList.set(position, comment)
            commentInserted++
            if (commentInserted == numberOfComments)
                callBack.updateData(commentList)
        }
    }
}

class IdToListOfPositionNCommentMap(
    private val context: Context,
    private val commentArray: JSONArray,
    callBack: VolleyCallBack<Comment>
) {
    private val commentList = CommentList(commentArray.length(), callBack)
    val map = mutableMapOf<Int, ArrayList<Pair<Int, Comment>>>()
    private val shared = Object()
    private var artistCounter = 0
    private var numberOfArtist = 0

    fun fillMapWithComments() {
        synchronized(shared) {
            for (i in 0 until commentArray.length()) {
                val commentObject = JSONObject(commentArray.get(i).toString())
                val userId = commentObject.get("user_id") as Int
                val comment = Comment(
                    Artist(userId, "", ""), commentObject.get("comment") as String,
                    MessagesDataSource.obtainDate(commentObject.get("time") as String)
                )
                this.addCommentNPositionToUserWithId(userId, i, comment)
            }
            numberOfArtist = map.size
        }
    }

    private fun addCommentNPositionToUserWithId(id: Int, position: Int, comment: Comment) {
        if (!map.containsKey(id)) {
            map[id] = ArrayList()
            fetchArtist(id)
        }
        map[id]!!.add(Pair(position, comment))
    }

    private fun replaceWithRightArtist(artist: Artist) {
        synchronized(shared) {}
        val listOfCommentsNPositions = map[artist.id]
        for (i in 0 until listOfCommentsNPositions!!.size)
            listOfCommentsNPositions[i].second.artist = artist
        synchronized(this) {
            artistCounter++
            if (artistCounter == numberOfArtist)
                fillListWithComments()
        }
    }


    private fun fillListWithComments() {
        for (listOfPositionNComment in map.values) {
            for (i in 0 until listOfPositionNComment.size) {
                commentList.addArtistCommentWithIdToPositionInList(listOfPositionNComment[i].first,
                                                                   listOfPositionNComment[i].second)
            }
        }
    }

    private fun fetchArtist(artistId: Int) {
        val url = "https://spotifiubyfy-users.herokuapp.com/users/user_by_id/" + artistId
        val getRequest = JsonObjectRequest(
            Request.Method.GET,
            url, null,
            { jsonArtist ->
                val artistName = jsonArtist.getString("name")
                val artistImage = "profilePictures/"+jsonArtist.getString("photo")
                Log.d("TAG", artistName)
                replaceWithRightArtist(Artist(artistId, artistName, artistImage))
            }
        ) { error ->
            val intent = Intent(context, PopUpWindow::class.java).apply {
                putExtra("popuptext", error.toString())
            }
            context.startActivity(intent)
        }
        MyRequestQueue.getInstance(context).addToRequestQueue(getRequest)
    }

}


class CommentsDataSource {

    companion object {

        fun searchRespectiveArtists(context: Context, commentArray: JSONArray,
                                    callBack: VolleyCallBack<Comment>) {
            val map = IdToListOfPositionNCommentMap(context, commentArray, callBack)
            map.fillMapWithComments()
        }

        fun getCommentsOfAlbum(context: Context, albumId: Int, callBack: VolleyCallBack<Comment>) {
            //val list = ArrayList<Comment>()
            /*list.add(Comment(Artist(13, "El killer", image_link), "Sick album broo!1 keep up the good work homie",
            LocalDateTime.of(2022, 6, 25, 5, 1)))
            list.add(Comment(Artist(13, "N00by", image_link), "this shit wack, listen to my shit, it better",
                LocalDateTime.of(2022, 6, 26, 9, 1)))
            list.add(Comment(Artist(13, "Pedro", image_link), "hello, I just wanna say thank you.",
                LocalDateTime.of(2022, 6, 27, 9, 1)))*/
            val url = "https://spotifiubyfy-messages.herokuapp.com/comments/" + 43 + "?skip=0&limit=100"
            val getRequest = JsonArrayRequest(
                Request.Method.GET,
                url, null,
                { response ->
                    this.searchRespectiveArtists(context, response, callBack)
                }
            ) { error ->
                val intent = Intent(context, PopUpWindow::class.java).apply {
                    putExtra("popuptext", error.toString())
                }
                context.startActivity(intent)
            }
            MyRequestQueue.getInstance(context).addToRequestQueue(getRequest)
        }
    }
}