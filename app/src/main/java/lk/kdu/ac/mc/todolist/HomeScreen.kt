package lk.kdu.ac.mc.todolist

import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalMapOf
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView

//navigation
import androidx.navigation.NavController

import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import lk.kdu.ac.mc.todolist.ui.theme.ToDoListTheme


//button
import androidx.compose.material3.ButtonDefaults //chatgpt for custom button color change

//gif showing context
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder

//import from package lk.kdu.ac.mc.todolist.sign_in_out
import lk.kdu.ac.mc.todolist.sign_in_out.GoogleAuthUiClient
import lk.kdu.ac.mc.todolist.sign_in_out.SignInResult

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import com.google.android.gms.auth.api.identity.Identity
import kotlinx.coroutines.launch



import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import coil.compose.AsyncImage

import kotlinx.coroutines.launch

import lk.kdu.ac.mc.todolist.ui.theme.ToDoListTheme


@Composable
fun HomeScreen(navController: NavController){

    //gif showing context , source : https://developer.android.com/social-and-messaging/guides/media-animated-gif

    val context = LocalContext.current //Gives  access to the Android context inside a Composabl

    val gifEnabledLoader = ImageLoader.Builder(context) //Creates a special image loader that can handle GIFs.
        .components {
            if (Build.VERSION.SDK_INT >= 28) {
                add(ImageDecoderDecoder.Factory())
            } else {
                add(GifDecoder.Factory())
            }
        }
        .build()

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
            if (result.resultCode == android.app.Activity.RESULT_OK) {
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


    ToDoListTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            // Use a Box to layer children, then specify their alignment
            Box(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()

                    .padding(top = 15.dp)
                    .padding(bottom = 105.dp)
            ) {
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
                    Greeting(
                        name = userName,
                        modifier = Modifier
                            .weight(1f)
                    )

                    if (user != null) {
                        AsyncImage(
                            model = user!!.profilePictureUrl,
                            contentDescription = "Profile Picture",
                            modifier = Modifier
                                .size(45.dp)
                                .clip(CircleShape)
                                .clickable { showAccountMenu = true }
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.AccountCircle,
                            contentDescription = "Sign In Icon",
                            tint = Color.Gray,
                            modifier = Modifier
                                .size(40.dp)
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

                // Align the creator to the bottom-center
                Creator(
                    name = "Udana Senanayake",
                    id = "D/BCS/23/0018",
                    modifier = Modifier
                        .padding(top = 110.dp)
                        .padding(horizontal = 15.dp)
                )
                Column(
                    Modifier.fillMaxSize() .padding(horizontal = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ){
                    //Coil Composable used to show images in Compose.
                    AsyncImage(
                        model = R.drawable.homesreengif,
                        contentDescription = "Home GIF",
                        imageLoader = gifEnabledLoader,
                        modifier = Modifier
                            .height(275.dp)
                            .fillMaxWidth()
                            .padding(bottom = 5.dp)
                    )

                    Button(
                        onClick = {
                            navController.navigate("${Routes.listscreen}/$userName")
                        },
                        modifier = Modifier
                            .padding(top = 75.dp)         // Top padding
                            .padding(bottom = 0.dp)
                            .height(60.dp)                // Custom height
                            .width(300.dp),               // Custom width
                        shape = RoundedCornerShape(20.dp), // Rounded corners
                        colors = ButtonDefaults.buttonColors(
                        )
                    ) {
                        Text(
                            text = "Go To Lists",
                            color = Color.White,
                            style = TextStyle(
                                fontSize = 25.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Left,
                            )
                        )
                    }

                }
            }
        }
    }
}




@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Welcome to $name's \nToDoList!",

        style = TextStyle(
            fontSize = 35.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Left,
            color = Color.White
        ),
        modifier = modifier.fillMaxWidth()
    )

}

@Composable
fun Creator(name: String ,id: String , modifier: Modifier = Modifier) {
    Text(
        text = "This app was created by $name - $id",

        style = TextStyle(
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Left,
        ),

        color = Color(0xFF964B00),
        modifier = modifier.fillMaxWidth()
    )

}

