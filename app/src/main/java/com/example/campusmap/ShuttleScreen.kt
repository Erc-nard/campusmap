package com.example.campusmap

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import kotlinx.coroutines.delay
import java.time.Duration
import java.time.LocalTime
import java.time.format.DateTimeFormatter

// ------------------------------------------------------------------------
// [데이터 구역] 요청하신 데이터로 교체 완료
// ------------------------------------------------------------------------

enum class ShuttleType { CAMPUS, OUTSIDE }

data class StationNode(
    val name: String,
    val xRatio: Float,
    val yRatio: Float,
    val time: LocalTime? = null // 통근용 절대 시간
)

// 1. 교내 셔틀 출발 시간표 (String -> LocalTime 변환)
val kaiMaruWeekdayTimes = listOf(
    "08:40", "08:55", "09:15", "09:35", "09:55",
    "10:10", "10:25", "10:45",
    "11:05", "11:25", "11:45",
    "12:40", "12:55",
    "13:15", "13:35", "13:55",
    "14:10", "14:25", "14:45",
    "15:05", "15:25", "15:40", "15:55",
    "16:15", "16:35", "16:55"
).map { LocalTime.parse(it, DateTimeFormatter.ofPattern("HH:mm")) }

// 2. 교내 셔틀 정류장 오프셋 및 좌표
// 좌표는 화면상의 대략적인 위치를 잡았습니다 (교내 노선은 보통 가운데나 특정 라인을 따르므로)
val campusStationOffsets = listOf( //0.12f 출발
    Triple("카이마루", 0, 0.12f), //시간,위치 (x축은 고정)
    Triple("스컴", 1, 0.20f),
    Triple("창의관", 2, 0.28f),
    Triple("의과학센터", 3, 0.36f),
    Triple("클리닉", 4, 0.44f),
    Triple("나노종합", 5, 0.52f),
    Triple("정문", 6, 0.60f),
    Triple("신소재공학동", 7, 0.68f),
    Triple("kisti", 8, 0.74f),
    Triple("희망다솜", 9, 0.82f),
    Triple("외국인아파트", 10, 0.90f)
)

// 3. 통근 1호차 데이터
val commuterBus1Stations = listOf(
    StationNode("대전복합터미널", 58f / 360f,  37f / 480f, LocalTime.of(7, 42)),
    StationNode("홍도동",       58f / 360f,  90f / 480f, LocalTime.of(7, 44)),
    StationNode("목동",         58f / 360f, 145f / 480f, LocalTime.of(7, 50)),
    StationNode("태평동 오거리", 58f / 360f, 185f / 480f, LocalTime.of(8, 0)),
    StationNode("가장동 래미안", 58f / 360f, 230f / 480f, LocalTime.of(8, 5)),
    StationNode("갈마동(성심)",  58f / 360f, 275f / 480f, LocalTime.of(8, 15)),
    StationNode("갈마동(바다)",  58f / 360f, 313f / 480f, LocalTime.of(8, 20)),
    StationNode("유성온천역",   58f / 360f, 370f / 480f, LocalTime.of(8, 28)),
    StationNode("KAIST",        58f / 360f, 405f / 480f, LocalTime.of(8, 35))
)

// 4. 통근 2호차 데이터
val commuterBus2Stations = listOf(
    StationNode("대동",      206f / 360f,  35f / 480f, LocalTime.of(7, 40)),
    StationNode("문창동",    206f / 360f,  82f / 480f, LocalTime.of(7, 45)),
    StationNode("부사동",    206f / 360f, 127f / 480f, LocalTime.of(7, 48)),
    StationNode("대흥동",    206f / 360f, 172f / 480f, LocalTime.of(7, 52)),
    StationNode("중촌동",    206f / 360f, 218f / 480f, LocalTime.of(7, 57)),
    StationNode("둔산동",    206f / 360f, 264f / 480f, LocalTime.of(8, 7)),
    StationNode("정부청사역",206f / 360f, 310f / 480f, LocalTime.of(8, 15)),
    StationNode("월평역",    206f / 360f, 355f / 480f, LocalTime.of(8, 25)),
    StationNode("궁동",      206f / 360f, 400f / 480f, LocalTime.of(8, 27)),
    StationNode("KAIST",     206f / 360f, 442f / 480f, LocalTime.of(8, 40))
)

