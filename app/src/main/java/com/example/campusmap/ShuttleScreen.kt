package com.example.campusmap

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

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
            shuttleType = selectedShuttle
        )
    }
}




@Composable
fun ShuttleMainScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "셔틀 안내 화면",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text("• 교내 셔틀 노선 안내")
        Text("• 정류장 위치")
        Text("• 운행 시간 요약") //기본적으로 배경에 사진, 버스모양은 노선따라 움직임
    }
}

@Composable
fun ShuttleTimetableScreen(
    modifier: Modifier = Modifier,
    shuttleType: ShuttleType
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = when (shuttleType) {
                ShuttleType.CAMPUS -> "교내 셔틀 시간표"
                ShuttleType.OUTSIDE -> "교외 셔틀 시간표"
                ShuttleType.MAIN_START -> "본교 출발 셔틀 시간표"
                ShuttleType.MUNJI_START -> "문지 출발 셔틀 시간표"
                ShuttleType.HWAAM_START -> "화암 출발 셔틀 시간표"
                ShuttleType.COMMUTE -> "통근 셔틀 시간표"
            },
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(16.dp))

        when (shuttleType) {
            ShuttleType.CAMPUS -> {
                Text("08:00")
                Text("08:20")
                Text("08:40")
            }
            ShuttleType.OUTSIDE -> {
                Text("08:10")
                Text("08:50")
            }
            ShuttleType.MAIN_START -> {
                Text("07:30")
                Text("08:30")
            }
            ShuttleType.MUNJI_START -> {
                Text("07:40")
                Text("08:40")
            }
            ShuttleType.HWAAM_START -> {
                Text("07:50")
                Text("08:50")
            }
            ShuttleType.COMMUTE -> {
                Text("06:50")
                Text("18:10")
            }
        }
    }
}


