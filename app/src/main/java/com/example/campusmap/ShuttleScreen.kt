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
import java.time.Duration
import java.time.format.DateTimeFormatter



enum class ShuttleType {
    CAMPUS,        // 교내
    OUTSIDE     // 통근
}

data class Station(
    val name: String,
    val xRatio: Float,
    val yRatio: Float,
    val time: LocalTime
)



val kaiMaruTimes = listOf( //카이마루에서부터~
    LocalTime.of(8, 40),
    LocalTime.of(8, 55),
    LocalTime.of(9, 10),
    LocalTime.of(9, 25),
    LocalTime.of(9, 40),
    LocalTime.of(9, 55),
    LocalTime.of(10, 10),
    LocalTime.of(10, 25),
    LocalTime.of(10, 40),
    LocalTime.of(10, 55),
    LocalTime.of(11, 10),
    LocalTime.of(11, 25),
    LocalTime.of(11, 40),

    // 점심 공백 있음 (11:40 → 12:55)
    LocalTime.of(12, 55),
    LocalTime.of(13, 10),
    LocalTime.of(13, 25),
    LocalTime.of(13, 40),
    LocalTime.of(13, 55),
    LocalTime.of(14, 10),
    LocalTime.of(14, 25),
    LocalTime.of(14, 40),
    LocalTime.of(14, 55),
    LocalTime.of(15, 10),
    LocalTime.of(15, 25),
    LocalTime.of(15, 40),
    LocalTime.of(15, 55),
    LocalTime.of(16, 10),
    LocalTime.of(16, 25),
    LocalTime.of(16, 40),
    LocalTime.of(16, 55)
)


val campusStations = kaiMaruTimes.map { time ->
    Station(
        name = "카이마루",
        xRatio = 60f / 360f,
        yRatio = 420f / 480f,
        time = time
    )
}

val kaiMaruWeekdayTimes = listOf(
    "08:40", "08:55", "09:10", "09:25", "09:40", "09:55",
    "10:10", "10:25", "10:40", "10:55",
    "11:10", "11:25", "11:40",
    "12:55",
    "13:10", "13:25", "13:40", "13:55",
    "14:10", "14:25", "14:40", "14:55",
    "15:10", "15:25", "15:40", "15:55",
    "16:10", "16:25", "16:40", "16:55"
)
//시간표

/*val commuterBus1Stations = listOf(
    Station("대전복합터미널", 40.dp, 480.dp, LocalTime.of(7, 42)),
    Station("홍도동", 80.dp, 440.dp, LocalTime.of(7, 44)),
    Station("목동", 120.dp, 400.dp, LocalTime.of(7, 50)),
    Station("태평동 오거리", 160.dp, 360.dp, LocalTime.of(8, 0)),
    Station("가장동 래미안 아파트", 200.dp, 320.dp, LocalTime.of(8, 5)),
    Station("갈마동(성심요양병원)", 240.dp, 280.dp, LocalTime.of(8, 15)),
    Station("갈마동(바다수산)", 280.dp, 240.dp, LocalTime.of(8, 20)),
    Station("유성온천역", 320.dp, 200.dp, LocalTime.of(8, 28)),
    Station("KAIST", 360.dp, 160.dp, LocalTime.of(8, 35))
)

val commuterBus2Stations = listOf(
    Station("대동", 40.dp, 480.dp, LocalTime.of(7, 40)),
    Station("문창동", 80.dp, 440.dp, LocalTime.of(7, 45)),
    Station("부사동", 120.dp, 400.dp, LocalTime.of(7, 48)),
    Station("대흥동", 160.dp, 360.dp, LocalTime.of(7, 52)),
    Station("둔산동", 200.dp, 320.dp, LocalTime.of(8, 8)),
    Station("정부청사역", 240.dp, 280.dp, LocalTime.of(8, 15)),
    Station("월평역", 280.dp, 240.dp, LocalTime.of(8, 25)),
    Station("궁동", 320.dp, 200.dp, LocalTime.of(8, 27)),
    Station("KAIST", 360.dp, 160.dp, LocalTime.of(8, 40))
)*/

