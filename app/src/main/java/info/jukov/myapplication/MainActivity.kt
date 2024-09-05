@file:OptIn(ExperimentalMaterial3Api::class)

package info.jukov.myapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.tooling.preview.Preview
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import info.jukov.myapplication.ui.theme.MyApplicationTheme
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        findViewById<ViewPager2>(R.id.view_pager).adapter = PagerAdapter(supportFragmentManager, lifecycle)
    }
}

class PagerAdapter(
    fm: FragmentManager,
    lifecycle: Lifecycle
) : FragmentStateAdapter(fm, lifecycle) {
    override fun createFragment(position: Int): Fragment =
        when (position) {
            0 -> PageFragment()
            1 -> PageFragment()
            else -> error("Wrong position")
        }

    override fun getItemCount() = 2
}

class PageFragment: Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnLifecycleDestroyed(lifecycle))
            setContent { Page() }
        }
    }
}

@Composable
fun Page() {
    var isRefreshing by remember { mutableStateOf(false) }
    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = {
            thread {
                isRefreshing = true
                Thread.sleep(1000)
                isRefreshing = false
            }
        },
        modifier = Modifier.fillMaxSize()
    ) {
        LazyColumn {
            items(100) {
                Text("Item $it", modifier = Modifier.fillMaxWidth())
            }
        }
    }
}