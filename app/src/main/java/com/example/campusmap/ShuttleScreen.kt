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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.util.lerp
import kotlinx.coroutines.delay
import java.time.LocalTime


enum class ShuttleType {
    CAMPUS,        // 교내
    OUTSIDE,       // 교외
    MUNJI_START,   // 문지 출발
    COMMUTE        // 통근
}

data class Station(
    val name: String,
    val x: Dp,
    val y: Dp,
    val arrivalTime: LocalTime
)

val testBaseTime = LocalTime.now()

val campusStations = listOf(
    Station("A", 60.dp, 420.dp, testBaseTime.plusSeconds(0)),
    Station("B", 180.dp, 320.dp, testBaseTime.plusSeconds(10)),
    Station("C", 300.dp, 240.dp, testBaseTime.plusSeconds(20))
)

val outsideStations = listOf(
    Station("외부1", 40.dp, 480.dp, testBaseTime.plusSeconds(0)),
    Station("외부2", 120.dp, 360.dp, testBaseTime.plusSeconds(8)),
    Station("외부3", 260.dp, 180.dp, testBaseTime.plusSeconds(20))
)

val munjiStations = listOf(
    Station("문지", 320.dp, 500.dp, testBaseTime.plusSeconds(0)),
    Station("캠퍼스", 160.dp, 280.dp, testBaseTime.plusSeconds(20))
)


val commuteStations1 = listOf(
    Station("통근1", 20.dp, 520.dp, testBaseTime.plusSeconds(0)),
    Station("통근2", 140.dp, 420.dp, testBaseTime.plusSeconds(5)),
    Station("통근3", 280.dp, 300.dp, testBaseTime.plusSeconds(10)),
    Station("통근4", 340.dp, 200.dp, testBaseTime.plusSeconds(15))
)


val commuteStations2 = listOf(
    Station("통근1", 20.dp, 520.dp, testBaseTime.plusSeconds(0)),
    Station("통근2", 140.dp, 420.dp, testBaseTime.plusSeconds(7)),
    Station("통근3", 280.dp, 300.dp, testBaseTime.plusSeconds(14)),
    Station("통근4", 340.dp, 200.dp, testBaseTime.plusSeconds(21))
)

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
                title = {
                    Text(
                        text = when (startShuttle) {
                            ShuttleType.CAMPUS -> "교내 셔틀"
                            ShuttleType.OUTSIDE -> "교외 셔틀"
                            ShuttleType.MUNJI_START -> "캠퍼스 왕복"
                            ShuttleType.COMMUTE -> "통근 셔틀"
                        },
                        style = MaterialTheme.typography.titleLarge
                    )
                    },
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
    val stations = when (shuttleType) {
        ShuttleType.CAMPUS -> campusStations
        ShuttleType.OUTSIDE -> outsideStations
        ShuttleType.MUNJI_START -> munjiStations
        ShuttleType.COMMUTE -> {
            val now = LocalTime.now()
            if (now.isBefore(LocalTime.of(18, 0))) {
                commuteStations1
            } else {
                commuteStations2
            }
        }
    }

    var showSheet by remember { mutableStateOf(false) }

    Box(modifier = modifier.fillMaxSize()) {

        Image(
            painter = painterResource(id = shuttleBackgroundImage(shuttleType)),
            contentDescription = null,
            modifier = Modifier.fillMaxSize()
        )

        // ✅ 탭마다 다른 애니메이션
        BusMovingLayer(stations)

        Button(
            onClick = { showSheet = true },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            Text("시간표 보기")
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
                        color = MaterialTheme.colorScheme.background,
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
fun rememberBusState(stations: List<Station>): Pair<Offset?, Boolean> {
    val now by produceState(initialValue = LocalTime.now()) {
        while (true) {
            value = LocalTime.now()
            delay(1000)
        }
    }

    // 운행 종료
    if (now.isAfter(stations.last().arrivalTime)) {
        return null to true
    }

    // 이동 중
    for (i in 0 until stations.size - 1) {
        val start = stations[i]
        val end = stations[i + 1]

        if (now.isAfter(start.arrivalTime) && now.isBefore(end.arrivalTime)) {
            val total =
                java.time.Duration.between(start.arrivalTime, end.arrivalTime).toMillis()
            val passed =
                java.time.Duration.between(start.arrivalTime, now).toMillis()

            val progress = passed.toFloat() / total

            val x = lerp(start.x.value, end.x.value, progress)
            val y = lerp(start.y.value, end.y.value, progress)

            return Offset(x, y) to false
        }
    }

    // 아직 출발 전
    val first = stations.first()
    return Offset(first.x.value, first.y.value) to false
}

@Composable
fun BusMovingLayer(
    stations: List<Station>
) {
    val (busOffset, finished) = rememberBusState(stations)

    Box(modifier = Modifier.fillMaxSize()) {

        // 🚍 버스
        if (busOffset != null) {
            Image(
                painter = painterResource(R.drawable.bus),
                contentDescription = "버스",
                modifier = Modifier
                    .offset(busOffset.x.dp, busOffset.y.dp)
                    .size(40.dp)
            )
        }

        // ⛔ 운행 종료
        if (finished) {
            Text(
                "버스 운영 종료",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}

@Composable
fun TimetableImage(shuttleType: ShuttleType) {
    val imageRes = when (shuttleType) {
        ShuttleType.CAMPUS -> R.drawable.timetable_campus
        ShuttleType.OUTSIDE -> R.drawable.timetable_outside
        ShuttleType.MUNJI_START -> R.drawable.timetable_munji
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
        ShuttleType.MUNJI_START -> R.drawable.bg_munji_circle
        ShuttleType.COMMUTE -> R.drawable.bg_commute_circle
    }
}



