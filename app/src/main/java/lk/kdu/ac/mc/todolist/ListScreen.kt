package lk.kdu.ac.mc.todolist



import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api

import androidx.compose.material3.Text


import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

import androidx.navigation.NavController
import lk.kdu.ac.mc.todolist.ui.theme.ToDoListTheme

import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

import lk.kdu.ac.mc.todolist.pages.CalendarPage
import lk.kdu.ac.mc.todolist.pages.MyLists


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListScreen(navController: NavController, name: String) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val navItemList = listOf(
        NavItem("Bar", Icons.Default.List),
        NavItem("Home", Icons.Default.Home),
        NavItem("Calendar", Icons.Default.DateRange)
    )

    var selectedIndex by remember { mutableIntStateOf(1) } // Default to Home (MyLists)

    ToDoListTheme {
        //navigation drawer creation (source : https://www.youtube.com/watch?v=bs0mKjZ5pIs&t=94s)
        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                ModalDrawerSheet {
                    Text("Drawer Menu", modifier = Modifier.padding(16.dp))
                    Divider()
                    NavigationDrawerItem(
                        label = { Text("Option 1") },
                        selected = false,
                        onClick = { /* handle click */ }
                    )
                    NavigationDrawerItem(
                        label = { Text("Option 2") },
                        selected = false,
                        onClick = { /* handle click */ }
                    )
                }
            }
        ) {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text("ToDo App") }
                    )
                },
                //Bottom navigation bar (source : https://www.youtube.com/watch?v=O9csfKW3dZ4&list=PLgpnJydBcnPA5aNrlDxxKWSqAma7m3OIl&index=13)
                bottomBar = {
                    NavigationBar {
                        navItemList.forEachIndexed { index, navItem ->
                            NavigationBarItem(
                                selected = selectedIndex == index,
                                onClick = {
                                    if (index == 0) {
                                        // Open drawer
                                        scope.launch {
                                            drawerState.open()
                                        }
                                    } else {
                                        selectedIndex = index
                                    }
                                },
                                icon = {
                                    Icon(imageVector = navItem.icon, contentDescription = navItem.label)
                                },
                                label = {
                                    Text(text = navItem.label)
                                }
                            )
                        }
                    }
                }
            ) { innerPadding ->
                Box(modifier = Modifier.padding(innerPadding)) {
                    when (selectedIndex) {
                        1 -> MyLists()
                        2 -> CalendarPage()
                    }
                }
            }
        }
    }
}

