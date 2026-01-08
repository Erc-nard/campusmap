package com.example.campusmap

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * 셔틀 탭 내부에서만 사용하는 화면 상태
 */
private enum class ShuttleScreen {
    MAIN,
    TIMETABLE
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShuttleScreenRoot() {
    var currentScreen by rememberSaveable { mutableStateOf(ShuttleScreen.MAIN) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text( //상단 bar로 처리
                        text = if (currentScreen == ShuttleScreen.MAIN)
                            "셔틀"
                        else
                            "셔틀 시간표"
                    )
                },
                navigationIcon = {
                    if (currentScreen == ShuttleScreen.TIMETABLE) {
                        IconButton(onClick = {
                            currentScreen = ShuttleScreen.MAIN
                        }) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "뒤로가기"
                            )
                        }
                    }
                },

                actions = {
                    if (currentScreen == ShuttleScreen.MAIN) {
                        IconButton(onClick = {
                            currentScreen = ShuttleScreen.TIMETABLE
                        }) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack, //다른 아이콘으로 수정
                                contentDescription = "시간표"
                            )
                        }
                    }
                }


            )
        }
    ) { innerPadding ->
        when (currentScreen) {

            ShuttleScreen.MAIN -> {
                ShuttleMainScreen(
                    modifier = Modifier.padding(innerPadding)
                )
            }
            ShuttleScreen.TIMETABLE -> {
                ShuttleTimetableScreen(
                    modifier = Modifier.padding(innerPadding)
                )
            }
        }
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
fun ShuttleTimetableScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "셔틀 시간표", //이 탭으로 들어올때 어떤 버스선택했는지 확인하고 해당버스 내용 보여줌
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text("08:00")
        Text("08:30")
        Text("09:00")
        Text("09:30")
    }
}

