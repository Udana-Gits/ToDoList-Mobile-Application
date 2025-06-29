package lk.kdu.ac.mc.todolist


import android.app.Activity
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api

import androidx.compose.material3.Text


import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

import androidx.navigation.NavController
import lk.kdu.ac.mc.todolist.ui.theme.ToDoListTheme

// importing libries require to incons
import androidx.compose.material3.Icon
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material.icons.filled.Settings


import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.google.android.gms.auth.api.identity.Identity
import kotlinx.coroutines.launch

import lk.kdu.ac.mc.todolist.pages.CalendarPage
import lk.kdu.ac.mc.todolist.pages.MyLists
import lk.kdu.ac.mc.todolist.pages.TodoViewModel
import lk.kdu.ac.mc.todolist.sign_in_out.GoogleAuthUiClient
import lk.kdu.ac.mc.todolist.sign_in_out.SignInResult

import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.livedata.observeAsState




@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListScreen(navController: NavController, name: String) {




    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    //knowledge : https://www.youtube.com/watch?v=P3xQdINdrWY&list=PLgpnJydBcnPA5aNrlDxxKWSqAma7m3OIl&index=7, ChatGpt
    //source : https://github.com/bimalkaf/JetpackCompose_Playground/tree/main/3_TodoApp
    val todoViewModel: TodoViewModel = viewModel()


    LaunchedEffect(Unit) {
        todoViewModel.restoreTodosFromFirebase()
    }

    val todoList = todoViewModel.todoList.observeAsState()


    val navItemList = listOf(
        NavItem("Bar", Icons.Default.List),
        NavItem("Home", Icons.Default.Home),
        NavItem("Calendar", Icons.Default.DateRange)
    )

    val context = LocalContext.current //Gives  access to the Android context inside a Composabl


    //----------------------------------------------------------------------------------
    //sign in with google (knowlage : https://www.youtube.com/watch?v=zCIfBbm06QM , source : https://github.com/philipplackner/ComposeGoogleSignInCleanArchitecture)
    val oneTapClient = remember { Identity.getSignInClient(context) }
    val googleAuthUiClient = remember { GoogleAuthUiClient(context, oneTapClient) }
    val coroutineScope = rememberCoroutineScope()
    var user by remember { mutableStateOf(googleAuthUiClient.getSignedInUser()) }
    var showAccountMenu by remember { mutableStateOf(false) }

    val userName = user?.username?.split(" ")?.firstOrNull() ?: "Guest"


    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult(),
        onResult = { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                coroutineScope.launch {
                    val signInResult: SignInResult = googleAuthUiClient.signInWithIntent(result.data!!)
                    user = signInResult.data
                    signInResult.errorMessage?.let {
                        Toast.makeText(context, it, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    )
    //----------------------------------------------------------------------------------
    var isBackupEnabled by remember { mutableStateOf(false) }



    var selectedIndex by remember { mutableIntStateOf(1) } // Default to Home (MyLists)

    //backup to the firebase enable or disable by toggle button in navigation drawer


    ToDoListTheme {
        //navigation drawer creation (source : https://www.youtube.com/watch?v=bs0mKjZ5pIs&t=94s)
        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                ModalDrawerSheet {
                    // Align the profile icon or image to the top end
                    // Greeting and Profile Box
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp)
                            .background(Color(0xFFFFBE7C), shape = RoundedCornerShape(20.dp))
                            .padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.logo), // replace with your actual image name
                            contentDescription = "Greeting Image",
                            modifier = Modifier
                                .height(50.dp)  // set your desired height
                                .width(245.dp)  // set your desired width
                        )
                        if (user != null) {
                            Box(
                                modifier = Modifier
                                    .size(45.dp)
                                    .clickable { showAccountMenu = true }
                            ) {
                                AsyncImage(
                                    model = user!!.profilePictureUrl,
                                    contentDescription = "Profile Picture",
                                    modifier = Modifier
                                        .size(45.dp)
                                        .clip(CircleShape)
                                )
                                Icon(
                                    imageVector = if (isBackupEnabled) Icons.Default.CloudDone else Icons.Default.CloudOff,
                                    contentDescription = null,
                                    tint = Color(0xFF964B00),
                                    modifier = Modifier
                                        .align(Alignment.BottomEnd)
                                        .size(16.dp)
                                        .background(Color.White, shape = CircleShape)
                                        .padding(2.dp)
                                )
                            }

                        } else {
                            Icon(
                                imageVector = Icons.Default.AccountCircle,
                                contentDescription = "Sign In Icon",
                                tint = Color.Gray,
                                modifier = Modifier
                                    .size(45.dp)
                                    .clickable {
                                        coroutineScope.launch {
                                            val intentSender = googleAuthUiClient.signIn()
                                            intentSender?.let {
                                                launcher.launch(IntentSenderRequest.Builder(it).build())
                                            }
                                        }
                                    }
                            )
                        }
                    }

                    if (showAccountMenu && user != null) {
                        AlertDialog(
                            onDismissRequest = { showAccountMenu = false },
                            title = { Text("Account Options") },
                            text = {
                                Column {
                                    Text("Signed in as: ${user!!.username}")
                                }
                            },
                            confirmButton = {
                                TextButton(onClick = {
                                    coroutineScope.launch {
                                        val intentSender = googleAuthUiClient.signIn()
                                        intentSender?.let {
                                            launcher.launch(IntentSenderRequest.Builder(it).build())
                                        }
                                    }
                                    showAccountMenu = false
                                }) {
                                    Text("Switch Account")
                                }
                            },
                            dismissButton = {
                                TextButton(onClick = {
                                    coroutineScope.launch {
                                        googleAuthUiClient.signOut()
                                        user = null
                                    }
                                    showAccountMenu = false
                                }) {
                                    Text("Sign Out")
                                }
                            }
                        )
                    }
                    Divider()
                    Spacer(modifier = Modifier.height(20.dp))

                    NavigationDrawerItem(
                        label = {
                            Text(
                                if (todoViewModel.isBackupEnabled.value) "Turn off backup" else "Turn on backup",
                                style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold)
                            )
                        },
                        selected = todoViewModel.isBackupEnabled.value,
                        onClick = {
                            todoViewModel.isBackupEnabled.value = !todoViewModel.isBackupEnabled.value
                            if (todoViewModel.isBackupEnabled.value) {
                                todoList.value?.let { todoViewModel.backupTodosToFirebase(it) }
                            }
                        },
                        icon = {
                            Icon(
                                imageVector = if (todoViewModel.isBackupEnabled.value) Icons.Default.CloudDone else Icons.Default.CloudOff,
                                contentDescription = null
                            )
                        }
                    )
                    NavigationDrawerItem(

                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 4.dp),

                        label = { Text("Settings",
                                style = TextStyle(
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Left,
                                color = Color(0xFF964B00),
                        ),
                            modifier = Modifier.fillMaxWidth()
                                .padding(start = 16.dp)
                            )
                         },
                        selected = false,
                        onClick = { /* handle click */ },
                        icon = {
                            Icon(
                                imageVector = Icons.Default.Settings ,
                                contentDescription = null,
                                tint = Color(0xFF964B00)
                            )
                        }

                    )
                }
            }
        ) {
            Scaffold(
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
                        1 -> MyLists(todoViewModel)
                        2 -> CalendarPage()
                    }
                }
            }
        }
    }
}

