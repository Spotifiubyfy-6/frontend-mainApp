package com.example.spotifiubyfy01.Messages.albumMessages

import android.content.Context
import android.content.Intent
import android.widget.Toast
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
import java.time.ZoneOffset.UTC

class CommentList(
    private val numberOfComments: Int,
    private val callBack: VolleyCallBack<Comment>
) {
    private var commentInserted = 0
    private val commentList = ArrayList<Comment>()
    init {
        val localDate = LocalDateTime.now().atZone(UTC)
        for (i in 0 until numberOfComments)
            commentList.add(Comment(Artist(0,"kotfu", "cklin"), "1", "hateyou", localDate, false, false))
    }

    fun addArtistCommentWithIdToPositionInList(position: Int, comment: Comment) {
        synchronized(this) {
            commentList[position] = comment
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

    fun fillMapWithComments(authorId: Int, ownAlbum: Boolean, id: Int) {
        synchronized(shared) {
            for (i in 0 until commentArray.length()) {
                val commentObject = JSONObject(commentArray.get(i).toString())
                val userId = commentObject.get("user_id") as Int
                val commentId = (commentObject.get("id") as Int).toString()
                val comment = Comment(
                    Artist(userId, "", ""), commentId,
                    commentObject.get("comment") as String,
                    MessagesDataSource.obtainDate(commentObject.get("time") as String),
                            userId == authorId, ownAlbum || (id == userId)
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
        val url = "https://spotifiubyfy-users.herokuapp.com/users/user_by_id/$artistId"
        val getRequest = JsonObjectRequest(
            Request.Method.GET,
            url, null,
            { jsonArtist ->
                val artistName = jsonArtist.getString("name")
                val artistImage = "profilePictures/"+jsonArtist.getString("photo")
                replaceWithRightArtist(Artist(artistId, artistName, artistImage))
            }
        ) {
            Toast.makeText(context, "Cant find user", Toast.LENGTH_SHORT).show()
        }
        MyRequestQueue.getInstance(context).addToRequestQueue(getRequest)
    }

}


class CommentsDataSource {

    companion object {

        private fun searchRespectiveArtists(
            context: Context, commentArray: JSONArray, authorId: Int,
            callBack: VolleyCallBack<Comment>,
            ownAlbum: Boolean,
            userId: Int
        ) {
            val map = IdToListOfPositionNCommentMap(context, commentArray, callBack)
            map.fillMapWithComments(authorId, ownAlbum, userId)
        }

        fun getCommentsOfAlbum(context: Context, albumId: Int, authorId: Int, userId: Int,
                               ownAlbum: Boolean, callBack: VolleyCallBack<Comment>
        ) {
            val url =
                "https://spotifiubyfy-messages.herokuapp.com/comments/$albumId?skip=0&limit=100"
            val getRequest = JsonArrayRequest(
                Request.Method.GET,
                url, null,
                { response ->
                    if (response.length() == 0) {
                        callBack.updateData(ArrayList())
                        return@JsonArrayRequest
                    }
                    this.searchRespectiveArtists(context, response, authorId, callBack, ownAlbum, userId)
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