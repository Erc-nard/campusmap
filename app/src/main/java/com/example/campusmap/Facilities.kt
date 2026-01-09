package com.example.campusmap

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

data class FacilityCategory(override val id: Int, override val title: String, val items: List<FacilityItem>) : FacilityData {
    override val imageURL: String
        get() = items[0].imageURL
}
data class FacilityItem(override val id: Int, override val title: String, override val imageURL: String, val details: List<ItemDetail> = listOf()) : FacilityData
data class ItemDetail(val title: String, val content: @Composable () -> Unit)

val cafeteria = listOf(
    FacilityItem(id = 0, title = "카이마루", imageURL = "https://kaist.ac.kr/kr/img/content/sub05/sub0503_img09.jpg", details = listOf(
        ItemDetail("위치", @Composable { Text("학생식당 (북측 N11)") }),
        ItemDetail("운영 시간", @Composable { Text("조식: 08:00~09:00\n중식: 11:20~13:30\n석식: 17:20~19:00") })
    )),
    FacilityItem(id = 1, title = "서맛골", imageURL = "https://kaist.ac.kr/kr/img/content/sub05/sub0503_img02.jpg", details = listOf(
        ItemDetail("위치", @Composable { Text("학생회관 (서측 W2) 1층") }),
        ItemDetail("운영 시간", @Composable { Text("조식: 08:00~09:30\n중식: 11:30~13:30\n석식: 17:00~19:00") })
    )),
    FacilityItem(id = 2, title = "동맛골 (학생식당)", imageURL = "https://kaist.ac.kr/kr/img/content/sub05/sub0503_img03.jpg", details = listOf(
        ItemDetail("위치", @Composable { Text("교직원회관 (동측 E5) 1층") }),
        ItemDetail("운영 시간", @Composable { Text("조식: 08:00~10:00\n중식: 11:30~14:00\n라면: 14:00~15:30\n석식: 17:30~19:00") }),
        ItemDetail("메뉴", @Composable { Carousel(listOf(
            @Composable { Text("조식~") },
            @Composable { Text("중식~") },
            @Composable { Text("석식~") }
        )) })
    )),
    FacilityItem(id = 3, title = "동맛골 (교직원식당)", imageURL = "https://kaist.ac.kr/kr/img/content/sub05/sub0503_img10.jpg", details = listOf(
        ItemDetail("위치", @Composable { Text("교직원회관 (동측 E5) 2층") }),
        ItemDetail("운영 시간", @Composable { Text("중식: 11:30~13:30\n석식: 17:30~19:00") })
    )),
)
val cafe = listOf(
    FacilityItem(id = 0, title = "그라찌에", imageURL = "https://kaist.ac.kr/kr/img/content/sub05/sub0507_img01.jpg"),
    FacilityItem(id = 1, title = "카페 잇", imageURL = "https://kaist.ac.kr/kr/img/content/sub05/sub0507_img02.jpg"),
    FacilityItem(id = 2, title = "드롭탑", imageURL = "https://kaist.ac.kr/kr/img/content/sub05/sub0507_img03.jpg", details = listOf(
        ItemDetail(title = "위치", @Composable { Text("교육지원동 (서측 W8) 1층")})
    )),
    FacilityItem(id = 3, title = "카페 오가다", imageURL = "https://kaist.ac.kr/kr/img/content/sub05/sub0507_img06.jpg", details = listOf(
        ItemDetail(title = "위치", @Composable { Text("학술문화관 (동측 E9) 문화관 2층") })
    )),
    FacilityItem(id = 4, title = "카페드림", imageURL = "https://kaist.ac.kr/kr/img/content/sub05/sub0507_img08.jpg"),
    FacilityItem(id = 5, title = "주스킹", imageURL = "https://kaist.ac.kr/kr/img/content/sub05/sub0507_img09.jpg"),
    FacilityItem(id = 6, title = "파스쿠찌", imageURL = "https://kaist.ac.kr/kr/img/content/sub05/sub0507_img10.jpg"),
    FacilityItem(id = 7, title = "탐앤탐즈", imageURL = "https://kaist.ac.kr/kr/img/campus/tom%20and%20toms.jpg"),
    FacilityItem(id = 8, title = "앤제리너스", imageURL = "https://kaist.ac.kr/site/kr/img/campus/00z7p.jpg")
)
val kiosk = listOf(
    FacilityItem(id = 0, title = "북측 학생회관", imageURL = "https://kaist.ac.kr/kr/img/content/sub05/sub0505_img01.jpg"),
    FacilityItem(id = 1, title = "서측 학생회관", imageURL = "https://kaist.ac.kr/kr/img/content/sub05/sub0505_img01.jpg"),
    FacilityItem(id = 2, title = "동맛골", imageURL = "https://kaist.ac.kr/kr/img/content/sub05/sub0505_img01.jpg"),
    FacilityItem(id = 3, title = "나들/여울관", imageURL = "https://kaist.ac.kr/kr/img/content/sub05/sub0505_img01.jpg"),
    FacilityItem(id = 4, title = "희망/다솜관", imageURL = "https://kaist.ac.kr/kr/img/content/sub05/sub0505_img01.jpg"),
    FacilityItem(id = 5, title = "미르/나래관", imageURL = "https://kaist.ac.kr/kr/img/content/sub05/sub0505_img01.jpg"),
    FacilityItem(id = 6, title = "세종관", imageURL = "https://kaist.ac.kr/kr/img/content/sub05/sub0505_img01.jpg"),
    FacilityItem(id = 7, title = "김병호 IT융합센터", imageURL = "https://kaist.ac.kr/kr/img/content/sub05/sub0505_img01.jpg"),
)
val topLevelFacilitiesList = listOf(
    FacilityCategory(id = 0, title = "식당", items = cafeteria),
    FacilityCategory(id = 1, title = "카페", items = cafe),
    FacilityCategory(id = 2, title = "매점", items = kiosk)
)

@Composable
fun Carousel(contents: List<@Composable () -> Unit>) {
    val pagerState = rememberPagerState(pageCount = { contents.size })

    Column() {
        HorizontalPager(
            state = pagerState,
            contentPadding = PaddingValues(horizontal = 30.dp),
            pageSpacing = 10.dp
        ) { page ->
            val content = contents[page]
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                content()
            }
        }
    }
}