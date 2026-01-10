package com.example.campusmap

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.ShoppingCart
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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.unit.dp
import com.example.campusmap.ui.map.CampusMapScreen
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
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
    var showShuttleSheet by rememberSaveable { mutableStateOf(false) }
    var showShuttleScreen by rememberSaveable { mutableStateOf(false) }
    var selectedShuttle by rememberSaveable { mutableStateOf<ShuttleType?>(null) }

    val initialLatLng = LatLng(36.368038, 127.365761)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(initialLatLng, 16f)
    }
    var markerPosition by rememberSaveable { mutableStateOf(initialLatLng) }

    NavigationSuiteScaffold(
        navigationSuiteItems = {
            AppDestinations.entries.forEach {
                item(
                    icon = { Icon(it.icon, contentDescription = it.label) },
                    label = { Text(it.label) },
                    selected = it == currentDestination,
                    onClick = {
                        if (it == AppDestinations.SHUTTLE) {
                            showShuttleSheet = true
                        } else {
                            currentDestination = it
                        }
                    }
                )
            }
        }
    ) {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            when (currentDestination) {
                AppDestinations.MAP ->
                    Map(Modifier.padding(innerPadding), cameraPositionState, markerPosition)
                AppDestinations.FACILITIES ->
                    FacilitiesNavigation(padding = innerPadding, onMoveToMap = { coordinate ->
                        currentDestination = AppDestinations.MAP
                        markerPosition = coordinate
                        scope.launch {
                            cameraPositionState.animate(
                                update = CameraUpdateFactory.newLatLngZoom(coordinate, 18f)
                            )
                        }
                    })
                AppDestinations.SHUTTLE ->
                    Shuttle(
                        name = "Hello, world!",
                        modifier = Modifier.padding(innerPadding)
                    )
            }
        }
    }

    //BottomSheet
    if (showShuttleSheet) {
        ModalBottomSheet(
            onDismissRequest = { showShuttleSheet = false },
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
                        },
                        modifier = Modifier.weight(1f)
                    ) { Text("교내",style=MaterialTheme.typography.titleLarge) }

// 교외
                    Button(
                        onClick = {
                            selectedShuttle = ShuttleType.OUTSIDE
                            showShuttleSheet = false
                            showShuttleScreen = true
                        },
                        modifier = Modifier.weight(1f)
                    ) { Text("출근",style=MaterialTheme.typography.titleLarge) }


                }

                Spacer(modifier = Modifier.height(12.dp))


            }

        }
    }

    if (showShuttleScreen && selectedShuttle != null) {
        ShuttleScreenRoot(
            startShuttle = selectedShuttle!!,
            onClose = { showShuttleScreen = false }
        )
    }


}


enum class AppDestinations(
    val label: String,
    val icon: ImageVector,
) {
    MAP("지도", Icons.Default.Map),
    FACILITIES("시설", Icons.Default.Place),
    SHUTTLE("셔틀", Icons.Default.ShoppingCart),
}

@Composable
fun Map(modifier: Modifier = Modifier, cameraPositionState: CameraPositionState, markerPosition: LatLng) {
    CampusMapScreen(modifier = modifier.fillMaxSize(), cameraPositionState = cameraPositionState, markerPosition = markerPosition)
}

@Composable
fun Shuttle(name: String, modifier: Modifier = Modifier) {
    Text(
        text = name,
        modifier = modifier
    )
}
