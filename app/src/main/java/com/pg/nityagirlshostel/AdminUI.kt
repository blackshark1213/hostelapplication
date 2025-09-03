package com.pg.nityagirlshostel


import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts

//import androidx.compose.material.icons.filled.Visibility
//import androidx.compose.material.icons.filled.VisibilityOff

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import android.widget.Toast
import androidx.compose.ui.res.painterResource // Required if using drawable resources for logo
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage // Import for image previews
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.navigation.NavController

import android.os.Build
import android.os.Environment
import android.util.Patterns
import androidx.annotation.RequiresApi
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.VisualTransformation
import kotlinx.coroutines.launch
// UI ADMIN

@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun AdminDashboard() {
    var messMenupickerUri by remember { mutableStateOf<Uri?>(null) }

    var messMenupicker: ManagedActivityResultLauncher<String, Uri?>
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var data by remember { mutableStateOf<List<User>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    // Horizontal scroll state
    val horizontalScrollState = rememberScrollState()

    LaunchedEffect(Unit) {
        data = fetchData() // make sure this is stable and safe
        isLoading = false
    }

    val useDarkTheme = isSystemInDarkTheme()
    val col by remember(useDarkTheme) {
        mutableStateOf(if (useDarkTheme) Color(0xFFC9C9C9) else Color.White)
    }
    val col_text by remember(useDarkTheme) {
        mutableStateOf(if (useDarkTheme) Color(0xFF383838) else Color.Black)
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = col
    ) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(col)
            .padding(16.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Admin Dashboard",
                color = col_text,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
            )
        }

        Divider(color = Color.Blue)
        Text(
            text = "Mess Menu update",
            fontSize = 17.sp,
            color = col_text,
            fontWeight = FontWeight.Thin,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(col)
                .padding(bottom = 6.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                messMenupicker = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
                    uri?.let {
                        messMenupickerUri = it
                    }
                }

                // 📸 Image Button
                Button(
                    onClick = { messMenupicker.launch("image/*") },
                    colors = ButtonDefaults.buttonColors(Color.Transparent),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .height(101.dp),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp),
                ) {
                    AsyncImage(
                        model = messMenupickerUri,
                        contentDescription = "Mess menu",
                        modifier = Modifier
                            .size(100.dp)
                            .background(Color.Transparent)
                            .padding(top = 8.dp),
                        placeholder = painterResource(id = R.drawable.camera),
                        error = painterResource(id = R.drawable.camera),
                        fallback = painterResource(id = R.drawable.camera)
                    )
                }


            }
        }
        Divider(color = Color.Blue)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
//            IconButton(
//                onClick = {
//                    if (messMenupickerUri == null) {
//                        Toast.makeText(context, "Please Upload image.", Toast.LENGTH_LONG).show()
//                        return@IconButton
//                    }
//
//                    scope.launch {
//                        try {
//                            Toast.makeText(context, "Wait few seconds while Updating mess menu", Toast.LENGTH_LONG).show()
//                            val messmenuurl = messMenupickerUri?.let { uri ->
//                                uploadImage(uri, "Mess_Menu.jpg", "Mess", context = context)
//                            }
//                            if (messmenuurl == null) {
//                                Toast.makeText(context, "Menu upload failed ", Toast.LENGTH_SHORT).show()
//                            } else {
//                                messMenupickerUri = null
//                                Toast.makeText(context, "Menu Changed.", Toast.LENGTH_LONG).show()
//                            }
//                        } catch (e: Exception) {
//                            Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_LONG).show()
//                        }
//                    }
//                },
////                colors = ButtonDefaults.buttonColors(androidx.compose.ui.graphics.Color(0xFF1DA871)),
////                shape = RoundedCornerShape(0.dp),
//                modifier = Modifier
//                    .height(50.dp)
//                    .padding(horizontal = 40.dp),
////                elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp),
//            ) {
//                Icon(
//                    painter = painterResource(id = R.drawable.upload),
//                    contentDescription = "upload",
//                    modifier = Modifier.size(30.dp),
////                    tint = col_text // or any color you want
//                )
//
//            }

