package com.example.buttongame.Fragments


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.buttongame.LOG
import com.example.buttongame.MainAdapter
import com.example.buttongame.Networking.Networking

import com.example.buttongame.R
import com.example.buttongame.RecyclerviewItemDecoration
import kotlinx.android.synthetic.main.fragment_lobby.*
import org.jetbrains.anko.support.v4.runOnUiThread

/**
 * A simple [Fragment] subclass.
 */
class LobbyFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_lobby, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // add decorations for the recyclerview

        // make the api call to get the roomdata
        Networking.networkHandler.getRoomData { data ->
            runOnUiThread {
                lobby_fragment_recycler_view.layoutManager = LinearLayoutManager(view.context)
                lobby_fragment_recycler_view.addItemDecoration(RecyclerviewItemDecoration())
                lobby_fragment_recycler_view.adapter = MainAdapter(view.context, data)
            }
        }


    }


}
