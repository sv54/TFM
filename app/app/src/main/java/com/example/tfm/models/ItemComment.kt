package com.example.tfm.models

data class ItemComment(
    var id: Int = -1,
    var username: String = "none",
    var comment: String = "none",
    var permissionExtra: Int = 0,
    var days: Int = -1,
    var expenses: Int = -1,
    var rate: Int = -1,
    var userImage: String = ""
)
