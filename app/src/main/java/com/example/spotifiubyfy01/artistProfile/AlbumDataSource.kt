package com.example.spotifiubyfy01.artistProfile

var image_link = "https://he.cecollaboratory.com/public/layouts/images/group-default-logo.png"

class AlbumDataSource {
    companion object {
        fun createAlbumList(artist_id: Int) : List<Album> {
            val list = ArrayList<Album>()
            list.add(Album("Sgt Peppers Lonely Hearts Club Band", image_link))
            list.add(Album("Revolver", image_link))
            list.add(Album("Abbey Road", image_link))
            list.add(Album("Please Please me", image_link))
            list.add(Album("Rubber Soul", image_link))
            list.add(Album("Help!", image_link))
            list.add(Album("White Album", image_link))
            return list
        }
    }
}