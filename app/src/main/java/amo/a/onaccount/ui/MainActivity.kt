package amo.a.onaccount.ui

import amo.a.onaccount.R
import amo.a.onaccount.ui.fragments.MainFragment
import amo.a.onaccount.ui.fragments.OperationFragment
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import java.util.*

class MainActivity : AppCompatActivity(),MainFragment.Callbacks {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val isFragmentContainerEmpty = savedInstanceState == null
        if (isFragmentContainerEmpty) {
            replaceFragment(MainFragment.newInstance())
        }
    }

    private fun replaceFragment(fragment: Fragment){
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.main_container, fragment)
            .addToBackStack(null)
            .commit()
    }

    override fun onItemSelected(accountId:UUID,accountName:String) {
        replaceFragment(OperationFragment.newInstance(accountId,accountName))
    }
}