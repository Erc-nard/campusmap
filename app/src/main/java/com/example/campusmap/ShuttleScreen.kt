package com.example.campusmap

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.Image
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.boundsInParent
import androidx.compose.ui.util.lerp
import kotlinx.coroutines.delay
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity



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


val campusStationPositions = listOf(
    "카이마루", "스컴", "창의관", "의과학센터", "파팔라도",
    "나노종합", "정문", "신소재공학동", "희망/다솜관", "나눔관", "카이마루"
)
val now = LocalTime.now()


val campusStations = campusStationPositions.mapIndexed { index, name ->
    Station(
        name = name,
        xRatio = 153f/360f,
        yRatio = when(index) { //50간격
            0 -> 0f / 480f
            1 -> 110f / 480f
            2 -> 200f / 480f
            3 -> 290f / 480f
            4 -> 380f / 480f
            5 -> 470f / 480f
            6 -> 560f / 480f
            7 -> 650f / 480f
            8 -> 740f / 480f
            else -> 830f / 480f
        },
    time = kaiMaruTimes.getOrElse(index) { LocalTime.of(8,40) } // 대충 첫 번째 시간으로 초기화
    )
}


val kaiMaruWeekdayTimes = listOf(
    "08:40", "08:55", "09:15", "09:35", "09:55",
    "10:10", "10:25", "10:45",
    "11:05", "11:25", "11:45",
    "12:40","12:55",
    "13:15", "13:35", "13:55",
    "14:10", "14:25", "14:45",
    "15:05", "15:25", "15:40", "15:55",
    "16:15", "16:35", "16:55"
)


val commuterBus1Stations = listOf(
    Station("대전복합터미널", 38f / 360f,  30f / 480f, LocalTime.of(7, 42)),
    Station("홍도동",       38f / 360f,  80f / 480f, LocalTime.of(7, 44)),
    Station("목동",         38f / 360f, 130f / 480f, LocalTime.of(7, 50)),
    Station("태평동 오거리", 38f / 360f, 180f / 480f, LocalTime.of(8, 0)),
    Station("가장동 래미안", 38f / 360f, 230f / 480f, LocalTime.of(8, 5)),
    Station("갈마동(성심)",  38f / 360f, 280f / 480f, LocalTime.of(8, 15)),
    Station("갈마동(바다)",  38f / 360f, 330f / 480f, LocalTime.of(8, 20)),
    Station("유성온천역",   38f / 360f, 380f / 480f, LocalTime.of(8, 28)),
    Station("KAIST",        38f / 360f, 430f / 480f, LocalTime.of(8, 35))
)


val commuterBus2Stations = listOf(
    Station("대동",      190f / 360f,  30f / 480f, LocalTime.of(7, 40)),
    Station("문창동",    190f / 360f,  75f / 480f, LocalTime.of(7, 45)),
    Station("부사동",    190f / 360f, 120f / 480f, LocalTime.of(7, 48)),
    Station("대흥동",    190f / 360f, 165f / 480f, LocalTime.of(7, 52)),
    Station("중촌동",    190f / 360f, 210f / 480f, LocalTime.of(7, 57)),
    Station("둔산동",    190f / 360f, 255f / 480f, LocalTime.of(8, 7)),
    Station("정부청사역",190f / 360f, 300f / 480f, LocalTime.of(8, 15)),
    Station("월평역",    190f / 360f, 345f / 480f, LocalTime.of(8, 25)),
    Station("궁동",      190f / 360f, 390f / 480f, LocalTime.of(8, 27)),
    Station("KAIST",     190f / 360f, 435f / 480f, LocalTime.of(8, 40))
)
//테스트용






