package com.example.spotifiubyfy01.search

class DataSource{
    companion object {
        fun createDataSet(slice: String): List<Artist> {
            val list = ArrayList<Artist>()
            if (slice.isEmpty())
                return list
            list.add(Artist("The Beatles", image_link))
            list.add(Artist("The Rolling Stones", image_link))
            list.add(Artist("Metallica", image_link))
            list.add(Artist("Green Day", image_link))
            list.add(Artist("The Smiths", image_link))
            list.add(Artist("Dua Lipa", image_link))
            list.add(Artist("Canserbero", image_link))
            list.add(Artist("TuPac", image_link))
            list.add(Artist("Eminem", image_link))
            list.add(Artist("Muse", image_link))
            list.add(Artist("Coldplay", image_link))
            list.add(Artist("1975", image_link))
            list.add(Artist("Led Zeppelin", image_link))
            list.add(Artist("Ariana Grande", image_link))
            list.add(Artist("Juan Luis Guerra", image_link))
            list.add(Artist("Biggie", image_link))
            return list.filter { artist -> artist.username.contains(slice, ignoreCase = true) }
        }
    }
}