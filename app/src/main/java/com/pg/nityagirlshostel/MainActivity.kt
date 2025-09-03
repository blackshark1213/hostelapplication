package com.pg.nityagirlshostel

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.storage.Storage
import io.github.jan.supabase.storage.storage
import kotlinx.coroutines.launch
import android.widget.Toast
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource // Required if using drawable resources for logo
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage // Import for image previews
import android.util.Patterns // For email validation
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.filled.Add
import androidx.compose.ui.text.style.TextAlign
import kotlinx.coroutines.delay

import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

import android.os.Build
import androidx.annotation.RequiresApi
import io.github.jan.supabase.serializer.KotlinXSerializer
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.lifecycle.lifecycleScope
import com.google.firebase.FirebaseApp
import io.github.jan.supabase.SupabaseClient
import java.text.SimpleDateFormat
import java.util.Locale

// Data class for User. Re-enabling image URL fields as per the new UI.
@Serializable
data class User(
    val adhar_no: String,
    val name: String,
    val father: String?= null,
    val mother: String?= null,
    val mobile: String?= null,
    val email: String?= null,
    val per_address: String?= null,
    val image_url: String?= null,
    val adhar_image: String?= null,
    val PIN : String? = null,
    val adhar_back : String?= null,
    val join_date: String ? = null
)

@Serializable
data class ADMIN(
    val USERNAME : String,
    val PASSWD : String,
    val version : String,
    val MESS : String,
)

// Supabase client initialization. Now including Storage for image uploads.
val supabase = createSupabaseClient(
//    supabaseUrl = "https://zsappqupcgctlxquncsz.supabase.co",
//    supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InpzY
//    XBwcXVwY2djdGx4cXVuY3N6Iiwicm9sZSI6ImFub24iLCJpYXQiOjE3NTA0MDUxMjgsImV4cCI6MjA2NTk4MTEyOH0.lqYSIICdf_vHALlCvvx6C_TpcvaJEFgCB-pFymtyaRs",

    supabaseUrl = RemoteConfigManager.getSupabaseUrl(),
    supabaseKey = RemoteConfigManager.getSupabaseKey()
) {
    install(Postgrest)
    install(Storage) // Re-enabled Storage for image uploads
    defaultSerializer = KotlinXSerializer(
        Json {
            ignoreUnknownKeys = true // <-- Ignore fields not in your data class
//            encodeDefaults = true
        }
        )

}

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
//        FirebaseApp.initializeApp(this) // Optional, already done in MyApp but safe here
//
//        lifecycleScope.launch {
//            val success = RemoteConfigManager.fetchRemoteValues()
//            Log.d("RemoteConfig", "Fetch success: $success")
//        }

        setContent {
            // MaterialTheme provides basic styling; replace with your actual theme if available.
            MaterialTheme {
                val navController = rememberNavController()
                val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
                val scope = rememberCoroutineScope() // Scope for launching drawer operations

                ModalNavigationDrawer(
                    drawerState = drawerState,
                    drawerContent = {
                        AppDrawerContent(
                            onCloseDrawer = { scope.launch { drawerState.close() } },
                            // Add navigation logic here when you have multiple screens

                            onNavigateToUserForm = {
                                navController.navigate("user_form")
                                scope.launch { drawerState.close() }
                            },
                            onNavigateToAdminLogin = {
                                navController.navigate("admin_login")
                                scope.launch { drawerState.close() }
                            },
                            onNavigateToMessUI = {
                                navController.navigate("mess")
                                scope.launch { drawerState.close() }
                            } ,

                            onNavigateToLocation = {
                                navController.navigate("location")
                                scope.launch { drawerState.close() }
                            },
                            onNavigateToUpdate = {
                                navController.navigate("update")
                                scope.launch { drawerState.close() }
                            }
                        )
                    }
                )
                {
                    Scaffold(
                        modifier = Modifier
                            .fillMaxSize(),
//                            .background(Color.Black),
                        topBar = {
                            AppTopBar(
                                onMenuClick = { scope.launch { drawerState.open() } },
                                appName = "Niyati Girls Hostel",
                                context = this@MainActivity
                            )
                        }
                    ) { padding ->
                        // The main content of your app, now with padding from the Scaffold's topBar
                        NavHost(navController = navController, startDestination = "rules"
                        , modifier = Modifier
                                .padding(padding)
                                .verticalScroll(rememberScrollState())
                        ) {
                            composable("user_form") { AddUserForm(this@MainActivity) }
                            composable("admin_login") { LoginScreen(this@MainActivity, navController) }
                            composable("admin_dashboard") { AdminDashboard() }
                            composable("api") { Current_API() }
                            composable("mess") { Mess_UI() }
                            composable("location") { Location() }
                            composable("update") { Update() }
                            composable("rules") { Rule_UI(navController) }
                        }
                    }
                }
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
//        exitProcess(0)
    }
}
/**
 * Composable for the top application bar, including a logo, app name, and menu icon.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(onMenuClick: () -> Unit, appName: String ,context: Context) {
    val animatedBackgroundColor by animateColorAsState(
        targetValue = Color(0xFFBDB9B9),
        animationSpec = tween(durationMillis = 800, easing = FastOutSlowInEasing),
        label = "TopBarColor"
    )
    TopAppBar(
        title = {
            Column(
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.logo_),
                        contentDescription = "App Logo",
                        modifier = Modifier
                            .size(45.dp)
                            .background(Color.Transparent)
                            .clickable {
                                // EDIT IN FUTURE
                            }
                    )
                    TypingText(appName)
                }

                Row(
                    modifier = Modifier.fillMaxWidth().size(100.dp),
                    horizontalArrangement = Arrangement.SpaceBetween

                ) {
                    Text(
                        text = "Owner: Shubham Kumar",
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                        ),
                        textAlign = TextAlign.Start,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                    Text(
                        text = " +91 8789990658",
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                        ),
                        textAlign = TextAlign.End,
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .clickable {
                                val intent = Intent(Intent.ACTION_DIAL).apply {
                                    data = Uri.parse("tel:+918789990658")
                                }
                                context.startActivity(intent)
                            }
                    )
                }
            }
        },
        navigationIcon = {
            IconButton(onClick = onMenuClick) {
                Icon(
                    imageVector = Icons.Filled.Menu,
                    contentDescription = "Menu",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
                containerColor = animatedBackgroundColor
                )
    )
}
/**
 * Composable for the content inside the navigation drawer.
 */