@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShuttleScreenFixed(
    startShuttle: ShuttleType,
    onClose: () -> Unit
) {
    var selectedShuttle by rememberSaveable { mutableStateOf(startShuttle) }
    var showTimetable by remember { mutableStateOf(false) }

    Scaffold(
        // 1. TopAppBar: 뒤로가기 버튼 명시적 배치
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (selectedShuttle == ShuttleType.CAMPUS) "교내 셔틀" else "통근 셔틀",
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onClose) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "뒤로가기"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        // Scaffold의 innerPadding을 적용하여 상단 바 아래부터 시작하게 함
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 2. 노선도 영역: 세로를 꽉 채우는 박스
            Box(
                modifier = Modifier
                    .weight(1f) // 남은 세로 공간 다 차지
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                // 실제 이미지와 버스가 위치할 영역 (비율 유지)
                Box(
                    modifier = Modifier
                        .fillMaxHeight() // 높이 기준 채우기
                        .aspectRatio(if (selectedShuttle == ShuttleType.CAMPUS) 360f / 830f else 360f / 480f)
                ) {
                    // 배경 이미지
                    Image(
                        painter = painterResource(id = shuttleBackgroundImage(selectedShuttle)),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Fit
                    )

                    // 버스 레이어 (이미지 영역과 1:1 매칭)
                    val stationsList = when (selectedShuttle) {
                        ShuttleType.CAMPUS -> listOf(campusStations)
                        ShuttleType.OUTSIDE -> listOf(commuterBus1Stations, commuterBus2Stations)
                    }

                    stationsList.forEach { stations ->
                        BusMovingLayer(
                            stations = stations,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            }

            // 3. 하단 버튼
            Button(
                onClick = { showTimetable = true },
                modifier = Modifier
                    .padding(vertical = 24.dp)
                    .width(180.dp)
            ) {
                Text("시간표 보기")
            }
        }

        // 4. 바텀 시트
        if (showTimetable) {
            ShuttleTimetableBottomSheet(
                shuttleType = selectedShuttle,
                onDismiss = { showTimetable = false }
            )
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

    // ✅ 현재 시간이 포함된 운행 구간이 있는지 검사
    for (i in 0 until stations.size - 1) {
        val start = stations[i]
        val end = stations[i + 1]

        if (!now.isBefore(start.time) && now.isBefore(end.time)) {
            val total =
                java.time.Duration.between(start.time, end.time).toMillis()
            val passed =
                java.time.Duration.between(start.time, now).toMillis()

            val progress = passed.toFloat() / total

            val xRatio = lerp(start.xRatio, end.xRatio, progress)
            val yRatio = lerp(start.yRatio, end.yRatio, progress)

            return (xRatio to yRatio) to false // 운행 중
        }
    }

    // ✅ 어떤 구간에도 속하지 않으면 운행 종료
    return null to true
}


@Composable
fun BusMovingLayer(
    stations: List<Station>,
    modifier: Modifier = Modifier
) {
    val (busRatio, finished) = rememberBusState(stations)
    var size by remember { mutableStateOf(IntSize.Zero) }
    val density = LocalDensity.current

    Box(
        modifier = modifier.onGloballyPositioned { size = it.size }
    ) {
        if (!finished && busRatio != null && size != IntSize.Zero) {
            val (xRatio, yRatio) = busRatio

            val xPx = size.width * xRatio
            val yPx = size.height * yRatio

            Image(
                painter = painterResource(R.drawable.bus),
                contentDescription = "Bus",
                modifier = Modifier
                    .size(24.dp)
                    .offset(
                        x = with(density) { xPx.toDp() } - 12.dp,
                        y = with(density) { yPx.toDp() } - 12.dp
                    )
            )
        }

        // 🚌 운행 종료 시 뜨는 흰색 네모 알림
        if (finished) {
            Surface(
                color = androidx.compose.ui.graphics.Color.White.copy(alpha = 0.95f),
                shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp),
                shadowElevation = 8.dp,
                modifier = Modifier.align(Alignment.Center)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "🚌 현재 운행 정보가 없습니다",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                            color = androidx.compose.ui.graphics.Color.Black
                        )
                    )
                }
            }
        }
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
            text = "카이마루 (KAIST 학생식당) 출발 시간",
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "배차간격 15분 · 평일 전용",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(12.dp))

        // 헤더

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
        // 1호차
        Text("1호차", style = MaterialTheme.typography.titleSmall)
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
        Text("2호차", style = MaterialTheme.typography.titleSmall)
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





