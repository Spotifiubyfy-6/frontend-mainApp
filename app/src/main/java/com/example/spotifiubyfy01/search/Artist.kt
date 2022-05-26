package com.example.spotifiubyfy01.search
import java.io.Serializable

var image_link = "https://he.cecollaboratory.com/public/layouts/images/group-default-logo.png"

data class Artist(
    var id: Int,
    var username: String,
    var image: String
) : Serializable
