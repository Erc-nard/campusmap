package com.example.campusmap

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.campusmap.ui.theme.CampusmapTheme
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.foundation.clickable


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
    var currentDestination by rememberSaveable { mutableStateOf(AppDestinations.MAP) }
    var showShuttleSheet by rememberSaveable { mutableStateOf(false) }

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
                    Map("Android", Modifier.padding(innerPadding))
                AppDestinations.FACILITIES ->
                    Facilities(innerPadding)
                AppDestinations.SHUTTLE -> {}
            }
        }
    }

    // ⭐ BottomSheet는 여기
    if (showShuttleSheet) {
        ModalBottomSheet(
            onDismissRequest = { showShuttleSheet = false },
            sheetState = rememberModalBottomSheetState()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text("Choose your feeling", style = MaterialTheme.typography.titleLarge)

                Spacer(Modifier.height(16.dp))

                Text(
                    "😊 happy",
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showShuttleSheet = false }
                        .padding(12.dp)
                )

                Text(
                    "😢 sad",
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showShuttleSheet = false }
                        .padding(12.dp)
                )
            }
        }
    }
}


enum class AppDestinations(
    val label: String,
    val icon: ImageVector,
) {
    MAP("지도", Icons.Default.LocationOn),
    FACILITIES("시설", Icons.Default.Place),
    SHUTTLE("셔틀", Icons.Default.ShoppingCart),
}


@Composable
fun Map(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Facilities(paddingValues: PaddingValues) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "시설") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ),
                navigationIcon = {
                    IconButton(onClick = {}) { Text("=") }
                },
                actions = {
                    IconButton(onClick = {}) {
                        Icon(
                            imageVector = Icons.Default.Search, // 내장된 검색 아이콘 사용
                            contentDescription = "검색"
                        )
                    }
                }
            )
        },
        modifier = Modifier.padding(paddingValues)
    ) { innerPadding ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.padding(innerPadding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(topLevelFacilitiesList.size) { index ->
                PictureGridView(data = topLevelFacilitiesList[index])
            }
        }
    }
}

@Composable
fun PictureGridView(data: PhotoItemData) {
    Column {
        AsyncImage(
            model = data.imageURL,
            contentDescription = data.title,
            modifier = Modifier.size(200.dp).clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop // 이미지 비율 유지하며 채우기
        )
        Text(
            text = data.title,
            modifier = Modifier.padding(8.dp)
        )
    }
}

data class PhotoItemData(val id: Int, val title: String, val imageURL: String)
val topLevelFacilitiesList = List<PhotoItemData>(16) { index ->
    PhotoItemData(id = index, title = "Title", imageURL = "https://kaist.ac.kr/kr/img/content/sub05/sub0503_img09.jpg")
}

@Composable
fun Shuttle(name: String, modifier: Modifier = Modifier) {
    Text(
        text = name,
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    CampusmapTheme {
        Map("Android")
    }
}