@Composable
fun AppDrawerContent(
    onCloseDrawer: () -> Unit,
    onNavigateToUserForm: () -> Unit,
    onNavigateToAdminLogin: () -> Unit,
    onNavigateToMessUI: () -> Unit,
    onNavigateToLocation: () -> Unit,
    onNavigateToUpdate : () -> Unit,
) {
    ModalDrawerSheet {
        // Drawer Header
        var selectedItem by remember { mutableStateOf("") }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .height(150.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Image(
                painter = painterResource(id = R.drawable.logo_),
                contentDescription = "Hostel App Icon",
                modifier = Modifier.size(150.dp))
                //tint = MaterialTheme.colorScheme.onSurface)
                Spacer(Modifier.height(8.dp))
                Text(
                    text = "Nitya Girls Hostel",
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
                )
        }
        Divider()
        // Drawer Menu Items
        NavigationDrawerItem(
            label = { Text("Add New student") },
            selected = selectedItem == "USR", // Highlight this item as it's the current screen
            onClick = {
                onNavigateToUserForm()
                selectedItem = "USR"
                onCloseDrawer()
            },
            icon = { Icon(Icons.Filled.Add, contentDescription = "Add User") },
            colors = NavigationDrawerItemDefaults.colors(
                //selectedContainerColor = Color.Blue, // Background when selected
                selectedTextColor = Color.Black,     // Text when selected
                selectedIconColor = Color(0xFF1DA871),      // Icon when selected
                unselectedTextColor = Color.Gray,    // Optional
                unselectedIconColor = Color.Gray     // Optional
            )
        )
        Divider()
        NavigationDrawerItem(
            label = { Text("Mess Menu") },
            selected = selectedItem == "MESS",
            onClick = {  onNavigateToMessUI()
                selectedItem = "MESS"
                onCloseDrawer() },
            icon = { Icon(Icons.Default.DateRange, contentDescription = "mess") },
            colors = NavigationDrawerItemDefaults.colors(
                //selectedContainerColor = Color.Blue, // Background when selected
                selectedTextColor = Color.Black,     // Text when selected
                selectedIconColor = Color(0xFF1DA871),      // Icon when selected
                unselectedTextColor = Color.Gray,    // Optional
                unselectedIconColor = Color.Gray     // Optional
            )
        )
        Divider()
        NavigationDrawerItem(
            label = { Text("Location") },
            selected = selectedItem == "Location",
            onClick = { onNavigateToLocation()
                selectedItem = "Location"
                onCloseDrawer()},
            icon = { Icon(Icons.Default.LocationOn, contentDescription = "Location") },
            colors = NavigationDrawerItemDefaults.colors(
                //selectedContainerColor = Color.Blue, // Background when selected
                selectedTextColor = Color.Black,     // Text when selected
                selectedIconColor = Color(0xFF1DA871),     // Icon when selected
                unselectedTextColor = Color.Gray,    // Optional
                unselectedIconColor = Color.Gray     // Optional
            )
        )

        Divider()
        NavigationDrawerItem(
            label = { Text("Update") },
            selected = selectedItem == "Update",
            onClick = { onNavigateToUpdate()
                selectedItem = "Update"
                onCloseDrawer()},
            icon = { Icon(Icons.Default.Refresh, contentDescription = "Update") },
            colors = NavigationDrawerItemDefaults.colors(
                //selectedContainerColor = Color.Blue, // Background when selected
                selectedTextColor = Color.Black,     // Text when selected
                selectedIconColor = Color(0xFF1DA871),     // Icon when selected
                unselectedTextColor = Color.Gray,    // Optional
                unselectedIconColor = Color.Gray     // Optional
            )
        )
        Divider()
        NavigationDrawerItem(
            label = { Text("Admin Login") },
            selected = selectedItem == "ADM",
            onClick = { onNavigateToAdminLogin()
                selectedItem = "ADM"
                onCloseDrawer()},
            icon = { Icon(Icons.Default.Lock, contentDescription = "Admin") },
            colors = NavigationDrawerItemDefaults.colors(
                //selectedContainerColor = Color.Blue, // Background when selected
                selectedTextColor = Color.Black,     // Text when selected
                selectedIconColor = Color(0xFF1DA871),     // Icon when selected
                unselectedTextColor = Color.Gray,    // Optional
                unselectedIconColor = Color.Gray     // Optional
            )
        )

    }
}