val commuterBus1Stations = listOf(
    Station("대전복합터미널", 27f / 360f,  30f / 480f, LocalTime.now().plusSeconds(0)),
    Station("홍도동",       27f / 360f,  80f / 480f, LocalTime.now().plusSeconds(20)),
    Station("목동",         27f / 360f, 130f / 480f, LocalTime.now().plusSeconds(40)),
    Station("태평동 오거리", 27f / 360f, 180f / 480f, LocalTime.now().plusSeconds(60)),
    Station("가장동 래미안", 27f / 360f, 230f / 480f, LocalTime.now().plusSeconds(90)),
    Station("갈마동(성심)",  27f / 360f, 280f / 480f, LocalTime.now().plusSeconds(120)),
    Station("갈마동(바다)",  27f / 360f, 330f / 480f, LocalTime.now().plusSeconds(150)),
    Station("유성온천역",   27f / 360f, 380f / 480f, LocalTime.now().plusSeconds(180)),
    Station("KAIST",        27f / 360f, 430f / 480f, LocalTime.now().plusSeconds(210))
)


val commuterBus2Stations = listOf(
    Station("대동",      190f / 360f,  30f / 480f, LocalTime.now().plusSeconds(10)),
    Station("문창동",    190f / 360f,  75f / 480f, LocalTime.now().plusSeconds(30)),
    Station("부사동",    190f / 360f, 120f / 480f, LocalTime.now().plusSeconds(50)),
    Station("대흥동",    190f / 360f, 165f / 480f, LocalTime.now().plusSeconds(60)),
    Station("대흥동",    190f / 360f, 210f / 480f, LocalTime.now().plusSeconds(70)),
    Station("둔산동",    190f / 360f, 255f / 480f, LocalTime.now().plusSeconds(100)),
    Station("정부청사역",190f / 360f, 300f / 480f, LocalTime.now().plusSeconds(130)),
    Station("월평역",    190f / 360f, 345f / 480f, LocalTime.now().plusSeconds(160)),
    Station("궁동",      190f / 360f, 390f / 480f, LocalTime.now().plusSeconds(190)),
    Station("KAIST",     190f / 360f, 435f / 480f, LocalTime.now().plusSeconds(220))
)
//테스트용






@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShuttleScreenRoot(
    startShuttle: ShuttleType,
    onClose: () -> Unit
) {
    var selectedShuttle by rememberSaveable { mutableStateOf(startShuttle) }

    // BottomSheet 상태
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = false
    )
    var showTimetable by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = when (selectedShuttle) {
                            ShuttleType.CAMPUS -> "교내 셔틀"
                            ShuttleType.OUTSIDE -> "통근 셔틀"
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
        Box(modifier = Modifier.padding(innerPadding)) {
            // 지도 화면
            ShuttleMap(
                shuttleType = selectedShuttle,
                stationsList = when (selectedShuttle) {
                    ShuttleType.CAMPUS -> listOf(campusStations)
                    ShuttleType.OUTSIDE -> listOf(commuterBus1Stations, commuterBus2Stations)
                }
            )

            // 시간표 보기 버튼
            Button(
                onClick = { showTimetable = true },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp)
            ) {
                Text("시간표 보기")
            }

            // BottomSheet
            if (showTimetable) {
                ShuttleTimetableBottomSheet(
                    shuttleType = selectedShuttle,
                    onDismiss = { showTimetable = false }
                )
            }
        }
    }
}


@Composable
fun ShuttleMap(
    shuttleType: ShuttleType,
    stationsList: List<List<Station>>
) {
    BoxWithConstraints(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.TopStart
    ) {
        // 기준 캔버스 비율 (좌표 만들 때 사용한 값)
        val mapWidth = maxWidth
        val mapHeight = mapWidth * (480f / 360f)

        Box(
            modifier = Modifier
                .width(mapWidth)
                .height(mapHeight)
        ) {
            // 🗺 배경 지도 (좌표 기준)
            Image(
                painter = painterResource(id = shuttleBackgroundImage(shuttleType)),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Fit
            )

            // 🚍 여러 노선의 버스
            stationsList.forEach { stations ->
                BusMovingLayer(
                    stations = stations,
                    mapWidth = mapWidth,
                    mapHeight = mapHeight
                )
            }
        }
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
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 300.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 56.dp) // 버튼 공간 확보
            ) {
                // 드래그 바
                Box(
                    modifier = Modifier
                        .padding(vertical = 8.dp)
                        .size(width = 40.dp, height = 4.dp)
                        .align(Alignment.CenterHorizontally)
                        .background(
                            color = MaterialTheme.colorScheme.outlineVariant,
                            shape = RoundedCornerShape(2.dp)
                        )
                )

                Text(
                    text = when (shuttleType) {
                        ShuttleType.CAMPUS -> "교내 셔틀 시간표"
                        ShuttleType.OUTSIDE -> "통근 셔틀 시간표"
                    },
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(16.dp)
                )

                when (shuttleType) {
                    ShuttleType.CAMPUS -> KaiMaruTimetableContent()
                    ShuttleType.OUTSIDE -> CommuterTimetableContent()
                }
            }

            // 버튼: 우측 하단 고정
            IconButton(
                onClick = onDismiss,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp)
                    .size(48.dp)
            ) {
                Icon(Icons.Default.ArrowBack, contentDescription = "닫기")
            }
        }
    }
}



