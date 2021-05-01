package uk.geekhole.hotreddit.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.plugins.RxJavaPlugins
import uk.geekhole.hotreddit.R
import uk.geekhole.hotreddit.global.RxThrowable
import uk.geekhole.hotreddit.ui.feed.FeedFragment

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        showFragment(FeedFragment())

        // Global error handler to catch Rx errors and display them as a snackbar to the user.
        RxJavaPlugins.setErrorHandler {
            val message = if (it is RxThrowable) {
                getString(it.messageResourceId)
            } else {
                it.message ?: getString(R.string.error_generic)
            }
            
            Snackbar.make(findViewById(R.id.fragment_container), message, Snackbar.LENGTH_LONG).show()
        }
    }

    private fun showFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
        transaction.replace(R.id.fragment_container, fragment, fragment.javaClass.canonicalName)
        transaction.commitAllowingStateLoss()
        supportFragmentManager.executePendingTransactions()
    }
}