// 막차 시간 설정
val campusEndTime = LocalTime.of(17, 30) // 교내 마지막 도착 대략 17:15 + 여유
val commuter1EndTime = LocalTime.of(10, 0)
val commuter2EndTime = LocalTime.of(10, 0)


// ------------------------------------------------------------------------
// [UI 컴포저블]
// ------------------------------------------------------------------------

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShuttleScreenFixed(
    startShuttle: ShuttleType,
    onClose: () -> Unit
) {
    var selectedShuttle by rememberSaveable { mutableStateOf(startShuttle) }
    var showTimetable by remember { mutableStateOf(false) }

    // =================================================================
    // [⏰ 시간 제어 및 테스트 코드]
    // =================================================================
    // =================================================================
    // [⏰ 시간 제어 및 테스트 코드]
    // =================================================================
    val currentTimeState = produceState(initialValue = LocalTime.now()) {
        // 1. 시작 시간을 루프 '밖'에서 변수로 선언합니다.
       // var virtualTime = LocalTime.of(8, 40, 0) // 테스트 시작 시간 (아침 8시)

        while (true) {
            // 2. 현재 가상 시간을 UI에 반영
           // value = virtualTime
            value= LocalTime.now()

            // 3. 시간을 흐르게 함 (속도 조절은 여기서!)
            // 예: 0.1초(100ms)마다 실제 시간 10초씩 흐르게 설정 (100배속)
           // virtualTime = virtualTime.plusSeconds(2)
            delay(1000)

            // [참고] 만약 실제 속도(1초에 1초)로 보고 싶다면:
            // virtualTime = virtualTime.plusSeconds(1)
            // delay(1000)
        }
    }
    val now = currentTimeState.value
    // =================================================================
    // =================================================================

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (selectedShuttle == ShuttleType.CAMPUS) "교내 셔틀" else "통근 셔틀") },
                navigationIcon = {
                    IconButton(onClick = onClose) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                // ✅ 수정됨: 배경 사진 세로 채우기 (FillHeight)
                // 이미지가 잘리더라도 세로 길이를 꽉 채웁니다.
                Box(
                    modifier = Modifier
                        .fillMaxHeight() // 부모 높이에 맞춤
                        .aspectRatio(360f / 480f, matchHeightConstraintsFirst = true) // 비율 유지하되 높이 기준
                ) {
                    Image(
                        painter = painterResource(
                            id = if (selectedShuttle == ShuttleType.CAMPUS) R.drawable.bg_campus_circle
                            else R.drawable.bg_outside_circle
                        ),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.FillHeight // ✅ 세로 꽉 채우기 설정
                    )

                    // 움직이는 버스 레이어
                    BusMovingLayer(
                        shuttleType = selectedShuttle,
                        currentTime = now
                    )
                }
            }

            Button(
                onClick = { showTimetable = true },
                modifier = Modifier
                    .padding(vertical = 24.dp)
                    .width(180.dp)
            ) {
                Text("시간표 보기")
            }
        }

        if (showTimetable) {
            ShuttleTimetableBottomSheet(selectedShuttle) { showTimetable = false }
        }
    }
}

