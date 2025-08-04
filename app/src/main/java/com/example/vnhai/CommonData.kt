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
    var image: Int,
    val listMusic: List<Music>
)

var myPlaylist = listOf(Playlist("MyPlaylist", R.drawable.music1, listOf<Music>()))
var listUser = mutableListOf(User("hai", "hai@apero.vn"))