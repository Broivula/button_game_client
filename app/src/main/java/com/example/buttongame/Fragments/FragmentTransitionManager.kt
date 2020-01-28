package com.example.buttongame.Fragments

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.buttongame.R
import kotlinx.android.synthetic.main.activity_main.*

enum class FragmentID {
    MAIN,
    ROOMS,
    HIGHSCORE
}

object FragmentTransitionManager {

    fun switchFragments(target: FragmentID, supportFragmentManager: FragmentManager, enterAnimation: Int, exitAnimation: Int){

        val fManager = supportFragmentManager
        val fTransaction = fManager!!.beginTransaction()
        var fragment : Fragment? = null
        when(target){
            FragmentID.MAIN -> {
                fragment = MainScreenFragment()
            }
            FragmentID.ROOMS -> {
                fragment = LobbyFragment()
            }
            FragmentID.HIGHSCORE -> {

            }
        }
        fTransaction.setCustomAnimations(enterAnimation, exitAnimation)
        fTransaction.replace(R.id.main_fragment_containter, fragment!!)
        fTransaction.commit()
    }
}