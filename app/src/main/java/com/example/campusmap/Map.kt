package com.example.campusmap

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Adjust
import androidx.compose.material.icons.filled.BakeryDining
import androidx.compose.material.icons.filled.Balcony
import androidx.compose.material.icons.filled.BusinessCenter
import androidx.compose.material.icons.filled.DirectionsBus
import androidx.compose.material.icons.filled.HomeWork
import androidx.compose.material.icons.filled.LocalCafe
import androidx.compose.material.icons.filled.Nature
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.campusmap.ui.map.CampusMapScreen
import com.example.campusmap.ui.theme.appBackground
import com.example.campusmap.ui.theme.white
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import kotlinx.coroutines.launch

data class MapCategory(val icon: ImageVector, val text: String, val color: Color)
val mapCategories = listOf(
    MapCategory(Icons.Default.School, "강의·연구동", Color(95,190,235)),
    MapCategory(Icons.Default.Restaurant, "식당", Color(250, 189, 0, 255)),
    MapCategory(Icons.Default.BakeryDining, "베이커리", Color(189, 56, 14, 255)),
    MapCategory(Icons.Default.LocalCafe, "카페", Color(243, 118, 0, 255)),
    MapCategory(Icons.Default.ShoppingCart, "매점", Color(0, 203, 27, 255)),
    MapCategory(Icons.Default.DirectionsBus, "셔틀", Color.Black),
    MapCategory(Icons.Default.HomeWork, "기숙사", Color(69, 0, 255, 255)),
    MapCategory(Icons.Default.School, "학습공간", Color(101, 250, 170, 255)),
    MapCategory(Icons.Default.Nature, "복지시설", Color(205, 220, 57, 255)),
    MapCategory(Icons.Default.BusinessCenter, "행정시설", Color(156, 39, 176, 255)),
//    MapCategory(Icons.Default.Place,"가볼 만한 곳", Color(255, 0, 161, 255)),
)

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun Map(
    modifier: Modifier = Modifier,

    cameraPositionState: CameraPositionState,
    markerPositionsState: MutableState<List<LatLng>>,
    selectedBuildingState: MutableState<String?>,

    searchFieldText: MutableState<String>,
    searchQuery: MutableState<SearchQuery?>,
    selectedPlace: MutableState<SearchResult?>,

    getCurrentLocation: ((LatLng?) -> Unit) -> Unit
) {
    // 권한 관련
    val locationPermissionState = rememberPermissionState(
        android.Manifest.permission.ACCESS_FINE_LOCATION
    )
    val isGranted = locationPermissionState.status.isGranted
    LaunchedEffect(Unit) {
        locationPermissionState.launchPermissionRequest()
    }

    val mapProperties by remember(isGranted) {
        mutableStateOf(
            MapProperties(
                latLngBoundsForCameraTarget = LatLngBounds(
                    LatLng(36.36244323875914, 127.35429730754099),
                    LatLng(36.37798415287542, 127.3705715881045)
                ),
                minZoomPreference = 15f,
                maxZoomPreference = 20f,
                isMyLocationEnabled = isGranted
            )
        )
    }
    val uiSettings = MapUiSettings(myLocationButtonEnabled = false)
    val interactionSource = remember { MutableInteractionSource() }
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
                    searchFieldText.value = data.text
                    searchQuery.value = SearchQuery.Category(data.text)
                }
                .padding(horizontal = 10.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = data.icon,
                contentDescription = data.text,
                modifier = Modifier.size(16.dp),
                tint = data.color
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(data.text)
        }
    }
    @Composable
    fun SearchResultRow(data: SearchResult, clickable: Boolean = true, getCurrentLocation: ((LatLng?) -> Unit) -> Unit = {}) {
        var distanceString by remember {
            mutableStateOf<String?>(null)
        }
        getCurrentLocation { location ->
            if (data.coordinates == null)
                distanceString = null
            else {
                location?.let { currentLocation ->
                    val calculatedDistance = data.getDistance(currentLocation)
                    calculatedDistance?.let { distance ->
                        distanceString = if (distance >= 1000f)
                            "%.2f km".format(distance / 1000)
                        else
                            "%.0f m".format(distance)
                    }
                }
            }
        }

        Column(
            modifier = Modifier
                .clickable(enabled = clickable) {
                    searchFieldText.value = data.name
                    selectedPlace.value = data
                    data.coordinates?.let { coordinates ->
                        scope.launch {
                            cameraPositionState.animate(
                                update = CameraUpdateFactory.newLatLngZoom(coordinates, 18f)
                            )
                            markerPositionsState.value = listOf(coordinates)
                        }
                    }
                    selectedBuildingState.value =
                        data.buildingReference?.code ?: data.placeReference?.buildingCode
                }
                .fillMaxWidth()
                .padding(20.dp, 10.dp)
        ) {
            Row {
                Text(
                    text = data.name,
                    fontWeight = FontWeight.Bold
                )
                if (data.category.isNotBlank()) {
                    Text(
                        text = " · ${data.category}",
                        maxLines = 1
                    )
                }
            }
            Text(
                text = (data.locationDescription.ifBlank { "건물 정보 없음" })
                        + (if (distanceString != null) " · $distanceString" else ""),
                maxLines = 1,
                color = Color.Gray
            )
            if (data.description.isNotBlank()) {
                Text(
                    text = data.description,
                    maxLines = 1,
                    color = Color.Gray
                )
            }
        }
    }

    val searchResult = (if (searchQuery.value != null) getSearchResult(searchQuery.value!!) else listOf()).toMutableList()
    if (searchResult.isNotEmpty()) {
        getCurrentLocation { currentLocation ->
            currentLocation?.let {
                searchResult.sortWith(Comparator { lhs, rhs ->
                    val lhsDistance = lhs.getDistance(currentLocation) ?: Float.MAX_VALUE
                    val rhsDistance = rhs.getDistance(currentLocation) ?: Float.MIN_VALUE
                    (lhsDistance - rhsDistance).toInt()
                })
            }
        }
    }
    markerPositionsState.value = searchResult.mapNotNull { it.coordinates }
    selectedBuildingState.value = null
    val density = LocalDensity.current

    BoxWithConstraints {
        val containerHeight = maxHeight
        val statusBarHeight = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
        val handleHeight = 48.dp
        val maxSheetHeight = containerHeight - statusBarHeight - handleHeight

        BottomSheetScaffold(
            scaffoldState = sheetScaffoldState,
            sheetPeekHeight = if (searchQuery.value == null) 0.dp else 160.dp,
            sheetContainerColor = appBackground,
            sheetContent = {
                if (selectedPlace.value != null) {
                    LazyColumn(
                        modifier = Modifier
                            .heightIn(max = if (searchQuery.value == null) 0.dp else maxSheetHeight)
                            .padding(horizontal = 20.dp)
                            .padding(bottom = 20.dp)
                            .shadow( //그림자옵션
                                elevation = 10.dp,
                                shape = RoundedCornerShape(10.dp),
                                ambientColor = Color(0xFF5FBEEB),
                                spotColor = Color(0x5525739B),
                                clip = false
                            )
                            .clip(RoundedCornerShape(20.dp))
                            .background(white)
                    ) {
                        item {
                            SearchResultRow(selectedPlace.value!!, clickable = false, getCurrentLocation = getCurrentLocation)
                        }
                    }
                } else if (searchResult.isNotEmpty()) {
                    Text(
                        text = "검색 결과 ${searchResult.size}개",
                        modifier = Modifier
                            .padding(horizontal = 20.dp)
                            .padding(bottom = 10.dp)
                    )
                    LazyColumn(
                        modifier = Modifier
                            .heightIn(max = if (searchQuery.value == null) 0.dp else maxSheetHeight)
                            .padding(horizontal = 20.dp)
                            .padding(bottom = 20.dp)
                            .shadow( //그림자옵션
                                elevation = 10.dp,
                                shape = RoundedCornerShape(10.dp),
                                ambientColor = Color(0xFF5FBEEB),
                                spotColor = Color(0x5525739B),
                                clip = false
                            )
                            .clip(RoundedCornerShape(20.dp))
                            .background(white)
                    ) {
                        items(searchResult) { resultItem ->
                            SearchResultRow(resultItem, getCurrentLocation = getCurrentLocation)
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
                    modifier = modifier
                        .fillMaxSize(),
                    cameraPositionState = cameraPositionState,
                    mapProperties = mapProperties,
                    mapUiSettings = uiSettings,
                    markerPositionsState = markerPositionsState,
                    selectedBuildingState = selectedBuildingState,
                    onFloorTap = {
                        when (searchQuery.value) {
                            is SearchQuery.BuildingCode -> {
                                searchFieldText.value = ""
                                searchQuery.value = null
                                selectedPlace.value = null
                            }
                            else -> {}
                        }
                    },
                    onBuildingTap = { buildingCode ->
                        searchFieldText.value = buildingCode
                        searchQuery.value = SearchQuery.BuildingCode(buildingCode)
                        selectedBuildingState.value = buildingCode
                        selectedPlace.value = null
                    }
                )

                // 상단 검색창, 카테고리
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
                        if (selectedPlace.value != null) {
                            fun removeSelection() {
                                selectedPlace.value = null
                                searchFieldText.value = when (searchQuery.value) {
                                    is SearchQuery.Text -> (searchQuery.value as SearchQuery.Text).text
                                    is SearchQuery.Category -> (searchQuery.value as SearchQuery.Category).category
                                    is SearchQuery.BuildingCode -> (searchQuery.value as SearchQuery.BuildingCode).buildingCode
                                    else -> ""
                                }
                                markerPositionsState.value = searchResult.mapNotNull { it.coordinates }
                                selectedBuildingState.value = null
                            }
                            // 뒤로가기 버튼
                            BackHandler(enabled = true, onBack = ::removeSelection)
                            IconButton(onClick = ::removeSelection) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Default.ArrowBack,
                                    contentDescription = "뒤로"
                                )
                            }
                        } else if (searchQuery.value != null) {
                            fun clearSearchField() {
                                searchFieldText.value = ""
                                searchQuery.value = null
                            }
                            BackHandler(enabled = true, onBack = ::clearSearchField)
                            IconButton(onClick = ::clearSearchField) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Default.ArrowBack,
                                    contentDescription = "뒤로"
                                )
                            }
                        }
                        BasicTextField(
                            value = searchFieldText.value,
                            onValueChange = { newValue -> searchFieldText.value = newValue },
                            interactionSource = interactionSource,
                            modifier = Modifier
                                .weight(1f)
                                .padding(horizontal = if (selectedPlace.value == null && searchQuery.value == null) 20.dp else 0.dp),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                            keyboardActions = KeyboardActions(
                                onSearch = {
                                    keyboardController?.hide()
                                    selectedPlace.value = null
                                    searchQuery.value = SearchQuery.Text(searchFieldText.value.trimEnd())
                                    if (searchQuery.value == null) {
                                        scope.launch {
                                            sheetScaffoldState.bottomSheetState.partialExpand()
                                        }
                                    }
                                }
                            ),
                        ) { innerTextField ->
                            TextFieldDefaults.DecorationBox(
                                value = searchFieldText.value,
                                innerTextField = innerTextField,
                                enabled = true,
                                singleLine = true,
                                visualTransformation = VisualTransformation.None,
                                interactionSource = interactionSource,
                                contentPadding = PaddingValues(0.dp),
                                container = {
                                    Box {
                                        if (searchFieldText.value.isEmpty()) {
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
                                searchQuery.value = SearchQuery.Text(searchFieldText.value.trimEnd())
                                if (searchQuery.value == null) {
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
                    if (searchQuery.value == null) {
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

                // 현위치 버튼
                SmallFloatingActionButton(
                    onClick = {
                        getCurrentLocation { location ->
                            location?.let { location ->
                                scope.launch {
                                    cameraPositionState.animate(
                                        update = CameraUpdateFactory.newLatLng(location),
                                        durationMs = 1000
                                    )
                                }
                            }
                        }
                    },
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(start = 8.dp, bottom = 24.dp),
                    shape = CircleShape,
                    containerColor = white
                ) {
                    Icon(
                        imageVector = Icons.Default.Adjust,
                        contentDescription = "현재 위치 보기"
                    )
                }
            }
        }
    }
}