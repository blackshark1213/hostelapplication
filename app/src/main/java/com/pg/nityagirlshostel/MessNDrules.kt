package com.pg.nityagirlshostel

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage

@Composable
fun Mess_UI(){
    val scroller = rememberScrollState()
    Box(
        modifier = Modifier
            .fillMaxWidth(),
        contentAlignment = Alignment.BottomCenter
    ){
        Column (
            modifier = Modifier
                .fillMaxWidth()
                .padding(top=8.dp)
                .background(Color(0xFF76807B))
//            .verticalScroll(scroller)
                .horizontalScroll(scroller),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            TypingText("M E S S ","MENU" , 100L)

            AsyncImage(
                placeholder = painterResource(id = R.drawable.loading),
                model = "https://${supabase.supabaseUrl}/storage/v1/object/public/user-images/Mess/Mess_Menu.jpg", // Can be null, fallback will handle it
                contentDescription = "Mess menu",
                modifier = Modifier
                    .fillMaxSize(),
                fallback = painterResource(id = R.drawable.loading)
//            MaterialTheme
//                .padding(top = 8.dp),
            )
            Divider(color = androidx.compose.ui.graphics.Color.Red)
            TypingText("F A C I L I T I E S ","NGH" , 100L)


            AsyncImage(
                placeholder = painterResource(id = R.drawable.loading),
                model = "https://${supabase.supabaseUrl}/storage/v1/object/public/user-images/Facility/Facility.jpg", // Can be null, fallback will handle it
                contentDescription = "Facility",
                modifier = Modifier
                    .fillMaxSize()
                    .padding(2.dp),
                fallback = painterResource(id = R.drawable.loading)
            )
        }
    }
}


@Composable
fun Rule_UI(navController: NavController){
    val context = LocalContext.current
    val scroller = rememberScrollState()

    Toast.makeText(context, "Make sure to connect with internet.", Toast.LENGTH_LONG).show()
    Column (
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxSize()
            .padding(top=8.dp)
            .horizontalScroll(scroller),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Box(
            modifier = Modifier
                .fillMaxWidth(),
            contentAlignment = Alignment.BottomCenter
        ){

            Column(
                modifier = Modifier
                    .fillMaxSize()
//                    .background(col)
                    .padding(16.dp)
            ) {

                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                Text("Loding Rules.....", style = MaterialTheme.typography.titleMedium, modifier = Modifier.align(Alignment.CenterHorizontally))
//                Text("Make sure to connect with internet", style = MaterialTheme.typography.titleSmall, modifier = Modifier.align(Alignment.CenterHorizontally))
            }
            AsyncImage(
                model = "https://${supabase.supabaseUrl}/storage/v1/object/public/user-images/Rule/Rule.jpg", // Can be null, fallback will handle it
                contentDescription = "Facility",
                modifier = Modifier
                    .fillMaxSize()
                    .padding(2.dp),
                placeholder = painterResource(id = R.drawable.loading),
                fallback = painterResource(id = R.drawable.loading)
            )
        }
        Button(
            onClick = { navController.navigate("user_form") },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF837C7C), // Blue shade
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .height(50.dp),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp)
        ) {
            Text(
                text = "OK",
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
            )
        }


    }
}

@Composable
fun Location() {
    val context = LocalContext.current

        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse("https://maps.app.goo.gl/cczFnCBVRVWuaRDW8")
            //setPackage("com.google.android.apps.maps") // Optional: open specifically in Google Maps
        }

        // Verify that there's an app to handle the intent
        if (intent.resolveActivity(context.packageManager) != null) {
            context.startActivity(intent)
        } else {
            Toast.makeText(context, "Google Maps app not found.", Toast.LENGTH_SHORT).show()
        }
    Card(
        modifier = Modifier
            .fillMaxWidth()
//            .background(painterResource(id = R.drawable.ngh_map))
            .padding(8.dp),
        shape = RoundedCornerShape(32.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFC9BAC9))
    ) {
        Column(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Icon(
                imageVector = Icons.Default.LocationOn,
                contentDescription = null,
                tint = Color(0xFF1DA871),
                modifier = Modifier
                    .size(200.dp)
                    .padding(bottom = 16.dp)
            )
            Text(
                text = "Location Open In Google Maps.",
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(bottom = 10.dp)
            )
        }
    }
}

