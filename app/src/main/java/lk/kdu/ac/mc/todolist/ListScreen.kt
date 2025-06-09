package lk.kdu.ac.mc.todolist


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import lk.kdu.ac.mc.todolist.ui.theme.ToDoListTheme


@Composable
fun ListScreen(navController: NavController,name : String){
    ToDoListTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            // Use a Box to layer children, then specify their alignment
            Box(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .background(Color.Black)
                    .padding(top = 25.dp)
            ) {
                // Align the greeting to the top-center
                Greeting1(
                    name = name,
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(horizontal = 5.dp)
                )
                Column(
                    Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ){
                    Button(onClick = {
                        navController.navigate(Routes.homescreen)
                    }) {
                        Text(text = "press to go to Home Screen")
                    }
                }

            }
        }
    }
}

@Composable
fun Greeting1(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Welcome to $name's ToDoList!",

        style = TextStyle(
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Left,
        ),
        color = Color.White,
        modifier = modifier.fillMaxWidth()
    )

}

@Composable
fun Creator1(name: String ,id: String , modifier: Modifier = Modifier) {
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