package com.example.campusmap

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Facilities() {
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
    ) { innerPadding ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.padding(innerPadding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(topLevelFacilitiesList.size) { index ->
                PictureGridView(title = topLevelFacilitiesList[index].title, imageURL = topLevelFacilitiesList[index].items[0].imageURL)
            }
        }
    }
}

@Composable
fun FacilitiesCategoryView(data: Array<FacilityItem>) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(data.size) { index ->
            PictureGridView(title = data[index].title, imageURL = data[index].imageURL)
        }
    }
}

@Composable
fun PictureGridView(data: FacilityItem) {
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

@Composable
fun PictureGridView(title: String, imageURL: String) {
    Column {
        AsyncImage(
            model = imageURL,
            contentDescription = title,
            modifier = Modifier.size(200.dp).clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop // 이미지 비율 유지하며 채우기
        )
        Text(
            text = title,
            modifier = Modifier.padding(8.dp)
        )
    }
}

val cafeteria = arrayOf(
    FacilityItem(id = 0, title = "카이마루", imageURL = "https://kaist.ac.kr/kr/img/content/sub05/sub0503_img09.jpg"),
    FacilityItem(id = 1, title = "동맛골 (학생식당)", imageURL = "https://kaist.ac.kr/kr/img/content/sub05/sub0503_img03.jpg"),
    FacilityItem(id = 2, title = "동맛골 (교직원식당)", imageURL = "https://kaist.ac.kr/kr/img/content/sub05/sub0503_img10.jpg"),
    FacilityItem(id = 3, title = "서맛골", imageURL = "https://kaist.ac.kr/kr/img/content/sub05/sub0503_img02.jpg")
)
val cafe = arrayOf(
    FacilityItem(id = 0, title = "그라찌에", imageURL = "https://kaist.ac.kr/kr/img/content/sub05/sub0507_img01.jpg"),
    FacilityItem(id = 1, title = "카페 잇", imageURL = "https://kaist.ac.kr/kr/img/content/sub05/sub0507_img02.jpg"),
    FacilityItem(id = 2, title = "드롭탑", imageURL = "https://kaist.ac.kr/kr/img/content/sub05/sub0507_img03.jpg"),
    FacilityItem(id = 3, title = "카페 오가다", imageURL = "https://kaist.ac.kr/kr/img/content/sub05/sub0507_img06.jpg"),
    FacilityItem(id = 4, title = "카페드림", imageURL = "https://kaist.ac.kr/kr/img/content/sub05/sub0507_img08.jpg"),
    FacilityItem(id = 5, title = "주스킹", imageURL = "https://kaist.ac.kr/kr/img/content/sub05/sub0507_img09.jpg"),
    FacilityItem(id = 6, title = "파스쿠찌", imageURL = "https://kaist.ac.kr/kr/img/content/sub05/sub0507_img10.jpg"),
    FacilityItem(id = 7, title = "탐앤탐즈", imageURL = "https://kaist.ac.kr/kr/img/campus/tom%20and%20toms.jpg"),
    FacilityItem(id = 8, title = "앤제리너스", imageURL = "https://kaist.ac.kr/site/kr/img/campus/00z7p.jpg")
)
val topLevelFacilitiesList = arrayOf(
    FacilityCategory(id = 0, title = "식당", items = cafeteria),
    FacilityCategory(id = 1, title = "카페", items = cafe)
)