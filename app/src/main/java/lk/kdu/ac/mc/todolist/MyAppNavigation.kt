package lk.kdu.ac.mc.todolist

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

//seperate class for navigation to make mainactivity clean (source:https://www.youtube.com/watch?v=wJKwsI5WUI4)

@Composable
fun MyAppNavigation(){
    //navigation with arguments (source : https://www.youtube.com/watch?v=wJKwsI5WUI4)
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Routes.homescreen, builder ={
        composable(route = Routes.homescreen,) {
            HomeScreen(navController,)
        }
        composable(route = Routes.listscreen+"/{name}") {
            val name = it.arguments?.getString("name")
            ListScreen(navController, name?:"No name")
        }
    })
}