package com.example.campusmap

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Map
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import com.example.campusmap.ui.theme.CampusmapTheme
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.DirectionsBus
import androidx.compose.material.icons.filled.HomeWork
import androidx.compose.material.icons.filled.LocalCafe
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.School
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Adjust
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.NavigationRailItemDefaults
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteDefaults
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.example.campusmap.ui.map.CampusMapScreen
import com.example.campusmap.ui.theme.appBackground
import com.example.campusmap.ui.theme.black
import com.example.campusmap.ui.theme.dark
import com.example.campusmap.ui.theme.white
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.time.format.TextStyle


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CampusmapTheme {
                CampusmapApp()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@PreviewScreenSizes
@Composable
fun CampusmapApp() {
    val scope = rememberCoroutineScope()

    var currentDestination by rememberSaveable { mutableStateOf(AppDestinations.MAP) }

    // map tab
    val initialLatLng = LatLng(36.368038, 127.365761)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(initialLatLng, 16f)
    }
    val markerPositionsState = remember { mutableStateOf<List<LatLng>>(listOf()) }
    val selectedBuildingState = remember { mutableStateOf<String?>(null) }

    val context = LocalContext.current
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }
    val currentLocation = remember {
        mutableStateOf<LatLng?>(null)
    }
    fun getCurrentLocation(postAction: (LatLng?) -> Unit = {}) {
        scope.launch {
            try {
                val result = fusedLocationClient.getCurrentLocation(
                    Priority.PRIORITY_HIGH_ACCURACY,
                    null
                ).await()

                result?.let { location ->
                    currentLocation.value = LatLng(location.latitude, location.longitude)
                }
            } catch(e: SecurityException) {
                currentLocation.value = null
            }

            postAction(currentLocation.value)
        }
    }

    // facilities tab
    val navController = rememberNavController()

    // shuttle tab
    var showShuttleSheet by rememberSaveable { mutableStateOf(false) }
    var showShuttleScreen by rememberSaveable { mutableStateOf(false) }
    var selectedShuttle by rememberSaveable { mutableStateOf<ShuttleType?>(null) }

    val myItemColors = NavigationSuiteDefaults.itemColors(
        navigationBarItemColors = NavigationBarItemDefaults.colors(
            selectedIconColor = Color(0xFF004187),
            selectedTextColor = Color(0xFF004187),
            indicatorColor = Color(0xFFD0ECF9),
            unselectedIconColor = black,
            unselectedTextColor = black
        ),
        navigationRailItemColors = NavigationRailItemDefaults.colors(
            selectedIconColor = Color(0xFF004187),
            selectedTextColor = Color(0xFF004187),
            indicatorColor = Color(0xFFD0ECF9),
            unselectedIconColor = black,
            unselectedTextColor = black
        )
    )
    BackHandler(enabled = showShuttleScreen) {
        showShuttleScreen = false
        currentDestination = AppDestinations.MAP
    }

    NavigationSuiteScaffold(
        containerColor = Color.White,
        navigationSuiteColors = NavigationSuiteDefaults.colors(
            navigationBarContainerColor = Color.White,
            navigationBarContentColor = dark
        ),
        navigationSuiteItems = {
            AppDestinations.entries.forEach { destination ->
                item(
                    icon = { Icon(destination.icon, contentDescription = destination.label) },
                    label = { Text(destination.label) },
                    selected = destination == currentDestination,
                    colors = myItemColors,
                    onClick = {
                        when (destination) {
                            AppDestinations.MAP -> {
                                currentDestination = destination
                            }
                            AppDestinations.FACILITIES -> if (currentDestination != AppDestinations.FACILITIES) {
                                currentDestination = destination
                            } else {
                                val routeName = navController.currentBackStackEntry?.destination?.route?.substringAfterLast(".")
                                if (routeName == "FacilityItemRoute/{categoryIndex}/{index}") {
                                    navController.popBackStack()
                                }
                            }
                            AppDestinations.SHUTTLE -> {
                                showShuttleSheet = true
                            }
                        }
                    }

                )
            }
        }
    ) {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            when (currentDestination) {
                AppDestinations.MAP ->
                    Map(
                        Modifier.fillMaxHeight(),
                        cameraPositionState = cameraPositionState,
                        markerPositionsState = markerPositionsState,
                        selectedBuildingState = selectedBuildingState,
                        getCurrentLocation = ::getCurrentLocation
                    )
                AppDestinations.FACILITIES -> {
                    BackHandler(enabled = true) {
                        currentDestination = AppDestinations.MAP
                    }
                    FacilitiesNavigation(padding = innerPadding, navController = navController, onMoveToMap = { coordinate, buildingCode ->
                        currentDestination = AppDestinations.MAP
                        scope.launch {
                            cameraPositionState.animate(
                                update = CameraUpdateFactory.newLatLngZoom(coordinate, 18f)
                            )
                        }
                        selectedBuildingState.value = buildingCode
                        markerPositionsState.value = listOf(coordinate)
                    }, getCurrentLocation = ::getCurrentLocation)
                }
                AppDestinations.SHUTTLE ->
                    Shuttle(
                        name = "Hello, world!",
                        modifier = Modifier.padding(innerPadding)
                    )
            }
        }
    }

    // BottomSheet
    if (showShuttleSheet) {
        ModalBottomSheet(
            onDismissRequest = { showShuttleSheet = false },
            containerColor = Color.White,
            sheetState = rememberModalBottomSheetState()
        ) {
            Column (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // 교내
                    Button(
                        onClick = {
                            selectedShuttle = ShuttleType.CAMPUS
                            showShuttleSheet = false
                            showShuttleScreen = true
                            currentDestination = AppDestinations.SHUTTLE
                        },
                        modifier = Modifier.weight(1f)
                    ) { Text("교내", style = MaterialTheme.typography.titleLarge) }

                    // 교외
                    Button(
                        onClick = {
                            selectedShuttle = ShuttleType.OUTSIDE
                            showShuttleSheet = false
                            showShuttleScreen = true
                            currentDestination = AppDestinations.SHUTTLE
                        },
                        modifier = Modifier.weight(1f)
                    ) { Text("출근", style = MaterialTheme.typography.titleLarge) }
                }

                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }

    if (showShuttleScreen) {
        selectedShuttle?.let { shuttle ->
            ShuttleScreenFixed(
                startShuttle = shuttle,
                onClose = {
                    showShuttleScreen = false
                    currentDestination = AppDestinations.MAP
                }
            )
        }
    }
}


enum class AppDestinations(
    val label: String,
    val icon: ImageVector,
) {
    MAP("지도", Icons.Default.Map),
    FACILITIES("시설", Icons.Default.Place),
    SHUTTLE("셔틀버스", Icons.Default.DirectionsBus),
}

@Composable
fun Shuttle(name: String, modifier: Modifier = Modifier) {
    Text(
        text = name,
        modifier = modifier
    )
}