@OptIn(ExperimentalMaterial3Api::class) // Required for TextField
@Composable
fun AddUserForm(context: Context) {

    val scope = rememberCoroutineScope() // Coroutine scope for launching async operations

    // State variables to hold input field values
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var mobile by remember { mutableStateOf("") }
    // adharNo is kept as String initially to handle partial input before validation
    var adharNo by remember { mutableStateOf("") }
    var father by remember { mutableStateOf("") }
    var mother by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var join_date by remember { mutableStateOf("") }

    // State variables for image URIs (kept for structure, but not used in current logic)
    var profileImageUri by remember { mutableStateOf<Uri?>(null) }
    var adharImageUri by remember { mutableStateOf<Uri?>(null) }
    var adharBackImageUri by remember { mutableStateOf<Uri?>(null) }

    // Activity result launchers for picking images
    var imagePicker = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) {
        profileImageUri = it
    }
    var adharPicker = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) {
        adharImageUri = it
    }
    var adharBackPicker = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) {
        adharBackImageUri = it
    }
    var today_date by remember { mutableStateOf("") }
    LaunchedEffect(Unit) {
        today_date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(java.util.Date())
    }

    val useDarkTheme = isSystemInDarkTheme()
    val col by remember(useDarkTheme) {
        mutableStateOf(if (useDarkTheme) Color.Black else Color.White)
    }


    // Main layout container for the form
    Box(
        modifier = Modifier
            .fillMaxSize() // Fills the entire available space
            .background(Color(0xFFCD59DE))
            .padding(2.dp), // Overall padding around the form
        contentAlignment = Alignment.TopCenter // Centers the Column content horizontally, aligns to top
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth() // Makes the column take full width
                .background(col)
                //.verticalScroll(rememberScrollState())
                .padding(16.dp), // Inner padding for the column's content
            horizontalAlignment = Alignment.CenterHorizontally, // Centers items horizontally within the column
            verticalArrangement = Arrangement.spacedBy(10.dp) // Adds vertical space between elements
        ) {
            // Input field for Aadhaar Number
            TextField(
                value = adharNo,
                onValueChange = { newValue ->
                    // Allow only digits; you might want to add a length limit (e.g., 12 digits)
                    if (newValue.all { it.isDigit() } && newValue.length <= 12) {
                        adharNo = newValue
                    }
                },
                label = { Text("Aadhaar No (12 digits)") },
                singleLine = true, // Ensure the input is on a single line
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Gray)
            )
            // Input field for Name
            TextField(
                value = name,
                onValueChange = { newValue ->
                    // Allow only letters and spaces
                    if (newValue.all { it.isLetter() || it.isWhitespace() }) {
                        name = newValue
                    }
                },
                label = { Text("Name") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Words
                )
            )


            // Input field for Father's Name
            TextField(
                value = father,
                onValueChange = { newValue ->
                    // Allow only letters and spaces (optional, adjust as needed)
                    if (newValue.all { it.isLetter() || it.isWhitespace() }) {
                        father = newValue
                    }
                },
                label = { Text("Father's Name") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Words
                )
            )
            // Input field for Mother's Name
            TextField(
                value = mother,
                onValueChange = { newValue ->
                    // Allow only letters and spaces (optional, adjust as needed)
                    if (newValue.all { it.isLetter() || it.isWhitespace() }) {
                        mother = newValue
                    }
                },
                label = { Text("Mother's Name") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Words
                )
            )
            // Input field for Mobile Number
            TextField(
                value = mobile,
                onValueChange = { newValue ->
                    // Allow only digits and limit to 10 characters for mobile numbers
                    if (newValue.all { it.isDigit() } && newValue.length <= 10) {
                        mobile = newValue
                    }
                },
                label = { Text("Mobile No (10 digits)") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            // Input field for Email
            TextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                singleLine = true,
                placeholder = { Text("example@gmail.com") },
                modifier = Modifier.fillMaxWidth()
            )
            // Input field for Permanent Address
            TextField(
                value = address,
                onValueChange = { address = it },
                label = { Text("Permanent Address") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Words
                )
            )
            // Input field for Date Address

            TextField(
                value = join_date,
                onValueChange = { join_date = it },
                label = { Text("join date (keep Blank for today's date )") },
                placeholder = { Text("${today_date}") },
                modifier = Modifier.fillMaxWidth()
            )
            if(join_date.isEmpty()){
                join_date= today_date
            }


            Spacer(modifier = Modifier.height(20.dp)) // Adds extra space before buttons

            // Image picking buttons and previews
            Text(
                text = "Max Image Size: 190KB & Adhar Size: 500KB (Allowed)",
                style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray)
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.Top // Align items to top to prevent large images pushing buttons up
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.weight(1f)) {
                    Button(onClick = { adharPicker.launch("image/*") },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFB019D2), // Blue shade
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth()
                            .height(56.dp),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp),
                        ) {
                        Text(
                            "Aadhaar Front Image",
                            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                            fontSize = 15.sp
                        )
                    }
                    adharPicker = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
                        uri?.let {
                            val fileSize = getFileSize(context, it) // Get file size in bytes
                            if (fileSize <= 501 * 1024) {
                                adharImageUri = it // Save image URI if valid
                            } else {
                                Toast.makeText(context, "Image too large. Max 500KB allowed.", Toast.LENGTH_LONG).show()
                            }
                        }
                    }
                    // Show preview of Aadhaar image
                        AsyncImage(
                            model = adharImageUri,
                            contentDescription = "Aadhaar Front Image",
                            modifier = Modifier
                                .size(100.dp)
                                .background(Color.Transparent)
                                .padding(top = 8.dp),
                            placeholder = painterResource(id =  R.drawable.adhar_place_holder),
                            error = painterResource(id =  R.drawable.adhar_place_holder),
                            fallback = painterResource(id = R.drawable.adhar_place_holder)
                        )
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.weight(1f)) {
                    Button(onClick = { adharBackPicker.launch("image/*") },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFB019D2), // Blue shade
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth()
                            .height(56.dp),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp),
                        ) {
                        Text(
                            "Aadhaar Back Image",
                            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                            fontSize = 15.sp
                        )
                    }
                    adharBackPicker = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
                        uri?.let {
                            val fileSize = getFileSize(context, it) // Get file size in bytes
                            if (fileSize <= 501 * 1024) {
                                adharBackImageUri = it // Save image URI if valid
                            } else {
                                Toast.makeText(context, "Image too large. Max 500KB allowed.", Toast.LENGTH_LONG).show()
                            }
                        }
                    }
                    // Show preview of Aadhaar image
                    AsyncImage(
                        model = adharBackImageUri,
                        contentDescription = "Aadhaar Back Image",
                        modifier = Modifier
                            .size(100.dp)
                            .background(Color.Transparent)
                            .padding(top = 8.dp),
                        placeholder = painterResource(id =  R.drawable.adhar_place_holder_back),
                        error = painterResource(id =  R.drawable.adhar_place_holder_back),
                        fallback = painterResource(id = R.drawable.adhar_place_holder_back)
                    )
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.Top // Align items to top to prevent large images pushing buttons up
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.weight(1f)) {
                    Button(onClick = { imagePicker.launch("image/*") },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFB019D2), // Blue shade
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth()
                            .height(50.dp),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp),
                        ) {
                        Text(
                            "Pick Profile Image",
                            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                            fontSize = 18.sp
                        )
                    }
                    imagePicker = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
                        uri?.let {
                            val fileSize = getFileSize(context, it) // Get file size in bytes
                            if (fileSize <= 191 * 1024) { // 200 KB = 200 * 1024 bytes
                                profileImageUri = it // Save image URI if valid
                            } else {
                                Toast.makeText(context, "Image too large. Max 190KB allowed.", Toast.LENGTH_LONG).show()
                            }
                        }
                    }

                    // Show preview of profile image
                    AsyncImage(
                        model = profileImageUri, // Can be null, fallback will handle it
                        contentDescription = "Profile Image",
                        modifier = Modifier
                            .size(100.dp)
                            .padding(top = 8.dp),
                        placeholder = painterResource(id = R.drawable.image_place_holder),
                        error = painterResource(id = R.drawable.image_place_holder),
                        fallback = painterResource(id = R.drawable.image_place_holder)
                    )
                }
            }
            Spacer(modifier = Modifier.height(20.dp))

            // Submit Button
            Button(
                onClick = {
                    // --- Input Validation before submitting ---
                    if (adharNo.length != 12 || adharNo.toLongOrNull() == null) {
                        Toast.makeText(context, "Please enter a valid 12-digit Aadhaar No.", Toast.LENGTH_LONG).show()
                        return@Button // Exit onClick if validation fails
                    }
                    if (name.isBlank() || father.isBlank() || mother.isBlank() || address.isBlank() || join_date.isBlank()) {
                        Toast.makeText(context, "All fields (Name, Father, Mother, Address) are required.", Toast.LENGTH_LONG).show()
                        return@Button
                    }
                    if (mobile.length != 10) {
                        Toast.makeText(context, "Please enter a valid 10-digit Mobile No.", Toast.LENGTH_LONG).show()
                        return@Button
                    }
                    // Basic email format validation
                    if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                        Toast.makeText(context, "Please enter a valid email address.", Toast.LENGTH_LONG).show()
                        return@Button
                    }
                    if(profileImageUri == null || adharImageUri == null  || adharBackImageUri == null ){
                        Toast.makeText(context, "Please Upload all images.", Toast.LENGTH_LONG).show()
                        return@Button
                    }


                    // --- If validation passes, proceed with data submission ---
                    scope.launch {
                        try {
                            Toast.makeText(context, "Wait few seconds while uploading your details", Toast.LENGTH_LONG).show()

                            // Image upload calls - Re-enabled
                            val profileUrl = profileImageUri?.let { uri ->
                                // Using Aadhaar No for unique filename for consistency with user data
                                uploadImage(uri, "${name}_${adharNo}.jpg","Profile", context)
                            }
                            val adharUrl = adharImageUri?.let { uri ->
                                // Using Aadhaar No for unique filename
                                uploadImage(uri, "${name}_${adharNo}.jpg","AdharFront", context)
                            }
                            val adharBackUrl = adharBackImageUri?.let { uri ->
                                // Using Aadhaar No for unique filename
                                uploadImage(uri, "${name}_${adharNo}.jpg","AdharBack", context)
                            }

                            if (profileUrl == null){
                                Toast.makeText(context, "profile upload failed ", Toast.LENGTH_SHORT).show()
                            }
                            if (adharUrl == null){
                                Toast.makeText(context, "adhar upload failed ", Toast.LENGTH_SHORT).show()

                            }


                            // Create the User object from current form states
                            val user = User(
                                adhar_no = adharNo.trim(), // Convert validated String to Long
                                name = name.trim(), // Trim whitespace
                                father = father.trim(),
                                mother = mother.trim(),
                                mobile = mobile.trim(),
                                email = email.trim(),
                                per_address = address.trim(),
                                image_url = profileUrl, // Include uploaded URL
                                adhar_image = adharUrl, // Include uploaded URL
                                adhar_back = adharBackUrl, // Include uploaded URL
                                join_date = join_date
                                )

                            // Log the user data being sent for debugging
//                            Log.d("AddUserForm", "Attempting to add user: ${Json.encodeToString(User.serializer(), user)}")

                            // Call the suspend function to add the user to Supabase
                            val success = addUser(user)

                            if (success) {
                                Toast.makeText(context, "User ${user.name} added successfully!", Toast.LENGTH_SHORT).show()
                                // Clear form fields after successful submission for next entry
                                name = ""
                                email = ""
                                mobile = ""
                                adharNo = ""
                                father = ""
                                mother = ""
                                address = ""
                                profileImageUri = null
                                adharImageUri = null
                                adharBackImageUri  = null
                                join_date = today_date
                            } else {
                                // Inform the user to check logs for detailed errors
                                Toast.makeText(context, "Failed to add user. Check details OR Internet.", Toast.LENGTH_LONG).show()
                            }
                        } catch (e: Exception) {
                            // Catch any unexpected exceptions during the submission process
//                            Log.e("AddUserForm", "An unexpected error occurred during user submission: ${e.message}", e)
                            Toast.makeText(context, "An unexpected error occurred: ${e.message}", Toast.LENGTH_LONG).show()
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFB019D2), // Blue shade
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
                    .height(50.dp),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp),
//                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text(
                    "Submit",
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                    fontSize = 18.sp
                )
            }
        }
    }
}

