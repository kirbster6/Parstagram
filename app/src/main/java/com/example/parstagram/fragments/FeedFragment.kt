package com.example.parstagram.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.parstagram.MainActivity
import com.example.parstagram.Post
import com.example.parstagram.PostAdapter
import com.example.parstagram.R
import com.parse.FindCallback
import com.parse.ParseException
import com.parse.ParseQuery


open class FeedFragment : Fragment() {

    lateinit var postRecyclerView: RecyclerView

    lateinit var adapter: PostAdapter

    var allPosts: MutableList<Post> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_feed, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //This is where we set up our views and onclick listeners

        postRecyclerView = view.findViewById(R.id.postRecyclerView)

        //Steps to Populate RecyclerView
        //1. Create Layout for each row (.xml file)
        //2. Create data source for each row (This is the post class, already created)
        //3. Create adapter that will bridge data and row layout (Post Adapter Class)
        //4. Set adapter on recycler view
        adapter = PostAdapter(requireContext(), allPosts)
        postRecyclerView.adapter = adapter
        //5. Set layout manager on RecyclerView
        postRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        queryPosts()
    }

    open fun queryPosts() {

        val query: ParseQuery<Post> = ParseQuery.getQuery(Post::class.java)
        query.include(Post.KEY_USER)
        //Below makes it so that it shows based off of when the post was made.
        query.addDescendingOrder("createdAt");
        query.findInBackground(object: FindCallback<Post> {
            override fun done(posts: MutableList<Post>?, e: ParseException?) {
                if (e != null) {
                    Log.e(TAG, "Error Fetching Posts")
                }else{
                    if (posts != null) {
                        for (post in posts) {
                            Log.i(TAG, "POST: " + post.getDescription() + ", username: "+ post.getUser()?.username)
                        }

                        allPosts.addAll(posts)
                        adapter.notifyDataSetChanged()
                    }
                }
            }
        })
    }

    companion object {
        const val TAG = "FeedFragment"
    }
}