package com.devin.storykw.testing

import com.devin.storykw.backend.conn.ListStoryItem

object Data {

    fun genDummyListOfStory(): List<ListStoryItem> {
        val listItem: MutableList<ListStoryItem> = arrayListOf()
        for (i in 0..100) {
            val listStory = ListStoryItem(
                "Photo URL Story $i",
                "2022-02-22T22:22:22Z",
                "Story $i ",
                "Story Description",
                0.1,
                "$i",
                0.3
            )
            listItem.add(listStory)
        }
        return listItem
    }
}