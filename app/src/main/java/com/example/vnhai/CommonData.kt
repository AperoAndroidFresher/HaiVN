package com.example.vnhai

data class User(
    val username: String,
    val password: String
)

data class Music(
    val link: String,
    val name: String,
    val author: String,
    val duration: String,
)

data class Playlist(
    var name: String,
    var image: String,
    val listMusic: MutableList<Music>
)

var myPlaylistMusic = mutableListOf<Playlist>()
var listUser = mutableListOf(User("hai", "hai@apero.vn"))