/**
 * Suspended function to insert a User object into the Supabase 'Users' table.
 * Includes enhanced logging to help debug insertion failures.
 *
 * @param user The User object to be inserted.
 * @return True if the insertion was successful, false otherwise.
 */
suspend fun addUser(user: User): Boolean {
    return try {
        // Log the user object that is about to be inserted
//        Log.d("SupabaseService", "Preparing to insert user: ${Json.encodeToString(User.serializer(), user)}")

        // Perform the insertion operation
        supabase.postgrest.from("Users").insert(user)
        true
    } catch (e: Exception) {
        // Log the detailed error message and stack trace if insertion fails
//        Log.e("SupabaseService", "Failed to add user to Supabase: ${e.message}", e)
        false
    }
}

suspend fun uploadImage(uri: Uri, filename: String, PATH: String, context: Context): String? {
    val stream = context.contentResolver.openInputStream(uri)
    if (stream == null) {
        return null
    }
    val bytes = stream.readBytes()
    stream.close()

    return try {
        val bucket = supabase.storage.from("user-images") // ✅ only bucket name
        bucket.upload(
            path = "$PATH/$filename", // ✅ correct path inside the bucket
            data = bytes,
            upsert = true
        )
        bucket.publicUrl("$PATH/$filename")
    } catch (e: Exception) {
        null
    }
}

