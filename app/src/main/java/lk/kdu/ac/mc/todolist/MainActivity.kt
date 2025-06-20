package lk.kdu.ac.mc.todolist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import lk.kdu.ac.mc.todolist.ui.theme.ToDoListTheme


import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
        )
        //splash screen animation setup knowlage : https://www.youtube.com/watch?v=YjtxfPkCzok
        val splashScreen = installSplashScreen()
        splashScreen.setKeepOnScreenCondition {true}

        CoroutineScope(Dispatchers.Main).launch {
            delay(3900L)
            splashScreen.setKeepOnScreenCondition {false}
        }

        setContent {
            MyAppNavigation()
        }
    }
}