@Composable
fun BusMovingLayer(
    shuttleType: ShuttleType,
    currentTime: LocalTime
) {
    val busPositions = remember(shuttleType, currentTime) {
        if (shuttleType == ShuttleType.CAMPUS) {
            calculateCampusBusPositions(currentTime)
        } else {
            calculateCommuterBusPositions(currentTime)
        }
    }

    // 운행 중인지 체크 (막차 시간 이후인지)
    val isServiceRunning = remember(shuttleType, currentTime) {
        if (shuttleType == ShuttleType.CAMPUS) {
            !currentTime.isAfter(campusEndTime) && !currentTime.isBefore(LocalTime.of(8, 30))
        } else {
            // 통근은 아침에만 운행하므로 10시 이전까지만 체크
            !currentTime.isAfter(LocalTime.of(10, 0)) && !currentTime.isBefore(LocalTime.of(7, 30))
        }
    }

    var size by remember { mutableStateOf(IntSize.Zero) }
    val density = LocalDensity.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .onGloballyPositioned { size = it.size }
    ) {
        // 운행 시간이고 버스 위치가 있으면 렌더링
        if (isServiceRunning) {
            busPositions.forEach { (xRatio, yRatio) ->
                val xPx = size.width * xRatio
                val yPx = size.height * yRatio

                Image(
                    painter = painterResource(R.drawable.bus),
                    contentDescription = "Bus",
                    modifier = Modifier
                        .size(32.dp)
                        .offset(
                            x = with(density) { xPx.toDp() } - 16.dp,
                            y = with(density) { yPx.toDp() } - 16.dp
                        )
                )
            }
        }

        // 팝업 표시 조건:
        // 1. 아예 운행 시간이 지났거나 (isServiceRunning == false)
        // 2. 운행 시간 내지만 현재 떠있는 버스가 없을 때 (배차 간격 사이 or 점심시간)
        if (!isServiceRunning || busPositions.isEmpty()) {
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .shadow(8.dp, RoundedCornerShape(12.dp))
                    .background(Color.White, RoundedCornerShape(12.dp))
                    .padding(16.dp)
            ) {
                Text(
                    text = "현재 운행 중인 버스가 없습니다.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Black
                )
            }
        }
    }
}

// ------------------------------------------------------------------------
// [로직 함수]
// ------------------------------------------------------------------------

fun calculateCampusBusPositions(now: LocalTime): List<Pair<Float, Float>> {
    val activeBuses = mutableListOf<Pair<Float, Float>>()
    val defaultX = 171f / 360f
    val travelDuration = 10L // 운행 시간 10분

    kaiMaruWeekdayTimes.forEachIndexed { index, departureTime ->
        val arrivalTime = departureTime.plusMinutes(travelDuration)

        // 1. [운행 로직] 현재 시간이 출발~도착 사이일 때 (이동 중)
        if (!now.isBefore(departureTime) && now.isBefore(arrivalTime)) {
            val totalMillis = Duration.ofMinutes(travelDuration).toMillis()
            val passedMillis = Duration.between(departureTime, now).toMillis()
            val progress = passedMillis.toFloat() / totalMillis

            val startY = campusStationOffsets.first().third
            val endY = campusStationOffsets.last().third
            val currentY = lerp(startY, endY, progress)

            activeBuses.add(defaultX to currentY)
        }

        // 2. [대기 로직] 다음 버스가 출발 5분 전부터 카이마루에서 대기하게 함
        // (현재 시간이 다음 버스 출발 5분 전 ~ 출발 시간 사이일 때)
        val nextBusTime = kaiMaruWeekdayTimes.getOrNull(index) // 현재 루프의 버스를 기준으로 체크
        if (nextBusTime != null) {
            val standByStartTime = nextBusTime.minusMinutes(5) // 5분 전부터 대기
            if (!now.isBefore(standByStartTime) && now.isBefore(nextBusTime)) {
                val startY = campusStationOffsets.first().third // 카이마루 위치
                activeBuses.add(defaultX to startY)
            }
        }
    }
    // 중복 위치 방지 (대기 중인 버스와 이동 시작하는 버스가 겹칠 수 있음)
    return activeBuses.distinct()
}