@Composable
fun rememberBusState(
    stations: List<Station>
): Pair<Pair<Float, Float>?, Boolean> {

    val now by produceState(initialValue = LocalTime.now()) {
        while (true) {
            value = LocalTime.now()
            delay(1000)
        }
    }

    if (now.isAfter(stations.last().time)) {
        return null to true
    }
    for (i in 0 until stations.size - 1) {
        val start = stations[i]
        val end = stations[i + 1]

        if (now in start.time..end.time) {
            val total =
                java.time.Duration.between(start.time, end.time).toMillis()
            val passed =
                java.time.Duration.between(start.time, now).toMillis()

            val progress = passed.toFloat() / total

            val xRatio = lerp(start.xRatio, end.xRatio, progress)
            val yRatio = lerp(start.yRatio, end.yRatio, progress)

            return (xRatio to yRatio) to false
        }
    }

    return (stations.first().xRatio to stations.first().yRatio) to false
}

@Composable
fun BusMovingLayer(
    stations: List<Station>,
    mapWidth: Dp,
    mapHeight: Dp
) {
    val (busRatio, finished) = rememberBusState(stations)

    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        if (busRatio != null) {
            val (xRatio, yRatio) = busRatio

            Image(
                painter = painterResource(R.drawable.bus),
                contentDescription = null,
                modifier = Modifier
                    .offset(mapWidth * xRatio, mapHeight * yRatio)
                    .size(40.dp)
            )
        }

        if (finished) {
            Text(
                text = "버스 운영 종료",
                modifier = Modifier.align(Alignment.Center),
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}



@Composable
fun TimetableImage(shuttleType: ShuttleType) {
    val imageRes = when (shuttleType) {
        ShuttleType.CAMPUS -> R.drawable.timetable_campus
        ShuttleType.OUTSIDE -> R.drawable.timetable_outside
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
    }
}
@Composable
fun KaiMaruTimetableContent() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text(
            text = "카이마루 (KAIST 학생식당) 출발",
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "배차간격 15분 · 평일 전용",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "※ 현재 보시고 계시는 시간표는 평일 전용입니다.\n탑승 날짜가 평일인지 다시 한 번 확인해주세요.",
            style = MaterialTheme.typography.bodySmall
        )
        Spacer(modifier = Modifier.height(16.dp))

        // 헤더
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("도착 예정", style = MaterialTheme.typography.labelLarge)
        }

        Divider(modifier = Modifier.padding(vertical = 8.dp))

        // 시간표 목록
        kaiMaruWeekdayTimes.forEach { time ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("${time.replace(":", "시 ")}분")
            }
        }
    }
}

@Composable
fun CommuterTimetableContent() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text("통근 버스 안내", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            "교직원의 출퇴근을 위해 마련된 통근버스 특성 상, 학생 및 외부 방문자는 탑승이 어렵습니다.\n" +
                    "도로 교통사정에 따라 각 정류장에 약 5분의 시간차가 있을 수 있습니다.\n" +
                    "공식 홈페이지 시간표도 참고해 주세요!",
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(modifier = Modifier.height(16.dp))

        // 1호차
        Text("1호차 (74오 1718)", style = MaterialTheme.typography.titleSmall)
        Spacer(modifier = Modifier.height(4.dp))
        commuterBus1Stations.forEach { station ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(station.time.format(DateTimeFormatter.ofPattern("HH시 mm분")))
                Text(station.name)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 2호차
        Text("2호차 (74오 1717)", style = MaterialTheme.typography.titleSmall)
        Spacer(modifier = Modifier.height(4.dp))
        commuterBus2Stations.forEach { station ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(station.time.format(DateTimeFormatter.ofPattern("HH시 mm분")))
                Text(station.name)
            }
        }
    }
}





