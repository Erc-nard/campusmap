package com.example.campusmap

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.layout.ContentScale


enum class ShuttleType {
    CAMPUS,        // 교내
    OUTSIDE,       // 교외
    MAIN_START,    // 본교 출발
    MUNJI_START,   // 문지 출발
    HWAAM_START,   // 화암 출발
    COMMUTE        // 통근
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShuttleScreenRoot(
    startShuttle: ShuttleType,
    onClose: () -> Unit
) {
    //var currentScreen by rememberSaveable { mutableStateOf(ShuttleScreen.TIMETABLE) }
    var selectedShuttle by rememberSaveable { mutableStateOf(startShuttle) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("셔틀 시간표") },
                navigationIcon = {
                    IconButton(onClick = onClose) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "뒤로가기")
                    }
                }
            )
        }
    ) { innerPadding ->
        ShuttleTimetableScreen(
            modifier = Modifier.padding(innerPadding),
            shuttleType = selectedShuttle //셔틀 타입 받아옴
        )
    }
}


@Composable
fun ShuttleTimetableScreen(
    modifier: Modifier = Modifier,
    shuttleType: ShuttleType
) {
    var showSheet by remember { mutableStateOf(false) }

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        // 🔹 배경 이미지
        Image(
            painter = painterResource(id = shuttleBackgroundImage(shuttleType)),
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.Center)
                .size(260.dp),
            alpha = 0.15f
        )


        // 🔹 메인 콘텐츠
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Text(
                    text = when (shuttleType) {
                        ShuttleType.CAMPUS -> "교내 셔틀"
                        ShuttleType.OUTSIDE -> "교외 셔틀"
                        ShuttleType.MAIN_START -> "본교 출발"
                        ShuttleType.MUNJI_START -> "문지 출발"
                        ShuttleType.HWAAM_START -> "화암 출발"
                        ShuttleType.COMMUTE -> "통근 셔틀"
                    },
                    style = MaterialTheme.typography.titleLarge
                )
            }

            // 🔹 우측 하단 버튼
            Button(
                onClick = { showSheet = true },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp)
                    .width(120.dp)
            ) {
                Text("시간표 보기")
            }
        }
    }

    if (showSheet) {
        ShuttleTimetableBottomSheet(
            shuttleType = shuttleType,
            onDismiss = { showSheet = false }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShuttleTimetableBottomSheet(
    shuttleType: ShuttleType,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = false
    )

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 300.dp)
        ) {
            // 🔹 상단 드래그 바
            Box(
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .size(width = 40.dp, height = 4.dp)
                    .align(Alignment.CenterHorizontally)
                    .background(
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
                        shape = RoundedCornerShape(2.dp)
                    )
            )

            Text(
                text = "셔틀 시간표",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(16.dp)
            )

            // 🔹 시간표 이미지 (스크롤 가능)
            TimetableImage(shuttleType)
        }
    }
}

@Composable
fun TimetableImage(shuttleType: ShuttleType) {
    val imageRes = when (shuttleType) {
        ShuttleType.CAMPUS -> R.drawable.timetable_campus
        ShuttleType.OUTSIDE -> R.drawable.timetable_outside
        ShuttleType.MAIN_START -> R.drawable.timetable_main
        ShuttleType.MUNJI_START -> R.drawable.timetable_munji
        ShuttleType.HWAAM_START -> R.drawable.timetable_hwaam
        ShuttleType.COMMUTE -> R.drawable.timetable_commute
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Image(
            painter = painterResource(id = imageRes),
            contentDescription = "셔틀 시간표",
            modifier = Modifier.fillMaxWidth(),
            contentScale = ContentScale.Fit
        )
    }
}

@Composable
fun shuttleBackgroundImage(shuttleType: ShuttleType): Int {
    return when (shuttleType) {
        ShuttleType.CAMPUS -> R.drawable.bg_campus_circle
        ShuttleType.OUTSIDE -> R.drawable.bg_outside_circle
        ShuttleType.MAIN_START -> R.drawable.bg_main_circle
        ShuttleType.MUNJI_START -> R.drawable.bg_munji_circle
        ShuttleType.HWAAM_START -> R.drawable.bg_hwaam_circle
        ShuttleType.COMMUTE -> R.drawable.bg_commute_circle
    }
}


