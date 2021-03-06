package com.example.buttongame.Fragments

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.buttongame.R
import kotlinx.android.synthetic.main.activity_main.*

enum class FragmentID {
    MAIN,
    ROOMS,
    ABOUT
}

object FragmentTransitionManager {

    fun switchFragments(
        target: FragmentID,
        supportFragmentManager: FragmentManager,
        enterAnimation: Int,
        exitAnimation: Int,
        backEnterAnimation : Int? = null,
        backExitAnimation: Int? = null,
        fragmentTag : String? = null){

        val fManager = supportFragmentManager
        val fTransaction = fManager.beginTransaction()
        var fragment : Fragment? = null
        when(target){
            FragmentID.MAIN -> {
                fragment = MainScreenFragment()
            }
            FragmentID.ROOMS -> {
                fragment = LobbyFragment()
            }
            FragmentID.ABOUT -> {
                fragment = AboutFragment()
            }
        }
        if(backEnterAnimation == null){
            fTransaction.setCustomAnimations(enterAnimation, exitAnimation)
        }else{
            fTransaction.setCustomAnimations(enterAnimation, exitAnimation, backEnterAnimation, backExitAnimation!!).addToBackStack(fragmentTag)
        }

        supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        fTransaction.replace(R.id.main_fragment_containter, fragment!!)
        fTransaction.commit()
    }
}