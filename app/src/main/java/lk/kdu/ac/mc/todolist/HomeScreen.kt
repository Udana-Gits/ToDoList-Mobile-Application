package lk.kdu.ac.mc.todolist

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.navigation.NavController
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import lk.kdu.ac.mc.todolist.ui.theme.ToDoListTheme

import androidx.compose.material3.ButtonDefaults //chatgpt for cutom button color change



@Composable
fun HomeScreen(navController: NavController){

    val name = "udana"



    ToDoListTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            // Use a Box to layer children, then specify their alignment
            Box(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .background(Color.White)
                    .padding(top = 25.dp)
            ) {
                // Align the greeting to the top-center
                Greeting(
                    name = name,
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
                    Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ){
                    Button(onClick = {
                        navController.navigate("${Routes.listscreen}/$name") // navigation with aruguments
                    },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Blue //chatgpt for cutom button color change
                        )
                    ) {
                        Text(text = "press to go to List Screen",
                            color = Color.White
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
        text = "Welcome to $name's ToDoList!",

        style = TextStyle(
            fontSize = 30.sp,
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
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Left,
        ),

        color = Color(0xFFADFF2F),
        modifier = modifier.fillMaxWidth()
    )

}