suspend fun fetchData(): List<User> {
    return try {
        supabase.postgrest
            .from("Users")
            .select() // only request needed fields
            .decodeList<User>()

    } catch (e: Exception) {
        emptyList() // fallback if something goes wrong
    }
}

suspend fun fetchDataADMIN(): List<ADMIN> {
    return try {
        supabase.postgrest
            .from("ADMIN")
            .select {
                limit(1) // ✅ Limit to one row from server
            }
            .decodeList<ADMIN>()

    } catch (e: Exception) {
        emptyList() // fallback if something goes wrong
    }
}

suspend fun deleteUser(name:String ,adharNo: String): Boolean {
    val filename = "${name}_${adharNo}.jpg"
    return try {
        supabase.postgrest.from("Users")
            .delete {
                eq("adhar_no", adharNo)
            }
            .decodeList<User>() // optional if you want to decode deleted rows

        // Step 2: Delete the image from storage (assumes bucket "user-images" and path "Profile")
        supabase.storage
            .from("user-images") // Bucket name
            //.delete("Profile/${name}_${adharNo.substring(0, 5)}.jpg") // Full path to image
            .delete(listOf("Profile/$filename"))
        Log.e("profile", "file $filename")

        supabase.storage
            .from("user-images") // Bucket name
            //.delete("AdharBack/${name}_${adharNo.substring(0, 5)}.jpg") // Full path to image
            .delete(listOf("AdharBack/$filename"))
        Log.e("Adhar bk", "file $filename")


        supabase.storage
            .from("user-images") // Bucket name
            //.delete("AdharFront/${name}_${adharNo.substring(0, 5)}.jpg") // Full path to image
            .delete(listOf("AdharFront/$filename"))
        Log.e("Adhar fr", "file $filename")
        true

    } catch (e: Exception) {
        e.printStackTrace()
        false
    }
}

