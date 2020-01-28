package com.example.buttongame.Fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.buttongame.R
import com.example.buttongame.SetupObject
import kotlin.concurrent.thread

/**
 * A simple [Fragment] subclass.
 */
class LoadingFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_loading, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Thread.sleep(1000)
        FragmentTransitionManager.switchFragments(FragmentID.MAIN, fragmentManager!!, R.anim.anim_fade_in, R.anim.anim_fade_out)
      //  initiateSetup()
    }



    private fun initiateSetup(){
        thread { SetupObject.setup() }
    }


}