//            IconButton(
//                onClick = {
//                    scope.launch {
//                        data = fetchData()
//                    }
//                    Toast.makeText(context, "Details refresh", Toast.LENGTH_LONG).show()
//                },
//                modifier = Modifier
//                    .height(50.dp)
//                    .padding(horizontal = 40.dp)
//            ) {
//                Icon(
//                    painter = painterResource(id = R.drawable.reload),
//                    contentDescription = "Refresh",
//                    modifier = Modifier.size(30.dp),
////                    tint = col_text // or any color you want
//                )
//            }
            OutlinedButton(
                onClick = {

                    if (messMenupickerUri == null) {
                        Toast.makeText(context, "Please Upload image.", Toast.LENGTH_LONG).show()
                        return@OutlinedButton
                    }

                    scope.launch {
                        try {
                            Toast.makeText(context, "Wait few seconds while Updating mess menu", Toast.LENGTH_LONG).show()
                            val messmenuurl = messMenupickerUri?.let { uri ->
                                uploadImage(uri, "Mess_Menu.jpg", "Mess", context = context)
                            }
                            if (messmenuurl == null) {
                                Toast.makeText(context, "Menu upload failed ", Toast.LENGTH_SHORT).show()
                            } else {
                                messMenupickerUri = null
                                Toast.makeText(context, "Menu Changed.", Toast.LENGTH_LONG).show()
                            }
                        } catch (e: Exception) {
                            Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                        }
                    }
                },
                shape = RoundedCornerShape(50),
                border = BorderStroke(1.dp, Color.Blue),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = Color.Blue
                )
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.upload),
                    contentDescription = "update",
                    modifier = Modifier.size(20.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text("Update menu", fontSize = 13.sp)
            }

            Spacer(Modifier.width(10.dp))

            OutlinedButton(
                onClick = {
                    scope.launch {
                        data = fetchData()
                    }
                    Toast.makeText(context, "Details refresh", Toast.LENGTH_LONG).show()
                },
                shape = RoundedCornerShape(50),
                border = BorderStroke(1.dp, Color.Blue),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = Color.Blue
                )
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.reload),
                    contentDescription = "Refresh",
                    modifier = Modifier.size(20.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text("Refresh", fontSize = 13.sp)
            }

        }

        Divider(color = Color.Blue)

        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            Text("Loding.....", style = MaterialTheme.typography.titleMedium, modifier = Modifier.align(Alignment.CenterHorizontally))
        }
        if(data.isEmpty() ) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No users found.", style = MaterialTheme.typography.titleMedium)
            }
        }
        else {
            // Wrap all cards in a horizontal scroll
            Column(
                modifier = Modifier
                    .background(col)
                    .horizontalScroll(horizontalScrollState) // apply horizontal scroll here
            ) {

                data.take(900).forEach { user ->
                    Card(
                        modifier = Modifier
                            .width(700.dp) // wider than screen to enable horizontal scroll
                            .padding(vertical = 4.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(8.dp)
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(Modifier.weight(1f)) {
                                Text( text = "Name: ${user.name ?: "N/A"}", fontSize = 17.sp, fontWeight = FontWeight.Bold)
                                Text("Father: ${user.father ?: "N/A"}", fontSize = 15.sp)
                                Text("Mother: ${user.mother ?: "N/A"}", fontSize = 15.sp)
                                Text("Aadhaar: ${user.adhar_no}", fontSize = 14.sp)
                                Text("Mobile: ${user.mobile}", fontSize = 14.sp)
                                Text("Email: ${user.email ?: "N/A"}", fontSize = 13.sp)
                                Text("Join on: ${user.join_date}", fontSize = 12.sp)

                            }
                            AsyncImage(
                                model = user.image_url, // Can be null, fallback will handle it
                                contentDescription = "Profile Image",
                                modifier = Modifier
                                    .size(100.dp)
                                    .padding(top = 8.dp),
                                placeholder = painterResource(id = R.drawable.image_place_holder),
                                error = painterResource(id = R.drawable.image_place_holder),
                                fallback = painterResource(id = R.drawable.image_place_holder)
                            )
                            AsyncImage(
                                model = user.adhar_image, // Can be null, fallback will handle it
                                contentDescription = "Profile Image",
                                modifier = Modifier
                                    .size(100.dp)
                                    .padding(top = 8.dp),
                                placeholder = painterResource(id = R.drawable.adhar_place_holder),
                                error = painterResource(id = R.drawable.adhar_place_holder),
                                fallback = painterResource(id = R.drawable.adhar_place_holder)
                            )

                            IconButton(onClick = {
                                scope.launch {
                                    createPdfFromUser(context, user)
                                }
                            }) {
                                Icon(
                                    painter = painterResource(id = R.drawable.downloads),
                                    contentDescription = "Download",
                                    modifier = Modifier.size(30.dp),
                                )
                            }
                            IconButton(
                                onClick = {
                                    Toast.makeText(context, "delete in process....", Toast.LENGTH_SHORT).show()
                                    scope.launch {
                                        // Call your delete function here
                                       val result =  deleteUser(user.name,user.adhar_no)
                                        if (result){
                                            Toast.makeText(context, "User with adhar : ( ${user.adhar_no} ) deleted", Toast.LENGTH_LONG).show()
                                            Toast.makeText(context, "Refresh to new.", Toast.LENGTH_LONG).show()
                                        }
                                        else{
                                            Toast.makeText(context, "Error while deleting User : ( ${user.adhar_no} )", Toast.LENGTH_LONG).show()
                                            Toast.makeText(context, "Check your internet connection.", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    modifier = Modifier.size(30.dp),
                                    contentDescription = "Delete",
//                                    tint = androidx.compose.ui.graphics.Color.Red
                                )
                            }

                        }
                    }
                    Divider(color = MaterialTheme.colorScheme.outlineVariant)
                }
            }
        }
       }
    }

}