fun calculateCommuterBusPositions(now: LocalTime): List<Pair<Float, Float>> {
    val positions = mutableListOf<Pair<Float, Float>>()

    // 1호차 계산
    calculateSingleBusPosition(now, commuterBus1Stations)?.let {
        if (now.isBefore(commuter1EndTime)) positions.add(it)
    }

    // 2호차 계산
    calculateSingleBusPosition(now, commuterBus2Stations)?.let {
        if (now.isBefore(commuter2EndTime)) positions.add(it)
    }

    return positions
}

fun calculateSingleBusPosition(
    now: LocalTime,
    stations: List<StationNode>
): Pair<Float, Float>? {
    val startTime = stations.first().time ?: return null
    val endTime = stations.last().time ?: return null

    if (now.isBefore(startTime) || now.isAfter(endTime)) return null

    for (i in 0 until stations.size - 1) {
        val startNode = stations[i]
        val endNode = stations[i + 1]

        val sTime = startNode.time ?: continue
        val eTime = endNode.time ?: continue

        if (!now.isBefore(sTime) && now.isBefore(eTime)) {
            val totalMillis = Duration.between(sTime, eTime).toMillis()
            val passedMillis = Duration.between(sTime, now).toMillis()
            val progress = if (totalMillis > 0) passedMillis.toFloat() / totalMillis else 0f

            val currentX = lerp(startNode.xRatio, endNode.xRatio, progress)
            val currentY = lerp(startNode.yRatio, endNode.yRatio, progress)

            return currentX to currentY
        }
    }
    return null
}

// ------------------------------------------------------------------------
// [시간표 UI]
// ------------------------------------------------------------------------

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShuttleTimetableBottomSheet(
    shuttleType: ShuttleType,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 56.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = if (shuttleType == ShuttleType.CAMPUS) "교내 셔틀 시간표" else "통근 셔틀 시간표",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .padding(16.dp)
                // 교내 셔틀일 때만 제목도 중앙 정렬하고 싶다면 아래 주석 해제
                // .let { if (shuttleType == ShuttleType.CAMPUS) it.align(Alignment.CenterHorizontally) else it }
            )

            if (shuttleType == ShuttleType.CAMPUS) {
                // [교내 셔틀] 시간만 중앙에 표시
                kaiMaruWeekdayTimes.forEach { time ->
                    TimeRow(name = "", time = time, isCentered = true)
                }
            } else {
                // [통근 셔틀] 기존 양식 유지 (이름 - 시간)
                Text("1호차", Modifier.padding(16.dp), style = MaterialTheme.typography.titleSmall)
                commuterBus1Stations.forEach { node ->
                    node.time?.let { TimeRow(node.name, it, isCentered = false) }
                }
                HorizontalDivider(Modifier.padding(vertical = 8.dp))
                Text("2호차", Modifier.padding(16.dp), style = MaterialTheme.typography.titleSmall)
                commuterBus2Stations.forEach { node ->
                    node.time?.let { TimeRow(node.name, it, isCentered = false) }
                }
            }
        }
    }
}

@Composable
fun TimeRow(name: String, time: LocalTime, isCentered: Boolean = false) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        // isCentered가 true면 중앙 정렬(Center), false면 양끝 정렬(SpaceBetween)
        horizontalArrangement = if (isCentered) Arrangement.Center else Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 이름이 있을 때만 표시 (통근 셔틀용)
        if (name.isNotBlank()) {
            Text(text = name, style = MaterialTheme.typography.bodyLarge)
        }

        // 시간 표시
        Text(
            text = time.format(DateTimeFormatter.ofPattern("HH:mm")),
            style = MaterialTheme.typography.bodyLarge,
            // 중앙 정렬일 때(교내) 글자를 조금 더 강조
            fontWeight = if (isCentered) androidx.compose.ui.text.font.FontWeight.Bold else null
        )
    }
}