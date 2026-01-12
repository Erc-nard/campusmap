package com.example.campusmap

import android.os.Bundle
import android.util.EventLogTags
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material.icons.filled.Coffee
import androidx.compose.material.icons.filled.DirectionsBus
import androidx.compose.material.icons.filled.HomeWork
import androidx.compose.material.icons.filled.LocalCafe
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.School
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.NavigationRailItemDefaults
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteDefaults
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.window.core.layout.WindowSizeClass
import androidx.window.layout.WindowMetricsCalculator
import com.example.campusmap.ui.map.CampusMapScreen
import com.example.campusmap.ui.theme.appBackground
import com.example.campusmap.ui.theme.black
import com.example.campusmap.ui.theme.dark
import com.example.campusmap.ui.theme.white
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import kotlinx.coroutines.launch
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
    var showShuttleSheet by rememberSaveable { mutableStateOf(false) }
    var showShuttleScreen by rememberSaveable { mutableStateOf(false) }
    var selectedShuttle by rememberSaveable { mutableStateOf<ShuttleType?>(null) }

    val initialLatLng = LatLng(36.368038, 127.365761)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(initialLatLng, 16f)
    }
    var markerState = rememberMarkerState(position = initialLatLng)

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

    NavigationSuiteScaffold(
        containerColor = Color.White,
        navigationSuiteColors = NavigationSuiteDefaults.colors(
            navigationBarContainerColor = Color.White,
            navigationBarContentColor = dark
        ),
        navigationSuiteItems = {
            AppDestinations.entries.forEach { destination ->
                // 2. 미리 만들어둔 myItemColors 변수를 그대로 사용합니다.
                item(
                    icon = { Icon(destination.icon, contentDescription = destination.label) },
                    label = { Text(destination.label) },
                    selected = destination == currentDestination,
                    colors = myItemColors,
                    onClick = {
                        if (destination == AppDestinations.SHUTTLE) {
                            showShuttleSheet = true
                        } else {
                            currentDestination = destination
                        }
                    }

                )
            }
        }
    ) {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            when (currentDestination) {
                AppDestinations.MAP ->
                    Map(Modifier.fillMaxHeight(), cameraPositionState, markerState)
                AppDestinations.FACILITIES -> {
                    BackHandler(enabled = true) {
                        currentDestination = AppDestinations.MAP
                    }
                    FacilitiesNavigation(padding = innerPadding, onMoveToMap = { coordinate ->
                        currentDestination = AppDestinations.MAP
                        markerState.position = coordinate
                        scope.launch {
                            cameraPositionState.animate(
                                update = CameraUpdateFactory.newLatLngZoom(coordinate, 18f)
                            )
                        }
                    })
                }
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
                        },
                        modifier = Modifier.weight(1f)
                    ) { Text("교내", style = MaterialTheme.typography.titleLarge) }

                    // 교외
                    Button(
                        onClick = {
                            selectedShuttle = ShuttleType.OUTSIDE
                            showShuttleSheet = false
                            showShuttleScreen = true
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
                onClose = { showShuttleScreen = false }
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

data class MapCategory(val icon: ImageVector, val text: String, val color: Color)
val mapCategories = listOf(
    MapCategory(Icons.Default.School,"강의동", Color(95,190,235)),
    MapCategory(Icons.Default.Restaurant,"식당", Color(250, 189, 0, 255)),
    MapCategory(Icons.Default.LocalCafe,"카페", Color(243, 118, 0, 255)),
    MapCategory(Icons.Default.ShoppingCart,"매점", Color(0, 203, 27, 255)),
    MapCategory(Icons.Default.DirectionsBus,"셔틀", Color.Black),
    MapCategory(Icons.Default.HomeWork,"기숙사", Color(69, 0, 255, 255)),
//    MapCategory(Icons.Default.Place,"가볼 만한 곳", Color(255, 0, 161, 255)),
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Map(modifier: Modifier = Modifier, cameraPositionState: CameraPositionState, markerState: MarkerState) {
    val mapProperties = remember {
        MapProperties(
            latLngBoundsForCameraTarget = LatLngBounds(
                LatLng(36.36244323875914, 127.35429730754099),
                LatLng(36.37798415287542, 127.3705715881045)
            ),
            minZoomPreference = 15f,
            maxZoomPreference = 20f
        )
    }
    val interactionSource = remember { MutableInteractionSource() }
    var searchFieldText by remember { mutableStateOf("")}
    var searchQuery by remember { mutableStateOf("")}
    var selectedPlace by remember { mutableStateOf<PlaceData?>(null) }
    val keyboardController = LocalSoftwareKeyboardController.current
    val sheetScaffoldState = rememberBottomSheetScaffoldState()
    val scope = rememberCoroutineScope()

    @Composable
    fun MapCategoryButton(data: MapCategory) {
        Row(
            modifier = Modifier
                .shadow(8.dp, shape = RoundedCornerShape(20.dp))
                .clip(RoundedCornerShape(20.dp))
                .background(white)
                .clickable {
                    searchFieldText = data.text
                    searchQuery = data.text
                }
                .padding(horizontal = 10.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 🔹 아이콘
            Icon(
                imageVector = data.icon,
                contentDescription = data.text,
                modifier = Modifier.size(16.dp),
                tint = data.color
            )
            Spacer(modifier = Modifier.width(6.dp))
            // 🔹 텍스트
            Text(data.text)
        }
    }
    @Composable
    fun SearchResultRow(data: PlaceData, clickable: Boolean = true) {
        Column(
            modifier = Modifier
                .clickable(enabled = clickable) {
                    searchFieldText = data.title
                    selectedPlace = data
                    scope.launch {
                        cameraPositionState.animate(
                            update = CameraUpdateFactory.newLatLngZoom(data.coordinates, 18f)
                        )
                        markerState.position = data.coordinates
                    }
                }
                .fillMaxWidth()
                .padding(20.dp, 10.dp)
        ) {
            Row {
                Text(
                    text = data.title,
                    fontWeight = FontWeight.Bold
                )
                Text(" · ")
                Text(data.category)
            }
            Text(
                text = if (data.isBuildingItself) data.location.buildingCode else data.location.description,
                color = Color.Gray
            )
            Text(
                text = data.description,
                color = Color.Gray
            )
        }
    }

    val searchResult = places.filter { item ->
        item.title.contains(searchQuery) || item.category == searchQuery
                || item.location.buildingCode == searchQuery
                || (searchQuery.contains("-") && item.location.buildingCode == searchQuery.substringBefore('-'))
                || item.location.buildingName.contains(searchQuery)
                || item.keywords.contains(searchQuery)
                || item.description.contains(searchQuery)
    }
    val density = LocalDensity.current

    BoxWithConstraints {
        val containerHeight = maxHeight
        val statusBarHeight = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
        val handleHeight = 48.dp
        val maxSheetHeight = containerHeight - statusBarHeight - handleHeight

        BottomSheetScaffold(
            scaffoldState = sheetScaffoldState,
            sheetPeekHeight = if (searchQuery.isBlank()) 0.dp else 160.dp,
            sheetContainerColor = appBackground,
            sheetContent = {
                if (selectedPlace != null) {
                    LazyColumn(
                        modifier = Modifier
                            .heightIn(max = if (searchQuery.isBlank()) 0.dp else maxSheetHeight)
                            .padding(horizontal = 20.dp)
                            .padding(bottom = 20.dp)
                            .clip(RoundedCornerShape(20.dp))
                            .background(white)
                    ) {
                        item {
                            SearchResultRow(selectedPlace!!, clickable = false)
                        }
                    }
                } else if (searchResult.isNotEmpty()) {
                    Text(
                        text = "검색 결과 ${searchResult.size}개",
                        modifier = Modifier
                            .padding(horizontal = 40.dp)
                            .padding(bottom = 10.dp)
                    )
                    LazyColumn(
                        modifier = Modifier
                            .heightIn(max = if (searchQuery.isBlank()) 0.dp else maxSheetHeight)
                            .padding(horizontal = 20.dp)
                            .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                            .background(white)
                    ) {
                        items(searchResult) { resultItem ->
                            SearchResultRow(resultItem)
                        }
                    }
                } else {
                    Text(
                        text = "검색 결과가 없습니다.",
                        modifier = Modifier
                            .fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                }
            }
        ) { innerPadding ->
            Box {
                CampusMapScreen(
                    modifier = modifier.fillMaxSize(),
                    cameraPositionState = cameraPositionState,
                    mapProperties = mapProperties,
                    markerState = markerState
                )

                Column() {
                    Spacer(modifier = Modifier.windowInsetsTopHeight(WindowInsets.statusBars))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .shadow(elevation = 5.dp, shape = RoundedCornerShape(50.dp))
                            .clip(RoundedCornerShape(50.dp))
                            .background(Color.White),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (selectedPlace != null) {
                            BackHandler(enabled = true) { selectedPlace = null }
                            IconButton(
                                onClick = { selectedPlace = null }
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Default.ArrowBack,
                                    contentDescription = "뒤로"
                                )
                            }
                        } else if (searchQuery.isNotBlank()) {
                            fun clearSearchField() {
                                searchFieldText = ""
                                searchQuery = ""
                            }
                            BackHandler(enabled = true) { clearSearchField() }
                            IconButton(
                                onClick = { clearSearchField() }
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Default.ArrowBack,
                                    contentDescription = "뒤로"
                                )
                            }
                        }
                        BasicTextField(
                            value = searchFieldText,
                            onValueChange = { newValue -> searchFieldText = newValue },
                            interactionSource = interactionSource,
                            modifier = Modifier
                                .weight(1f)
                                .padding(horizontal = if (selectedPlace == null && searchQuery.isBlank()) 20.dp else 0.dp),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                            keyboardActions = KeyboardActions(
                                onSearch = {
                                    keyboardController?.hide()
                                    selectedPlace = null
                                    searchQuery = searchFieldText.trimEnd()
                                    if (searchQuery.isBlank()) {
                                        scope.launch {
                                            sheetScaffoldState.bottomSheetState.partialExpand()
                                        }
                                    }
                                }
                            ),
                        ) { innerTextField ->
                            TextFieldDefaults.DecorationBox(
                                value = searchFieldText,
                                innerTextField = innerTextField,
                                enabled = true,
                                singleLine = true,
                                visualTransformation = VisualTransformation.None,
                                interactionSource = interactionSource,
                                contentPadding = PaddingValues(0.dp),
                                container = {
                                    Box {
                                        if (searchFieldText.isEmpty()) {
                                            Text(
                                                text = "검색",
                                                fontSize = 16.sp,
                                                color = Color.Gray
                                            )
                                        }
                                        TextFieldDefaults.Container(
                                            enabled = true,
                                            isError = false,
                                            interactionSource = interactionSource,
                                            colors = TextFieldDefaults.colors(
                                                focusedContainerColor = Color.Transparent,
                                                unfocusedContainerColor = Color.Transparent,
                                                focusedIndicatorColor = Color.Transparent,
                                                unfocusedIndicatorColor = Color.Transparent
                                            )
                                        )
                                    }
                                }
                            )
                        }
                        IconButton(
                            onClick = {
                                keyboardController?.hide()
                                searchQuery = searchFieldText.trimEnd()
                                if (searchQuery.isBlank()) {
                                    scope.launch {
                                        sheetScaffoldState.bottomSheetState.partialExpand()
                                    }
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "검색"
                            )
                        }
                    }
                    if (searchQuery.isBlank()) {
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            contentPadding = PaddingValues(16.dp, 12.dp)
                        ) {
                            items(mapCategories) { item ->
                                MapCategoryButton(item)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Shuttle(name: String, modifier: Modifier = Modifier) {
    Text(
        text = name,
        modifier = modifier
    )
}