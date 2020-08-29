package com.ks.flowerrecognition

class Flower {
    var id: Int = 0
    var name: String? = null
    var description: String? = null
    var photo: String? = null

    constructor(){}

    constructor(id: Int, name: String?, description: String?, photo: String?) {
        this.id = id
        this.name = name
        this.description = description
        this.photo = photo
    }


}