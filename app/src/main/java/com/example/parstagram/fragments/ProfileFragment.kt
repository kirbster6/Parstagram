package com.example.parstagram.fragments

import android.util.Log
import com.example.parstagram.Post
import com.parse.FindCallback
import com.parse.ParseException
import com.parse.ParseQuery
import com.parse.ParseUser

class ProfileFragment: FeedFragment() {
    override fun queryPosts() {
        val query: ParseQuery<Post> = ParseQuery.getQuery(Post::class.java)
        query.include(Post.KEY_USER)
        //Only return posts from the current user.
        query.whereEqualTo(Post.KEY_USER, ParseUser.getCurrentUser())
        //Below makes it so that it shows based off of when the post was made.
        query.addDescendingOrder("createdAt");
        query.findInBackground(object : FindCallback<Post> {
            override fun done(posts: MutableList<Post>?, e: ParseException?) {
                if (e != null) {
                    Log.e(TAG, "Error Fetching Posts")
                } else {
                    if (posts != null) {
                        for (post in posts) {
                            Log.i(
                                TAG,
                                "POST: " + post.getDescription() + ", username: " + post.getUser()?.username
                            )
                        }

                        allPosts.addAll(posts)
                        adapter.notifyDataSetChanged()
                    }
                }
            }
        })
    }
}