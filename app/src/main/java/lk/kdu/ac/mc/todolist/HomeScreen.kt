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
                    .background(Color.White)
                    .padding(top = 25.dp)
                    .padding(bottom = 105.dp)
            ) {
                // Align the greeting to the top-center
                Greeting(
                    name = userName,
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(horizontal = 5.dp)
                )
                // Align the creator to the bottom-center
                Creator(
                    name = "Udana Senanayake",
                    id = "D/BCS/23/0018",
                    modifier = Modifier
                        .padding(top = 70.dp)
                        .padding(horizontal = 5.dp)
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
                            containerColor = Color.Blue
                        )
                    ) {
                        Text(
                            text = "My Do Lists",
                            color = Color.White,
                            style = TextStyle(
                                fontSize = 25.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Left,
                            )
                        )
                    }
                    Spacer(modifier = Modifier.height(30.dp))

                    if (user != null) {
                        AsyncImage(
                            model = user!!.profilePictureUrl,
                            contentDescription = "Profile Picture",
                            modifier = Modifier
                                .height(80.dp)
                                .width(80.dp)
                                .background(Color.Gray, shape = RoundedCornerShape(40.dp))
                        )

                        // Show username
                        Text(
                            text = user!!.username ?: "User",
                            style = TextStyle(
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Medium,
                                textAlign = TextAlign.Center,
                            ),
                            modifier = Modifier
                                .padding(top = 8.dp)
                                .fillMaxWidth()
                        )

                        Button(
                            onClick = {
                                coroutineScope.launch {
                                    googleAuthUiClient.signOut()
                                    user = null
                                }
                            },
                            modifier = Modifier
                                .padding(top = 10.dp)
                                .height(50.dp)
                                .width(200.dp)
                        ) {
                            Text("Sign Out")
                        }
                    } else {
                        Button(
                            onClick = {
                                coroutineScope.launch {
                                    val intentSender = googleAuthUiClient.signIn()
                                    intentSender?.let {
                                        launcher.launch(IntentSenderRequest.Builder(it).build())
                                    }
                                }
                            },
                            modifier = Modifier
                                .padding(top = 10.dp)
                                .height(50.dp)
                                .width(200.dp)
                        ) {
                            Text("Sign In with Google")
                        }
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
        ),
        color = Color.Black,
        modifier = modifier.fillMaxWidth()
    )

}

@Composable
fun Creator(name: String ,id: String , modifier: Modifier = Modifier) {
    Text(
        text = "This app was created by $name - $id",

        style = TextStyle(
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Left,
        ),

        color = Color.Blue,
        //color = Color(0xFFADFF2F),
        modifier = modifier.fillMaxWidth()
    )

}