suspend fun searchUserByName(name: String): List<User> {
    return try {
        supabase.postgrest
            .from("Users")
            .select {
                ilike("name", "%$name%") // ✅ inside filter block
            }
            .decodeList<User>()
    } catch (e: Exception) {
        e.printStackTrace()
        emptyList()
    }
}

// ANIMATION TEXT

@Composable
fun TypingText(
    fullText: String,
    DEV : String  = "Patna",
    typingSpeed: Long = 100L,     // milliseconds per character
    restartDelay: Long = 5000L    // delay before restarting the typing animation
) {
    var displayedText by remember { mutableStateOf("") }
    var i by remember { mutableStateOf("1") }

    LaunchedEffect(Unit) {
        while (true) {
            if(i.isNotEmpty()){
                i = ""
                displayedText = ""
                fullText.forEachIndexed { index, _ ->
                    displayedText = fullText.substring(0, index + 1)
                    delay(typingSpeed)
                }
                delay(restartDelay)
            }else{
                i = "1"
                displayedText = ""
                DEV.forEachIndexed { index, _ ->
                    displayedText = DEV.substring(0, index + 1)
                    delay(typingSpeed)
                }
                delay(restartDelay)
            }
        }
    }

    Text(
        text = displayedText,
        color = Color.Black,
        style = MaterialTheme.typography.titleLarge.copy(
            fontSize = 20.sp, // slightly bigger for better readability
            fontWeight = FontWeight.Bold,
        ),
        textAlign = TextAlign.Center,
        modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp)
    )
}


// get filesize and check for givien range
fun getFileSize(context: Context, uri: Uri): Long {
    val returnCursor = context.contentResolver.query(uri, null, null, null, null)
    returnCursor?.use { cursor ->
        val sizeIndex = cursor.getColumnIndex(android.provider.OpenableColumns.SIZE)
        cursor.moveToFirst()
        return cursor.getLong(sizeIndex)
    }
    return 0L
}

