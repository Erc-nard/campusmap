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
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.window.core.layout.WindowSizeClass
import androidx.window.layout.WindowMetricsCalculator
import com.example.campusmap.ui.map.CampusMapScreen
import com.example.campusmap.ui.theme.black
import com.example.campusmap.ui.theme.dark
import com.example.campusmap.ui.theme.white
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.launch


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
    var markerPosition by rememberSaveable { mutableStateOf(initialLatLng) }

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
            navigationBarContainerColor = Color(0xFFFDFDFD),
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
                    Map(Modifier.fillMaxHeight(), cameraPositionState, markerPosition)
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
    FACILITIES("시설 안내", Icons.Default.Place),
    SHUTTLE("셔틀버스", Icons.Default.DirectionsBus),
}

data class MapCategory(val icon: ImageVector, val text: String, val color: Color)
val mapCategories = listOf(
    MapCategory(Icons.Default.School,"강의동", Color(95,190,235)),
    MapCategory(Icons.Default.Restaurant,"식당", Color(250, 189, 0, 255)),
    MapCategory(Icons.Default.LocalCafe,"카페", Color(243, 118, 0, 255)),
    MapCategory(Icons.Default.ShoppingCart,"매점", Color(0, 203, 27, 255)),
    MapCategory(Icons.Default.DirectionsBus,"셔틀 정류장", Color.Black),
    MapCategory(Icons.Default.HomeWork,"기숙사", Color(69, 0, 255, 255)),
    MapCategory(Icons.Default.Place,"가볼 만한 곳", Color(255, 0, 161, 255)),
)
data class PlaceData(
    val title: String,
    val category: String,
    val isBuildingItself: Boolean = false,
    val location: Location,
    val coordinates: LatLng = LatLng(0.0, 0.0),
    val keywords: List<String> = listOf(),
    val description: String = "",
    val imageURL: String = ""
)
val places = listOf(
    PlaceData(
        title = "산업경영학동",
        category = "강의동",
        isBuildingItself = true,
        location = Location("E2", "산업경영학동"),
        coordinates = LatLng(0.0, 0.0),
        keywords = listOf("산경동", "수리과", "수학과", "산시공", "산공", "산공과"),
        description = "수리과학과, 산업및시스템공학과"
    ),
    PlaceData(
        title = "정보전자공학동",
        category = "강의동",
        isBuildingItself = true,
        location = Location("E3", "정보전자공학동"),
        coordinates = LatLng(0.0, 0.0),
        keywords = listOf("전산", "전산과", "전전", "전자과"),
        description = "전산학부, 전기및전자공학부"
    ),
    PlaceData(
        title = "자연과학동",
        category = "강의동",
        isBuildingItself = true,
        location = Location("E6", "자연과학동"),
        coordinates = LatLng(0.0, 0.0),
        keywords = listOf("자과동", "수학과", "수리과", "자연과학부", "물리과", "궁리실험관", "실험"),
        description = "수리과학과, 물리학과, 화학과, 생명과학과"
    ),
    PlaceData(
        title = "창의학습관",
        category = "강의동",
        isBuildingItself = true,
        location = Location("E11", "창의학습관"),
        coordinates = LatLng(0.0, 0.0),
        keywords = listOf("창의관", "터만홀"),
        description = "새내기과정학부"
    ),
    PlaceData(
        title = "정문술빌딩",
        category = "강의동",
        isBuildingItself = true,
        location = Location("E16", "정문술빌딩"),
        coordinates = LatLng(0.0, 0.0),
        keywords = listOf("바뇌과"),
        description = "바이오및뇌공학과"
    ),
    PlaceData(
        title = "양분순빌딩",
        category = "강의동",
        isBuildingItself = true,
        location = Location("E16-1", "양분순빌딩"),
        coordinates = LatLng(0.0, 0.0),
        keywords = listOf("바뇌과"),
        description = "바이오및뇌공학과"
    ),
    PlaceData(
        title = "응용공학동",
        category = "강의동",
        isBuildingItself = true,
        location = Location("W1", "응용공학동"),
        coordinates = LatLng(0.0, 0.0),
        keywords = listOf("응공동", "소재과", "건환공", "생화공", "화생공", "화학생명공학과"),
        description = "신소재공학과, 건설및환경공학과, 생명화학공학과"
    ),
    PlaceData(
        title = "디지털인문사회과학부동",
        category = "강의동",
        isBuildingItself = true,
        location = Location("N4", "디지털인문사회과학부동"),
        coordinates = LatLng(0.0, 0.0),
        keywords = listOf("교양과목", "인사동"),
        description = "디지털인문사회과학부"
    ),
    PlaceData(
        title = "기계공학동",
        category = "강의동",
        isBuildingItself = true,
        location = Location("N7", "기계공학동"),
        coordinates = LatLng(0.0, 0.0),
        keywords = listOf("기계동", "원양공", "원양과", "항공과", "기계과"),
        description = "원자력및양자공학과, 항공우주공학과, 기계공학과"
    ),

    PlaceData(
        title = "캘리포니아 베이커리",
        category = "카페",
        location = Location("E6-5", "자연과학동"),
        coordinates = LatLng(0.0, 0.0),
        keywords = listOf("캘포", "빵집"),
        description = "빵, 커피, 차, 음료"
    ),
    PlaceData(
        title = "카페드롭탑",
        category = "카페",
        location = Location("W8", "교육지원동"),
        coordinates = LatLng(0.0, 0.0),
        description = "커피, 차, 음료"
    ),
    PlaceData(
        title = "파스쿠찌",
        category = "카페",
        location = Location("E3", "정보전자공학동"),
        coordinates = LatLng(0.0, 0.0),
        keywords = listOf("파스쿠치"),
        description = "커피, 차, 음료"
    ),
    PlaceData(
        title = "그라찌에",
        category = "카페",
        location = Location("E4", "KI빌딩"),
        coordinates = LatLng(0.0, 0.0),
        keywords = listOf("그라찌에"),
        description = "커피, 차, 음료"
    ),
    PlaceData(
        title = "탐앤탐스",
        category = "카페",
        location = Location("N1", "김병호IT융합빌딩", 2),
        coordinates = LatLng(0.0, 0.0),
        keywords = listOf("탐탐"),
        description = "커피, 차, 음료"
    ),
    PlaceData(
        title = "카페 오가다",
        category = "카페",
        location = Location("E9", "학술문화관", 2),
        coordinates = LatLng(0.0, 0.0),
        description = "커피, 차, 음료"
    ),

    PlaceData(
        title = "동측식당 매점",
        category = "매점",
        location = Location("E5", "교직원회관")
    ),
    PlaceData(
        title = "서측 학생회관 매점",
        category = "매점",
        location = Location("W2", "학생회관-2")
    ),
    PlaceData(
        title = "잡화점",
        category = "매점",
        location = Location("N13", "태울관", 2),
        description = "잡화, 전자제품, 학용품"
    ),

    PlaceData(
        title = "사랑관",
        category = "기숙사",
        isBuildingItself = true,
        location = Location("N14", "사랑관"),
        description = "북측 남학생 기숙사, 학부생"
    ),
    PlaceData(
        title = "소망관",
        category = "기숙사",
        isBuildingItself = true,
        location = Location("N16", "소망관"),
        description = "북측 남학생 기숙사, 학부생"
    ),
    PlaceData(
        title = "성실관",
        category = "기숙사",
        isBuildingItself = true,
        location = Location("N17", "성실관"),
        description = "북측 남학생 기숙사, 학부생"
    ),
    PlaceData(
        title = "진리관",
        category = "기숙사",
        isBuildingItself = true,
        location = Location("N18", "진리관"),
        description = "북측 남학생 기숙사, 학부생"
    ),
    PlaceData(
        title = "아름관",
        category = "기숙사",
        isBuildingItself = true,
        location = Location("N19", "아름관"),
        description = "북측 여학생 기숙사, 학부생, 체력단련실"
    ),
    PlaceData(
        title = "신뢰관",
        category = "기숙사",
        isBuildingItself = true,
        location = Location("N20", "신뢰관"),
        description = "북측 남학생 기숙사, 학부생, 체력단련실"
    ),
    PlaceData(
        title = "지혜관",
        category = "기숙사",
        isBuildingItself = true,
        location = Location("N21", "지혜관"),
        description = "북측 남학생 기숙사, 학부생"
    ),
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Map(modifier: Modifier = Modifier, cameraPositionState: CameraPositionState, markerPosition: LatLng) {
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
    var searchFieldText by remember { mutableStateOf("")}
    var searchQuery by remember { mutableStateOf("")}
    val keyboardController = LocalSoftwareKeyboardController.current
    val sheetScaffoldState = rememberBottomSheetScaffoldState()
    val scope = rememberCoroutineScope()

    @Composable
    fun MapCategoryButton(data: MapCategory) {
        Row(
            modifier = Modifier
                .shadow(3.dp, shape = RoundedCornerShape(20.dp))
                .border(
                    width = 2.dp,
                    color = white,
                    shape = RoundedCornerShape(20.dp)
                )
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
    fun SearchResultRow(data: PlaceData) {
        Column(
            modifier = Modifier
                .clickable {}
                .fillMaxWidth()
                .padding(16.dp, 8.dp)
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

    Scaffold(
        topBar = {
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
                    if (searchQuery.isNotBlank()) {
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
                    TextField(
                        value = searchFieldText,
                        onValueChange = { newValue -> searchFieldText = newValue },
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                        keyboardActions = KeyboardActions(
                            onSearch = {
                                keyboardController?.hide()
                                searchQuery = searchFieldText.trimEnd()
                                if (searchQuery.isBlank()) {
                                    scope.launch {
                                        sheetScaffoldState.bottomSheetState.partialExpand()
                                    }
                                }
                            }
                        ),
                        modifier = Modifier
                            .weight(1f),
                        placeholder = { Text("건물, 식당, 편의시설 검색") },
                        singleLine = true,
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        )
                    )
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
    ) { innerPadding ->
        val searchResult = places.filter { item ->
            item.title.contains(searchQuery) || item.category == searchQuery
                    || item.location.buildingCode == searchQuery
                    || (searchQuery.contains("-") && item.location.buildingCode == searchQuery.substringBefore('-'))
                    || item.location.buildingName.contains(searchQuery)
                    || item.keywords.contains(searchQuery)
                    || item.description.contains(searchQuery)
        }
        BottomSheetScaffold(
            scaffoldState = sheetScaffoldState,
            sheetPeekHeight = if (searchQuery.isBlank()) 0.dp else 140.dp,
            sheetContent = {
                LazyColumn(
                    modifier = Modifier.heightIn(max =
                        if (searchQuery.isBlank()) {
                            0.dp
                        } else {
                            LocalConfiguration.current.screenHeightDp.dp - innerPadding.calculateTopPadding() - 160.dp
                        })
                ) {
                    if (searchResult.isNotEmpty()) {
                        items(searchResult) { resultItem ->
                            SearchResultRow(resultItem)
                        }
                    } else {
                        item {
                            Text(
                                text = "검색 결과가 없습니다.",
                                modifier = Modifier
                                    .fillMaxWidth(),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        ) {
            CampusMapScreen(
                modifier = modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                mapProperties = mapProperties,
                markerPosition = markerPosition
            )
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