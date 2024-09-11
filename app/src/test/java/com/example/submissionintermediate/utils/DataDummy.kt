package com.example.submissionintermediate.utils

import com.example.submissionintermediate.database.response.ListStoryItem

object DataDummy {
    fun generateDummyMVModel(): List<ListStoryItem> {
        val newList: MutableList<ListStoryItem> = arrayListOf()
        for (i in 0..10){
            val news = ListStoryItem(
                "https://static.wikia.nocookie.net/boboiboy/images/d/d8/Galaxy_1-16.png",
                "2023-11-09T09:49:49",
                "Tapops",
                "Description $i",
                i.toDouble(),
                "id_$i",
                i.toDouble(),
            )
            newList.add(news)
        }
        return newList
    }
}