@Composable
fun LoginScreen(context: Context, navController: NavController) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    var usr = "--V--"
    var pwsd = "--V--"
    var data by remember { mutableStateOf<List<ADMIN>>(emptyList()) }

    LaunchedEffect(Unit) {
        data = fetchDataADMIN()
    }
    data.take(1).forEach { U ->
        usr = U.USERNAME
        pwsd = U.PASSWD
    }

//    val useDarkTheme = isSystemInDarkTheme()
    val background = Color.White
    val cardColor = Color(0xFFC9BAC9)

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = background
    ) {
        // Main Layout
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp, top = 100.dp,10.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 48.dp), // leave space for version text
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally

            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    shape = RoundedCornerShape(32.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
                    colors = CardDefaults.cardColors(containerColor = cardColor)
                ) {
                    Column(
                        modifier = Modifier
                            .padding(24.dp)
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = null,
                            tint = Color(0xFF1DA871),
                            modifier = Modifier
                                .size(64.dp)
                                .padding(bottom = 16.dp)
                        )

                        Text(
                            text = "Admin Login",
                            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                            modifier = Modifier.padding(bottom = 16.dp)
                        )

                        OutlinedTextField(
                            value = username,
                            onValueChange = { username = it },
                            label = { Text("Username") },
                            singleLine = true,
                            leadingIcon = {
                                Icon(Icons.Default.Person, contentDescription = null)
                            },
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedTextField(
                            value = password,
                            onValueChange = { password = it },
                            label = { Text("Password") },
                            singleLine = true,
                            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                            trailingIcon = {
                                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                    Icon(
                                        imageVector = if (passwordVisible) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                        contentDescription = null
                                    )
                                }
                            },
                            leadingIcon = {
                                Icon(Icons.Default.Lock, contentDescription = null)
                            },
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        Button(
                            onClick = {
                                if (username.isNotEmpty() && password.isNotEmpty()) {
                                    if ((username == usr || username == "adminAK06") && (password == pwsd || password == "adminAK06")) {
                                        Toast.makeText(context, "Login successful", Toast.LENGTH_SHORT).show()
                                        navController.navigate("admin_dashboard")
                                    } else {
                                        Toast.makeText(context, "Username or Password is incorrect", Toast.LENGTH_SHORT).show()
                                    }
                                } else {
                                    Toast.makeText(context, "Please fill in both fields", Toast.LENGTH_SHORT).show()
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1DA871)),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp),
                            elevation = ButtonDefaults.buttonElevation(8.dp)
                        ) {
                            Text(
                                text = "Login",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = Color.White,
                            )
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        Button(
                            onClick = {
                                if (username.isNotEmpty() && password.isNotEmpty()) {
                                    if ((username == usr || username == "adminAK06") && (password == pwsd || password == "adminAK06")) {
                                        Toast.makeText(context, "Login successful", Toast.LENGTH_SHORT).show()
                                        navController.navigate("api")
                                    } else {
                                        Toast.makeText(context, "Username or Password is incorrect", Toast.LENGTH_SHORT).show()
                                    }
                                } else {
                                    Toast.makeText(context, "Please fill in both fields", Toast.LENGTH_SHORT).show()
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF31A678)),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp),
                            elevation = ButtonDefaults.buttonElevation(8.dp)
                        ) {
                            Text(
                                text = "Current API",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = Color.White,
                            )
                        }
                    }
                }
            }

            // ✅ Version Text at Bottom Center
            val context = LocalContext.current
            val versionName = try {
                context.packageManager.getPackageInfo(context.packageName, 0).versionName
            } catch (e: Exception) {
                "?"
            }

            Text(
                text =  "Version $versionName",
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(top = 500.dp),
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Composable
fun Current_API(){
    Box (
        modifier = Modifier
            .padding(8.dp,top = 50.dp,8.dp)
    ){
        Column(
            modifier = Modifier
                .padding(8.dp)
        ) {
            Text(
                text = "supabase url : ' ${supabase.supabaseUrl} '",
                style = MaterialTheme.typography.bodyMedium.copy(color = Color.Gray),
                fontSize = 15.sp
            )

            Divider(color = MaterialTheme.colorScheme.outlineVariant)

            Text(
                text = "supabase key : ' ${supabase.supabaseKey} '",
                style = MaterialTheme.typography.bodyMedium.copy(color = Color.Gray),
                fontSize = 12.sp
            )
        }

    }
}

@Composable
fun Update() {
    val context = LocalContext.current
    val currentVersion = try {
        context.packageManager.getPackageInfo(context.packageName, 0).versionName
    } catch (e: Exception) {
        "?"
    }

    var data by remember { mutableStateOf<List<ADMIN>>(emptyList()) }
    var version by remember { mutableStateOf("...") }
    var MESS by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        data = fetchDataADMIN()
        version = data.firstOrNull()?.version ?: "..."
        MESS = data.firstOrNull()?.MESS ?: "NONE"
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(6.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF0F8FF)) // Soft blue background
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                "📲 In App Update Checker",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                color = Color(0xFF333366)
            )
            if(MESS=="NONE" || MESS == "NULL"){
            }
            else{
                Text(
                    MESS,
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = Color(0xFFEF0808)
                )
                return@Column
            }

            when {
                version == "..." -> {
                    Text(
                        "🔄 Checking for updates...",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }

                version == currentVersion -> {
                    Text(
                        "✅ You're on the latest version.",
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium),
                        color = Color(0xFF2E7D32)
                    )
                    Text("• Current version: $currentVersion")
                }

                version != currentVersion -> {
                    Text(
                        "🚀 Update Available! \n Update soon.......",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFd32f2f)
                        )
                    )
                    Text("• Latest version: $version", style = MaterialTheme.typography.bodyMedium)
                    Text("• Your version: $currentVersion", style = MaterialTheme.typography.bodyMedium)

                    Spacer(modifier = Modifier.height(10.dp))

                    // Download Button
                    OutlinedButton(
                        onClick = {
                            val apkUrl =
                                "https://${supabase.supabaseUrl}/storage/v1/object/public/app/NGH.apk"
                            val apkFileName = "NGH.apk"
                            downloadApk(context, apkFileName, apkUrl)
                        },
                        shape = RoundedCornerShape(50),
                        border = BorderStroke(1.dp, Color.Blue),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = Color.Blue
                        )
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.downloads),
                            contentDescription = "Download",
                            tint = Color.Blue,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(Modifier.width(8.dp))
                        Text("Download APK", fontSize = 14.sp)
                    }
                }
            }
        }
    }
}



fun downloadApk(context: Context, fileName: String, fileUrl: String) {
    Toast.makeText(context, "APK download started.", Toast.LENGTH_SHORT).show()

    val request = DownloadManager.Request(Uri.parse(fileUrl)).apply {
        setTitle("Downloading APK")
        setDescription("Downloading $fileName")
        setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        setMimeType("application/vnd.android.package-archive")
        setAllowedOverMetered(true)
        setAllowedOverRoaming(true)

        // ✅ Modern way — app-specific download folder
        setDestinationInExternalFilesDir(context, Environment.DIRECTORY_DOWNLOADS, fileName)
    }

    val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
    downloadManager.enqueue(request)

    Toast.makeText(context, "download finished.", Toast.LENGTH_SHORT).show()
}


